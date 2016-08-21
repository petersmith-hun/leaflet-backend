package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Tag;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.TagRepository}.
 *
 * @author Peter Smith
 */
public interface TagDAO extends BaseDAO<Tag, Long>, SelfStatusAwareDAO<Long> {
}
