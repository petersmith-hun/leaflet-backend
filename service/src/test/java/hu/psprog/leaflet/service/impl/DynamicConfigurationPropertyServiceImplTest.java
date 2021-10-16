package hu.psprog.leaflet.service.impl;

import hu.psprog.leaflet.persistence.dao.DynamicConfigurationPropertyDAO;
import hu.psprog.leaflet.persistence.entity.DynamicConfigurationProperty;
import hu.psprog.leaflet.service.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import javax.persistence.PersistenceException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DynamicConfigurationPropertyServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
public class DynamicConfigurationPropertyServiceImplTest {

    private static final String KEY_1 = "KEY_1";
    private static final String KEY_2 = "KEY_2";
    private static final String KEY_3 = "KEY_3";
    private static final String VALUE_1 = "VALUE_1";
    private static final String VALUE_2 = "VALUE_2";
    private static final String VALUE_3 = "VALUE_3";
    private static final String NEW_KEY = "new-key";
    private static final String NEW_VALUE = "new-value";
    private static final String POPULATE_KEY = "populate-key";
    private static final String POPULATE_VALUE = "populate-value";

    @Mock
    private DynamicConfigurationPropertyDAO dynamicConfigurationPropertyDAO;

    @InjectMocks
    private DynamicConfigurationPropertyServiceImpl dynamicConfigurationPropertyService;

    @BeforeEach
    public void setup() {
        Field dcpStore = ReflectionUtils.findField(DynamicConfigurationPropertyServiceImpl.class, "dcpStore");
        dcpStore.setAccessible(true);
        ReflectionUtils.setField(dcpStore, dynamicConfigurationPropertyService, prepareDCPStore());
    }

    @Test
    public void shouldPopulateStore() {

        // given
        DynamicConfigurationProperty property = new DynamicConfigurationProperty(POPULATE_KEY, POPULATE_VALUE);
        given(dynamicConfigurationPropertyDAO.findAll()).willReturn(Collections.singletonList(property));

        // when
        dynamicConfigurationPropertyService.populateStore();

        // then
        Map<String, String> result = dynamicConfigurationPropertyService.getAll();
        assertThat(result.size(), equalTo(1));
        assertThat(result.get(POPULATE_KEY), equalTo(POPULATE_VALUE));
    }

    @Test
    public void shouldGetAll() {

        // when
        Map<String, String> result = dynamicConfigurationPropertyService.getAll();

        // then
        assertThat(result.containsKey(KEY_1), is(true));
        assertThat(result.containsKey(KEY_2), is(true));
        assertThat(result.containsKey(KEY_3), is(true));
    }

    @Test
    public void shouldGet() {

        // when
        String result = dynamicConfigurationPropertyService.get(KEY_1);

        // then
        assertThat(result, equalTo(VALUE_1));
    }

    @Test
    public void shouldGetReturnNullWithNonExistingKey() {

        // when
        String result = dynamicConfigurationPropertyService.get("non-existing-key");

        // then
        assertThat(result, nullValue());
    }

    @Test
    public void shouldAdd() throws ServiceException {

        // when
        dynamicConfigurationPropertyService.add(NEW_KEY, NEW_VALUE);

        // then
        verify(dynamicConfigurationPropertyDAO).save(new DynamicConfigurationProperty(NEW_KEY, NEW_VALUE));
        assertThat(dynamicConfigurationPropertyService.getAll().containsKey(NEW_KEY), is(true));
    }

    @Test
    public void shouldAddThrowServiceExceptionIfPropertyCouldNotBeSaved() {

        // given
        doThrow(PersistenceException.class).when(dynamicConfigurationPropertyDAO).save(any(DynamicConfigurationProperty.class));

        // when
        Assertions.assertThrows(ServiceException.class, () -> dynamicConfigurationPropertyService.add(NEW_KEY, NEW_VALUE));

        // then
        // exception expected
    }

    @Test
    public void shouldUpdate() throws ServiceException {

        // when
        dynamicConfigurationPropertyService.update(KEY_1, NEW_VALUE);

        // then
        verify(dynamicConfigurationPropertyDAO).updateOne(KEY_1, new DynamicConfigurationProperty(KEY_1, NEW_VALUE));
        assertThat(dynamicConfigurationPropertyService.get(KEY_1), equalTo(NEW_VALUE));
    }

    @Test
    public void shouldUpdateThrowServiceExceptionIfPropertyCouldNotBeUpdated() {

        // given
        doThrow(PersistenceException.class).when(dynamicConfigurationPropertyDAO).updateOne(anyString(), any(DynamicConfigurationProperty.class));

        // when
        Assertions.assertThrows(ServiceException.class, () -> dynamicConfigurationPropertyService.update(KEY_1, NEW_VALUE));

        // then
        // exception expected
    }

    @Test
    public void shouldDelete() throws ServiceException {

        // when
        dynamicConfigurationPropertyService.delete(KEY_1);

        // then
        verify(dynamicConfigurationPropertyDAO).delete(KEY_1);
        assertThat(dynamicConfigurationPropertyService.get(KEY_1), nullValue());
    }

    @Test
    public void shouldDeleteThrowServiceExceptionIfPropertyCouldNotBeDeleted() {

        // given
        doThrow(PersistenceException.class).when(dynamicConfigurationPropertyDAO).delete(anyString());

        // when
        Assertions.assertThrows(ServiceException.class, () -> dynamicConfigurationPropertyService.delete(KEY_1));

        // then
        // exception expected
    }

    private Map<String, String> prepareDCPStore() {

        Map<String, String> dcp = new HashMap<>();
        dcp.put(KEY_1, VALUE_1);
        dcp.put(KEY_2, VALUE_2);
        dcp.put(KEY_3, VALUE_3);

        return dcp;
    }
}