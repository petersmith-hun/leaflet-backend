package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ConflictingRequestException;
import hu.psprog.leaflet.bridge.client.exception.RequestProcessingFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.stream.Collectors;

import static hu.psprog.leaflet.bridge.client.domain.OrderBy.Comment.CREATED;
import static hu.psprog.leaflet.bridge.client.domain.OrderDirection.ASC;
import static hu.psprog.leaflet.bridge.client.domain.OrderDirection.DESC;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Acceptance tests for {@code /comments} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class CommentsControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final Comparator<CommentDataModel> ASC_CREATED = Comparator.comparing(CommentDataModel::getCreated);
    private static final Comparator<CommentDataModel> DESC_CREATED = Comparator.comparing(CommentDataModel::getCreated).reversed();

    private static final String CONTROL_COMMENT_1 = "comment-1";
    private static final Long CONTROL_COMMENT_ID = 1L;
    private static final String DELETED_COMMENT = "DELETED_COMMENT";
    private static final String CONTROL_COMMENT_AUTH = "comment-auth";
    private static final String CONTROL_COMMENT_ANON = "comment-anon";

    private static final String ANONYMOUS_USER_NAME = "Anonymous User";
    private static final String TEST_USER_1_EMAIL = "test-user-1@ac-leaflet.local";

    @Autowired
    private CommentBridgeService commentBridgeService;

    @Test
    public void shouldReturnCommentByID() throws CommunicationFailureException {

        // given
        ExtendedCommentDataModel control = getControl(CONTROL_COMMENT_1, ExtendedCommentDataModel.class);

        // when
        ExtendedCommentDataModel result = commentBridgeService.getComment(CONTROL_COMMENT_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    @Parameters(source = CommentAcceptanceTestDataProvider.class, method = "pageOfComments")
    public void shouldReturnCommentsForEntry(long entryID, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection,
                                             long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious,
                                             int expectedLogicallyDeletedCount)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<CommentListDataModel> result = commentBridgeService.getPageOfCommentsForEntry(entryID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertLogicallyDeletedComments(result, expectedLogicallyDeletedCount);
    }

    @Test
    @Parameters(source = CommentAcceptanceTestDataProvider.class, method = "pageOfPublicComments")
    public void shouldReturnPublicCommentsForEntry(long entryID, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection,
                                                   long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious,
                                                   int expectedLogicallyDeletedCount)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<CommentListDataModel> result = commentBridgeService.getPageOfPublicCommentsForEntry(entryID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertLogicallyDeletedComments(result, expectedLogicallyDeletedCount);
    }

    @Test
    @ResetDatabase
    public void shouldCreateCommentWithAuthenticatedUser() throws CommunicationFailureException {

        // given
        CommentCreateRequestModel control = getControl(CONTROL_COMMENT_AUTH, CONTROL_SUFFIX_CREATE, CommentCreateRequestModel.class);

        // when
        CommentDataModel result = commentBridgeService.createComment(control);

        // then
        assertThat(result, notNullValue());
        ExtendedCommentDataModel current = commentBridgeService.getComment(result.getId());
        assertThat(current.getContent(), equalTo(control.getContent()));
        assertThat(current.getAssociatedEntry().getId(), equalTo(control.getEntryId()));
        assertThat(current.getOwner().getId(), equalTo(1L));
    }

    @Test
    @ResetDatabase
    public void shouldCreateCommentWithAnonymousUser() throws CommunicationFailureException {

        // given
        clearAuthentication();
        CommentCreateRequestModel control = getControl(CONTROL_COMMENT_ANON, CONTROL_SUFFIX_CREATE, CommentCreateRequestModel.class);

        // when
        CommentDataModel result = commentBridgeService.createComment(control);

        // then
        assertThat(result, notNullValue());
        restoreAuthentication();
        ExtendedCommentDataModel current = commentBridgeService.getComment(result.getId());
        assertThat(current.getContent(), equalTo(control.getContent()));
        assertThat(current.getAssociatedEntry().getId(), equalTo(control.getEntryId()));
        assertThat(current.getOwner().getUsername(), equalTo(ANONYMOUS_USER_NAME));
    }

    @Test
    @ResetDatabase
    public void shouldCommentCreationFailForAnonymousUserWithRegisteredUserEmail() throws CommunicationFailureException {

        // given
        clearAuthentication();
        CommentCreateRequestModel control = getControl(CONTROL_COMMENT_ANON, CONTROL_SUFFIX_CREATE, CommentCreateRequestModel.class);
        control.setEmail(TEST_USER_1_EMAIL);

        // when
        try {
            commentBridgeService.createComment(control);
            fail("Test case should have thrown exception.");
        } catch (ConflictingRequestException e) {

            // then
            // exception expected
            restoreAuthentication();
        }
    }

    @Test(expected = RequestProcessingFailureException.class)
    @ResetDatabase
    public void shouldCommentCreationFailForAuthenticatedUserWithDifferentAuthenticatedUserID() throws CommunicationFailureException {

        // given
        CommentCreateRequestModel control = getControl(CONTROL_COMMENT_AUTH, CONTROL_SUFFIX_CREATE, CommentCreateRequestModel.class);
        control.setAuthenticatedUserId(2L);

        // when
        commentBridgeService.createComment(control);

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldUpdateCommentContent() throws CommunicationFailureException {

        // given
        CommentUpdateRequestModel commentUpdateRequestModel = new CommentUpdateRequestModel();
        commentUpdateRequestModel.setContent("Updated comment content");

        // when
        commentBridgeService.updateComment(CONTROL_COMMENT_ID, commentUpdateRequestModel);

        // then
        ExtendedCommentDataModel current = commentBridgeService.getComment(CONTROL_COMMENT_ID);
        assertThat(current.getContent(), equalTo(commentUpdateRequestModel.getContent()));
    }

    @Test
    @ResetDatabase
    public void shouldUpdateCommentStatus() throws CommunicationFailureException {

        // when
        commentBridgeService.changeStatus(CONTROL_COMMENT_ID);

        // then
        assertThat(commentBridgeService.getComment(CONTROL_COMMENT_ID).isEnabled(), is(false));
        assertThat(commentBridgeService.getPageOfPublicCommentsForEntry(1L, 1, 20, CREATED, ASC).getBody().getComments().stream()
                .noneMatch(commentDataModel -> CONTROL_COMMENT_ID.equals(commentDataModel.getId())), is(true));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteCommentLogically() throws CommunicationFailureException {

        // when
        commentBridgeService.deleteCommentLogically(CONTROL_COMMENT_ID);

        // then
        ExtendedCommentDataModel current = commentBridgeService.getComment(CONTROL_COMMENT_ID);
        assertThat(current.isDeleted(), is(true));
        assertThat(current.getContent(), equalTo(DELETED_COMMENT));
    }

    @Test(expected = ResourceNotFoundException.class)
    @ResetDatabase
    public void shouldDeleteCommentPermanently() throws CommunicationFailureException {

        // when
        commentBridgeService.deleteCommentPermanently(CONTROL_COMMENT_ID);

        // then
        // this call should cause exception
        commentBridgeService.getComment(CONTROL_COMMENT_ID);
    }

    private void assertLogicallyDeletedComments(WrapperBodyDataModel<CommentListDataModel> result, int expectedLogicallyDeletedCount) {
        assertThat(result.getBody().getComments().stream()
                .filter(CommentDataModel::isDeleted)
                .count(), equalTo((long) expectedLogicallyDeletedCount));
        assertThat(result.getBody().getComments().stream()
                .filter(CommentDataModel::isDeleted)
                .allMatch(commentDataModel -> DELETED_COMMENT.equals(commentDataModel.getContent())), is(true));
    }

    private Comparator<CommentDataModel> getComparator(OrderBy.Comment orderBy, OrderDirection orderDirection) {

        if (orderBy == CREATED && orderDirection == ASC) {
            return ASC_CREATED;
        } else {
            return DESC_CREATED;
        }
    }

    private void assertPaginatedResult(WrapperBodyDataModel<CommentListDataModel> result, Comparator<CommentDataModel> comparator,
                                       long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious) {

        assertThat(result, notNullValue());
        assertThat(result.getBody().getComments().size(), equalTo(expectedBodySize));
        assertThat(result.getPagination(), notNullValue());
        assertThat(result.getPagination().getPageCount(), equalTo(expectedPageCount));
        assertThat(result.getPagination().isHasNext(), equalTo(expectedHasNext));
        assertThat(result.getPagination().isHasPrevious(), equalTo(expectedHasPrevious));
        assertThat(result.getPagination().getEntityCount(), equalTo(expectedEntityCount));
        assertThat(result.getBody().getComments().stream()
                .sorted(comparator)
                .collect(Collectors.toList())
                .equals(result.getBody().getComments()), is(true));
    }

    public static class CommentAcceptanceTestDataProvider {

        public static Object[] pageOfComments() {
            return new Object[] {
                    // entry ID, page, limit, order by, order direction, exp. all comments, exp. body size, exp. num. of pages, exp. has next, exp. has previous, logically deleted
                    new Object[] {1, 1, 4, CREATED, ASC, 10, 4, 3, true, false, 1},
                    new Object[] {1, 2, 4, CREATED, ASC, 10, 4, 3, true, true, 2},
                    new Object[] {1, 3, 4, CREATED, ASC, 10, 2, 3, false, true, 1},
                    new Object[] {2, 1, 4, CREATED, ASC, 0, 0, 0, false, false, 0},
                    new Object[] {1, 1, 20, CREATED, DESC, 10, 10, 1, false, false, 4}
            };
        }

        public static Object[] pageOfPublicComments() {
            return new Object[] {
                    // entry ID, page, limit, order by, order direction, exp. all comments, exp. body size, exp. num. of pages, exp. has next, exp. has previous, logically deleted
                    new Object[] {1, 1, 4, CREATED, ASC, 7, 4, 2, true, false, 1},
                    new Object[] {1, 2, 4, CREATED, ASC, 7, 3, 2, false, true, 3},
                    new Object[] {1, 3, 4, CREATED, ASC, 7, 0, 2, false, true, 0},
                    new Object[] {2, 1, 4, CREATED, ASC, 0, 0, 0, false, false, 0},
                    new Object[] {1, 1, 20, CREATED, DESC, 7, 7, 1, false, false, 4}
            };
        }
    }
}
