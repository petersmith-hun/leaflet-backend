package hu.psprog.leaflet.web.rest.conversion;

import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Converts {@link List} of {@link ObjectError} objects to {@link ValidationErrorMessageListDataModel}.
 *
 * @author Peter Smith
 */
@Component
public class ValidationErrorMessagesConverter implements Converter<List<ObjectError>, ValidationErrorMessageListDataModel> {

    @Override
    public ValidationErrorMessageListDataModel convert(List<ObjectError> objectErrors) {

        ValidationErrorMessageListDataModel.Builder builder = new ValidationErrorMessageListDataModel.Builder();
        objectErrors.forEach(error -> builder.withItem(convert(error)));

        return builder.build();
    }

    private ValidationErrorMessageDataModel convert(ObjectError objectError) {

        ValidationErrorMessageDataModel.Builder builder = new ValidationErrorMessageDataModel.Builder();

        if (objectError instanceof FieldError) {
            builder.withField(((FieldError) objectError).getField());
        }

        return builder.withMessage(objectError.getDefaultMessage()).build();
    }
}
