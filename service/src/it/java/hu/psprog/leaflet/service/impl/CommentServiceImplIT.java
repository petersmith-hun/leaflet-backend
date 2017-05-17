package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.common.OrderDirection;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.helper.TestObjectReader;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for {@link CommentServiceImpl}.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class CommentServiceImplIT {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    static final String DELETED_COMMENT = "DELETED_COMMENT";

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private static final String COMMENT_1 = "comment_1";
    private static final String COMMENT_NEW = "comment_new";
    private static final String ENTRY_1 = "entry_1";

    @Autowired
    @Qualifier("commentServiceImpl")
    private CommentService commentService;

    @Autowired
    private TestObjectReader testObjectReader;

    private CommentVO controlCommentVO;
    private EntryVO controlEntryVO;

    @Before
    public void setup() throws Exception {
        controlCommentVO = testObjectReader.read(COMMENT_1, TestObjectReader.ObjectDirectory.VO, CommentVO.class);
        controlEntryVO = testObjectReader.read(ENTRY_1, TestObjectReader.ObjectDirectory.VO, EntryVO.class);
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testGetOne() throws ServiceException {

        // when
        CommentVO result = commentService.getOne(controlCommentVO.getId());

        // then
        assertThat(result.getContent(), equalTo(controlCommentVO.getContent()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testGetAll() throws ServiceException {

        // when
        List<CommentVO> result = commentService.getAll();

        // then
        assertThat(result.stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.size(), equalTo(10));
        assertThat(result.get(0).getContent(), equalTo(controlCommentVO.getContent()));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    @Parameters(method = "pageOfCommentsForEntry", source = PagingParameterProvider.class)
    public void testGetPageOfCommentsForEntry(int page, int itemNumber) {

        // given
        int limit = 6;
        OrderDirection direction = OrderDirection.DESC;
        CommentVO.OrderBy orderBy = CommentVO.OrderBy.CREATED;

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfCommentsForEntry(page, limit, direction, orderBy, controlEntryVO);

        // then
        assertThat(result.getEntityCountOnPage(), equalTo(itemNumber));
        assertThat(result.getEntityCount(), equalTo(10L));
        assertThat(result.getPageCount(), equalTo(2));
        assertThat(result.getPageSize(), equalTo(limit));
        assertThat(result.getEntitiesOnPage(), notNullValue());
        assertThat(result.getEntitiesOnPage().stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.getEntitiesOnPage().size(), equalTo(itemNumber));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    @Parameters(method = "pageOfPublicCommentsForEntry", source = PagingParameterProvider.class)
    public void testGetPageOfPublicCommentsForEntry(int page, int itemNumber, long numberOfDeletedComments) {

        // given
        int limit = 4;
        OrderDirection direction = OrderDirection.DESC;
        CommentVO.OrderBy orderBy = CommentVO.OrderBy.CREATED;

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfPublicCommentsForEntry(page, limit, direction, orderBy, controlEntryVO);

        // then
        assertThat(result.getEntityCountOnPage(), equalTo(itemNumber));
        assertThat(result.getEntityCount(), equalTo(7L));
        assertThat(result.getPageCount(), equalTo(2));
        assertThat(result.getPageSize(), equalTo(limit));
        assertThat(result.getEntitiesOnPage(), notNullValue());
        assertThat(result.getEntitiesOnPage().stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.getEntitiesOnPage().size(), equalTo(itemNumber));
        assertThat(result.getEntitiesOnPage().stream()
                .filter(CommentVO::isDeleted)
                .count(), equalTo(numberOfDeletedComments));
        assertThat(result.getEntitiesOnPage().stream()
                .filter(CommentVO::isDeleted)
                .allMatch(commentVO -> commentVO.getContent().equals(DELETED_COMMENT)), is(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    @Parameters(method = "pageOfCommentsForUser", source = PagingParameterProvider.class)
    public void testGetPageOfCommentsForUser(int page, int itemNumber) throws ServiceException, IOException {

        // given
        int limit = 5;
        OrderDirection direction = OrderDirection.DESC;
        CommentVO.OrderBy orderBy = CommentVO.OrderBy.CREATED;
        UserVO ownerVO = new UserVO.Builder()
                .withId(2L)
                .withUsername("IT Editor")
                .withEmail("lflt-it-5101@leaflet.dev")
                .withPassword("lflt1234")
                .withAuthorities(Arrays.asList(new SimpleGrantedAuthority("EDITOR")))
                .withEnabled(true)
                .withLocale(Locale.EN)
                .withCreated(new Date(1471514400000L))
                .withLastModified(new Date(1471514400000L))
                .createUserVO();

        // when
        EntityPageVO<CommentVO> result = commentService.getPageOfCommentsForUser(page, limit, direction, orderBy, ownerVO);

        // then
        assertThat(result.getEntityCountOnPage(), equalTo(itemNumber));
        assertThat(result.getEntityCount(), equalTo(6L));
        assertThat(result.getPageCount(), equalTo(2));
        assertThat(result.getPageSize(), equalTo(limit));
        assertThat(result.getEntitiesOnPage(), notNullValue());
        assertThat(result.getEntitiesOnPage().stream().allMatch(Objects::nonNull), equalTo(true));
        assertThat(result.getEntitiesOnPage().size(), equalTo(itemNumber));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testCount() {

        // when
        Long result = commentService.count();

        // then
        assertThat(result, equalTo(10L));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testCreateOne() throws IOException, ServiceException {

        // given
        CommentVO createdComment = testObjectReader.read(COMMENT_NEW, TestObjectReader.ObjectDirectory.VO, CommentVO.class);

        // when
        Long result = commentService.createOne(createdComment);

        // then
        assertThat(result, greaterThanOrEqualTo(11L));
        assertThat(commentService.getOne(result), notNullValue());
        assertThat(commentService.getOne(result).getContent(), equalTo("Test comment newly created"));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testUpdateOne() throws ServiceException {

        // given
        String updatedCommentContent = "Updated comment content";
        Long id = 4L;
        CommentVO commentToUpdate = commentService.getOne(id);
        commentToUpdate.setContent(updatedCommentContent);

        // when
        CommentVO result = commentService.updateOne(id, commentToUpdate);

        // then
        CommentVO updatedCommentVO = commentService.getOne(id);
        assertThat(updatedCommentVO, notNullValue());
        assertThat(updatedCommentVO.getContent(), equalTo(updatedCommentContent));
        assertThat(result.equals(updatedCommentVO), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testDeleteByEntity() throws ServiceException {

        // given
        Long id = 4L;
        CommentVO commentToDelete = commentService.getOne(id);

        // when
        commentService.deleteByEntity(commentToDelete);

        // then
        assertThat(commentService.count(), equalTo(9L));
        assertThat(commentService.getAll().stream().noneMatch(commentToDelete::equals), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testEnable() throws ServiceException {

        // given
        Long id = 5L;

        // when
        commentService.enable(id);

        // then
        assertThat(commentService.getOne(id).isEnabled(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testDisable() throws ServiceException {

        // given
        Long id = 1L;

        // when
        commentService.disable(id);

        // then
        assertThat(commentService.getOne(id).isEnabled(), equalTo(false));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testLogicalDeletion() throws ServiceException {

        // given
        Long id = 1L;
        assertThat(commentService.getOne(id).isDeleted(), equalTo(false));

        // when
        commentService.deleteLogicallyByEntity(CommentVO.wrapMinimumVO(id));

        // then
        assertThat(commentService.getOne(id).isDeleted(), equalTo(true));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_COMMENTS)
    public void testRestoreEntity() throws ServiceException {

        // given
        Long id = 10L;
        assertThat(commentService.getOne(id).isDeleted(), equalTo(true));

        // when
        commentService.restoreEntity(CommentVO.wrapMinimumVO(id));

        // then
        assertThat(commentService.getOne(id).isDeleted(), equalTo(false));
    }

    public static class PagingParameterProvider {

        public static Object[] pageOfCommentsForEntry() {
            return new Object[] {
                    new Object[] {1, 6},
                    new Object[] {2, 4}
            };
        }

        public static Object[] pageOfPublicCommentsForEntry() {
            return new Object[] {
                    new Object[] {1, 4, 1L},
                    new Object[] {2, 3, 0L}
            };
        }

        public static Object[] pageOfCommentsForUser() {
            return new Object[] {
                    new Object[] {1, 5},
                    new Object[] {2, 1}
            };
        }
    }
}
