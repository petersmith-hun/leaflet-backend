package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.TagRepository}.
 *
 * @author Peter Smith
 */
public interface TagDAO extends BaseDAO<Tag, Long>, SelfStatusAwareDAO<Long> {

    List<Tag> findAll(Specification<Tag> specification);
}
