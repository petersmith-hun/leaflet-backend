package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.converter.AuthorityToRoleConverter;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitScope;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService} interface.
 *
 * @author Peter Smith
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String ENTITY_COULD_NOT_BE_PERSISTED = "Entity could not be persisted.";
    private static final String EMAIL_ADDRESS_IS_ALREADY_IN_USE = "Email address is already in use";

    private final UserDAO userDAO;
    private final UserToUserVOConverter userToUserVOConverter;
    private final UserVOToUserConverter userVOToUserConverter;
    private final AuthorityToRoleConverter authorityToRoleConverter;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, UserToUserVOConverter userToUserVOConverter, UserVOToUserConverter userVOToUserConverter,
                           AuthorityToRoleConverter authorityToRoleConverter) {
        this.userDAO = userDAO;
        this.userToUserVOConverter = userToUserVOConverter;
        this.userVOToUserConverter = userVOToUserConverter;
        this.authorityToRoleConverter = authorityToRoleConverter;
    }

    @Override
    @PermitScope.Read.OwnUserOrElevated
    public UserVO getOne(@P("id") Long userID) throws ServiceException {

        return userDAO.findById(userID)
                .map(userToUserVOConverter::convert)
                .orElseThrow(() -> new EntityNotFoundException(User.class, userID));
    }

    @Override
    @PermitScope.Read.Users
    public List<UserVO> getAll() {

        return userDAO.findAll().stream()
                .map(userToUserVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitScope.Write.OwnUser
    public void deleteByID(@P("id") Long userID) throws ServiceException {

        if (!userDAO.exists(userID)) {
            throw new EntityNotFoundException(User.class, userID);
        }

        userDAO.delete(userID);
        LOGGER.info("User of ID [{}] has been deleted", userID);
    }

    @Override
    @PermitScope.Write.Users
    public Long createOne(UserVO entity) throws ServiceException {

        try {
            User user = userVOToUserConverter.convert(entity);
            User savedUser = userDAO.save(user);

            LOGGER.info("[{}] user has been created with ID [{}]", savedUser.getRole(), savedUser.getId());

            return savedUser.getId();

        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(EMAIL_ADDRESS_IS_ALREADY_IN_USE, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }
    }

    @Override
    @PermitScope.Write.OwnUser
    public UserVO updateOne(Long id, UserVO updatedEntity) throws ServiceException {

        try {
            return userDAO.updateOne(id, userVOToUserConverter.convert(updatedEntity))
                    .map(logUpdate())
                    .map(userToUserVOConverter::convert)
                    .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(EMAIL_ADDRESS_IS_ALREADY_IN_USE, e);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }
    }

    @Override
    public Long registerNoLogin(UserVO entity) throws ServiceException {

        if (!isNoLoginRole(entity)) {
            throw new ServiceException("Only users with role NO_LOGIN can be created via registerNoLogin service entry point.");
        }

        return createOne(entity);
    }

    @Override
    @PermitScope.Write.OwnUser
    public void changePassword(Long id, String password) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.updatePassword(id, password);
        LOGGER.info("Password has been updated for user [{}]", id);
    }

    @Override
    @PermitScope.Write.Users
    public void changeAuthority(Long id, GrantedAuthority grantedAuthority) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.updateRole(id, authorityToRoleConverter.convert(grantedAuthority));
        LOGGER.info("Role of user [{}] has been updated to [{}]", id, grantedAuthority.getAuthority());
    }

    @Override
    @PermitScope.Read.Users
    public EntityPageVO<UserVO> getEntityPage(int page, int limit, OrderDirection direction, UserVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<User> entityPage = userDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, userToUserVOConverter);
    }

    @Override
    @PermitScope.Write.Users
    public void enable(Long id) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.enable(id);
        LOGGER.info("Enabled user of ID [{}]", id);
    }


    @Override
    @PermitScope.Write.Users
    public void disable(Long id) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.disable(id);
        LOGGER.info("Disabled user of ID [{}]", id);
    }

    @Override
    public UserVO silentGetUserByEmail(String email) {

        return Optional.ofNullable(userDAO.findByEmail(email))
                .map(userToUserVOConverter::convert)
                .orElse(null);
    }

    private boolean isNoLoginRole(UserVO entity) {

        return entity.getAuthorities().stream()
                .allMatch(grantedAuthority -> grantedAuthority.equals(Authority.NO_LOGIN));
    }

    private Function<User, User> logUpdate() {

        return user -> {
            LOGGER.info("User of ID [{}] has been updated", user.getId());
            return user;
        };
    }
}
