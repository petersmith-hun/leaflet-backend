package hu.psprog.leaflet.web.test;

import hu.psprog.leaflet.api.rest.request.attachment.AttachmentRequestModel;
import hu.psprog.leaflet.api.rest.request.category.CategoryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.comment.CommentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.document.DocumentUpdateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.entry.EntryInitialStatus;
import hu.psprog.leaflet.api.rest.request.entry.EntryUpdateRequestModel;
import hu.psprog.leaflet.api.rest.request.file.FileUploadRequestModel;
import hu.psprog.leaflet.api.rest.request.file.UpdateFileMetaInfoRequestModel;
import hu.psprog.leaflet.api.rest.request.tag.TagAssignmentRequestModel;
import hu.psprog.leaflet.api.rest.request.tag.TagCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryDataModel;
import hu.psprog.leaflet.api.rest.response.category.CategoryListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentDataModel;
import hu.psprog.leaflet.api.rest.response.comment.CommentListDataModel;
import hu.psprog.leaflet.api.rest.response.comment.ExtendedCommentDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageDataModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentDataModel;
import hu.psprog.leaflet.api.rest.response.document.DocumentListDataModel;
import hu.psprog.leaflet.api.rest.response.document.EditDocumentDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.api.rest.response.entry.ExtendedEntryDataModel;
import hu.psprog.leaflet.api.rest.response.file.DirectoryDataModel;
import hu.psprog.leaflet.api.rest.response.file.DirectoryListDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileDataModel;
import hu.psprog.leaflet.api.rest.response.file.FileListDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.service.common.Authority;
import hu.psprog.leaflet.service.vo.AcceptorInfoVO;
import hu.psprog.leaflet.service.vo.AttachmentRequestVO;
import hu.psprog.leaflet.service.vo.AuthRequestVO;
import hu.psprog.leaflet.service.vo.AuthResponseVO;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.FileInputVO;
import hu.psprog.leaflet.service.vo.LoginContextVO;
import hu.psprog.leaflet.service.vo.TagAssignmentVO;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.service.vo.UpdateFileMetaInfoVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import hu.psprog.leaflet.service.vo.UserVO;
import hu.psprog.leaflet.web.rest.conversion.CommonFormatter;
import hu.psprog.leaflet.web.rest.conversion.JULocaleToLeafletLocaleConverter;
import org.junit.Before;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.fail;
import static org.mockito.BDDMockito.given;

/**
 * Conversion test objects.
 *
 * @author Peter Smith
 */
public abstract class ConversionTestObjects {

    private static final Date CREATED = createDate(-5);
    private static final Date LAST_MODIFIED = createDate(-3);
    private static final String DESCRIPTION = "Description";
    private static final boolean ENABLED = true;
    private static final long ID = 1L;
    private static final String TITLE = "Title";
    private static final Date LAST_LOGIN = createDate(0);
    private static final List<GrantedAuthority> AUTHORITIES = Collections.singletonList(Authority.ADMIN);
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
    private static final String OBJECT_NAME = "validation";
    private static final String FIELD = "invalid-field";
    private static final String FIELD_VALIDATION_FAILED = "Field validation failed";
    private static final String VALIDATION_FAILED = "Validation failed";
    private static final String FORMATTED_CREATED_DATE = "formatted-created-date";
    private static final String FORMATTED_LAST_MODIFIED_DATE = "formatted-last-modified-date";
    private static final String FORMATTED_LAST_LOGIN_DATE = "formatted-last-login-date";
    private static final String IMAGE = "IMAGE";
    private static final String SUB_1 = "sub1";
    private static final String SUB_2 = "sub2";
    private static final String IMAGE_JPG = "image/jpg";
    private static final MockMultipartFile MOCK_MULTIPART_FILE = new MockMultipartFile(ORIGINAL_FILENAME, ORIGINAL_FILENAME, IMAGE_JPG, CONTENT.getBytes());
    private static final String IMAGE_GIF = "image/gif";
    private static final String ROOT_DIRECTORY_NAME = "images";
    private static final String ADMIN = "ADMIN";
    private static final String TOKEN = "token";

