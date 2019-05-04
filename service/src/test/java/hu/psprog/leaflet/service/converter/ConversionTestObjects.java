package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Comment;
import hu.psprog.leaflet.persistence.entity.Document;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.Role;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.persistence.entity.User;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import hu.psprog.leaflet.service.vo.UserVO;
import org.springframework.security.core.GrantedAuthority;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Test objects for conversion tests.
 *
 * @author Peter Smith
 */
public abstract class ConversionTestObjects {

    private static final Date CREATED = createDate(-5);
    private static final Date PUBLISHED = createDate(2);
    private static final Date LAST_MODIFIED = createDate(-3);
    private static final String DESCRIPTION = "Description";
    private static final boolean ENABLED = true;
    private static final long ID = 1L;
    private static final String TITLE = "Title";
    private static final String PASSWORD = "test-pw";
    private static final String USERNAME = "username";
    private static final Locale LOCALE = Locale.EN;
    private static final String EMAIL = "test@leaflet.dev";
    private static final Date LAST_LOGIN = createDate(0);
    private static final List<GrantedAuthority> AUTHORITIES = Collections.singletonList(Authority.ADMIN);
    private static final Role ROLE = Role.ADMIN;
    private static final String PATH = "path";
    private static final String ORIGINAL_FILENAME = "original-filename";
    private static final String MIME = "mime";
    private static final UUID PATH_UUID = UUID.randomUUID();
    private static final String STORED_FILENAME = "stored-filename";
    private static final String CONTENT = "content";
    private static final String RAW_CONTENT = "raw-content";
    private static final String LINK = "link";
    private static final String PROLOGUE = "prologue";
    private static final String SEO_TITLE = "seo-title";
    private static final String SEO_DESCRIPTION = "seo-description";
    private static final String SEO_KEYWORDS = "seo-keywords";
    private static final String ENTRY_STATUS = "PUBLIC";
    private static final String DELETED_COMMENT = "DELETED_COMMENT";
    private static final String ROUTE_ID = "route-test";
    private static final String NAME = "Test Route";
    private static final String URL = "/test/route";
    private static final int SEQUENCE_NUMBER = 5;
    private static final FrontEndRouteType TYPE = FrontEndRouteType.HEADER_MENU;
    private static final FrontEndRouteAuthRequirement AUTH_REQUIREMENT = FrontEndRouteAuthRequirement.AUTHENTICATED;

    static final CategoryVO CATEGORY_VO = CategoryVO.getBuilder()
            .withCreated(CREATED)
            .withDescription(DESCRIPTION)
            .withEnabled(ENABLED)
            .withId(ID)
            .withLastModified(LAST_MODIFIED)
            .withTitle(TITLE)
            .build();

    static final Category CATEGORY = Category.getBuilder()
            .withCreated(CREATED)
            .withDescription(DESCRIPTION)
            .withEnabled(ENABLED)
            .withId(ID)
            .withLastModified(LAST_MODIFIED)
            .withTitle(TITLE)
            .build();

    static final Category CATEGORY_MINIMUM = Category.getBuilder()
            .withId(ID)
            .build();

    static final UserVO USER_VO = UserVO.getBuilder()
            .withId(ID)
            .withCreated(CREATED)
            .withPassword(PASSWORD)
            .withUsername(USERNAME)
            .withLocale(LOCALE)
            .withEmail(EMAIL)
            .withLastLogin(LAST_LOGIN)
            .withAuthorities(AUTHORITIES)
            .withEnabled(ENABLED)
            .build();

    static final UserVO USER_MINIMUM_VO = UserVO.wrapMinimumVO(ID);

    static final User USER = User.getBuilder()
            .withId(ID)
            .withCreated(CREATED)
            .withPassword(PASSWORD)
            .withUsername(USERNAME)
            .withDefaultLocale(LOCALE)
            .withEmail(EMAIL)
            .withLastLogin(LAST_LOGIN)
            .withRole(ROLE)
            .withEnabled(ENABLED)
            .build();

    static final User USER_MINIMUM = User.getBuilder()
            .withId(ID)
            .build();

    static final UploadedFileVO UPLOADED_FILE_VO = UploadedFileVO.getBuilder()
            .withId(ID)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEnabled(ENABLED)
            .withPath(PATH)
            .withOriginalFilename(ORIGINAL_FILENAME)
            .withAcceptedAs(MIME)
            .withPathUUID(PATH_UUID)
            .withStoredFilename(STORED_FILENAME)
            .withDescription(DESCRIPTION)
            .build();

    static final UploadedFile UPLOADED_FILE = UploadedFile.getBuilder()
            .withId(ID)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEnabled(ENABLED)
            .withPath(PATH)
            .withOriginalFilename(ORIGINAL_FILENAME)
            .withMime(MIME)
            .withPathUUID(PATH_UUID)
            .withStoredFilename(STORED_FILENAME)
            .withDescription(DESCRIPTION)
            .build();

    static final TagVO TAG_VO = TagVO.getBuilder()
            .withId(ID)
            .withTitle(TITLE)
            .withEnabled(ENABLED)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .build();

    static final Tag TAG = Tag.getBuilder()
            .withId(ID)
            .withTitle(TITLE)
            .withEnabled(ENABLED)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .build();

    static final EntryVO ENTRY_VO = EntryVO.getBuilder()
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withPublished(PUBLISHED)
            .withEnabled(ENABLED)
            .withLastModified(LAST_MODIFIED)
            .withLocale(LOCALE)
            .withId(ID)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withEntryStatus(ENTRY_STATUS)
            .withOwner(USER_VO)
            .withCategory(CATEGORY_VO)
            .withAttachments(Collections.emptyList())
            .withTags(Collections.emptyList())
            .build();

