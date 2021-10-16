package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.facade.TagFacade;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import hu.psprog.leaflet.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link TagsController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class TagsControllerTest extends AbstractControllerBaseTest {

    private static final List<TagVO> TAG_VO_LIST = Collections.singletonList(TAG_VO);
    private static final long TAG_ID = 1L;
    private static final String LOCATION_HEADER = "/tags/" + TAG_ID;

    @Mock
    private TagFacade tagFacade;

    @InjectMocks
    private TagsController controller;

    @BeforeEach
    public void setup() {
        super.setup();
        given(conversionService.convert(TAG_VO_LIST, TagListDataModel.class)).willReturn(TAG_LIST_DATA_MODEL);
        given(conversionService.convert(TAG_VO, TagDataModel.class)).willReturn(TAG_DATA_MODEL);
    }

    @Test
    public void shouldGetAllTags() {

        // given
        given(tagFacade.getAll()).willReturn(TAG_VO_LIST);

        // when
        ResponseEntity<TagListDataModel> result = controller.getAllTags();

        // then
        assertResponse(result, HttpStatus.OK, TAG_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetAllPublicTags() {

        // given
        given(tagFacade.getPublicTags()).willReturn(TAG_VO_LIST);

        // when
        ResponseEntity<TagListDataModel> result = controller.getAllPublicTags();

        // then
        assertResponse(result, HttpStatus.OK, TAG_LIST_DATA_MODEL);
    }

    @Test
    public void shouldGetTag() throws ServiceException, ResourceNotFoundException {

        // given
        given(tagFacade.getOne(TAG_ID)).willReturn(TAG_VO);

        // when
        ResponseEntity<TagDataModel> result = controller.getTag(TAG_ID);

        // then
        assertResponse(result, HttpStatus.OK, TAG_DATA_MODEL);
    }

    @Test
    public void shouldGetTagWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(tagFacade).getOne(TAG_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.getTag(TAG_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldCreateTag() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(TAG_CREATE_REQUEST_MODEL, TagVO.class)).willReturn(TAG_VO);
        given(tagFacade.createOne(TAG_VO)).willReturn(TAG_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createTag(TAG_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, TAG_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldCreateTagWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.createTag(TAG_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldCreateTagWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(TAG_CREATE_REQUEST_MODEL, TagVO.class)).willReturn(TAG_VO);
        doThrow(ServiceException.class).when(tagFacade).createOne(TAG_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.createTag(TAG_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdateTag() throws ServiceException, RequestCouldNotBeFulfilledException {

        // given
        given(conversionService.convert(TAG_CREATE_REQUEST_MODEL, TagVO.class)).willReturn(TAG_VO);
        given(tagFacade.updateOne(TAG_ID, TAG_VO)).willReturn(TAG_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateTag(TAG_ID, TAG_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, TAG_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldUpdateTagWithValidationError() throws RequestCouldNotBeFulfilledException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.updateTag(TAG_ID, TAG_CREATE_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldUpdateTagWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(TAG_CREATE_REQUEST_MODEL, TagVO.class)).willReturn(TAG_VO);
        doThrow(ServiceException.class).when(tagFacade).updateOne(TAG_ID, TAG_VO);

        // when
        Assertions.assertThrows(RequestCouldNotBeFulfilledException.class, () -> controller.updateTag(TAG_ID, TAG_CREATE_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldDeleteTag() throws ResourceNotFoundException, ServiceException {

        // when
        controller.deleteTag(TAG_ID);

        // then
        verify(tagFacade).deletePermanently(TAG_ID);
    }

    @Test
    public void shouldDeleteTagWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(tagFacade).deletePermanently(TAG_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.deleteTag(TAG_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldChangeStatus() throws ServiceException, ResourceNotFoundException {

        // given
        given(tagFacade.changeStatus(TAG_ID)).willReturn(TAG_VO);

        // when
        ResponseEntity<TagDataModel> result = controller.changeStatus(TAG_ID);

        // then
        assertResponse(result, HttpStatus.CREATED, TAG_DATA_MODEL, LOCATION_HEADER);
    }

    @Test
    public void shouldChangeStatusWithServiceException() throws ServiceException {

        // given
        doThrow(ServiceException.class).when(tagFacade).changeStatus(TAG_ID);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.changeStatus(TAG_ID));

        // then
        // exception expected
    }

    @Test
    public void shouldAttachTag() throws ResourceNotFoundException, ServiceException {

        // given
        given(conversionService.convert(TAG_ASSIGNMENT_REQUEST_MODEL, TagAssignmentVO.class)).willReturn(TAG_ASSIGNMENT_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.attachTag(TAG_ASSIGNMENT_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.CREATED, null);
        verify(tagFacade).attachTagToEntry(TAG_ASSIGNMENT_VO);
    }

    @Test
    public void shouldAttachTagWithValidationError() throws ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.attachTag(TAG_ASSIGNMENT_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldAttachTagWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(TAG_ASSIGNMENT_REQUEST_MODEL, TagAssignmentVO.class)).willReturn(TAG_ASSIGNMENT_VO);
        doThrow(ServiceException.class).when(tagFacade).attachTagToEntry(TAG_ASSIGNMENT_VO);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.attachTag(TAG_ASSIGNMENT_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }

    @Test
    public void shouldDetachTag() throws ResourceNotFoundException, ServiceException {

        // given
        given(conversionService.convert(TAG_ASSIGNMENT_REQUEST_MODEL, TagAssignmentVO.class)).willReturn(TAG_ASSIGNMENT_VO);

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.detachTag(TAG_ASSIGNMENT_REQUEST_MODEL, bindingResult);

        // then
        assertResponse(result, HttpStatus.NO_CONTENT, null);
        verify(tagFacade).detachTagFromEntry(TAG_ASSIGNMENT_VO);
    }

    @Test
    public void shouldDetachTagWithValidationError() throws ResourceNotFoundException {

        // given
        givenValidationError();

        // when
        ResponseEntity<BaseBodyDataModel> result = controller.detachTag(TAG_ASSIGNMENT_REQUEST_MODEL, bindingResult);

        // then
        assertValidationError(result);
    }

    @Test
    public void shouldDetachTagWithServiceException() throws ServiceException {

        // given
        given(conversionService.convert(TAG_ASSIGNMENT_REQUEST_MODEL, TagAssignmentVO.class)).willReturn(TAG_ASSIGNMENT_VO);
        doThrow(ServiceException.class).when(tagFacade).detachTagFromEntry(TAG_ASSIGNMENT_VO);

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> controller.detachTag(TAG_ASSIGNMENT_REQUEST_MODEL, bindingResult));

        // then
        // exception expected
    }
}