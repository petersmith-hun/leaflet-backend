package hu.psprog.leaflet.service.mail.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Comment notification model class.
 *
 * @author Peter Smith
 */
public class CommentNotification {

    private String username;
    private String email;
    private String content;
    private String authorEmail;
    private String authorName;
    private String entryTitle;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CommentNotification that = (CommentNotification) o;

        return new EqualsBuilder()
                .append(username, that.username)
                .append(email, that.email)
                .append(content, that.content)
                .append(authorEmail, that.authorEmail)
                .append(authorName, that.authorName)
                .append(entryTitle, that.entryTitle)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(email)
                .append(content)
                .append(authorEmail)
                .append(authorName)
                .append(entryTitle)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("email", email)
                .append("content", content)
                .append("authorEmail", authorEmail)
                .append("authorName", authorName)
                .append("entryTitle", entryTitle)
                .toString();
    }

    public static CommentNotificationBuilder getBuilder() {
        return new CommentNotificationBuilder();
    }

    /**
     * Builder for {@link CommentNotification}.
     */
    public static final class CommentNotificationBuilder {
        private String username;
        private String email;
        private String content;
        private String authorEmail;
        private String authorName;
        private String entryTitle;

        private CommentNotificationBuilder() {
        }

        public CommentNotificationBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public CommentNotificationBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public CommentNotificationBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public CommentNotificationBuilder withAuthorEmail(String authorEmail) {
            this.authorEmail = authorEmail;
            return this;
        }

        public CommentNotificationBuilder withEntryTitle(String entryTitle) {
            this.entryTitle = entryTitle;
            return this;
        }

        public CommentNotificationBuilder withAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public CommentNotification build() {
            CommentNotification commentNotification = new CommentNotification();
            commentNotification.authorEmail = this.authorEmail;
            commentNotification.authorName = this.authorName;
            commentNotification.content = this.content;
            commentNotification.username = this.username;
            commentNotification.entryTitle = this.entryTitle;
            commentNotification.email = this.email;
            return commentNotification;
        }
    }
}
