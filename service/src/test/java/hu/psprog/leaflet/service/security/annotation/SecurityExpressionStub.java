package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.service.vo.CommentVO;

/**
 * Methods with security expressions to be tested.
 *
 * @author Peter Smith
 */
class SecurityExpressionStub {

    @PermitAdmin
    boolean testPermitAdmin() {
        return true;
    }

    @PermitEditorOrAdmin
    boolean testPermitEditorOrAdmin() {
        return true;
    }

    @PermitServiceOrAdmin()
    boolean testPermitServiceOrAdmin() {
        return true;
    }

    @PermitAuthenticated
    boolean testPermitAuthenticated() {
        return true;
    }

    @PermitSelf.User
    boolean testPermitSelfUser(Long id) {
        return true;
    }

    @PermitSelf.Entry
    boolean testPermitSelfEntry(Long id) {
        return true;
    }

    @PermitSelf.Comment
    boolean testPermitSelfComment(Long id) {
        return true;
    }

    @PermitSelf.CommentByEntity
    boolean testPermitSelfCommentByEntity(CommentVO entity) {
        return true;
    }
}