    protected static final String PASSWORD = "test-pw";
    protected static final String USERNAME = "username";
    protected static final Locale LOCALE = Locale.EN;
    protected static final String EMAIL = "test@leaflet.dev";
    protected static final String REMOTE_ADDRESS = "localhost";
    protected static final UUID DEVICE_ID = UUID.randomUUID();

    protected static final List<ObjectError> OBJECT_ERROR_LIST = Arrays.asList(
            prepareObjectError(true),
            prepareObjectError(false));

    protected static final ValidationErrorMessageListDataModel VALIDATION_ERROR_MESSAGE_LIST_DATA_MODEL = ValidationErrorMessageListDataModel.getBuilder()
            .withItem(ValidationErrorMessageDataModel.getExtendedBuilder()
                    .withField(FIELD)
                    .withMessage(FIELD_VALIDATION_FAILED)
                    .build())
            .withItem(ValidationErrorMessageDataModel.getExtendedBuilder()
                    .withMessage(VALIDATION_FAILED)
                    .build())
            .build();

    protected static final AttachmentRequestVO ATTACHMENT_REQUEST_VO = AttachmentRequestVO.getBuilder()
            .withEntryID(ID)
            .withPathUUID(PATH_UUID)
            .build();

    protected static final AttachmentRequestModel ATTACHMENT_REQUEST_MODEL = prepareAttachmentRequestModel();

    protected static final CategoryVO CATEGORY_VO = CategoryVO.getBuilder()
            .withCreated(CREATED)
            .withDescription(DESCRIPTION)
            .withEnabled(ENABLED)
            .withId(ID)
            .withLastModified(LAST_MODIFIED)
            .withTitle(TITLE)
            .build();

    protected static CategoryVO CATEGORY_MINIMUM_VO = CategoryVO.wrapMinimumVO(ID);

    protected static final CategoryVO CATEGORY_VO_FOR_CREATE = CategoryVO.getBuilder()
            .withDescription(DESCRIPTION)
            .withTitle(TITLE)
            .build();

    protected static final CategoryCreateRequestModel CATEGORY_CREATE_REQUEST_MODEL = prepareCategoryCreateRequestModel();

    protected static final CategoryDataModel CATEGORY_DATA_MODEL = CategoryDataModel.getBuilder()
            .withID(ID)
            .withTitle(TITLE)
            .withDescription(DESCRIPTION)
            .withCreated(FORMATTED_CREATED_DATE)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .withEnabled(ENABLED)
            .build();

    protected static final CategoryListDataModel CATEGORY_LIST_DATA_MODEL = CategoryListDataModel.getBuilder()
            .withItem(CATEGORY_DATA_MODEL)
            .build();

    protected static final UserVO USER_VO = UserVO.getBuilder()
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

    protected static final ExtendedUserDataModel EXTENDED_USER_DATA_MODEL = ExtendedUserDataModel.getExtendedBuilder()
            .withId(ID)
            .withCreated(FORMATTED_CREATED_DATE)
            .withUsername(USERNAME)
            .withLocale(LOCALE.name())
            .withEmail(EMAIL)
            .withLastLogin(FORMATTED_LAST_LOGIN_DATE)
            .withRole(ADMIN)
            .build();

    protected static final UserListDataModel USER_LIST_DATA_MODEL = UserListDataModel.getBuilder()
            .withItem(EXTENDED_USER_DATA_MODEL)
            .build();

    protected static final UserVO USER_VO_FOR_UPDATE = UserVO.getBuilder()
            .withUsername(USERNAME)
            .withLocale(LOCALE)
            .withEmail(EMAIL)
            .build();

    protected static final UserVO USER_MINIMUM_VO = UserVO.wrapMinimumVO(ID);

    protected static final UserDataModel USER_DATA_MODEL = UserDataModel.getBuilder()
            .withId(ID)
            .withUsername(USERNAME)
            .build();

    protected static final UserInitializeRequestModel USER_INITIALIZE_REQUEST_MODEL = prepareUserInitializeRequestModel();

    protected static final UserCreateRequestModel USER_CREATE_REQUEST_MODEL = prepareUserCreateRequestModel();

    protected static final AuthRequestVO AUTH_REQUEST_VO = AuthRequestVO.getBuilder()
            .withPassword(PASSWORD)
            .withUsername(EMAIL)
            .build();

