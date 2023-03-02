package hu.psprog.leaflet.service.facade.impl;

import hu.psprog.leaflet.service.DocumentService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DocumentFacadeImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DocumentFacadeImplTest {

    private static final long DOCUMENT_ID = 3L;
    private static final DocumentVO DOCUMENT_VO = DocumentVO.wrapMinimumVO(DOCUMENT_ID);

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentFacadeImpl documentFacade;

    @Test
    public void shouldGetOne() throws ServiceException {

        // when
        documentFacade.getOne(DOCUMENT_ID);

        // then
        verify(documentService).getOne(DOCUMENT_ID);
    }

    @Test
    public void shouldGetByLink() throws ServiceException {

        // given
        String link = "link-1";

        // when
        documentFacade.getByLink(link);

        // then
        verify(documentService).getByLink(link);
    }

    @Test
    public void shouldGetAll() {

        // when
        documentFacade.getAll();

        // then
        verify(documentService).getAll();
    }

    @Test
    public void shouldGetAllPublic() {

        // when
        documentFacade.getPublicDocuments();

        // then
        verify(documentService).getPublicDocuments();
    }

    @Test
    public void shouldCreateOne() throws ServiceException {

        // given
        given(documentService.createOne(DOCUMENT_VO)).willReturn(DOCUMENT_ID);

        // when
        documentFacade.createOne(DOCUMENT_VO);

        // then
        verify(documentService).createOne(DOCUMENT_VO);
        verify(documentService).getOne(DOCUMENT_ID);
    }

    @Test
    public void shouldUpdateOne() throws ServiceException {

        // when
        documentFacade.updateOne(DOCUMENT_ID, DOCUMENT_VO);

        // then
        verify(documentService).updateOne(DOCUMENT_ID, DOCUMENT_VO);
        verify(documentService).getOne(DOCUMENT_ID);
    }

    @Test
    public void shouldDeletePermanently() throws ServiceException {

        // when
        documentFacade.deletePermanently(DOCUMENT_ID);

        // then
        verify(documentService).deleteByID(DOCUMENT_ID);
    }

    @Test
    public void shouldChangeStatusToEnabled() throws ServiceException {

        // given
        given(documentService.getOne(DOCUMENT_ID)).willReturn(DOCUMENT_VO);

        // when
        documentFacade.changeStatus(DOCUMENT_ID);

        // then
        verify(documentService).enable(DOCUMENT_ID);
    }

    @Test
    public void shouldChangeStatusToDisabled() throws ServiceException {

        // given
        given(documentService.getOne(DOCUMENT_ID)).willReturn(DocumentVO.getBuilder()
                .withEnabled(true)
                .build());

        // when
        documentFacade.changeStatus(DOCUMENT_ID);

        // then
        verify(documentService).disable(DOCUMENT_ID);
    }
}