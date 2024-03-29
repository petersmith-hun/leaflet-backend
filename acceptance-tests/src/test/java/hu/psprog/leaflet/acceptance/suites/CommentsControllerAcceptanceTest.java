package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentSearchParameters;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentListDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.client.exception.UnauthorizedAccessException;
import hu.psprog.leaflet.bridge.service.CommentBridgeService;
import hu.psprog.leaflet.service.vo.mail.CommentNotification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static hu.psprog.leaflet.bridge.client.domain.OrderBy.Comment.CREATED;
import static hu.psprog.leaflet.bridge.client.domain.OrderDirection.ASC;
import static hu.psprog.leaflet.bridge.client.domain.OrderDirection.DESC;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Acceptance tests for {@code /comments} endpoints.
 *
 * @author Peter Smith
 */
@LeafletAcceptanceSuite
public class CommentsControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final Comparator<CommentDataModel> ASC_CREATED = Comparator.comparing(CommentDataModel::created);
    private static final Comparator<CommentDataModel> DESC_CREATED = Comparator.comparing(CommentDataModel::created).reversed();
    private static final Comparator<ExtendedCommentDataModel> ASC_CREATED_EXTENDED = Comparator.comparing(ExtendedCommentDataModel::created);
    private static final Comparator<ExtendedCommentDataModel> DESC_CREATED_EXTENDED = Comparator.comparing(ExtendedCommentDataModel::created).reversed();

    private static final String CONTROL_COMMENT_1 = "comment-1";
    private static final Long CONTROL_COMMENT_ID = 1L;
    private static final String DELETED_COMMENT = "DELETED_COMMENT";
    private static final String CONTROL_COMMENT = "comment";

    private static final String CONTROL_ENTRY_LINK = "entry-1";
    private static final String ENTRY_WITHOUT_COMMENTS_LINK = "entry-2";
    private static final String ENTRY_NON_EXISTING_LINK = "entry-non-existing";

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

    @ParameterizedTest
    @MethodSource("pageOfCommentsDataProvider")
    public void shouldReturnCommentsForEntry(long entryID, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection,
                                             long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious,
                                             int expectedLogicallyDeletedCount)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<CommentListDataModel> result = commentBridgeService.getPageOfCommentsForEntry(entryID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertLogicallyDeletedComments(result.body().comments(), expectedLogicallyDeletedCount);
        assertThat(result.menu(), nullValue());
    }

    @ParameterizedTest
    @MethodSource("pageOfPublicCommentsDataProvider")
    public void shouldReturnPublicCommentsForEntry(String entryLink, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection,
                                                   long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious,
                                                   int expectedLogicallyDeletedCount)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<CommentListDataModel> result = commentBridgeService.getPageOfPublicCommentsForEntry(entryLink, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedResult(result, getComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertLogicallyDeletedComments(result.body().comments(), expectedLogicallyDeletedCount);
        assertThat(result.menu(), nullValue());
    }

    @ParameterizedTest
    @MethodSource("pageOfCommentsForUserDataProvider")
    public void shouldReturnCommentsForUser(long userID, int page, int limit, OrderBy.Comment orderBy, OrderDirection orderDirection,
                                            long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious,
                                            int expectedLogicallyDeletedCount, int expectedEnabledCount)
            throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.getPageOfCommentsForUser(userID, page, limit, orderBy, orderDirection);

        // then
        assertPaginatedExtendedResult(result, getExtendedComparator(orderBy, orderDirection), expectedEntityCount, expectedBodySize, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertLogicallyDeletedExtendedComments(result.body().comments(), expectedLogicallyDeletedCount);
        assertEnabledExtendedComments(result.body().comments(), expectedEnabledCount);
        assertThat(result.menu(), nullValue());
    }

    @Test
    public void shouldSearchCommentWithEmptySearchParameters() throws CommunicationFailureException {

        // given
        CommentSearchParameters commentSearchParameters = new CommentSearchParameters();

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.searchComments(commentSearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(10L));
        assertThat(result.pagination().entityCountOnPage(), equalTo(10));
        assertThat(result.pagination().pageCount(), equalTo(1));
    }

    @Test
    public void shouldSearchCommentReturnDisabledAndNonDeletedCommentsOnly() throws CommunicationFailureException {

        // given
        CommentSearchParameters commentSearchParameters = new CommentSearchParameters();
        commentSearchParameters.setDeleted(false);
        commentSearchParameters.setEnabled(false);

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.searchComments(commentSearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(2L));
        assertThat(result.pagination().entityCountOnPage(), equalTo(2));
        assertThat(result.pagination().pageCount(), equalTo(1));
        assertThat(result.body()
                .comments().stream()
                .allMatch(ExtendedCommentDataModel::deleted), is(false));
        assertThat(result.body()
                .comments().stream()
                .allMatch(ExtendedCommentDataModel::enabled), is(false));
    }

    @Test
    public void shouldSearchCommentReturnDeletedCommentsOnly() throws CommunicationFailureException {

        // given
        CommentSearchParameters commentSearchParameters = new CommentSearchParameters();
        commentSearchParameters.setDeleted(true);
        commentSearchParameters.setLimit(3);

        // when
        WrapperBodyDataModel<ExtendedCommentListDataModel> result = commentBridgeService.searchComments(commentSearchParameters);

        // then
        assertThat(result.pagination().entityCount(), equalTo(5L));
        assertThat(result.pagination().entityCountOnPage(), equalTo(3));
        assertThat(result.pagination().pageCount(), equalTo(2));
        assertThat(result.body()
                .comments().stream()
                .allMatch(ExtendedCommentDataModel::deleted), is(true));
    }

    @Test
    @ResetDatabase
    public void shouldCreateCommentWithAuthenticatedUser() throws CommunicationFailureException {

        // given
        CommentCreateRequestModel control = getControl(CONTROL_COMMENT, CONTROL_SUFFIX_CREATE, CommentCreateRequestModel.class);
        CommentNotification expectedCommentNotification = prepareCommentNotification();

        // when
        CommentDataModel result = commentBridgeService.createComment(control, RECAPTCHA_TOKEN);

        // then
        assertThat(result, notNullValue());
        ExtendedCommentDataModel current = commentBridgeService.getComment(result.id());
        assertThat(current.content(), equalTo(control.getContent()));
        assertThat(current.associatedEntry().id(), equalTo(control.getEntryId()));
        assertThat(current.owner().id(), equalTo(1L));
        assertThat(notificationService.getCommentNotification(), notNullValue());
        assertThat(notificationService.getCommentNotification(), equalTo(expectedCommentNotification));
    }

    @Test
    @ResetDatabase
    public void shouldCommentCreationFailForAnonymousUser() throws CommunicationFailureException {

        // given
        clearAuthentication();
        CommentCreateRequestModel control = getControl(CONTROL_COMMENT, CONTROL_SUFFIX_CREATE, CommentCreateRequestModel.class);

        // when
        try {
            commentBridgeService.createComment(control, RECAPTCHA_TOKEN);
            fail("Test case should have thrown exception.");
        } catch (UnauthorizedAccessException e) {

            // then
            // exception expected
            restoreAuthentication();
        }
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
        assertThat(current.content(), equalTo(commentUpdateRequestModel.getContent()));
    }

    @Test
    @ResetDatabase
    public void shouldUpdateCommentStatus() throws CommunicationFailureException {

        // when
        commentBridgeService.changeStatus(CONTROL_COMMENT_ID);

        // then
        assertThat(commentBridgeService.getComment(CONTROL_COMMENT_ID).enabled(), is(false));
        assertThat(commentBridgeService.getPageOfPublicCommentsForEntry(CONTROL_ENTRY_LINK, 1, 20, CREATED, ASC).body().comments().stream()
                .noneMatch(commentDataModel -> CONTROL_COMMENT_ID.equals(commentDataModel.id())), is(true));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteCommentLogically() throws CommunicationFailureException {

        // given
        assertThat(commentBridgeService.getComment(CONTROL_COMMENT_ID).lastModified().getYear(), equalTo(2017));

        // when
        commentBridgeService.deleteCommentLogically(CONTROL_COMMENT_ID);

        // then
        ExtendedCommentDataModel current = commentBridgeService.getComment(CONTROL_COMMENT_ID);
        assertThat(current.deleted(), is(true));
        assertThat(current.content(), equalTo(DELETED_COMMENT));
        assertThat(current.lastModified().getYear() >= 2019, is(true));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteCommentPermanently() throws CommunicationFailureException {

        // when
        commentBridgeService.deleteCommentPermanently(CONTROL_COMMENT_ID);

        // then
        // this call should cause exception
        Assertions.assertThrows(ResourceNotFoundException.class, () -> commentBridgeService.getComment(CONTROL_COMMENT_ID));
    }

    private CommentNotification prepareCommentNotification() {

        return CommentNotification.getBuilder()
                .withEmail("test-admin@ac-leaflet.local")
                .withUsername("Administrator")
                .withContent("lorem ipsum dolor sit amet comment")
                .withAuthorEmail("test-admin@ac-leaflet.local")
                .withAuthorName("Administrator")
                .withEntryTitle("Entry #01 title")
                .build();
    }

    private void assertLogicallyDeletedComments(List<CommentDataModel> result, int expectedLogicallyDeletedCount) {
        assertThat(result.stream()
                .filter(CommentDataModel::deleted)
                .count(), equalTo((long) expectedLogicallyDeletedCount));
        assertThat(result.stream()
                .filter(CommentDataModel::deleted)
                .allMatch(commentDataModel -> DELETED_COMMENT.equals(commentDataModel.content())), is(true));
    }

    private void assertLogicallyDeletedExtendedComments(List<ExtendedCommentDataModel> result, int expectedLogicallyDeletedCount) {
        assertThat(result.stream()
                .filter(ExtendedCommentDataModel::deleted)
                .count(), equalTo((long) expectedLogicallyDeletedCount));
        assertThat(result.stream()
                .filter(ExtendedCommentDataModel::deleted)
                .allMatch(commentDataModel -> DELETED_COMMENT.equals(commentDataModel.content())), is(true));
    }

    private void assertEnabledExtendedComments(List<ExtendedCommentDataModel> result, int expectedLogicallyDeletedCount) {
        assertThat(result.stream()
                .filter(ExtendedCommentDataModel::enabled)
                .count(), equalTo((long) expectedLogicallyDeletedCount));
    }

    private Comparator<CommentDataModel> getComparator(OrderBy.Comment orderBy, OrderDirection orderDirection) {

        return orderBy == CREATED && orderDirection == ASC
                ? ASC_CREATED
                : DESC_CREATED;
    }

    private Comparator<ExtendedCommentDataModel> getExtendedComparator(OrderBy.Comment orderBy, OrderDirection orderDirection) {

        return orderBy == CREATED && orderDirection == ASC
                ? ASC_CREATED_EXTENDED
                : DESC_CREATED_EXTENDED;
    }

    private void assertPaginatedResult(WrapperBodyDataModel<CommentListDataModel> result, Comparator<CommentDataModel> comparator,
                                       long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious) {

        assertThat(result, notNullValue());
        assertPagination(result, expectedEntityCount, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertThat(result.body().comments().size(), equalTo(expectedBodySize));
        assertThat(result.body().comments().stream()
                .sorted(comparator)
                .toList()
                .equals(result.body().comments()), is(true));
    }

    private void assertPaginatedExtendedResult(WrapperBodyDataModel<ExtendedCommentListDataModel> result, Comparator<ExtendedCommentDataModel> comparator,
                                               long expectedEntityCount, int expectedBodySize, int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious) {

        assertThat(result, notNullValue());
        assertPagination(result, expectedEntityCount, expectedPageCount, expectedHasNext, expectedHasPrevious);
        assertThat(result.body().comments().size(), equalTo(expectedBodySize));
        assertThat(result.body().comments().stream()
                .sorted(comparator)
                .toList()
                .equals(result.body().comments()), is(true));
    }

    private void assertPagination(WrapperBodyDataModel<?> result, long expectedEntityCount,
                                  int expectedPageCount, boolean expectedHasNext, boolean expectedHasPrevious) {
        assertThat(result.pagination(), notNullValue());
        assertThat(result.pagination().pageCount(), equalTo(expectedPageCount));
        assertThat(result.pagination().hasNext(), equalTo(expectedHasNext));
        assertThat(result.pagination().hasPrevious(), equalTo(expectedHasPrevious));
        assertThat(result.pagination().entityCount(), equalTo(expectedEntityCount));
    }

    private static Stream<Arguments> pageOfCommentsDataProvider() {

        return Stream.of(
                // entry ID, page, limit, order by, order direction, exp. all comments, exp. body size, exp. num. of pages, exp. has next, exp. has previous, logically deleted
                Arguments.of(1, 1, 4, CREATED, ASC, 10, 4, 3, true, false, 1),
                Arguments.of(1, 2, 4, CREATED, ASC, 10, 4, 3, true, true, 2),
                Arguments.of(1, 3, 4, CREATED, ASC, 10, 2, 3, false, true, 2),
                Arguments.of(2, 1, 4, CREATED, ASC, 0, 0, 0, false, false, 0),
                Arguments.of(1, 1, 20, CREATED, DESC, 10, 10, 1, false, false, 5)
        );
    }

    private static Stream<Arguments> pageOfPublicCommentsDataProvider() {

        return Stream.of(
                // entry ID, page, limit, order by, order direction, exp. all comments, exp. body size, exp. num. of pages, exp. has next, exp. has previous, logically deleted
                Arguments.of(CONTROL_ENTRY_LINK, 1, 4, CREATED, ASC, 7, 4, 2, true, false, 1),
                Arguments.of(CONTROL_ENTRY_LINK, 2, 4, CREATED, ASC, 7, 3, 2, false, true, 3),
                Arguments.of(CONTROL_ENTRY_LINK, 3, 4, CREATED, ASC, 7, 0, 2, false, true, 0),
                Arguments.of(ENTRY_WITHOUT_COMMENTS_LINK, 1, 4, CREATED, ASC, 0, 0, 0, false, false, 0),
                Arguments.of(ENTRY_NON_EXISTING_LINK, 1, 4, CREATED, ASC, 0, 0, 0, false, false, 0),
                Arguments.of(CONTROL_ENTRY_LINK, 1, 20, CREATED, DESC, 7, 7, 1, false, false, 4)
        );
    }

    private static Stream<Arguments> pageOfCommentsForUserDataProvider() {

        return Stream.of(
                // user ID, page, limit, order by, order direction, exp. all comments, exp. body size, exp. num. of pages, exp. has next, exp. has previous, logically deleted, enabled
                Arguments.of(1, 1, 4, CREATED, ASC, 0, 0, 0, false, false, 0, 0),
                Arguments.of(2, 1, 4, CREATED, ASC, 6, 4, 2, true, false, 1, 4),
                Arguments.of(2, 2, 4, CREATED, ASC, 6, 2, 2, false, true, 2, 2),
                Arguments.of(4, 1, 10, CREATED, ASC, 4, 4, 1, false, false, 2, 1)
        );
    }
}
