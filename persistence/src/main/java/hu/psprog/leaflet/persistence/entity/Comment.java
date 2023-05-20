package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Comment entity class.
 *
 * Relations:
 *  - {@link Comment} N:1 {@link Entry}
 *  - {@link Comment} N:1 {@link User}
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_COMMENTS)
public class Comment extends LogicallyDeletableSelfStatusAwareIdentifiableEntity<Long> {

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.COLUMN_USER_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_COMMENT_USER))
    private User user;

    @ManyToOne
    @JoinColumn(name = DatabaseConstants.COLUMN_ENTRY_ID,
            foreignKey = @ForeignKey(name = DatabaseConstants.FK_COMMENT_ENTRY))
    private Entry entry;

    @Column(name = DatabaseConstants.COLUMN_CONTENT, length = 2000)
    private String content;
}
