package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DocumentDAO;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.repository.specification.DocumentSpecification;
import hu.psprog.leaflet.service.converter.DocumentToDocumentVOConverter;
import hu.psprog.leaflet.service.converter.DocumentVOToDocumentConverter;
import hu.psprog.leaflet.service.exception.ConstraintViolationException;
import hu.psprog.leaflet.service.exception.EntityNotFoundException;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTest {

    @Mock
    private DocumentDAO documentDAO;

    @Mock
    private DocumentToDocumentVOConverter documentToDocumentVOConverter;

    @Mock
    private DocumentVOToDocumentConverter documentVOToDocumentConverter;

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
        given(documentDAO.findById(id)).willReturn(Optional.of(document));
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        DocumentVO result = documentService.getOne(id);

        // then
        assertThat(result, equalTo(documentVO));
        verify(documentDAO).findById(id);
        verify(documentToDocumentVOConverter).convert(document);
    }

    @Test
    public void testGetOneWithNonExistingDocument() {

        // given
        Long id = 1L;
        given(documentDAO.findById(id)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> documentService.getOne(id));

        // then
        // expected exception
        verify(documentDAO).findById(id);
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
    public void testGetPublicDocuments() {

        // given
        List<DocumentVO> documentVOList = Arrays.asList(documentVO, documentVO, documentVO);
        given(documentDAO.findAll(DocumentSpecification.IS_ENABLED)).willReturn(Arrays.asList(document, document, document));
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        List<DocumentVO> result = documentService.getPublicDocuments();

        // then
        assertThat(result, equalTo(documentVOList));
        verify(documentDAO).findAll(DocumentSpecification.IS_ENABLED);
        verify(documentToDocumentVOConverter, times(3)).convert(document);
    }

    @Test
    public void testGetByLinkWithExistingDocument() throws ServiceException {

        // given
        String link = "document-link";
        given(documentDAO.findByLink(link)).willReturn(Optional.of(document));
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        DocumentVO result = documentService.getByLink(link);

        // then
        assertThat(result, equalTo(documentVO));
        verify(documentDAO).findByLink(link);
        verify(documentToDocumentVOConverter).convert(document);
    }

    @Test
    public void testGetByLinkWithNonExistingDocument() {

        // given
        String link = "document-link";
        given(documentDAO.findByLink(link)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> documentService.getByLink(link));

        // then
        // expected exception
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

    @Test
    public void testCreateShouldThrowConstraintViolationException() {

        // given
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        doThrow(DataIntegrityViolationException.class).when(documentDAO).save(document);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> documentService.createOne(documentVO));

        // then
        // exception expected
    }

    @Test
    public void testCreateShouldThrowServiceException() {

        // given
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        doThrow(IllegalArgumentException.class).when(documentDAO).save(document);

        // when
        Assertions.assertThrows(ServiceException.class, () -> documentService.createOne(documentVO));

        // then
        // exception expected
    }

    @Test
    public void testUpdateOneWithSuccess() throws ServiceException {

        // given
        Long id = 1L;
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        given(documentDAO.updateOne(id, document)).willReturn(Optional.of(document));
        given(documentToDocumentVOConverter.convert(document)).willReturn(documentVO);

        // when
        DocumentVO result = documentService.updateOne(id, documentVO);

        // then
        assertThat(result, equalTo(documentVO));
        verify(documentToDocumentVOConverter).convert(document);
        verify(documentVOToDocumentConverter).convert(documentVO);
        verify(documentDAO).updateOne(id, document);
    }

    @Test
    public void testUpdateOneWithFailure() {

        // given
        Long id = 1L;
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        given(documentDAO.updateOne(id, document)).willReturn(Optional.empty());

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> documentService.updateOne(id, documentVO));

        // then
        // expected exception
        verify(documentToDocumentVOConverter, never()).convert(document);
        verify(documentVOToDocumentConverter).convert(documentVO);
        verify(documentDAO).updateOne(id, document);
    }

    @Test
    public void testUpdateShouldThrowConstraintViolationException() {

        // given
        Long id = 1L;
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        doThrow(DataIntegrityViolationException.class).when(documentDAO).updateOne(id, document);

        // when
        Assertions.assertThrows(ConstraintViolationException.class, () -> documentService.updateOne(id, documentVO));

        // then
        // exception expected
    }

    @Test
    public void testUpdateShouldThrowServiceException() {

        // given
        Long id = 1L;
        given(documentVOToDocumentConverter.convert(documentVO)).willReturn(document);
        doThrow(IllegalArgumentException.class).when(documentDAO).updateOne(id, document);

        // when
        Assertions.assertThrows(ServiceException.class, () -> documentService.updateOne(id, documentVO));

        // then
        // exception expected
    }

    @Test
    public void testDeleteByIDWithExistingDocument() throws ServiceException {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(true);

        // when
        documentService.deleteByID(id);

        // then
        verify(documentDAO).delete(id);
    }


    @Test
    public void testDeleteByIDWithNonExistingDocument() {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> documentService.deleteByID(id));

        // then
        // expected exception
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

    @Test
    public void testEnableWithFailure() {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> documentService.enable(id));

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

    @Test
    public void testDisableWithFailure() {

        // given
        Long id = 1L;
        given(documentDAO.exists(id)).willReturn(false);

        // when
        Assertions.assertThrows(EntityNotFoundException.class, () -> documentService.disable(id));

        // then
        verify(documentDAO).exists(id);
        verify(documentDAO, never()).disable(any(Long.class));
    }
}
