package hu.psprog.leaflet.web.rest.conversion;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageDataModel;
import hu.psprog.leaflet.api.rest.response.layout.DefaultListLayoutDataModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Converts {@link List} of {@link ObjectError} objects to Vali
 *
 * @author Peter Smith
 */
@Component
public class ValidationErrorMessagesConverter implements Converter<List<ObjectError>, BaseBodyDataModel> {

    private static final String LIST_NODE_NAME = "errors";

    @Override
    public BaseBodyDataModel convert(List<ObjectError> objectErrors) {

        DefaultListLayoutDataModel.Builder responseBuilder = new DefaultListLayoutDataModel.Builder();
        responseBuilder.setNodeName(LIST_NODE_NAME);
        objectErrors.forEach(userVO -> responseBuilder.withItem(convert(userVO)));

        return responseBuilder.build();
    }

    private BaseBodyDataModel convert(ObjectError objectError) {

        ValidationErrorMessageDataModel.Builder builder = new ValidationErrorMessageDataModel.Builder();

        if (objectError instanceof FieldError) {
            builder.withField(((FieldError) objectError).getField());
        }

        return builder.withMessage(objectError.getDefaultMessage()).build();
    }
}
