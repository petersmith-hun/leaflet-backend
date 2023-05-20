package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Date;
import java.util.List;

/**
 * Blog entry entity class.
 *
 * Relations:
 *  - {@link Entry} 1:N {@link Comment}
 *  - {@link Entry} N:1 {@link Category}
 *  - {@link Entry} N:1 {@link User}
 *  - {@link Entry} N:M {@link UploadedFile}
 *  - {@link Entry} N:M {@link Tag}
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_ENTRIES,
        uniqueConstraints = @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_LINK, name = DatabaseConstants.UK_ENTRY_LINK))
public class Entry extends SelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_ENTRY_USER))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = DatabaseConstants.COLUMN_CATEGORY_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_ENTRY_CATEGORY))
    private Category category;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DatabaseConstants.TABLE_ENTRIES_TAGS,
            joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_ENTRY)),
            inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_TAG_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_TAGS_TAG)))
    private List<Tag> tags;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = DatabaseConstants.TABLE_ENTRIES_UPLOADED_FILES,
            joinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_UPLOADED_FILES_ENTRY)),
            inverseJoinColumns = @JoinColumn(name = DatabaseConstants.COLUMN_UPLOADED_FILE_ID,
                    foreignKey = @ForeignKey(name = DatabaseConstants.FK_NM_ENTRIES_UPLOADED_FILES_UPLOADED_FILE)))
    private List<UploadedFile> attachments;

    @Column(name = DatabaseConstants.COLUMN_TITLE)
    private String title;

    @Column(name = DatabaseConstants.COLUMN_LINK)
    private String link;

    @Column(name = DatabaseConstants.COLUMN_PROLOGUE, columnDefinition = DatabaseConstants.DEF_TEXT)
    private String prologue;

    @Column(name = DatabaseConstants.COLUMN_RAW_CONTENT, columnDefinition = DatabaseConstants.DEF_LONGTEXT)
    private String rawContent;

    @Column(name = DatabaseConstants.COLUMN_SEO_TITLE)
    private String seoTitle;

    @Column(name = DatabaseConstants.COLUMN_SEO_DESCRIPTION, length = 4095)
    private String seoDescription;

    @Column(name = DatabaseConstants.COLUMN_SEO_KEYWORDS)
    private String seoKeywords;

    @Column(name = DatabaseConstants.COLUMN_LOCALE)
    @Enumerated(EnumType.STRING)
    private Locale locale;

    @Column(name = DatabaseConstants.COLUMN_STATUS)
    @Enumerated(EnumType.STRING)
    private EntryStatus status;

    @Column(name = DatabaseConstants.COLUMN_DATE_PUBLISHED)
    private Date published;
}
