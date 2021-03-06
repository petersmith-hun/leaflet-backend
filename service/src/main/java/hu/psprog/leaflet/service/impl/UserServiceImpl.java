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
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.security.annotation.PermitAdmin;
import hu.psprog.leaflet.service.security.annotation.PermitReclaim;
import hu.psprog.leaflet.service.security.annotation.PermitSelf;
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

    private UserDAO userDAO;
    private UserToUserVOConverter userToUserVOConverter;
    private UserVOToUserConverter userVOToUserConverter;
    private AuthorityToRoleConverter authorityToRoleConverter;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, UserToUserVOConverter userToUserVOConverter, UserVOToUserConverter userVOToUserConverter,
                           AuthorityToRoleConverter authorityToRoleConverter) {
        this.userDAO = userDAO;
        this.userToUserVOConverter = userToUserVOConverter;
        this.userVOToUserConverter = userVOToUserConverter;
        this.authorityToRoleConverter = authorityToRoleConverter;
    }

    @Override
    @PermitSelf.UserOrAdmin
    public UserVO getOne(@P("id") Long userID) throws ServiceException {

        User user = userDAO.findOne(userID);

        if(user == null) {
            throw new EntityNotFoundException(User.class, userID);
        }

        return userToUserVOConverter.convert(user);
    }

    @Override
    @PermitAdmin
    public List<UserVO> getAll() {

        return userDAO.findAll().stream()
                .map(userToUserVOConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @PermitAdmin
    public Long count() {

        return userDAO.count();
    }

    @Override
    @PermitSelf.User
    public void deleteByID(@P("id") Long userID) throws ServiceException {

        if (!userDAO.exists(userID)) {
            throw new EntityNotFoundException(User.class, userID);
        }

        userDAO.delete(userID);
        LOGGER.info("User of ID [{}] has been deleted", userID);
    }

    @Override
    @PermitAdmin
    public Long createOne(UserVO entity) throws ServiceException {

        User user = userVOToUserConverter.convert(entity);
        User savedUser;
        try {
            savedUser = userDAO.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(EMAIL_ADDRESS_IS_ALREADY_IN_USE, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (savedUser == null) {
            throw new EntityCreationException(User.class);
        }

        LOGGER.info("[{}] user has been created with ID [{}]", savedUser.getRole(), savedUser.getId());

        return savedUser.getId();
    }

    @Override
    @PermitSelf.User
    public UserVO updateOne(Long id, UserVO updatedEntity) throws ServiceException {

        User updatedUser;
        try {
            updatedUser = userDAO.updateOne(id, userVOToUserConverter.convert(updatedEntity));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintViolationException(EMAIL_ADDRESS_IS_ALREADY_IN_USE, e);
        } catch (Exception e) {
            throw new ServiceException(ENTITY_COULD_NOT_BE_PERSISTED, e);
        }

        if (updatedUser == null) {
            throw new EntityNotFoundException(User.class, id);
        }

        LOGGER.info("User of ID [{}] has been updated", id);

        return userToUserVOConverter.convert(updatedUser);
    }

    @Override
    public Long register(UserVO entity) throws ServiceException {

        if (!isUserRole(entity)) {
            throw new ServiceException("Only users with role USER can be created via register service entry point.");
        }

        return createOne(entity);
    }

    @Override
    public Long registerNoLogin(UserVO entity) throws ServiceException {

        if (!isNoLoginRole(entity)) {
            throw new ServiceException("Only users with role NO_LOGIN can be created via registerNoLogin service entry point.");
        }

        return createOne(entity);
    }

    @Override
    @PermitSelf.User
    public void changePassword(Long id, String password) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.updatePassword(id, password);
        LOGGER.info("Password has been updated for user [{}]", id);
    }

    @Override
    @PermitReclaim
    public void reclaimPassword(Long id, String password) throws EntityNotFoundException {
        changePassword(id, password);
    }

    @Override
    @PermitAdmin
    public void changeAuthority(Long id, GrantedAuthority grantedAuthority) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.updateRole(id, authorityToRoleConverter.convert(grantedAuthority));
        LOGGER.info("Role of user [{}] has been updated to [{}]", id, grantedAuthority.getAuthority());
    }

    @Override
    @PermitAdmin
    public EntityPageVO<UserVO> getEntityPage(int page, int limit, OrderDirection direction, UserVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<User> entityPage = userDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, userToUserVOConverter);
    }

    @Override
    @PermitAdmin
    public void enable(Long id) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.enable(id);
        LOGGER.info("Enabled user of ID [{}]", id);
    }


    @Override
    @PermitAdmin
    public void disable(Long id) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.disable(id);
        LOGGER.info("Disabled user of ID [{}]", id);
    }

    @Override
    public void updateLastLogin(String email) throws EntityNotFoundException {

        User user = userDAO.findByEmail(email);
        if(user == null) {
            throw new EntityNotFoundException(User.class, email);
        }
        userDAO.updateLastLogin(email);
    }

    @Override
    public UserVO silentGetUserByEmail(String email) {

        return Optional.ofNullable(userDAO.findByEmail(email))
                .map(userToUserVOConverter::convert)
                .orElse(null);
    }

    private boolean isUserRole(UserVO entity) {
        return entity.getAuthorities().stream()
                .allMatch(grantedAuthority -> grantedAuthority.equals(Authority.USER));
    }

    private boolean isNoLoginRole(UserVO entity) {
        return entity.getAuthorities().stream()
                .allMatch(grantedAuthority -> grantedAuthority.equals(Authority.NO_LOGIN));
    }
}
