package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.tag.TagAssignmentRequestModel;
import hu.psprog.leaflet.api.rest.request.tag.TagCreateRequestModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EditEntryDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagDataModel;
import hu.psprog.leaflet.api.rest.response.tag.TagListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.client.exception.ResourceNotFoundException;
import hu.psprog.leaflet.bridge.service.EntryBridgeService;
import hu.psprog.leaflet.bridge.service.TagBridgeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /tags} endpoints.
 *
 * @author Peter Smith
 */
@LeafletAcceptanceSuite
public class TagsControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final int NUMBER_OF_ALL_TAGS = 9;
    private static final int NUMBER_OF_PUBLIC_TAGS = 6;
    private static final long CONTROL_ENTRY_ID = 1L;
    private static final long CONTROL_TAG_ID = 1L;
    private static final String CONTROL_TAG_1 = "tag-1";

    @Autowired
    private TagBridgeService tagBridgeService;

    @Autowired
    private EntryBridgeService entryBridgeService;

    @Test
    public void shouldGetAllTags() throws CommunicationFailureException {

        // when
        TagListDataModel result = tagBridgeService.getAllTags();

        // then
        assertThat(result, notNullValue());
        assertThat(result.tags().size(), equalTo(NUMBER_OF_ALL_TAGS));
    }

    @Test
    public void shouldGetPublicTags() throws CommunicationFailureException {

        // when
        WrapperBodyDataModel<TagListDataModel> result = tagBridgeService.getAllPublicTags();

        // then
        assertThat(result, notNullValue());
        assertThat(result.body().tags().size(), equalTo(NUMBER_OF_PUBLIC_TAGS));
        assertThat(result.body().tags().stream().allMatch(TagDataModel::enabled), is(true));
    }

    @Test
    public void shouldGetTagByID() throws CommunicationFailureException {

        // given
        TagDataModel control = getControl(CONTROL_TAG_1, TagDataModel.class);

        // when
        TagDataModel result = tagBridgeService.getTag(CONTROL_TAG_ID);

        // then
        assertThat(result, notNullValue());
        assertThat(result, equalTo(control));
    }

    @Test
    public void shouldGetTagByIDThrowResourceNotFoundIfTagDoesNotExist() {

        // given
        Long nonExistingTagID = 20L;

        // when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> tagBridgeService.getTag(nonExistingTagID));

        // then
        // exception expected
    }

    @Test
    @ResetDatabase
    public void shouldCreateTag() throws CommunicationFailureException {

        // given
        TagCreateRequestModel tagCreateRequestModel = prepareTagCreateRequestModel("New tag");

        // when
        TagDataModel result = tagBridgeService.createTag(tagCreateRequestModel);

        // then
        assertThat(result.id(), notNullValue());
        TagDataModel current = tagBridgeService.getTag(result.id());
        assertThat(current.name(), equalTo(tagCreateRequestModel.getName()));
        assertThat(current.enabled(), is(false));
    }

    @Test
    @ResetDatabase
    public void shouldUpdateTag() throws CommunicationFailureException {

        // given
        TagCreateRequestModel tagCreateRequestModel = prepareTagCreateRequestModel("Updated tag");

        // when
        tagBridgeService.updateTag(CONTROL_TAG_ID, tagCreateRequestModel);

        // then
        TagDataModel current = tagBridgeService.getTag(CONTROL_TAG_ID);
        assertThat(current.name(), equalTo(tagCreateRequestModel.getName()));
    }

    @Test
    @ResetDatabase
    public void shouldChangeStatus() throws CommunicationFailureException {

        // when
        tagBridgeService.changeStatus(CONTROL_TAG_ID);

        // then
        TagDataModel current = tagBridgeService.getTag(CONTROL_TAG_ID);
        assertThat(current.enabled(), is(false));
    }

    @Test
    @ResetDatabase
    public void shouldDeleteTag() throws CommunicationFailureException {

        // given
        Long tagToDelete = 9L;

        // when
        tagBridgeService.deleteTag(tagToDelete);

        // then
        TagListDataModel current = tagBridgeService.getAllTags();
        assertThat(current.tags().size(), equalTo(8));
        assertThat(current.tags().stream()
                .noneMatch(tagDataModel -> tagToDelete.equals(tagDataModel.id())), is(true));
    }

    @Test
    @ResetDatabase
    public void shouldAttachTagToEntry() throws CommunicationFailureException {

        // given
        Long tagToAttach = 8L;
        assertTagAssignmentState(tagToAttach, 3,false);

        // when
        tagBridgeService.attachTag(prepareTagAssignmentRequestModel(tagToAttach));

        // then
        assertTagAssignmentState(tagToAttach, 4,true);
    }

    @Test
    @ResetDatabase
    public void shouldDetachTagFromEntry() throws CommunicationFailureException {

        // given
        Long tagToAttach = 2L;
        assertTagAssignmentState(tagToAttach, 3, true);

        // when
        tagBridgeService.detachTag(prepareTagAssignmentRequestModel(tagToAttach));

        // then
        assertTagAssignmentState(tagToAttach, 2,false);
    }

    private void assertTagAssignmentState(Long tagID, int numberOfTagsAttached, boolean attached) throws CommunicationFailureException {
        WrapperBodyDataModel<EditEntryDataModel> current = entryBridgeService.getEntryByID(CONTROL_ENTRY_ID);
        assertThat(current.body().tags().size(), equalTo(numberOfTagsAttached));
        assertThat(current.body().tags().stream()
                .anyMatch(tagDataModel -> tagID.equals(tagDataModel.id())), is(attached));
    }

    private TagAssignmentRequestModel prepareTagAssignmentRequestModel(Long tagID) {

        TagAssignmentRequestModel tagAssignmentRequestModel = new TagAssignmentRequestModel();
        tagAssignmentRequestModel.setEntryID(CONTROL_ENTRY_ID);
        tagAssignmentRequestModel.setTagID(tagID);

        return tagAssignmentRequestModel;
    }

    private TagCreateRequestModel prepareTagCreateRequestModel(String name) {

        TagCreateRequestModel tagCreateRequestModel = new TagCreateRequestModel();
        tagCreateRequestModel.setName(name);

        return tagCreateRequestModel;
    }
}
