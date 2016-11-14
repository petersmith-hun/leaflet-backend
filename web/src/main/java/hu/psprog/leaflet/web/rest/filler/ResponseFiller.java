package hu.psprog.leaflet.web.rest.filler;

import org.springframework.web.servlet.ModelAndView;

/**
 * Interface for response fillers. A response filler extends REST response with specific sections.
 *
 * @author Peter Smith
 */
public interface ResponseFiller {

    void fill(ModelAndView modelAndView);

    boolean shouldFill();
}