    protected static final LoginRequestModel LOGIN_REQUEST_MODEL = prepareLoginRequestModel();

    protected static final AuthResponseVO AUTH_RESPONSE_VO_WITH_SUCCESS = AuthResponseVO.getBuilder()
            .withAuthenticationResult(AuthResponseVO.AuthenticationResult.AUTH_SUCCESS)
            .withToken(TOKEN)
            .build();

    protected static final AuthResponseVO AUTH_RESPONSE_VO_WITH_FAILURE = AuthResponseVO.getBuilder()
            .withAuthenticationResult(AuthResponseVO.AuthenticationResult.INVALID_CREDENTIALS)
            .build();

    protected static final LoginResponseDataModel LOGIN_RESPONSE_DATA_MODEL_WITH_SUCCESS = LoginResponseDataModel.getBuilder()
            .withToken(TOKEN)
            .withStatus(LoginResponseDataModel.AuthenticationResult.AUTH_SUCCESS)
            .build();

    protected static final LoginResponseDataModel LOGIN_RESPONSE_DATA_MODEL_WITH_FAILURE = LoginResponseDataModel.getBuilder()
            .withStatus(LoginResponseDataModel.AuthenticationResult.INVALID_CREDENTIALS)
            .build();

    protected static final UpdateProfileRequestModel UPDATE_PROFILE_REQUEST_MODEL = prepareUpdateProfileRequestModel();

    protected static final PasswordResetDemandRequestModel PASSWORD_RESET_DEMAND_REQUEST_MODEL = preparePasswordResetDemandRequestModel();

    protected static final LoginContextVO LOGIN_CONTEXT_VO_FOR_LOGIN = LoginContextVO.getBuilder()
            .withUsername(EMAIL)
            .withPassword(PASSWORD)
            .withRemoteAddress(REMOTE_ADDRESS)
            .withDeviceID(DEVICE_ID)
            .build();

    protected static final LoginContextVO LOGIN_CONTEXT_VO_FOR_PASSWORD_RESET = LoginContextVO.getBuilder()
            .withUsername(EMAIL)
            .withRemoteAddress(REMOTE_ADDRESS)
            .withDeviceID(DEVICE_ID)
            .build();

    protected static final LoginContextVO LOGIN_CONTEXT_VO_FOR_RENEWAL = LoginContextVO.getBuilder()
            .withRemoteAddress(REMOTE_ADDRESS)
            .withDeviceID(DEVICE_ID)
            .build();

    protected static final UploadedFileVO UPLOADED_FILE_VO = UploadedFileVO.getBuilder()
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

    protected static final FileDataModel FILE_DATA_MODEL = FileDataModel.getBuilder()
            .withOriginalFilename(ORIGINAL_FILENAME)
            .withReference("/" + PATH_UUID + "/" + STORED_FILENAME)
            .withAcceptedAs(MIME)
            .withDescription(DESCRIPTION)
            .withPath(PATH)
            .build();

    protected static final FileListDataModel FILE_LIST_DATA_MODEL = FileListDataModel.getBuilder()
            .withItem(FILE_DATA_MODEL)
            .build();

    protected static final AcceptorInfoVO ACCEPTOR_INFO_VO = AcceptorInfoVO.getBuilder()
            .withId(IMAGE)
            .withChildrenDirectories(Arrays.asList(SUB_1, SUB_2))
            .withAcceptableMimeTypes(Arrays.asList(IMAGE_JPG, IMAGE_GIF))
            .withRootDirectoryName(ROOT_DIRECTORY_NAME)
            .build();

    protected static final DirectoryDataModel DIRECTORY_DATA_MODEL = DirectoryDataModel.getBuilder()
            .withId(IMAGE)
            .withChildren(Arrays.asList(SUB_1, SUB_2))
            .withAcceptableMimeTypes(Arrays.asList(IMAGE_JPG, IMAGE_GIF))
            .withRoot(ROOT_DIRECTORY_NAME)
            .build();

    protected static final DirectoryListDataModel DIRECTORY_LIST_DATA_MODEL = DirectoryListDataModel.getBuilder()
            .withItem(DIRECTORY_DATA_MODEL)
            .build();

