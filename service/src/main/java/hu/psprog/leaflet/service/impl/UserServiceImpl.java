package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.UserDAO;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.security.jwt.JWTComponent;
import hu.psprog.leaflet.security.jwt.model.JWTAuthenticationAnswerModel;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.service.converter.AuthorityToRoleConverter;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.AuthRequestVO;
import hu.psprog.leaflet.service.vo.AuthResponseVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService} interface.
 *
 * @author Peter Smith
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserToUserVOConverter userToUserVOConverter;

    @Autowired
    private UserVOToUserConverter userVOToUserConverter;

    @Autowired
    private AuthorityToRoleConverter authorityToRoleConverter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTComponent jwtComponent;

    @Autowired
    private RunLevel runLevel;

    @Override
    public UserVO getOne(Long userID) throws ServiceException {

        User user = userDAO.findOne(userID);

        if(user == null) {
            throw new EntityNotFoundException(User.class, userID);
        }

        return userToUserVOConverter.convert(user);
    }

    @Override
    public List<UserVO> getAll() {

        return userDAO.findAll().stream()
                .map(user -> userToUserVOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {

        return userDAO.count();
    }

    @Override
    public void deleteByEntity(UserVO entity) throws ServiceException {

        if (!userDAO.exists(entity.getId())) {
            throw new EntityNotFoundException(User.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long userID) throws ServiceException {

        try {
            userDAO.delete(userID);
        } catch (IllegalArgumentException exc) {
            throw new EntityNotFoundException(User.class, userID);
        }
    }

    @Override
    public void deleteBulkByIDs(List<Long> ids) throws ServiceException {

        for (Long id : ids) {
            deleteByID(id);
        }
    }

    @Override
    public Long createOne(UserVO entity) throws ServiceException {

        User user = userVOToUserConverter.convert(entity);
        User savedUser;
        try {
            savedUser = userDAO.save(user);
        } catch (PersistenceException e) {
            throw new ConstraintViolationException(e);
        }

        if (savedUser == null) {
            throw new EntityCreationException(User.class);
        }

        return savedUser.getId();
    }

    @Override
    public List<Long> createBulk(List<UserVO> entities) throws ServiceException {

        List<Long> ids = new LinkedList<>();
        for(UserVO entity : entities) {
            Long id = createOne(entity);
            ids.add(id);
        }

        return ids;
    }

    @Override
    public UserVO updateOne(Long id, UserVO updatedEntity) throws ServiceException {

        User updatedUser;
        try {
            updatedUser = userDAO.updateOne(id, userVOToUserConverter.convert(updatedEntity));
        } catch (PersistenceException e) {
            throw new ConstraintViolationException(e);
        }

        if (updatedUser == null) {
            throw new EntityNotFoundException(User.class, id);
        }

        return userToUserVOConverter.convert(updatedUser);
    }

    @Override
    public List<UserVO> updateBulk(Map<Long, UserVO> updatedEntities) throws ServiceException {

        List<UserVO> userVOs = new LinkedList<>();

        Iterator<Map.Entry<Long, UserVO>> entities = updatedEntities.entrySet().iterator();
        while (entities.hasNext()) {
            Map.Entry<Long, UserVO> currentEntity = entities.next();
            UserVO updatedEntity = updateOne(currentEntity.getKey(), currentEntity.getValue());
            userVOs.add(updatedEntity);
        }

        return userVOs;
    }

    @Override
    public Long initialize(UserVO userVO) throws UserInitializationException, EntityCreationException {

        if(runLevel != RunLevel.INIT) {
            throw new UserInitializationException("Application is NOT in INIT mode");
        }

        long userCount = userDAO.count();
        if(userCount > 0) {
            throw new UserInitializationException("Application already initialized");
        }

        User user = userVOToUserConverter.convert(userVO);
        user.setRole(Role.ADMIN);
        User savedUser = userDAO.save(user);

        if (savedUser == null) {
            throw new EntityCreationException(User.class);
        }

        return savedUser.getId();
    }

    @Override
    public void changePassword(Long id, String password) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.updatePassword(id, password);
    }

    @Override
    public void changeAuthority(Long id, GrantedAuthority grantedAuthority) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.updateRole(id, authorityToRoleConverter.convert(grantedAuthority));
    }

    @Override
    public EntityPageVO<UserVO> getEntityPage(int page, int limit, OrderDirection direction, UserVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page<User> entityPage = userDAO.findAll(pageable);

        return PageableUtil.convertPage(entityPage, userToUserVOConverter, UserVO.class);
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.enable(id);
    }


    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!userDAO.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userDAO.disable(id);
    }

    @Override
    public AuthResponseVO claimToken(AuthRequestVO authRequestVO) {

        AuthResponseVO.AuthResponseVOBuilder builder = AuthResponseVO.getBuilder();
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(authRequestVO.getUsername(), authRequestVO.getPassword());
            authenticationManager.authenticate(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestVO.getUsername());
            JWTAuthenticationAnswerModel authenticationAnswer = jwtComponent.generateToken(userDetails);

            return builder
                    .withAuthenticationResult(AuthResponseVO.AuthenticationResult.AUTH_SUCCESS)
                    .withToken(authenticationAnswer.getToken())
                    .build();

        } catch (AuthenticationException exception) {

            return builder
                    .withAuthenticationResult(AuthResponseVO.AuthenticationResult.INVALID_CREDENTIALS)
                    .build();
        }
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
}
