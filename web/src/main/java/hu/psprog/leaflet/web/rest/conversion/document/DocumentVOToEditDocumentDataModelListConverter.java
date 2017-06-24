package hu.psprog.leaflet.web.rest.conversion.document;

import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.service.vo.DocumentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link List} of {@link DocumentVO} value objects to {@link DocumentListDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class DocumentVOToEditDocumentDataModelListConverter implements Converter<List<DocumentVO>, DocumentListDataModel> {

    private DocumentVOToEditDocumentDataModelEntityConverter documentVOToEditDocumentDataModelEntityConverter;

    @Autowired
    public DocumentVOToEditDocumentDataModelListConverter(DocumentVOToEditDocumentDataModelEntityConverter documentVOToEditDocumentDataModelEntityConverter) {
        this.documentVOToEditDocumentDataModelEntityConverter = documentVOToEditDocumentDataModelEntityConverter;
    }

    @Override
    public DocumentListDataModel convert(List<DocumentVO> source) {

        DocumentListDataModel.DocumentListDataModelBuilder builder = DocumentListDataModel.getBuilder();
        source.stream()
                .map(documentVOToEditDocumentDataModelEntityConverter::convert)
                .forEach(builder::withItem);

        return builder.build();
    }
}
