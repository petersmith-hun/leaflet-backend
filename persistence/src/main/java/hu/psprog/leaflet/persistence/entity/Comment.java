package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