    protected static final FileUploadRequestModel FILE_UPLOAD_REQUEST_MODEL = prepareFileUploadRequestModel();

    protected static final FileInputVO FILE_INPUT_VO = prepareFileInputVO();

    protected static final UpdateFileMetaInfoVO UPDATE_FILE_META_INFO_VO = UpdateFileMetaInfoVO.getBuilder()
            .withOriginalFilename(ORIGINAL_FILENAME)
            .withDescription(DESCRIPTION)
            .build();

    protected static final UpdateFileMetaInfoRequestModel UPDATE_FILE_META_INFO_REQUEST_MODEL = prepareUpdateFileMetaInfoRequestModel();

    protected static final TagVO TAG_VO = TagVO.getBuilder()
            .withId(ID)
            .withTitle(TITLE)
            .withEnabled(ENABLED)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .build();

    protected static final TagDataModel TAG_DATA_MODEL = TagDataModel.getBuilder()
            .withId(ID)
            .withName(TITLE)
            .withEnabled(ENABLED)
            .withCreated(FORMATTED_CREATED_DATE)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .build();

    protected static final TagListDataModel TAG_LIST_DATA_MODEL = TagListDataModel.getBuilder()
            .withItem(TAG_DATA_MODEL)
            .build();

    protected static final TagVO TAG_VO_FOR_CREATE = TagVO.getBuilder()
            .withTitle(TITLE)
            .build();

    protected static final TagAssignmentVO TAG_ASSIGNMENT_VO = TagAssignmentVO.getBuilder()
            .withTagID(2L)
            .withEntryID(3L)
            .build();

    protected static final TagAssignmentRequestModel TAG_ASSIGNMENT_REQUEST_MODEL = prepareTagAssignmentRequestModel();

    protected static final TagCreateRequestModel TAG_CREATE_REQUEST_MODEL = prepareTagCreateRequestModel();

    protected static final EntryVO ENTRY_VO = EntryVO.getBuilder()
            .withContent(CONTENT)
            .withRawContent(RAW_CONTENT)
            .withCreated(CREATED)
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

    protected static final EntryVO ENTRY_VO_FOR_CREATE = EntryVO.getBuilder()
            .withContent(CONTENT)
            .withRawContent(RAW_CONTENT)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withEntryStatus(ENTRY_STATUS)
            .withOwner(USER_MINIMUM_VO)
            .withCategory(CATEGORY_MINIMUM_VO)
            .build();

    protected static final EntryVO ENTRY_VO_FOR_UPDATE = EntryVO.getBuilder()
            .withContent(CONTENT)
            .withRawContent(RAW_CONTENT)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withEntryStatus(ENTRY_STATUS)
            .withCategory(CATEGORY_MINIMUM_VO)
            .build();

    protected static final EntryVO ENTRY_MINIMUM_VO = EntryVO.wrapMinimumVO(ID);

    protected static final EntryUpdateRequestModel ENTRY_UPDATE_REQUEST_MODEL = prepareEntryUpdateRequestModel();

    protected static final EntryCreateRequestModel ENTRY_CREATE_REQUEST_MODEL = prepareEntryCreateRequestModel();

    protected static final EntryDataModel ENTRY_DATA_MODEL = EntryDataModel.getBuilder()
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .withTitle(TITLE)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withCreated(FORMATTED_CREATED_DATE)
            .withId(ID)
            .withLocale(LOCALE.name())
            .withCategory(CategoryDataModel.getBuilder()
                    .withID(ID)
                    .withTitle(TITLE)
                    .build())
            .withUser(USER_DATA_MODEL)
            .withAttachments(Collections.emptyList())
            .withTags(Collections.emptyList())
            .build();

    protected static final ExtendedEntryDataModel EXTENDED_ENTRY_DATA_MODEL = ExtendedEntryDataModel.getExtendedBuilder()
            .withContent(CONTENT)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .withTitle(TITLE)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withCreated(FORMATTED_CREATED_DATE)
            .withId(ID)
            .withLocale(LOCALE.name())
            .withCategory(CategoryDataModel.getBuilder()
                    .withID(ID)
                    .withTitle(TITLE)
                    .build())
            .withUser(USER_DATA_MODEL)
            .withAttachments(Collections.emptyList())
            .withTags(Collections.emptyList())
            .build();

