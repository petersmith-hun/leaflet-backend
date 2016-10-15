package hu.psprog.leaflet.web.rest.controller;

import hu.psprog.leaflet.api.rest.response.common.BaseBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entity.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.layout.DefaultListLayoutDataModel;
import hu.psprog.leaflet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user-related entry points.
 *
 * @author Peter Smith
 */
@RestController
@RequestMapping(UsersController.BASE_MAPPING)
public class UsersController {

    static final String BASE_MAPPING = "/users";
    private static final String LIST_NODE_NAME = "users";

    @Autowired
    private UserService userService;

    /**
     * GET /users
     * Returns basic information of all existing users.
     *
     * @return list of existing users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public BaseBodyDataModel getAllUsers() {

        DefaultListLayoutDataModel.Builder responseBuilder = new DefaultListLayoutDataModel.Builder();
        responseBuilder.setNodeName(LIST_NODE_NAME);

        userService.getAll().forEach(user -> responseBuilder.withItem(new ExtendedUserDataModel.Builder()
                        .withEmail(user.getEmail())
                        .withUsername(user.getUsername())
                        .withID(user.getId())
                        .build()));

        return responseBuilder.build();
    }
}
