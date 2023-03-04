package hu.psprog.leaflet.service.util;

import hu.psprog.leaflet.persistence.dao.EntryDAO;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.EntryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Component to properly set date of publish of an entry.
 *
 * @author Peter Smith
 */
@Component
public class PublishHandler {

    private final EntryDAO entryDAO;

    @Autowired
    public PublishHandler(EntryDAO entryDAO) {
        this.entryDAO = entryDAO;
    }

    /**
     * Sets publish date of a newly created entry, if its status is {@link EntryStatus#PUBLIC}.
     * Otherwise, sets it to null.
     *
     * @param entryToBeCreated {@link Entry} object to be updated
     */
    public void updatePublishDate(Entry entryToBeCreated) {
        updateIfPublished(entryToBeCreated);
    }

    /**
     * Sets publish date of an entry that is being updated according to the following rules:
     *  - setting publish date is aborted in case a non-existing entry
     *  - if an entry is already public, keeps the currently set publish date
     *  - if an entry is not yet public, but the current update operation publishes it, sets the publish date to the current date
     *  - if an entry is not yet public, and the current update operation does not publish it, sets the publish date to null
     *
     * @param entryID ID of the entry currently being updated
     * @param entryToBeUpdated {@link Entry} object to be updated
     */
    public void updatePublishDate(Long entryID, Entry entryToBeUpdated) {

        entryDAO.findById(entryID).ifPresent(currentEntry -> {
            if (isPublic(currentEntry)) {
                entryToBeUpdated.setPublished(currentEntry.getPublished());
            } else {
                updateIfPublished(entryToBeUpdated);
            }
        });
    }

    private void updateIfPublished(Entry entry) {

        entry.setPublished(isPublic(entry)
                ? new Date()
                : null);
    }

    private boolean isPublic(Entry entry) {
        return entry.getStatus() == EntryStatus.PUBLIC;
    }
}