    protected static final EditEntryDataModel EDIT_ENTRY_DATA_MODEL = EditEntryDataModel.getExtendedBuilder()
            .withRawContent(RAW_CONTENT)
            .withEnabled(ENABLED)
            .withEntryStatus(ENTRY_STATUS)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .withTitle(TITLE)
            .withLink(LINK)
            .withPrologue(PROLOGUE)
            .withCreated(FORMATTED_CREATED_DATE)
            .withId(ID)
            .withLocale(LOCALE.name())
            .withCategory(CategoryDataModel.getBuilder()
                    .withID(ID)
                    .withTitle(TITLE)
                    .build())
            .withUser(USER_DATA_MODEL)
            .withAttachments(Collections.emptyList())
            .withTags(Collections.emptyList())
            .build();

    protected static final EntryListDataModel ENTRY_LIST_DATA_MODEL = EntryListDataModel.getBuilder()
            .withItem(ENTRY_DATA_MODEL)
            .build();

    protected static final CommentVO COMMENT_VO = CommentVO.getBuilder()
            .withId(ID)
            .withContent(CONTENT)
            .withCreated(CREATED)
            .withLastModified(LAST_MODIFIED)
            .withEntryVO(ENTRY_VO)
            .withOwner(USER_VO)
            .withEnabled(ENABLED)
            .withDeleted(false)
            .build();

    protected static final CommentUpdateRequestModel COMMENT_UPDATE_REQUEST_MODEL = prepareCommentUpdateRequestModel();

    protected static final CommentCreateRequestModel COMMENT_CREATE_REQUEST_MODEL = prepareCommentCreateRequestModel();

    protected static final CommentVO COMMENT_VO_FOR_CREATE = CommentVO.getBuilder()
            .withContent(CONTENT)
            .withOwner(UserVO.getBuilder()
                    .withId(ID)
                    .withEmail(EMAIL)
                    .withUsername(USERNAME)
                    .build())
            .withEntryVO(ENTRY_MINIMUM_VO)
            .build();

    protected static final CommentVO COMMENT_VO_FOR_UPDATE = CommentVO.getBuilder()
            .withContent(CONTENT)
            .build();

    protected static final CommentDataModel COMMENT_DATA_MODEL = CommentDataModel.getBuilder()
            .withId(ID)
            .withOwner(USER_DATA_MODEL)
            .withContent(CONTENT)
            .withDeleted(false)
            .withCreated(FORMATTED_CREATED_DATE)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .build();

    protected static final CommentListDataModel COMMENT_LIST_DATA_MODEL = CommentListDataModel.getBuilder()
            .withItem(COMMENT_DATA_MODEL)
            .build();

    protected static final ExtendedCommentDataModel EXTENDED_COMMENT_DATA_MODEL = ExtendedCommentDataModel.getExtendedBuilder()
            .withId(ID)
            .withOwner(USER_DATA_MODEL)
            .withContent(CONTENT)
            .withDeleted(false)
            .withCreated(FORMATTED_CREATED_DATE)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .withEnabled(ENABLED)
            .withAssociatedEntry(ENTRY_DATA_MODEL)
            .build();

    protected static final DocumentVO DOCUMENT_VO_WITH_OWNER = DocumentVO.getBuilder()
            .withContent(CONTENT)
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

    protected static final DocumentVO DOCUMENT_VO_WITH_OWNER_FOR_CREATE = DocumentVO.getBuilder()
            .withContent(CONTENT)
            .withRawContent(RAW_CONTENT)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withLink(LINK)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .withOwner(USER_MINIMUM_VO)
            .build();

    protected static final DocumentVO DOCUMENT_VO_WITHOUT_OWNER_FOR_UPDATE = DocumentVO.getBuilder()
            .withContent(CONTENT)
            .withRawContent(RAW_CONTENT)
            .withEnabled(ENABLED)
            .withLocale(LOCALE)
            .withLink(LINK)
            .withSeoTitle(SEO_TITLE)
            .withSeoDescription(SEO_DESCRIPTION)
            .withSeoKeywords(SEO_KEYWORDS)
            .withTitle(TITLE)
            .build();

