package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Category;
import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.User;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.EntryRepository}.
 *
 * @author Peter Smith
 */
public interface EntryDAO extends BaseDAO<Entry, Long>, SelfStatusAwareDAO<Long> {

    public Entry findByLink(String link);

    public List<Entry> findByUser(User user);

    public List<Entry> findByCategory(Category category);

    public List<Entry> findByTags(Tag tag);
}
