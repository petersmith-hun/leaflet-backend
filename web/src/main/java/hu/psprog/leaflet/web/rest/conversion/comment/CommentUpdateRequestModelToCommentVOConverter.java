package hu.psprog.leaflet.web.rest.conversion.comment;

import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts {@link CommentUpdateRequestModel} model to {@link CommentVO} value object.
 *
 * @author Peter Smith
 */
@Component
public class CommentUpdateRequestModelToCommentVOConverter implements Converter<CommentUpdateRequestModel, CommentVO> {

    private static final String USER_ID_TOKEN_ATTRIBUTE = "uid";

    @Override
    public CommentVO convert(CommentUpdateRequestModel source) {

        CommentVO.CommentVOBuilder<?, ?> builder = CommentVO.getBuilder()
                .withContent(source.getContent());

        if (source instanceof CommentCreateRequestModel createRequestModel) {
            builder
                    .withOwner(UserVO.wrapMinimumVO(getUserID()))
                    .withEntryVO(EntryVO.wrapMinimumVO(createRequestModel.getEntryId()));
        }

        return builder.build();
    }

    private Long getUserID() {

        Long userID = null;

        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            userID = (Long) jwtAuthenticationToken.getTokenAttributes().get(USER_ID_TOKEN_ATTRIBUTE);
        }

        if (Objects.isNull(userID)) {
            throw new AccessDeniedException("Failed to authorize user");
        }

        return userID;
    }
}
