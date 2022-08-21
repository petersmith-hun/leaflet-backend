package hu.psprog.leaflet.service.security.evaluator;

import hu.psprog.leaflet.persistence.dao.BaseDAO;
import hu.psprog.leaflet.persistence.dao.CommentDAO;
import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.IdentifiableEntity;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * Evaluates ownership of resources for PermitSelf permission types.
 *
 * @author Peter Smith
 */
@Component
public class OwnershipEvaluator {

    private static final String USER_ID_TOKEN_ATTRIBUTE = "uid";

    private final CommentDAO commentDAO;
    private final EntryDAO entryDAO;

    @Autowired
    public OwnershipEvaluator(CommentDAO commentDAO, EntryDAO entryDAO) {
        this.commentDAO = commentDAO;
        this.entryDAO = entryDAO;
    }

    /**
     * Validates that a user is accessing their own account data.
     *
     * @param authentication current {@link Authentication} object
     * @param id user ID
     * @return {@code true} if the given user ID is the same as in the one in the {@link Authentication} object, {@code false} otherwise
     */
    public boolean isSelf(Authentication authentication, Long id) {

        return extractUserID(authentication)
                .map(id::equals)
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own account data.
     *
     * @param authentication current {@link Authentication} object
     * @param userVO user object containing their ID
     * @return {@code true} if the given user ID is the same as in the one in the {@link Authentication} object, {@code false} otherwise
     */
    public boolean isSelf(Authentication authentication, UserVO userVO) {

        return extractUserID(authentication)
                .map(userVO.getId()::equals)
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own entry.
     *
     * @param authentication current {@link Authentication} object
     * @param id entry ID
     * @return {@code true} if the given entry has an owner with the same user ID as the one in the {@link Authentication} object, {@code false} otherwise
     */
    public boolean isOwnEntry(Authentication authentication, Long id) {

        return extractUserID(authentication)
                .map(userID -> isOwnEntry(userID, id))
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own comment.
     *
     * @param authentication current {@link Authentication} object
     * @param id comment ID
     * @return {@code true} if the given comment has an owner with the same user ID as the one in the {@link Authentication} object, {@code false} otherwise
     */
    public boolean isOwnComment(Authentication authentication, Long id) {

        return extractUserID(authentication)
                .map(userID -> isOwnComment(userID, id))
                .orElse(false);
    }

    /**
     * Validates that a user is accessing their own entry.
     *
     * @param authentication current {@link Authentication} object
     * @param commentVO comment object
     * @return {@code true} if the given comment has an owner with the same user ID as the one in the {@link Authentication} object, {@code false} otherwise
     */
    public boolean isOwnComment(Authentication authentication, CommentVO commentVO) {

        return extractUserID(authentication)
                .map(userID -> isOwnComment(userID, commentVO.getId()))
                .orElse(false);
    }

    private Optional<Long> extractUserID(Authentication authentication) {

        Long userID = null;
        if (authentication instanceof JwtAuthenticationToken) {
            userID = (Long) ((JwtAuthenticationToken) authentication).getTokenAttributes().get(USER_ID_TOKEN_ATTRIBUTE);
        }

        return Optional.ofNullable(userID);
    }

    private boolean isOwnEntry(Long userID, Long entryID) {
        return isOwnItem(entryDAO, Entry::getUser, userID, entryID);
    }

    private boolean isOwnComment(Long userID, Long commentID) {
        return isOwnItem(commentDAO, Comment::getUser, userID, commentID);
    }

    private <T extends IdentifiableEntity<Long>> boolean isOwnItem(BaseDAO<T, Long> sourceDAO, Function<T, User> ownerMapper,
                                                                   Long targetUserID, Long itemID) {

        return sourceDAO.findById(itemID)
                .map(ownerMapper)
                .map(User::getId)
                .map(targetUserID::equals)
                .orElse(false);
    }
}
