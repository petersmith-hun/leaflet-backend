package hu.psprog.leaflet.service.testdata;

import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import hu.psprog.leaflet.persistence.entity.Locale;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.vo.CategoryVO;
import hu.psprog.leaflet.service.vo.CommentVO;
import hu.psprog.leaflet.service.vo.DocumentVO;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.FrontEndRouteVO;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.service.vo.UserVO;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Test object collection.
 *
 * @author Peter Smith
 */
public interface TestObjects {

    Date GENERAL_DATE = Date.from(Instant.ofEpochMilli(1471514400000L));
    Date ROUTE_DATE = Timestamp.from(Instant.ofEpochMilli(1520971200000L));
    Timestamp UPLOADED_FILE_DATE = createUploadedFileDate();
    UserVO GENERAL_OWNER = UserVO.wrapMinimumVO(2L);

    CategoryVO CATEGORY_VO_1 = CategoryVO.getBuilder()
            .withId(1L)
            .withTitle("IT category")
            .withDescription("Control category object JSON descriptor")
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .build();

    CategoryVO CATEGORY_VO_NEW = CategoryVO.getBuilder()
            .withId(4L)
            .withTitle("IT category 4")
            .withDescription("Control category object JSON descriptor for new category")
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .build();

    CommentVO COMMENT_VO_1 = CommentVO.getBuilder()
            .withId(1L)
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .withDeleted(false)
            .withContent("Test comment 1")
            .withEntryVO(EntryVO.wrapMinimumVO(1L))
            .withOwner(GENERAL_OWNER)
            .build();

    CommentVO COMMENT_VO_NEW = CommentVO.getBuilder()
            .withId(11L)
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .withDeleted(false)
            .withContent("Test comment newly created")
            .withEntryVO(EntryVO.wrapMinimumVO(1L))
            .withOwner(UserVO.wrapMinimumVO(3L))
            .build();

    DocumentVO DOCUMENT_VO_1 = DocumentVO.getBuilder()
            .withId(1L)
            .withTitle("Lorem ipsum dolor sit amet")
            .withLink("lorem-ipsum-dolor-sit-amet-20160818")
            .withRawContent("markdown code document 1")
            .withSeoTitle("Lorem ipsum dolor sit amet")
            .withSeoDescription("Lorem ipsum dolor sit amet")
            .withSeoKeywords("Lorem ipsum dolor sit amet")
            .withLocale(Locale.EN)
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .withOwner(GENERAL_OWNER)
            .build();

    DocumentVO DOCUMENT_VO_NEW = DocumentVO.getBuilder()
            .withId(5L)
            .withTitle("Test document newly created")
            .withLink("test-document-newly-created-20161009")
            .withRawContent("markdown code document 5")
            .withSeoTitle("Test document newly created")
            .withSeoDescription("Test document newly created")
            .withSeoKeywords("Test document newly created")
            .withLocale(Locale.EN)
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .withOwner(GENERAL_OWNER)
            .build();

    EntryVO ENTRY_VO_1 = EntryVO.getBuilder()
            .withId(1L)
            .withTitle("Lorem ipsum dolor sit amet")
            .withLink("lorem-ipsum-dolor-sit-amet-20160818")
            .withPrologue("Fusce nec tortor vitae lorem volutpat finibus. Fusce condimentum diam sit amet leo pretium tincidunt. Duis vitae interdum dui. Nulla facilisi.")
            .withRawContent("markdown code entry 1")
            .withSeoTitle("Lorem ipsum dolor sit amet")
            .withSeoDescription("Lorem ipsum dolor sit amet")
            .withSeoKeywords("Lorem ipsum dolor sit amet")
            .withEntryStatus("PUBLIC")
            .withLocale(Locale.EN)
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .withOwner(GENERAL_OWNER)
            .build();

    EntryVO ENTRY_VO_NEW = EntryVO.getBuilder()
            .withId(4L)
            .withTitle("Lorem ipsum dolor sit amet 2")
            .withLink("lorem-ipsum-dolor-sit-amet-2-20160818")
            .withPrologue("Fusce nec tortor vitae lorem volutpat finibus. Fusce condimentum diam sit amet leo pretium tincidunt. Duis vitae interdum dui. Nulla facilisi.")
            .withRawContent("markdown code entry 4")
            .withSeoTitle("Lorem ipsum dolor sit amet")
            .withSeoDescription("Lorem ipsum dolor sit amet")
            .withSeoKeywords("Lorem ipsum dolor sit amet")
            .withEntryStatus("PUBLIC")
            .withLocale(Locale.EN)
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .withOwner(GENERAL_OWNER)
            .build();

    FrontEndRouteVO FRONT_END_ROUTE_VO_1 = FrontEndRouteVO.getBuilder()
            .withRouteId("route-1")
            .withName("Route 1")
            .withUrl("/route/header/1")
            .withSequenceNumber(1)
            .withType(FrontEndRouteType.HEADER_MENU)
            .withId(1L)
            .withCreated(ROUTE_DATE)
            .withLastModified(ROUTE_DATE)
            .withEnabled(true)
            .build();

    FrontEndRouteVO FRONT_END_ROUTE_VO_NEW = FrontEndRouteVO.getBuilder()
            .withRouteId("route_test_new")
            .withName("Test route new")
            .withUrl("/test/standalone/new")
            .withSequenceNumber(1)
            .withType(FrontEndRouteType.STANDALONE)
            .build();

    TagVO TAG_VO_1 = TagVO.getBuilder()
            .withId(1L)
            .withTitle("Tag #1")
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .build();

    TagVO TAG_VO_ALREADY_ATTACHED = TagVO.getBuilder()
            .withId(20L)
            .withTitle("Tag #20")
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .build();

    TagVO TAG_VO_NEW = TagVO.getBuilder()
            .withId(21L)
            .withTitle("Tag #21")
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .build();

    TagVO TAG_VO_TO_ATTACH = TagVO.getBuilder()
            .withId(16L)
            .withTitle("Tag #16")
            .withCreated(GENERAL_DATE)
            .withLastModified(GENERAL_DATE)
            .withEnabled(true)
            .build();

    UploadedFile UPLOADED_FILE_ALREADY_ATTACHED = UploadedFile.getBuilder()
            .withId(3L)
            .withCreated(UPLOADED_FILE_DATE)
            .withOriginalFilename("original_filename_3.jpg")
            .withPath("images/stored_filename_3.jpg")
            .withMime("image/jpeg")
            .withStoredFilename("stored_filename_3.jpg")
            .withPathUUID(UUID.fromString("058c9d47-6ce4-3e48-9c44-35bb9c74b378"))
            .withDescription("Uploaded file #3")
            .withEnabled(true)
            .build();

    UploadedFile UPLOADED_FILE_TO_ATTACH = UploadedFile.getBuilder()
            .withId(1L)
            .withCreated(UPLOADED_FILE_DATE)
            .withOriginalFilename("original_filename_1.jpg")
            .withPath("images/stored_filename_1.jpg")
            .withMime("image/jpeg")
            .withStoredFilename("stored_filename_1.jpg")
            .withPathUUID(UUID.fromString("d4b1830d-f368-37a0-88f9-2faf7fa8ded6"))
            .withDescription("Uploaded file #1")
            .withEnabled(true)
            .build();

    private static Timestamp createUploadedFileDate() {
        try {
            return new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2017-05-13T15:00:00Z").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
