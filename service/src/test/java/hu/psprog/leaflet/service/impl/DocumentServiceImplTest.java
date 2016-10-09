package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.service.converter.DocumentToDocumentVOConverter;
import hu.psprog.leaflet.service.converter.DocumentVOToDocumentConverter;
import hu.psprog.leaflet.service.converter.UserVOToUserConverter;
import hu.psprog.leaflet.service.exception.EntityCreationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentServiceImpl} class.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceImplTest {

    @Mock
    private DocumentDAO documentDAO;

    @Mock
    private DocumentToDocumentVOConverter documentToDocumentVOConverter;

    @Mock
    private DocumentVOToDocumentConverter documentVOToDocumentConverter;

    @Mock
    private UserVOToUserConverter userVOToUserConverter;

    @Mock
    private Document document;

    @Mock
    private DocumentVO documentVO;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test
    public void testGetOneWithExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        given(documentDAO.findOne(id)).willReturn(document);
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        DocumentVO result = documentService.getOne(id);

        // then
        assertThat(result, equalTo(documentVO));
        verify(documentDAO).findOne(id);
        verify(documentToDocumentVOConverter).convert(document);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetOneWithNonExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        given(documentDAO.findOne(id)).willReturn(null);

        // when
        DocumentVO result = documentService.getOne(id);

        // then
        // expected exception
        assertThat(result, equalTo(documentVO));
        verify(documentDAO).findOne(id);
        verify(documentToDocumentVOConverter, never()).convert(document);
    }

    @Test
    public void testGetAllWithPopulatedList() {

        // given
        List<DocumentVO> documentVOList = Arrays.asList(documentVO, documentVO, documentVO);
        given(documentDAO.findAll()).willReturn(Arrays.asList(document, document, document));
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        List<DocumentVO> result = documentService.getAll();

        // then
        assertThat(result, equalTo(documentVOList));
        verify(documentDAO).findAll();
        verify(documentToDocumentVOConverter, times(3)).convert(document);
    }

    @Test
    public void testGetAllWithEmptyList() {

        // given
        given(documentDAO.findAll()).willReturn(new LinkedList<>());

        // when
        List<DocumentVO> result = documentService.getAll();

        // then
        assertThat(result, empty());
        verify(documentDAO).findAll();
        verify(documentToDocumentVOConverter, never()).convert(any(Document.class));
    }

    @Test
    public void testGetByLinkWithExistingDocument() throws ServiceException {

        // given
        String link = "document-link";
        given(documentDAO.findByLink(link)).willReturn(document);
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        DocumentVO result = documentService.getByLink(link);

        // then
        assertThat(result, equalTo(documentVO));
        verify(documentDAO).findByLink(link);
        verify(documentToDocumentVOConverter).convert(document);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetByLinkWithNonExistingDocument() throws ServiceException {

        // given
        String link = "document-link";
        given(documentDAO.findByLink(link)).willReturn(null);

        // when
        DocumentVO result = documentService.getByLink(link);

        // then
        // expected exception
        assertThat(result, equalTo(documentVO));
        verify(documentDAO).findByLink(link);
        verify(documentToDocumentVOConverter, never()).convert(any(Document.class));
    }

    @Test
    public void testCreateOneWithSuccess() throws ServiceException {

        // given
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        given(documentDAO.save(document)).willReturn(document);
        given(document.getId()).willReturn(1L);

        // when
        Long result = documentService.createOne(documentVO);

        // then
        assertThat(result, equalTo(document.getId()));
        verify(documentVOToDocumentConverter).convert(documentVO);
        verify(documentDAO).save(document);
        verify(document, atLeastOnce()).getId();
    }

    @Test(expected = EntityCreationException.class)
    public void testCreateOneWithFailure() throws ServiceException {

        // given
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        given(documentDAO.save(document)).willReturn(null);

        // when
        documentService.createOne(documentVO);

        // then
        // expected exception
        verify(documentVOToDocumentConverter).convert(documentVO);
        verify(documentDAO).save(document);
        verify(document, never()).getId();
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        given(documentDAO.updateOne(id, document)).willReturn(document);
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        DocumentVO result = documentService.updateOne(id, documentVO);

        // then
        assertThat(result, equalTo(documentVO));
        verify(documentToDocumentVOConverter).convert(document);
        verify(documentVOToDocumentConverter).convert(documentVO);
        verify(documentDAO).updateOne(id, document);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateOneWithFailure() throws ServiceException {

        // given
        Long id = 1L;
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        given(documentDAO.updateOne(id, document)).willReturn(null);

        // when
        documentService.updateOne(id, documentVO);

        // then
        // expected exception
        verify(documentToDocumentVOConverter, never()).convert(document);
        verify(documentVOToDocumentConverter).convert(documentVO);
        verify(documentDAO).updateOne(id, document);
    }

    @Test
    public void testDeleteByEntityWithExistingDocument() throws ServiceException {

        // given
        given(documentDAO.exists(documentVO.getId())).willReturn(true);

        // when
        documentService.deleteByEntity(documentVO);

        // then
        verify(documentDAO).delete(documentVO.getId());
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByEntityWithNonExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        given(documentDAO.exists(documentVO.getId())).willReturn(false);
        given(documentVO.getId()).willReturn(1L);

        // when
        documentService.deleteByEntity(documentVO);

        // then
        verify(documentDAO, never()).delete(any(Long.class));
    }

    @Test
    public void testDeleteByIDWithExistingDocument() throws ServiceException {

        // given
        Long id = 1L;

        // when
        documentService.deleteByID(id);

        // then
        verify(documentDAO).delete(id);
    }


    @Test(expected = EntityNotFoundException.class)
    public void testDeleteByIDWithNonExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        doThrow(IllegalArgumentException.class).when(documentDAO).delete(id);

        // when
        documentService.deleteByID(id);

        // then
        verify(documentDAO).delete(id);
    }

    @Test
    public void testEnableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(true);

        // when
        documentService.enable(id);

        // then
        verify(documentDAO).exists(id);
        verify(documentDAO).enable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testEnableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(false);

        // when
        documentService.enable(id);

        // then
        verify(documentDAO).exists(id);
        verify(documentDAO, never()).enable(any(Long.class));
    }

    @Test
    public void testDisableWithSuccess() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(true);

        // when
        documentService.disable(id);

        // then
        verify(documentDAO).exists(id);
        verify(documentDAO).disable(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDisableWithFailure() throws EntityNotFoundException {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(false);

        // when
        documentService.disable(id);

        // then
        verify(documentDAO).exists(id);
        verify(documentDAO, never()).disable(any(Long.class));
    }
}
