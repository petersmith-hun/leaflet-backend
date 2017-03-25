package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts {@link CommentUpdateRequestModel} model to {@link CommentVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class CommentUpdateRequestModelToCommentVOConverter implements Converter<CommentUpdateRequestModel, CommentVO> {

    @Override
    public CommentVO convert(CommentUpdateRequestModel source) {

        CommentVO.Builder builder = new CommentVO.Builder()
                .withContent(source.getContent());

        if (source instanceof CommentCreateRequestModel) {
            CommentCreateRequestModel createRequestModel = (CommentCreateRequestModel) source;
            builder.withOwner(new UserVO.Builder()
                    .withId(createRequestModel.getAuthenticatedUserId())
                    .withEmail(createRequestModel.getEmail())
                    .withUsername(createRequestModel.getUsername())
                    .createUserVO());
            builder.withEntryVO(EntryVO.wrapMinimumVO(createRequestModel.getEntryId()));
        }

        return builder.createCommentVO();
    }
}
