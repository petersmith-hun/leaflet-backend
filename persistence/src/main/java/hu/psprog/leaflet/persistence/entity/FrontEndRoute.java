package hu.psprog.leaflet.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * Entity class to represent a front-end route.
 *
 * @author Peter Smith
 */
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
    private FrontEndRouteType type;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public FrontEndRouteType getType() {
        return type;
    }

    public void setType(FrontEndRouteType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FrontEndRoute that = (FrontEndRoute) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(routeId, that.routeId)
                .append(sequenceNumber, that.sequenceNumber)
                .append(name, that.name)
                .append(url, that.url)
                .append(type, that.type)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(routeId)
                .append(name)
                .append(url)
                .append(sequenceNumber)
                .append(type)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("routeId", routeId)
                .append("name", name)
                .append("url", url)
                .append("sequenceNumber", sequenceNumber)
                .append("type", type)
                .append("created", getCreated())
                .append("lastModified", getLastModified())
                .append("enabled", isEnabled())
                .append("id", getId())
                .toString();
    }

    public static FrontEndRouteBuilder getBuilder() {
        return new FrontEndRouteBuilder();
    }

    /**
     * Builder for {@link FrontEndRoute} entity.
     */
    public static final class FrontEndRouteBuilder {
        private String routeId;
        private String name;
        private String url;
        private Date created;
        private Long id;
        private Integer sequenceNumber;
        private Date lastModified;
        private FrontEndRouteType type;
        private boolean enabled;

        private FrontEndRouteBuilder() {
        }

        public FrontEndRouteBuilder withRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public FrontEndRouteBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FrontEndRouteBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public FrontEndRouteBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public FrontEndRouteBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public FrontEndRouteBuilder withSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public FrontEndRouteBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public FrontEndRouteBuilder withType(FrontEndRouteType type) {
            this.type = type;
            return this;
        }

        public FrontEndRouteBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public FrontEndRoute build() {
            FrontEndRoute frontEndRoute = new FrontEndRoute();
            frontEndRoute.setRouteId(routeId);
            frontEndRoute.setName(name);
            frontEndRoute.setUrl(url);
            frontEndRoute.setCreated(created);
            frontEndRoute.setId(id);
            frontEndRoute.setSequenceNumber(sequenceNumber);
            frontEndRoute.setLastModified(lastModified);
            frontEndRoute.setType(type);
            frontEndRoute.setEnabled(enabled);
            return frontEndRoute;
        }
    }
}
