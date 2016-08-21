package hu.psprog.leaflet.persistence.dao.impl;

import hu.psprog.leaflet.persistence.dao.TagDAO;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * DAO implementation for {@link TagRepository}.
 *
 * @author Peter Smith
 */
@Component
public class TagDAOImpl extends SelfStatusAwareDAOImpl<Tag, Long> implements TagDAO {

    @Autowired
    public TagDAOImpl(final TagRepository tagRepository) {
        super(tagRepository);
    }

    @Override
    public Tag updateOne(Long id, Tag updatedEntity) {

        Tag currentTag = jpaRepository.getOne(id);
        if (currentTag != null) {
            currentTag.setTitle(updatedEntity.getTitle());
            currentTag.setLastModified(new Date());
            jpaRepository.flush();
        }

        return currentTag;
    }
}