    static final Entry ENTRY_BASE_DATA = Entry.getBuilder()
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withPublished(PUBLISHED)
            .withEnabled(ENABLED)
            .withLastModified(LAST_MODIFIED)
            .withLocale(LOCALE)
            .withId(ID)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withStatus(EntryStatus.valueOf(ENTRY_STATUS))
            .withUser(USER_MINIMUM)
            .withCategory(CATEGORY_MINIMUM)
            .build();

    static final EntryVO ENTRY_MINIMUM_VO = EntryVO.wrapMinimumVO(ID);

    static final EntryVO ENTRY_VO_FOR_MINIMUM_NO_NPE_MODEL = EntryVO.getBuilder()
            .withId(ID)
            .withEntryStatus(ENTRY_STATUS)
            .withAttachments(Collections.emptyList())
            .withTags(Collections.emptyList())
            .build();

    static final Entry ENTRY_MINIMUM = Entry.getBuilder()
            .withId(ID)
            .build();

    static final Entry ENTRY_MINIMUM_NO_NPE = Entry.getBuilder()
            .withId(ID)
            .withStatus(EntryStatus.valueOf(ENTRY_STATUS))
            .build();

    static final CommentVO COMMENT_VO = CommentVO.getBuilder()
            .withId(ID)
            .withContent(CONTENT)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEntryVO(ENTRY_VO)
            .withOwner(USER_VO)
            .withEnabled(ENABLED)
            .withDeleted(false)
            .build();

    static final CommentVO COMMENT_VO_DELETED = CommentVO.getBuilder()
            .withId(ID)
            .withContent(DELETED_COMMENT)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEntryVO(ENTRY_VO)
            .withOwner(USER_VO)
            .withEnabled(ENABLED)
            .withDeleted(true)
            .build();

    static final Comment COMMENT = Comment.getBuilder()
            .withId(ID)
            .withContent(CONTENT)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEntry(ENTRY_MINIMUM)
            .withUser(USER_MINIMUM)
            .withEnabled(ENABLED)
            .withDeleted(false)
            .build();

    static final Comment COMMENT_DELETED = Comment.getBuilder()
            .withId(ID)
            .withContent(CONTENT)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEntry(ENTRY_MINIMUM)
            .withUser(USER_MINIMUM)
            .withEnabled(ENABLED)
            .withDeleted(true)
            .build();

    static final CommentVO COMMENT_MINIMUM_VO = CommentVO.wrapMinimumVO(ID);

    static final Comment COMMENT_MINIMUM = Comment.getBuilder()
            .withId(ID)
            .build();

    static final DocumentVO DOCUMENT_VO_WITH_OWNER = DocumentVO.getBuilder()
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLastModified(LAST_MODIFIED)
            .withLocale(LOCALE)
            .withId(ID)
            .withLink(LINK)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withOwner(USER_VO)
            .build();

    static final DocumentVO DOCUMENT_VO_WITHOUT_OWNER = DocumentVO.getBuilder()
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLastModified(LAST_MODIFIED)
            .withLocale(LOCALE)
            .withId(ID)
            .withLink(LINK)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .build();

    static final Document DOCUMENT_WITH_OWNER = Document.getBuilder()
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLastModified(LAST_MODIFIED)
            .withLocale(LOCALE)
            .withId(ID)
            .withLink(LINK)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withUser(USER)
            .build();

    static final Document DOCUMENT_WITHOUT_OWNER = Document.getBuilder()
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
            .withEnabled(ENABLED)
            .withLastModified(LAST_MODIFIED)
            .withLocale(LOCALE)
            .withId(ID)
            .withLink(LINK)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .build();

    static final DocumentVO DOCUMENT_MINIMUM_VO = DocumentVO.wrapMinimumVO(ID);

    static final Document DOCUMENT_MINIMUM = Document.getBuilder()
            .withId(ID)
            .build();

    static final FrontEndRoute FRONT_END_ROUTE = FrontEndRoute.getBuilder()
            .withId(ID)
            .withRouteId(ROUTE_ID)
            .withName(NAME)
            .withUrl(URL)
            .withSequenceNumber(SEQUENCE_NUMBER)
            .withType(TYPE)
            .withEnabled(ENABLED)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withAuthRequirement(AUTH_REQUIREMENT)
            .build();

    static final FrontEndRoute FRONT_END_ROUTE_MINIMUM = FrontEndRoute.getBuilder()
            .withRouteId(ROUTE_ID)
            .withName(NAME)
            .withUrl(URL)
            .withSequenceNumber(SEQUENCE_NUMBER)
            .withType(TYPE)
            .withEnabled(ENABLED)
            .withAuthRequirement(AUTH_REQUIREMENT)
            .build();

    static final FrontEndRouteVO FRONT_END_ROUTE_VO = FrontEndRouteVO.getBuilder()
            .withId(ID)
            .withRouteId(ROUTE_ID)
            .withName(NAME)
            .withUrl(URL)
            .withSequenceNumber(SEQUENCE_NUMBER)
            .withType(TYPE)
            .withEnabled(ENABLED)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withAuthRequirement(AUTH_REQUIREMENT)
            .build();

    private static Date createDate(int dayOffset) {

        Calendar calendar = new Calendar.Builder()
                .setInstant(new Date())
                .build();
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);

        return calendar.getTime();
    }
}
