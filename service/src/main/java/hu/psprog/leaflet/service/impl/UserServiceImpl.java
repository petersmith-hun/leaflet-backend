package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.persistence.facade.UserRepositoryFacade;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.common.RunLevel;
import hu.psprog.leaflet.service.converter.AuthorityToRoleConverter;
import hu.psprog.leaflet.service.converter.UserToUserVOConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.util.PageableUtil;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link UserService} interface.
 *
 * @author Peter Smith
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USERNAME_NOT_FOUND_MESSAGE_PATTERN = "User identified by username [%s] not found";

    @Autowired
    private UserRepositoryFacade userRepository;

    @Autowired
    private UserToUserVOConverter userToUserVOConverter;

    @Autowired
    private UserVOToUserConverter userVOToUserConverter;

    @Autowired
    private AuthorityToRoleConverter authorityToRoleConverter;

    @Autowired
    private RunLevel runLevel;

    /**
     * Loads a user by its email address (instead of username).
     *
     * @param email email address to load user by
     * @return UserDetails object holding necessary user data
     * @throws UsernameNotFoundException when no user found by specified email address
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE_PATTERN, email));
        }

        return userToUserVOConverter.convert(user);
    }

    @Override
    public UserVO getOne(Long userID) throws ServiceException {

        User user = userRepository.findOne(userID);

        if(user == null) {
            throw new EntityNotFoundException(User.class, userID);
        }

        return userToUserVOConverter.convert(user);
    }

    @Override
    public List<UserVO> getAll() {

        return userRepository.findAll().stream()
                .map(user -> userToUserVOConverter.convert(user))
                .collect(Collectors.toList());
    }

    @Override
    public Long count() {

        return userRepository.count();
    }

    @Override
    public void deleteByEntity(UserVO entity) throws ServiceException {

        if (!userRepository.exists(entity.getId())) {
            throw new EntityNotFoundException(User.class, entity.getId());
        }

        deleteByID(entity.getId());
    }

    @Override
    public void deleteByID(Long userID) throws ServiceException {

        try {
            userRepository.delete(userID);
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
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new EntityCreationException(User.class);
        }

        return user.getId();
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

        User updatedUser = userRepository.updateOne(id, userVOToUserConverter.convert(updatedEntity));

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

        long userCount = userRepository.count();
        if(userCount > 0) {
            throw new UserInitializationException("Application already initialized");
        }

        userVO.setAuthorities(Arrays.asList(Authority.ADMIN));
        User user = userVOToUserConverter.convert(userVO);
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new EntityCreationException(User.class);
        }

        return savedUser.getId();
    }

    @Override
    public void changePassword(Long id, String password) throws EntityNotFoundException {

        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userRepository.updatePassword(id, password);
    }

    @Override
    public void changeAuthority(Long id, GrantedAuthority grantedAuthority) throws EntityNotFoundException {

        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userRepository.updateRole(id, authorityToRoleConverter.convert(grantedAuthority));
    }


    @Override
    public EntityPageVO<UserVO> getEntityPage(int page, int limit, OrderDirection direction, UserVO.OrderBy orderBy) {

        Pageable pageable = PageableUtil.createPage(page, limit, direction, orderBy.getField());
        Page entityPage = userRepository.findAll(pageable);

        return PageableUtil.convertPage(entityPage, userToUserVOConverter);
    }

    @Override
    public void enable(Long id) throws EntityNotFoundException {

        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userRepository.enable(id);
    }

    @Override
    public void disable(Long id) throws EntityNotFoundException {

        if (!userRepository.exists(id)) {
            throw new EntityNotFoundException(User.class, id);
        }

        userRepository.disable(id);
    }
}
