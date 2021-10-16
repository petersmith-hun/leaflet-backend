package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.service.config.LeafletITContextConfig;
import hu.psprog.leaflet.service.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * Integration tests for {@link DynamicConfigurationPropertyServiceImpl}.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = LeafletITContextConfig.class)
@ActiveProfiles(LeafletITContextConfig.INTEGRATION_TEST_CONFIG_PROFILE)
public class DynamicConfigurationPropertyServiceImplIT {

    private static final int NUMBER_OF_DCP_ENTRIES = 4;
    private static final String CONTROL_KEY = "PAGE_TITLE";
    private static final String CONTROL_VALUE = "Default page title";

    @Autowired
    private DynamicConfigurationPropertyService dynamicConfigurationPropertyService;

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DCP)
    public void testGetAll() {

        // when
        forceReload();
        Map<String, String> result = dynamicConfigurationPropertyService.getAll();

        // then
        assertThat(result.size(), equalTo(NUMBER_OF_DCP_ENTRIES));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DCP)
    public void testGetWithPopulatedStore() {

        // when
        forceReload();
        String result = dynamicConfigurationPropertyService.get(CONTROL_KEY);

        // then
        assertThat(result, equalTo(CONTROL_VALUE));
    }

    @Test
    @Transactional
    public void testGetWithNotPopulatedStore() {

        // when
        forceReload();
        String result = dynamicConfigurationPropertyService.get(CONTROL_KEY);

        // then
        assertThat(result, nullValue());
    }

    @Test
    @Transactional
    public void testAdd() throws ServiceException {

        // when
        forceReload();
        dynamicConfigurationPropertyService.add(CONTROL_KEY, CONTROL_VALUE);

        // then
        assertThat(dynamicConfigurationPropertyService.get(CONTROL_KEY), equalTo(CONTROL_VALUE));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DCP)
    public void testUpdate() throws ServiceException {

        // given
        String updatedValue = "New value";

        // when
        forceReload();
        dynamicConfigurationPropertyService.update(CONTROL_KEY, updatedValue);

        // then
        assertThat(dynamicConfigurationPropertyService.get(CONTROL_KEY), equalTo(updatedValue));
    }

    @Test
    @Transactional
    @Sql(LeafletITContextConfig.INTEGRATION_TEST_DB_SCRIPT_DCP)
    public void testDelete() throws ServiceException {

        // when
        forceReload();
        dynamicConfigurationPropertyService.delete(CONTROL_KEY);

        // then
        deletionCheck();
        forceReload();
        deletionCheck();
    }

    private void deletionCheck() {
        assertThat(dynamicConfigurationPropertyService.getAll().values().stream()
                .noneMatch(item -> item.equals(CONTROL_VALUE)), equalTo(true));
        assertThat(dynamicConfigurationPropertyService.getAll().size(), equalTo(3));
    }

    private void forceReload() {
        ((DynamicConfigurationPropertyServiceImpl) dynamicConfigurationPropertyService).populateStore();
    }
}
