package hu.psprog.leaflet.persistence.entity;

/**
 * Database constants used in entities.
 *
 * @author Peter Smith
 */
public final class DatabaseConstants {

    static final String TABLE_PREFIX = "leaflet_";

    static final String TABLE_USERS = TABLE_PREFIX + "users";
    static final String TABLE_CATEGORIES = TABLE_PREFIX + "categories";
    static final String TABLE_ATTACHMENTS = TABLE_PREFIX + "attachments";
    static final String TABLE_DOCUMENTS = TABLE_PREFIX + "documents";
    static final String TABLE_COMMENTS = TABLE_PREFIX + "comments";
    static final String TABLE_TAGS = TABLE_PREFIX + "tags";
    static final String TABLE_ENTRIES = TABLE_PREFIX + "entries";
    static final String TABLE_ENTRIES_TAGS = TABLE_PREFIX + "entries_tags";

    static final String COLUMN_DATE_CREATED = "date_created";
    static final String COLUMN_DATE_LAST_MODIFIED = "date_last_modified";
    static final String COLUMN_IS_ENABLED = "is_enabled";
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_ROLE = "role";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_DEFAULT_LOCALE = "default_locale";
    static final String COLUMN_DATE_LAST_LOGIN = "date_last_login";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_ENTRY_ID = "entry_id";
    static final String COLUMN_FILENAME = "filename";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_IS_PROTECTED = "is_protected";
    static final String COLUMN_USER_ID = "user_id";
    static final String COLUMN_LINK = "link";
    static final String COLUMN_CONTENT = "content";
    static final String COLUMN_SEO_TITLE = "seo_title";
    static final String COLUMN_SEO_DESCRIPTION = "seo_description";
    static final String COLUMN_SEO_KEYWORDS = "seo_keywords";
    static final String COLUMN_LOCALE = "locale";
    static final String COLUMN_CATEGORY_ID = "category_id";
    static final String COLUMN_PROLOGUE = "prologue";
    static final String COLUMN_TAG_ID = "tag_id";

    static final String FK_ATTACHMENT_ENTRY = "FK_ATTACHMENT_ENTRY";
    static final String FK_DOCUMENT_USER = "FK_DOCUMENT_USER";
    static final String FK_COMMENT_USER = "FK_COMMENT_USER";
    static final String FK_COMMENT_ENTRY = "FK_COMMENT_ENTRY";
    static final String FK_NM_ENTRIES_TAGS_TAG = "FK_NM_ENTRIES_TAGS_TAG";
    static final String FK_NM_ENTRIES_TAGS_ENTRY = "FK_NM_ENTRIES_TAGS_ENTRY";
    static final String FK_ENTRY_CATEGORY = "FK_ENTRY_CATEGORY";
    static final String FK_ENTRY_USER = "FK_ENTRY_USER";

    static final String DEF_TEXT = "TEXT";
    static final String DEF_LONGTEXT = "LONGTEXT";

    static final String MAPPED_BY_USER = "user";
    static final String MAPPED_BY_CATEGORY = "category";
    static final String MAPPED_BY_ENTRY = "entry";
    static final String COLUMN_STATUS = "status";

    private DatabaseConstants() {}
}
