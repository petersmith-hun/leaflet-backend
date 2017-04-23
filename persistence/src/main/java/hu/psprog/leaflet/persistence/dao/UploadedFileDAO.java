package hu.psprog.leaflet.persistence.dao;

import hu.psprog.leaflet.persistence.entity.UploadedFile;

/**
 * DAO for {@link hu.psprog.leaflet.persistence.repository.UploadedFileRepository}.
 *
 * @author Peter Smith
 */
public interface UploadedFileDAO extends BaseDAO<UploadedFile, Long>, SelfStatusAwareDAO<Long> {

    UploadedFile findByPath(String path);
}
