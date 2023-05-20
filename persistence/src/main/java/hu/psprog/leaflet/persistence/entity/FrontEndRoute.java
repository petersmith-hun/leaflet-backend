package hu.psprog.leaflet.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entity class to represent a front-end route.
 *
 * @author Peter Smith
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(builderMethodName = "getBuilder", setterPrefix = "with")
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.TABLE_FRONT_END_ROUTES,
        uniqueConstraints = @UniqueConstraint(columnNames = DatabaseConstants.COLUMN_ROUTE_ID, name = DatabaseConstants.UK_ROUTE_ID))
public class FrontEndRoute extends SelfStatusAwareIdentifiableEntity<Long> {

    @Column(name = DatabaseConstants.COLUMN_ROUTE_ID)
    private String routeId;

    @Column(name = DatabaseConstants.COLUMN_NAME)
    private String name;

    @Column(name = DatabaseConstants.COLUMN_URL)
    private String url;

    @Column(name = DatabaseConstants.COLUMN_SEQUENCE_NUMBER)
    private Integer sequenceNumber;

    @Column(name = DatabaseConstants.COLUMN_TYPE)
    @Enumerated(EnumType.STRING)
    private FrontEndRouteType type;

    @Column(name = DatabaseConstants.COLUMN_AUTH_REQUIREMENT)
    @Enumerated(EnumType.STRING)
    private FrontEndRouteAuthRequirement authRequirement;
}
