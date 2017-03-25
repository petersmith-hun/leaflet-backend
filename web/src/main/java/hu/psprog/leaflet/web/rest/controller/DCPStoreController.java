package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.request.dcp.DCPRequestModel;
import hu.psprog.leaflet.api.rest.response.common.ValidationErrorMessageListDataModel;
import hu.psprog.leaflet.api.rest.response.dcp.DCPDataModel;
import hu.psprog.leaflet.api.rest.response.dcp.DCPListDataModel;
import hu.psprog.leaflet.service.DynamicConfigurationPropertyService;
import hu.psprog.leaflet.service.exception.ServiceException;
import hu.psprog.leaflet.web.exception.RequestCouldNotBeFulfilledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

/**
 * REST controller for DCP Store related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(value = BaseController.BASE_PATH_DCP)
public class DCPStoreController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DCPStoreController.class);

    private static final String NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_CREATED = "New configuration entry could not be created.";
    private static final String NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_UPDATED = "Configuration entry could not be updated.";
    private static final String NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_DELETED = "Configuration entry could not be deleted.";

    @Autowired
    private DynamicConfigurationPropertyService dynamicConfigurationPropertyService;

    /**
     * GET /dcp
     * Lists all existing DCP Store entries.
     *
     * @return list of existing DCP entries
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAll() {

        Map<String, String> dcpStore = dynamicConfigurationPropertyService.getAll();
        DCPListDataModel.Builder builder = new DCPListDataModel.Builder();
        dcpStore.entrySet().forEach(entry -> builder.withItem(createDataModel(entry)));

        return wrap(builder.build());
    }

    /**
     * POST /dcp
     * Creates a new DCP Store entry.
     *
     * @param dcpRequestModel {@link DCPRequestModel} object holding entry data
     * @param bindingResult validation results
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView create(@RequestBody @Valid DCPRequestModel dcpRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                dynamicConfigurationPropertyService.add(dcpRequestModel.getKey(), dcpRequestModel.getValue());
                return null;
            } catch (ServiceException e) {
                LOGGER.error(NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_CREATED, e);
                throw new RequestCouldNotBeFulfilledException(NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_CREATED);
            }
        }
    }

    /**
     * PUT /dcp
     * Updates an existing DCP Store entry.
     *
     * @param dcpRequestModel {@link DCPRequestModel} object holding entry data
     * @param bindingResult validation results
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView update(@RequestBody @Valid DCPRequestModel dcpRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                dynamicConfigurationPropertyService.update(dcpRequestModel.getKey(), dcpRequestModel.getValue());
                return null;
            } catch (ServiceException e) {
                LOGGER.error(NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_UPDATED, e);
                throw new RequestCouldNotBeFulfilledException(NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_UPDATED);
            }
        }
    }

    /**
     * DELETE /dcp
     * Deletes an existing DCP Store entry.
     *
     * @param dcpRequestModel {@link DCPRequestModel} object holding entry data (only the key is required)
     * @param bindingResult validation results
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ModelAndView remove(@RequestBody @Valid DCPRequestModel dcpRequestModel, BindingResult bindingResult)
            throws RequestCouldNotBeFulfilledException {

        if (bindingResult.hasErrors()) {
            return wrap(conversionService.convert(bindingResult.getAllErrors(), ValidationErrorMessageListDataModel.class));
        } else {
            try {
                dynamicConfigurationPropertyService.delete(dcpRequestModel.getKey());
                return null;
            } catch (ServiceException e) {
                LOGGER.error(NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_DELETED, e);
                throw new RequestCouldNotBeFulfilledException(NEW_CONFIGURATION_ENTRY_COULD_NOT_BE_DELETED);
            }
        }
    }

    private DCPDataModel createDataModel(Map.Entry<String, String> dcpEntry) {

        return new DCPDataModel.Builder()
                .withKey(dcpEntry.getKey())
                .withValue(dcpEntry.getValue())
                .build();
    }
}