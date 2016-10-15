package hu.psprog.leaflet.web.rest.mapper;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.layout.BaseLayoutDataModel;
import hu.psprog.leaflet.web.config.WebMVCConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Message converter which automatically wraps answer into default layout model if request is not flagged as AJAX.
 *
 * @author Peter Smith
 */
public class AutoWrappingJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        Object response = object;
        if (type.getTypeName().equals(BaseBodyDataModel.class.getName()) && !((boolean) httpServletRequest.getAttribute(WebMVCConfiguration.IS_AJAX_REQUEST))) {
            response = wrapResponseIntoBaseLayoutModel((BaseBodyDataModel) object);
        }

        super.writeInternal(response, type, outputMessage);
    }

    private BaseBodyDataModel wrapResponseIntoBaseLayoutModel(BaseBodyDataModel response) {

        BaseLayoutDataModel.Builder builder = new BaseLayoutDataModel.Builder()
                .withBody(response);

        return builder.build();
    }
}
