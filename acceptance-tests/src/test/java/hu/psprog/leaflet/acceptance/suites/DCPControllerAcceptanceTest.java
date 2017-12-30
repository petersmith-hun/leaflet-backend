package hu.psprog.leaflet.acceptance.suites;

import hu.psprog.leaflet.acceptance.config.LeafletAcceptanceSuite;
import hu.psprog.leaflet.acceptance.config.ResetDatabase;
import hu.psprog.leaflet.api.rest.request.dcp.DCPRequestModel;
import hu.psprog.leaflet.api.rest.response.dcp.DCPDataModel;
import hu.psprog.leaflet.api.rest.response.dcp.DCPListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.DCPStoreBridgeService;
import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Acceptance tests for {@code /dcp} endpoints.
 *
 * @author Peter Smith
 */
@RunWith(JUnitParamsRunner.class)
@LeafletAcceptanceSuite
public class DCPControllerAcceptanceTest extends AbstractParameterizedBaseTest {

    private static final String DCP_NEW_ENTRY_KEY = "NEW_ENTRY";
    private static final String DCP_NEW_ENTRY_VALUE = "new entry value";

    private static final String DCP_CONTROL_KEY = "PAGE_TITLE";
    private static final String DCP_CONTROL_VALUE = "Default page title";

    @Autowired
    private DCPStoreBridgeService dcpStoreBridgeService;

    @Test
    public void shouldReturnDCPContent() throws CommunicationFailureException {

        // when
        DCPListDataModel result = dcpStoreBridgeService.getAllDCPEntries();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getDcpStore().size(), equalTo(4));
    }

    @Test
    @ResetDatabase
    public void shouldCreateDCPEntry() throws CommunicationFailureException {

        // given
        DCPRequestModel dcpRequestModel = new DCPRequestModel();
        dcpRequestModel.setKey(DCP_NEW_ENTRY_KEY);
        dcpRequestModel.setValue(DCP_NEW_ENTRY_VALUE);

        // when
        dcpStoreBridgeService.createDCPEntry(dcpRequestModel);

        // then
        assertKey(DCP_NEW_ENTRY_KEY);
        assertValue(DCP_NEW_ENTRY_KEY, DCP_NEW_ENTRY_VALUE);
    }

    @Test
    @ResetDatabase
    public void shouldUpdateDCPEntry() throws CommunicationFailureException {

        // given
        DCPRequestModel dcpRequestModel = new DCPRequestModel();
        dcpRequestModel.setKey(DCP_CONTROL_KEY);
        dcpRequestModel.setValue(DCP_NEW_ENTRY_VALUE);

        // when
        dcpStoreBridgeService.updateDCPEntry(dcpRequestModel);

        // then
        assertValue(DCP_CONTROL_KEY, DCP_NEW_ENTRY_VALUE);
    }

    @Test
    @ResetDatabase
    public void shouldDeleteDCPEntry() throws CommunicationFailureException {

        // when
        dcpStoreBridgeService.removeDCPEntry(DCP_CONTROL_KEY);

        // then
        assertMissingKey(DCP_CONTROL_KEY);

        // manual restore for DCP as database restore is not enough in this case
        restoreDCP();
    }

    private void assertKey(String key) throws CommunicationFailureException {
        DCPListDataModel dcp = dcpStoreBridgeService.getAllDCPEntries();
        assertThat(dcp.getDcpStore().stream()
                .anyMatch(dcpDataModel -> key.equals(dcpDataModel.getKey())), is(true));
    }

    private void assertMissingKey(String key) throws CommunicationFailureException {
        DCPListDataModel dcp = dcpStoreBridgeService.getAllDCPEntries();
        assertThat(dcp.getDcpStore().stream()
                .noneMatch(dcpDataModel -> key.equals(dcpDataModel.getKey())), is(true));
    }

    private void assertValue(String key, String value) throws CommunicationFailureException {
        DCPListDataModel dcp = dcpStoreBridgeService.getAllDCPEntries();
        assertThat(dcp.getDcpStore().stream()
                .filter(dcpDataModel -> key.equals(dcpDataModel.getKey()))
                .findFirst()
                .map(DCPDataModel::getValue)
                .orElse(null), equalTo(value));
    }

    private void restoreDCP() throws CommunicationFailureException {
        DCPRequestModel dcpRequestModel = new DCPRequestModel();
        dcpRequestModel.setKey(DCP_CONTROL_KEY);
        dcpRequestModel.setValue(DCP_CONTROL_VALUE);
        dcpStoreBridgeService.createDCPEntry(dcpRequestModel);
    }
}
