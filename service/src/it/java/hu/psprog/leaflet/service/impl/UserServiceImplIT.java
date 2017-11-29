package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.service.UserService;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.exception.UserInitializationException;
import hu.psprog.leaflet.service.helper.UserVOTestDataGenerator;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * Integration tests for {@link UserServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class UserServiceImplIT {

    private static final String USER_ID1_EMAIL = "lflt-it-5101@leaflet.dev";
    private static final String USER_ID1_USERNAME = "Administrator";
    private static final long USER_ID1_ID = 1L;
    private static final String USER_ID1_PASSWORD = "lflt1234";
    private static final Locale USER_ID1_LOCALE = Locale.EN;
    private static final boolean USER_ID1_ENABLED = true;
    private static final Date USER_ID1_CREATED = new Timestamp(1471514400000L);
    private static final Date USER_ID1_LAST_MODIFIED = new Timestamp(1471514400000L);

    private static final String USER_ID6_EMAIL = "lflt-it-5106@leaflet.dev";
    private static final String USER_ID6_USERNAME = "User Created";
    private static final long USER_ID6_ID = 6L;
    private static final String USER_ID6_PASSWORD = "lflt1234";
    private static final Locale USER_ID6_LOCALE = Locale.EN;
    private static final boolean USER_ID6_ENABLED = true;
    private static final Date USER_ID6_CREATED = new Timestamp(1471514400000L);
    private static final Collection<GrantedAuthority> ADMIN_AUTHORITY = Arrays.asList(new SimpleGrantedAuthority("ADMIN"));

    @Autowired
    private UserService userService;

    private UserVO controlUserVO;
    private UserVO createdUserVO;

    @Before
    public void setup() {

        controlUserVO = UserVO.getBuilder()
                .withId(USER_ID1_ID)
                .withUsername(USER_ID1_USERNAME)
                .withPassword(USER_ID1_PASSWORD)
                .withLocale(USER_ID1_LOCALE)
                .withEnabled(USER_ID1_ENABLED)
                .withCreated(USER_ID1_CREATED)
                .withLastModified(USER_ID1_LAST_MODIFIED)
                .withEmail(USER_ID1_EMAIL)
                .withAuthorities(ADMIN_AUTHORITY)
                .build();

        createdUserVO = UserVO.getBuilder()
                .withId(USER_ID6_ID)
                .withUsername(USER_ID6_USERNAME)
                .withPassword(USER_ID6_PASSWORD)
                .withLocale(USER_ID6_LOCALE)
                .withEnabled(USER_ID6_ENABLED)
                .withCreated(USER_ID6_CREATED)
                .withEmail(USER_ID6_EMAIL)
                .withAuthorities(ADMIN_AUTHORITY)
                .build();
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testGetOneWithExistingUser() throws ServiceException {

        // when
        UserVO result = userService.getOne(USER_ID1_ID);

        // then
        assertThat(result, equalTo(controlUserVO));
    }

    @Test(expected = EntityNotFoundException.class)
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testGetOneWithNonExistingUser() throws ServiceException {

        // given
        long nonExistingUserID = 6L;

        // when
        userService.getOne(nonExistingUserID);

        // then
        // expected exception
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testGetAll() {

        // when
        List<UserVO> result = userService.getAll();

        // then
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.get(0), equalTo(controlUserVO));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testCount() {

        // when
        long result = userService.count();

        // then
        assertThat(result, equalTo(5L));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testCreateOne() throws ServiceException {

        // when
        Long result = userService.createOne(createdUserVO);

        // then
        UserVO resultUser = userService.getOne(result);
        assertThat(resultUser.getId(), equalTo(result));
        assertThat(resultUser.getAuthorities(), equalTo(createdUserVO.getAuthorities()));
        assertThat(resultUser.getEmail(), equalTo(createdUserVO.getEmail()));
        assertThat(resultUser.getLastLogin(), nullValue());
        assertThat(resultUser.getLocale(), equalTo(createdUserVO.getLocale()));
        assertThat(resultUser.getPassword(), equalTo(createdUserVO.getPassword()));
        assertThat(resultUser.getUsername(), equalTo(createdUserVO.getUsername()));
        assertThat(resultUser.getLastModified(), nullValue());
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedUsername = "User Beta Updated";
        Long id = 3L;
        UserVO userToUpdate = userService.getOne(id);
        UserVO updateVO = UserVO.getBuilder()
                .withId(id)
                .withEmail(userToUpdate.getEmail())
                .withUsername(updatedUsername)
                .build();

        // when
        UserVO updatedUserVO = userService.updateOne(id, updateVO);

        // then
        UserVO resultedUserVO = userService.getOne(id);
        assertThat(updatedUserVO.getUsername(), equalTo(updatedUsername));
        assertThat(resultedUserVO.getUsername(), equalTo(updatedUsername));
        assertThat(resultedUserVO.getEmail(), equalTo(userToUpdate.getEmail()));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testChangePassword() throws ServiceException {

        // given
        String updatedPassword = "new password";

        // when
        userService.changePassword(USER_ID1_ID, updatedPassword);

        // then
        assertThat(userService.getOne(USER_ID1_ID).getPassword(), equalTo(updatedPassword));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testChangeRole() throws ServiceException {

        // when
        userService.changeAuthority(USER_ID1_ID, Authority.EDITOR);

        // then
        assertThat(userService.getOne(USER_ID1_ID).getAuthorities().iterator().next(), equalTo(Authority.EDITOR));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testGetEntityPage() {

        // given
        int page = 2;
        int limit = 2;
        OrderDirection direction = OrderDirection.DESC;
        UserVO.OrderBy orderBy = UserVO.OrderBy.ID;
        List<UserVO> allEntities = userService.getAll().stream()
                .sorted((e, f) -> f.getId().compareTo(e.getId())).collect(Collectors.toList());

        // when
        EntityPageVO<UserVO> result = userService.getEntityPage(page, limit, direction, orderBy);

        // then
        assertThat(result.getEntitiesOnPage().size(), equalTo(2));
        assertThat(result.getEntitiesOnPage(), equalTo(allEntities.subList(2, 4)));
        assertThat(result.isLast(), equalTo(false));
        assertThat(result.isFirst(), equalTo(false));
        assertThat(result.hasNext(), equalTo(true));
        assertThat(result.hasPrevious(), equalTo(true));
        assertThat(result.getEntityCount(), equalTo(5L));
        assertThat(result.getEntityCountOnPage(), equalTo(2));
        assertThat(result.getPageCount(), equalTo(3));
        assertThat(result.getPageNumber(), equalTo(2));

    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testStatusChange() throws ServiceException {

        // when
        userService.disable(USER_ID1_ID);

        // then
        assertThat(userService.getOne(USER_ID1_ID).isEnabled(), equalTo(false));

        // when
        userService.enable(USER_ID1_ID);

        // then
        assertThat(userService.getOne(USER_ID1_ID).isEnabled(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(scripts = LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_USERS)
    public void testUpdateLastLogin() throws ServiceException {

        // given
        assertThat(userService.getOne(USER_ID1_ID).getLastLogin(), nullValue());

        // when
        userService.updateLastLogin(USER_ID1_EMAIL);

        // then
        UserVO user = userService.getOne(USER_ID1_ID);
        assertThat(user.getLastLogin(), notNullValue());
        long diff = Math.abs(System.currentTimeMillis() - user.getLastLogin().getTime());
        assertThat(diff < 3000, equalTo(true));
    }
}
