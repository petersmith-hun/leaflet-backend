package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.CommentFacade;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntityPageVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link CommentsController}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentsControllerTest extends AbstractControllerBaseTest {

    private static final long ENTRY_ID = 1L;
    private static final long COMMENT_ID = 2L;
    private static final String LOCATION_HEADER = "/comments/" + COMMENT_ID;
    private static final CommentDataModel COMMENT_DATA_MODEL_AFTER_CREATE = CommentDataModel.getBuilder().withId(COMMENT_ID).build();
    private static final List<CommentVO> COMMENT_VO_LIST = Collections.singletonList(COMMENT_VO);

    @Mock
    private CommentFacade commentFacade;

    @InjectMocks
    private CommentsController controller;

    @Before
    public void setup() {
        super.setup();
        given(conversionService.convert(COMMENT_VO_LIST, CommentListDataModel.class)).willReturn(COMMENT_LIST_DATA_MODEL);
        given(conversionService.convert(COMMENT_VO, ExtendedCommentDataModel.class)).willReturn(EXTENDED_COMMENT_DATA_MODEL);
        given(conversionService.convert(COMMENT_VO, CommentDataModel.class)).willReturn(COMMENT_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfPublicCommentsForEntry() {

        // given
        given(commentFacade.getPageOfPublicCommentsForEntry(ENTRY_ID, PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.getBuilder()
                .withEntitiesOnPage(COMMENT_VO_LIST)
                .build());

        // when
        ResponseEntity<CommentListDataModel> result = controller.getPageOfPublicCommentsForEntry(ENTRY_ID, PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, COMMENT_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetPageOfCommentsForEntry() {

        // given
        given(commentFacade.getPageOfCommentsForEntry(ENTRY_ID, PAGE, LIMIT, DIRECTION, ORDER_BY)).willReturn(EntityPageVO.getBuilder()
                .withEntitiesOnPage(COMMENT_VO_LIST)
                .build());

        // when
        ResponseEntity<CommentListDataModel> result = controller.getPageOfCommentsForEntry(ENTRY_ID, PAGE, LIMIT, ORDER_BY, DIRECTION);

        // then
        assertResponse(result, HttpStatus.OK, COMMENT_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetCommentsById() throws ServiceException, ResourceNotFoundException {

        // given
        given(commentFacade.getOne(COMMENT_ID)).willReturn(COMMENT_VO);

        // when
        ResponseEntity<ExtendedCommentDataModel> result = controller.getCommentById(COMMENT_ID);

        // then
        assertResponse(result, HttpStatus.OK, EXTENDED_COMMENT_DATA_MODEL);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldGetCommentsByIdWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(commentFacade).getOne(COMMENT_ID);

        // when
        controller.getCommentById(COMMENT_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldCreateComment() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(COMMENT_CREATE_REQUEST_MODEL, CommentVO.class)).willReturn(COMMENT_VO);
        given(commentFacade.createOne(COMMENT_VO)).willReturn(COMMENT_ID);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createComment(COMMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, COMMENT_DATA_MODEL_AFTER_CREATE, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateCommentWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createComment(COMMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateCommentWithConstraintViolation() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(COMMENT_CREATE_REQUEST_MODEL, CommentVO.class)).willReturn(COMMENT_VO);
        doThrow(ConstraintViolationException.class).when(commentFacade).createOne(COMMENT_VO);

        // when
        controller.createComment(COMMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test(expected = RequestCouldNotBeFulfilledException.class)
    public void shouldCreateCommentWithServiceException() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(COMMENT_CREATE_REQUEST_MODEL, CommentVO.class)).willReturn(COMMENT_VO);
        doThrow(ServiceException.class).when(commentFacade).createOne(COMMENT_VO);

        // when
        controller.createComment(COMMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateComment() throws ServiceException, ResourceNotFoundException {

        // given
        given(conversionService.convert(COMMENT_UPDATE_REQUEST_MODEL, CommentVO.class)).willReturn(COMMENT_VO);
        given(commentFacade.updateOne(COMMENT_ID, COMMENT_VO)).willReturn(COMMENT_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateComment(COMMENT_ID, COMMENT_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, COMMENT_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateCommentWithValidationError() throws ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateComment(COMMENT_ID, COMMENT_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldUpdateCommentWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        given(conversionService.convert(COMMENT_UPDATE_REQUEST_MODEL, CommentVO.class)).willReturn(COMMENT_VO);
        doThrow(ServiceException.class).when(commentFacade).updateOne(COMMENT_ID, COMMENT_VO);

        // when
        controller.updateComment(COMMENT_ID, COMMENT_UPDATE_REQUEST_MODEL, bindingResult);

        // then
        // exception expected
    }

    @Test
    public void shouldChangeCommentStatus() throws ServiceException, ResourceNotFoundException {

        // given
        given(commentFacade.changeStatus(COMMENT_ID)).willReturn(COMMENT_VO);

        // when
        ResponseEntity<ExtendedCommentDataModel> result = controller.changeCommentStatus(COMMENT_ID);

        // then
        assertResponse(result, HttpStatus.CREATED, EXTENDED_COMMENT_DATA_MODEL, LOCATION_HEADER);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldChangeCommentStatusWithServiceException() throws ServiceException, ResourceNotFoundException {

        // given
        doThrow(ServiceException.class).when(commentFacade).changeStatus(COMMENT_ID);

        // when
        controller.changeCommentStatus(COMMENT_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteCommentLogically() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteCommentLogically(COMMENT_ID);

        // then
        verify(commentFacade).deleteLogically(COMMENT_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDeleteCommentLogicallyWithServiceException() throws ResourceNotFoundException, ServiceException {

        // given
        doThrow(ServiceException.class).when(commentFacade).deleteLogically(COMMENT_ID);

        // when
        controller.deleteCommentLogically(COMMENT_ID);

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteCommentPermanently() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteCommentPermanently(COMMENT_ID);

        // then
        verify(commentFacade).deletePermanently(COMMENT_ID);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldDeleteCommentPermanentlyWithServiceException() throws ResourceNotFoundException, ServiceException {

        // given
        doThrow(ServiceException.class).when(commentFacade).deletePermanently(COMMENT_ID);

        // when
        controller.deleteCommentPermanently(COMMENT_ID);

        // then
        // exception expected
    }
}