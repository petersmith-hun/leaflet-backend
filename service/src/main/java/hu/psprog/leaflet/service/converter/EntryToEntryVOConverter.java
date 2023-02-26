package hu.psprog.leaflet.service.converter;

import hu.psprog.leaflet.persistence.entity.Entry;
import hu.psprog.leaflet.persistence.entity.Tag;
import hu.psprog.leaflet.persistence.entity.UploadedFile;
import hu.psprog.leaflet.service.vo.EntryVO;
import hu.psprog.leaflet.service.vo.TagVO;
import hu.psprog.leaflet.service.vo.UploadedFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converts {@link Entry} to {@link EntryVO} object.
 *
 * @author Peter Smith
 */
@Component
public class EntryToEntryVOConverter implements Converter<Entry, EntryVO> {

    private final UserToUserVOConverter userToUserVOConverter;
    private final CategoryToCategoryVOConverter categoryToCategoryVOConverter;
    private final UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter;
    private final TagToTagVOConverter tagToTagVOConverter;

    @Autowired
    public EntryToEntryVOConverter(UserToUserVOConverter userToUserVOConverter, CategoryToCategoryVOConverter categoryToCategoryVOConverter,
                                   UploadedFileToUploadedFileVOConverter uploadedFileToUploadedFileVOConverter, TagToTagVOConverter tagToTagVOConverter) {
        this.userToUserVOConverter = userToUserVOConverter;
        this.categoryToCategoryVOConverter = categoryToCategoryVOConverter;
        this.uploadedFileToUploadedFileVOConverter = uploadedFileToUploadedFileVOConverter;
        this.tagToTagVOConverter = tagToTagVOConverter;
    }

    @Override
    public EntryVO convert(Entry source) {

        EntryVO.EntryVOBuilder<?, ?> builder = EntryVO.getBuilder();
        builder.withRawContent(source.getRawContent())
                .withCreated(source.getCreated())
                .withPublished(source.getPublished())
                .withEnabled(source.isEnabled())
                .withLastModified(source.getLastModified())
                .withEntryStatus(source.getStatus().name())
                .withLocale(source.getLocale())
                .withId(source.getId())
                .withLink(source.getLink())
                .withPrologue(source.getPrologue())
                .withSeoTitle(source.getSeoTitle())
                .withSeoDescription(source.getSeoDescription())
                .withSeoKeywords(source.getSeoKeywords())
                .withTitle(source.getTitle())
                .withAttachments(mapAttachments(source.getAttachments()))
                .withTags(mapTags(source.getTags()));

        if (Objects.nonNull(source.getCategory())) {
            builder.withCategory(categoryToCategoryVOConverter.convert(source.getCategory()));
        }

        if (Objects.nonNull(source.getUser())) {
            builder.withOwner(userToUserVOConverter.convert(source.getUser()));
        }

        return builder.build();
    }

    private List<UploadedFileVO> mapAttachments(List<UploadedFile> uploadedFiles) {
        return Optional.ofNullable(uploadedFiles)
                .orElse(Collections.emptyList()).stream()
                .map(uploadedFileToUploadedFileVOConverter::convert)
                .collect(Collectors.toList());
    }

    private List<TagVO> mapTags(List<Tag> tags) {
        return Optional.ofNullable(tags)
                .orElse(Collections.emptyList()).stream()
                .map(tagToTagVOConverter::convert)
                .collect(Collectors.toList());
    }
}