    protected static final DocumentUpdateRequestModel DOCUMENT_UPDATE_REQUEST_MODEL = prepareDocumentUpdateRequestModel();

    protected static final DocumentCreateRequestModel DOCUMENT_CREATE_REQUEST_MODEL = prepareDocumentCreateRequestModel();

    protected static final DocumentDataModel DOCUMENT_DATA_MODEL = DocumentDataModel.getBuilder()
            .withId(ID)
            .withLink(LINK)
            .withTitle(TITLE)
            .withContent(CONTENT)
            .withCreated(FORMATTED_CREATED_DATE)
            .withLocale(LOCALE.name())
            .withUser(USER_DATA_MODEL)
            .build();

    protected static final EditDocumentDataModel EDIT_DOCUMENT_DATA_MODEL = EditDocumentDataModel.getExtendedBuilder()
            .withId(ID)
            .withLink(LINK)
            .withTitle(TITLE)
            .withContent(CONTENT)
            .withCreated(FORMATTED_CREATED_DATE)
            .withLocale(LOCALE.name())
            .withUser(USER_DATA_MODEL)
            .withLastModified(FORMATTED_LAST_MODIFIED_DATE)
            .withEnabled(ENABLED)
            .withRawContent(RAW_CONTENT)
            .build();

    protected static final DocumentListDataModel DOCUMENT_LIST_DATA_MODEL = DocumentListDataModel.getBuilder()
            .withItem(EDIT_DOCUMENT_DATA_MODEL)
            .build();

    private static ObjectError prepareObjectError(boolean withFieldError) {

        ObjectError error;
        if (withFieldError) {
            error = new FieldError(OBJECT_NAME, FIELD, FIELD_VALIDATION_FAILED);
        } else {
            error = new ObjectError(OBJECT_NAME, VALIDATION_FAILED);
        }

        return error;
    }

    private static AttachmentRequestModel prepareAttachmentRequestModel() {

        AttachmentRequestModel attachmentRequestModel = new AttachmentRequestModel();
        attachmentRequestModel.setEntryID(ID);
        attachmentRequestModel.setPathUUID(PATH_UUID);

        return attachmentRequestModel;
    }

    private static CategoryCreateRequestModel prepareCategoryCreateRequestModel() {

        CategoryCreateRequestModel categoryCreateRequestModel = new CategoryCreateRequestModel();
        categoryCreateRequestModel.setTitle(TITLE);
        categoryCreateRequestModel.setDescription(DESCRIPTION);

        return categoryCreateRequestModel;
    }

    private static CommentUpdateRequestModel prepareCommentUpdateRequestModel() {

        CommentUpdateRequestModel commentUpdateRequestModel = new CommentUpdateRequestModel();
        commentUpdateRequestModel.setContent(CONTENT);

        return commentUpdateRequestModel;
    }

    private static CommentCreateRequestModel prepareCommentCreateRequestModel() {

        CommentCreateRequestModel commentCreateRequestModel = new CommentCreateRequestModel();
        commentCreateRequestModel.setContent(CONTENT);
        commentCreateRequestModel.setAuthenticatedUserId(ID);
        commentCreateRequestModel.setEmail(EMAIL);
        commentCreateRequestModel.setUsername(USERNAME);
        commentCreateRequestModel.setEntryId(ID);

        return commentCreateRequestModel;
    }

    private static DocumentUpdateRequestModel prepareDocumentUpdateRequestModel() {

        DocumentUpdateRequestModel documentUpdateRequestModel = new DocumentUpdateRequestModel();
        documentUpdateRequestModel.setTitle(TITLE);
        documentUpdateRequestModel.setContent(CONTENT);
        documentUpdateRequestModel.setRawContent(RAW_CONTENT);
        documentUpdateRequestModel.setLink(LINK);
        documentUpdateRequestModel.setEnabled(ENABLED);
        documentUpdateRequestModel.setLocale(java.util.Locale.ENGLISH);
        documentUpdateRequestModel.setMetaTitle(SEO_TITLE);
        documentUpdateRequestModel.setMetaDescription(SEO_DESCRIPTION);
        documentUpdateRequestModel.setMetaKeywords(SEO_KEYWORDS);

        return documentUpdateRequestModel;
    }

