package hu.psprog.leaflet.service.security.evaluator;

import hu.psprog.leaflet.security.jwt.auth.JWTAuthenticationToken;
import hu.psprog.leaflet.security.jwt.model.JWTPayload;
import hu.psprog.leaflet.security.jwt.model.Role;
import hu.psprog.leaflet.service.CommentService;
import hu.psprog.leaflet.service.EntryService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Evaluates ownership of resources for PermitSelf permission types.
 *
 * @author Peter Smith
 */
@Component
public class OwnershipEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnershipEvaluator.class);

    private EntryService entryService;
    private CommentService commentService;

    @Autowired
    public OwnershipEvaluator(EntryService entryService, @Qualifier("commentServiceImpl") CommentService commentService) {
        this.entryService = entryService;
        this.commentService = commentService;
    }

    /**
     * Validates that a user is accessing their own user entry.
     *
     * @param authentication current {@link Authentication} object
     * @param id user ID
     * @return {@code true} if the given user ID is the same as in the one in the {@link Authentication} object, {@code false} otherwise
     */
    public boolean isSelf(Authentication authentication, Long id) {

        return extractPayload(authentication)
                .map(jwtPayload -> jwtPayload.getId().equals(id.intValue()))
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own user entry or an admin is accessing user information.
     *
     * @param authentication current {@link Authentication} object
     * @param id user ID
     * @return {@code true} if the given user ID is the same as in the one in the {@link Authentication} object,
     * or the authenticated user is an admin, {@code false} otherwise
     */
    public boolean isSelfOrAdmin(Authentication authentication, Long id) {

        return extractPayload(authentication)
                .map(jwtPayload -> jwtPayload.getId().equals(id.intValue()) || isAdmin(jwtPayload))
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own user entry or an admin/editor is accessing user information.
     *
     * @param authentication current {@link Authentication} object
     * @param userVO user VO
     * @return {@code true} if the given user ID is the same as in the one in the {@link Authentication} object,
     * or the authenticated user is either an admin or an editor, {@code false} otherwise
     */
    public boolean isSelfOrModerator(Authentication authentication, UserVO userVO) {

        return extractPayload(authentication)
                .map(jwtPayload -> jwtPayload.getId().equals(userVO.getId().intValue()) || isModerator(jwtPayload))
                .orElse(false);
    }

    /**
     * Validates that an editor is accessing their own entry.
     * Admin users can also access.
     *
     * @param authentication current {@link Authentication} object
     * @param id entry ID
     * @return {@code true} if the entry belongs to the current user or the current user is an admin, {@code false} otherwise
     */
    public boolean isOwnEntryOrAdmin(Authentication authentication, Long id) {

        return extractPayload(authentication)
                .map(jwtPayload -> isAdmin(jwtPayload) || isOwnEntry(jwtPayload, id))
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own comment.
     * Comments are also accessible for "moderators" (admin and editor users).
     *
     * @param authentication current {@link Authentication} object
     * @param id comment ID
     * @return {@code true} if the comment belongs to the current user or the current user is a moderator, {@code false} otherwise
     */
    public boolean isOwnCommentOrModerator(Authentication authentication, Long id) {

        return extractPayload(authentication)
                .map(jwtPayload -> isModerator(jwtPayload) || isOwnComment(jwtPayload, id))
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own comment.
     * Comments are also accessible for "moderators" (admin and editor users).
     *
     * @param authentication current {@link Authentication} object
     * @param commentVO comment entity
     * @return {@code true} if the comment belongs to the current user or the current user is a moderator, {@code false} otherwise
     */
    public boolean isOwnCommentOrModerator(Authentication authentication, CommentVO commentVO) {
        return isOwnCommentOrModerator(authentication, commentVO.getId());
    }

    private Optional<JWTPayload> extractPayload(Authentication authentication) {

        JWTPayload payload = null;
        if (authentication instanceof JWTAuthenticationToken) {
            payload = (JWTPayload) authentication.getDetails();
        }

        return Optional.ofNullable(payload);
    }

    private boolean isOwnEntry(JWTPayload jwtPayload, Long entryID) {

        boolean isOwnEntry = false;
        try {
            EntryVO entryVO = entryService.getOne(entryID);
            isOwnEntry = entryVO.getOwner().getId() == jwtPayload.getId().longValue();
        } catch (ServiceException e) {
            LOGGER.error("Error occurred while retrieving entry data.", e);
        }

        return isOwnEntry;
    }

    private boolean isOwnComment(JWTPayload jwtPayload, Long commentID) {

        boolean isOwnComment = false;
        try {
            CommentVO commentVO = commentService.getOne(commentID);
            isOwnComment = commentVO.getOwner().getId() == jwtPayload.getId().longValue();
        } catch (ServiceException e) {
            LOGGER.error("Error occurred while retrieving comment data.", e);
        }

        return isOwnComment;
    }

    private boolean isAdmin(JWTPayload jwtPayload) {
        return jwtPayload.getRole() == Role.ADMIN;
    }

    private boolean isModerator(JWTPayload jwtPayload) {
        return isAdmin(jwtPayload) || jwtPayload.getRole() == Role.EDITOR;
    }
}
