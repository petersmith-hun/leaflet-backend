package hu.psprog.leaflet.service.vo.mail;

import lombok.Builder;
import lombok.Data;

/**
 * Comment notification model class.
 *
 * @author Peter Smith
 */
@Data
@Builder(builderMethodName = "getBuilder", setterPrefix = "with")
public class CommentNotification {

    private final String username;
    private final String email;
    private final String content;
    private final String authorEmail;
    private final String authorName;
    private final String entryTitle;
}