    private static DocumentCreateRequestModel prepareDocumentCreateRequestModel() {

        DocumentCreateRequestModel documentCreateRequestModel = new DocumentCreateRequestModel();
        documentCreateRequestModel.setUserID(ID);
        documentCreateRequestModel.setTitle(TITLE);
        documentCreateRequestModel.setContent(CONTENT);
        documentCreateRequestModel.setRawContent(RAW_CONTENT);
        documentCreateRequestModel.setLink(LINK);
        documentCreateRequestModel.setEnabled(ENABLED);
        documentCreateRequestModel.setLocale(java.util.Locale.ENGLISH);
        documentCreateRequestModel.setMetaTitle(SEO_TITLE);
        documentCreateRequestModel.setMetaDescription(SEO_DESCRIPTION);
        documentCreateRequestModel.setMetaKeywords(SEO_KEYWORDS);

        return documentCreateRequestModel;
    }

    private static EntryUpdateRequestModel prepareEntryUpdateRequestModel() {

        EntryUpdateRequestModel entryUpdateRequestModel = new EntryUpdateRequestModel();
        entryUpdateRequestModel.setTitle(TITLE);
        entryUpdateRequestModel.setContent(CONTENT);
        entryUpdateRequestModel.setRawContent(RAW_CONTENT);
        entryUpdateRequestModel.setPrologue(PROLOGUE);
        entryUpdateRequestModel.setCategoryID(ID);
        entryUpdateRequestModel.setStatus(EntryInitialStatus.PUBLIC);
        entryUpdateRequestModel.setLink(LINK);
        entryUpdateRequestModel.setEnabled(ENABLED);
        entryUpdateRequestModel.setLocale(java.util.Locale.ENGLISH);
        entryUpdateRequestModel.setMetaTitle(SEO_TITLE);
        entryUpdateRequestModel.setMetaDescription(SEO_DESCRIPTION);
        entryUpdateRequestModel.setMetaKeywords(SEO_KEYWORDS);

        return entryUpdateRequestModel;
    }

    private static EntryCreateRequestModel prepareEntryCreateRequestModel() {

        EntryCreateRequestModel entryCreateRequestModel = new EntryCreateRequestModel();
        entryCreateRequestModel.setUserID(ID);
        entryCreateRequestModel.setTitle(TITLE);
        entryCreateRequestModel.setContent(CONTENT);
        entryCreateRequestModel.setRawContent(RAW_CONTENT);
        entryCreateRequestModel.setPrologue(PROLOGUE);
        entryCreateRequestModel.setCategoryID(ID);
        entryCreateRequestModel.setStatus(EntryInitialStatus.PUBLIC);
        entryCreateRequestModel.setLink(LINK);
        entryCreateRequestModel.setEnabled(ENABLED);
        entryCreateRequestModel.setLocale(java.util.Locale.ENGLISH);
        entryCreateRequestModel.setMetaTitle(SEO_TITLE);
        entryCreateRequestModel.setMetaDescription(SEO_DESCRIPTION);
        entryCreateRequestModel.setMetaKeywords(SEO_KEYWORDS);

        return entryCreateRequestModel;
    }

    private static FileUploadRequestModel prepareFileUploadRequestModel() {

        FileUploadRequestModel fileUploadRequestModel = new FileUploadRequestModel();
        fileUploadRequestModel.setInputFile(MOCK_MULTIPART_FILE);
        fileUploadRequestModel.setDescription(DESCRIPTION);
        fileUploadRequestModel.setSubFolder(SUB_1);

        return fileUploadRequestModel;
    }

    private static UpdateFileMetaInfoRequestModel prepareUpdateFileMetaInfoRequestModel() {

        UpdateFileMetaInfoRequestModel updateFileMetaInfoRequestModel = new UpdateFileMetaInfoRequestModel();
        updateFileMetaInfoRequestModel.setDescription(DESCRIPTION);
        updateFileMetaInfoRequestModel.setOriginalFilename(ORIGINAL_FILENAME);

        return updateFileMetaInfoRequestModel;
    }

    private static TagAssignmentRequestModel prepareTagAssignmentRequestModel() {

        TagAssignmentRequestModel tagAssignmentRequestModel = new TagAssignmentRequestModel();
        tagAssignmentRequestModel.setTagID(2L);
        tagAssignmentRequestModel.setEntryID(3L);

        return tagAssignmentRequestModel;
    }

