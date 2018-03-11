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
    static final String TABLE_DOCUMENTS = TABLE_PREFIX + "documents";
    static final String TABLE_COMMENTS = TABLE_PREFIX + "comments";
    static final String TABLE_TAGS = TABLE_PREFIX + "tags";
    static final String TABLE_ENTRIES = TABLE_PREFIX + "entries";
    static final String TABLE_ENTRIES_TAGS = TABLE_PREFIX + "entries_tags";
    static final String TABLE_DCP = TABLE_PREFIX + "dynamic_config_properties";
    static final String TABLE_UPLOADED_FILES = TABLE_PREFIX + "uploaded_files";
    static final String TABLE_ENTRIES_UPLOADED_FILES = TABLE_PREFIX + "entries_uploaded_files";
    static final String TABLE_FRONT_END_ROUTES = TABLE_PREFIX + "front_end_routes";

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
    static final String COLUMN_USER_ID = "user_id";
    static final String COLUMN_LINK = "link";
    static final String COLUMN_CONTENT = "content";
    static final String COLUMN_RAW_CONTENT = "raw_content";
    static final String COLUMN_SEO_TITLE = "seo_title";
    static final String COLUMN_SEO_DESCRIPTION = "seo_description";
    static final String COLUMN_SEO_KEYWORDS = "seo_keywords";
    static final String COLUMN_LOCALE = "locale";
    static final String COLUMN_CATEGORY_ID = "category_id";
    static final String COLUMN_PROLOGUE = "prologue";
    static final String COLUMN_TAG_ID = "tag_id";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_KEY = "dcp_key";
    static final String COLUMN_VALUE = "dcp_value";
    static final String COLUMN_DELETED = "is_deleted";
    static final String COLUMN_PATH = "path";
    static final String COLUMN_ORIGINAL_FILENAME = "original_filename";
    static final String COLUMN_MIME = "mime";
    static final String COLUMN_PATH_UUID = "path_uuid";
    static final String COLUMN_STORED_FILENAME = "stored_filename";
    static final String COLUMN_UPLOADED_FILE_ID = "uploaded_file_id";
    static final String COLUMN_TYPE = "type";
    static final String COLUMN_URL = "url";
    static final String COLUMN_SEQUENCE_NUMBER = "sequence_number";
    static final String COLUMN_NAME = "name";

    static final String FK_NM_ENTRIES_UPLOADED_FILES_ENTRY = "FK_NM_ENTRIES_UPLOADED_FILES_ENTRY";
    static final String FK_NM_ENTRIES_UPLOADED_FILES_UPLOADED_FILE = "FK_NM_ENTRIES_UPLOADED_FILES_UPLOADED_FILE";
    static final String FK_DOCUMENT_USER = "FK_DOCUMENT_USER";
    static final String FK_COMMENT_USER = "FK_COMMENT_USER";
    static final String FK_COMMENT_ENTRY = "FK_COMMENT_ENTRY";
    static final String FK_NM_ENTRIES_TAGS_TAG = "FK_NM_ENTRIES_TAGS_TAG";
    static final String FK_NM_ENTRIES_TAGS_ENTRY = "FK_NM_ENTRIES_TAGS_ENTRY";
    static final String FK_ENTRY_CATEGORY = "FK_ENTRY_CATEGORY";
    static final String FK_ENTRY_USER = "FK_ENTRY_USER";

    static final String DEF_TEXT = "TEXT";
    static final String DEF_LONGTEXT = "LONGTEXT";

    static final String UK_UPLOADED_FILE_PATH = "UK_UPLOADED_FILE_PATH";
    static final String UK_UPLOADED_FILE_PATH_UUID = "UK_UPLOADED_FILE_PATH_UUID";
    static final String UK_TAG_TITLE = "UK_TAG_TITLE";
    static final String UK_USER_EMAIL = "UK_USER_EMAIL";
    static final String UK_ENTRY_LINK = "UK_ENTRY_LINK";
    static final String UK_DOCUMENT_LINK = "UK_DOCUMENT_LINK";

    private DatabaseConstants() {}
}
