package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.TagDAO;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO implementation for {@link TagRepository}.
 *
 * @author Peter Smith
 */
@Component
public class TagDAOImpl extends SelfStatusAwareDAOImpl<Tag, Long> implements TagDAO {

    @Autowired
    public TagDAOImpl(final TagRepository tagRepository, JpaContext jpaContext) {
        super(tagRepository, jpaContext);
    }

    @Override
    public List<Tag> findAll(Specification<Tag> specification) {
        return ((TagRepository) jpaRepository).findAll(specification);
    }

    @Override
    protected void doUpdate(Tag currentEntity, Tag updatedEntity) {
        currentEntity.setTitle(updatedEntity.getTitle());
    }
}