    private static TagCreateRequestModel prepareTagCreateRequestModel() {

        TagCreateRequestModel tagCreateRequestModel = new TagCreateRequestModel();
        tagCreateRequestModel.setName(TITLE);

        return tagCreateRequestModel;
    }

    private static LoginRequestModel prepareLoginRequestModel() {

        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(EMAIL);
        loginRequestModel.setPassword(PASSWORD);

        return loginRequestModel;
    }

    private static UpdateProfileRequestModel prepareUpdateProfileRequestModel() {

        UpdateProfileRequestModel updateProfileRequestModel = new UpdateProfileRequestModel();
        updateProfileRequestModel.setUsername(USERNAME);
        updateProfileRequestModel.setEmail(EMAIL);
        updateProfileRequestModel.setDefaultLocale(java.util.Locale.ENGLISH);

        return updateProfileRequestModel;
    }

    private static UserInitializeRequestModel prepareUserInitializeRequestModel() {

        UserInitializeRequestModel userInitializeRequestModel = new UserInitializeRequestModel();
        userInitializeRequestModel.setEmail(EMAIL);
        userInitializeRequestModel.setDefaultLocale(java.util.Locale.ENGLISH);
        userInitializeRequestModel.setUsername(USERNAME);
        userInitializeRequestModel.setPassword(PASSWORD);
        userInitializeRequestModel.setPasswordConfirmation(PASSWORD);

        return userInitializeRequestModel;
    }

    private static UserCreateRequestModel prepareUserCreateRequestModel() {

        UserCreateRequestModel userCreateRequestModel = new UserCreateRequestModel();
        userCreateRequestModel.setEmail(EMAIL);
        userCreateRequestModel.setDefaultLocale(java.util.Locale.ENGLISH);
        userCreateRequestModel.setUsername(USERNAME);
        userCreateRequestModel.setPassword(PASSWORD);
        userCreateRequestModel.setPasswordConfirmation(PASSWORD);
        userCreateRequestModel.setRole(ADMIN);

        return userCreateRequestModel;
    }

    private static PasswordResetDemandRequestModel preparePasswordResetDemandRequestModel() {

        PasswordResetDemandRequestModel passwordResetDemandRequestModel = new PasswordResetDemandRequestModel();
        passwordResetDemandRequestModel.setEmail(EMAIL);

        return passwordResetDemandRequestModel;
    }

    private static Date createDate(int dayOffset) {

        Calendar calendar = new Calendar.Builder()
                .setInstant(new Date())
                .build();
        calendar.add(Calendar.DAY_OF_MONTH, dayOffset);

        return calendar.getTime();
    }

    private static FileInputVO prepareFileInputVO() {
        try {
            return FileInputVO.getBuilder()
                    .withContentType(IMAGE_JPG)
                    .withOriginalFilename(ORIGINAL_FILENAME)
                    .withSize(CONTENT.length())
                    .withFileContentStream(MOCK_MULTIPART_FILE.getInputStream())
                    .withRelativePath(SUB_1)
                    .withDescription(DESCRIPTION)
                    .build();
        } catch (IOException e) {
            fail("Failed to init test");
            return null;
        }
    }

    @Mock
    private CommonFormatter commonFormatter;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JULocaleToLeafletLocaleConverter localeConverter;

    @Before
    public void setup() {
        given(httpServletRequest.getLocale()).willReturn(java.util.Locale.ENGLISH);
        given(commonFormatter.formatDate(CREATED, java.util.Locale.ENGLISH)).willReturn(FORMATTED_CREATED_DATE);
        given(commonFormatter.formatDate(LAST_MODIFIED, java.util.Locale.ENGLISH)).willReturn(FORMATTED_LAST_MODIFIED_DATE);
        given(commonFormatter.formatDate(LAST_LOGIN, java.util.Locale.ENGLISH)).willReturn(FORMATTED_LAST_LOGIN_DATE);
        given(localeConverter.convert(java.util.Locale.ENGLISH)).willReturn(LOCALE);
    }

    protected JULocaleToLeafletLocaleConverter getLocaleConverter() {
        return localeConverter;
    }
}
