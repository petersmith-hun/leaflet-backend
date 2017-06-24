package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Converts {@link CommentVO} value object to {@link CommentDataModel} model.
 *
 * @author Peter Smith
 */
@Component
public class CommentVOToCommentDataModelConverter implements Converter<CommentVO, CommentDataModel> {

    private HttpServletRequest httpServletRequest;
    private CommonFormatter commonFormatter;

    @Autowired
    public CommentVOToCommentDataModelConverter(HttpServletRequest httpServletRequest, CommonFormatter commonFormatter) {
        this.httpServletRequest = httpServletRequest;
        this.commonFormatter = commonFormatter;
    }

    @Override
    public CommentDataModel convert(CommentVO source) {
        return CommentDataModel.getBuilder()
                .withId(source.getId())
                .withOwner(UserDataModel.getBuilder()
                        .withId(source.getOwner().getId())
                        .withUsername(source.getOwner().getUsername())
                        .build())
                .withContent(source.getContent())
                .withDeleted(source.isDeleted())
                .withCreated(commonFormatter.formatDate(source.getCreated(), httpServletRequest.getLocale()))
                .withLastModified(commonFormatter.formatDate(source.getLastModified(), httpServletRequest.getLocale()))
                .build();
    }
}
