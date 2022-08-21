package hu.psprog.leaflet.service.security.annotation;

import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.UserVO;

/**
 * Methods with security expressions to be tested.
 *
 * @author Peter Smith
 */
class SecurityExpressionStub {

    @PermitScope.Read.OwnUserOrElevated
    boolean testPermitScopeReadOwnUserOrElevated(Long id) {
        return true;
    }

    @PermitScope.Read.OwnCommentsOrElevated
    boolean testPermitScopeReadOwnCommentsOrElevated(Long id) {
        return true;
    }

    @PermitScope.Read.OwnCommentsListOrElevated
    boolean testPermitScopeReadOwnCommentsListOrElevated(UserVO userVO) {
        return true;
    }

    @PermitScope.Read.Categories
    boolean testPermitScopeReadCategories() {
        return true;
    }

    @PermitScope.Read.Comments
    boolean testPermitScopeReadComments() {
        return true;
    }

    @PermitScope.Read.Documents
    boolean testPermitScopeReadDocuments() {
        return true;
    }

    @PermitScope.Read.Entries
    boolean testPermitScopeReadEntries() {
        return true;
    }

    @PermitScope.Read.Tags
    boolean testPermitScopeReadTags() {
        return true;
    }

    @PermitScope.Read.Users
    boolean testPermitScopeReadUsers() {
        return true;
    }

    @PermitScope.Read.Admin
    boolean testPermitScopeReadAdmin() {
        return true;
    }

    @PermitScope.Write.OwnUser
    boolean testPermitScopeWriteOwnUser(Long id) {
        return true;
    }

    @PermitScope.Write.OwnCommentOrElevated
    boolean testPermitScopeWriteOwnCommentOrElevated(Long id) {
        return true;
    }

    @PermitScope.Write.OwnCommentByEntityOrElevated
    boolean testPermitScopeWriteOwnCommentByEntityOrElevated(CommentVO entity) {
        return true;
    }

    @PermitScope.Write.OwnEntryOrElevated
    boolean testPermitScopeWriteOwnEntryOrElevated(Long id) {
        return true;
    }

    @PermitScope.Write.Categories
    boolean testPermitScopeWriteCategories() {
        return true;
    }

    @PermitScope.Write.Comments
    boolean testPermitScopeWriteComments() {
        return true;
    }

    @PermitScope.Write.Documents
    boolean testPermitScopeWriteDocuments() {
        return true;
    }

    @PermitScope.Write.Entries
    boolean testPermitScopeWriteEntries() {
        return true;
    }

    @PermitScope.Write.Tags
    boolean testPermitScopeWriteTags() {
        return true;
    }

    @PermitScope.Read.Users
    boolean testPermitScopeWriteUsers() {
        return true;
    }

    @PermitScope.Write.Admin
    boolean testPermitScopeWriteAdmin() {
        return true;
    }

    @PermitScope.DenyAlways
    boolean testDenyAlways() {
        return true;
    }
}
