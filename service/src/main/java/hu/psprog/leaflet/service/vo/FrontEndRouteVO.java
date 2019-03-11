package hu.psprog.leaflet.service.vo;

import hu.psprog.leaflet.persistence.entity.FrontEndRoute;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteAuthRequirement;
import hu.psprog.leaflet.persistence.entity.FrontEndRouteType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Value object for {@link FrontEndRoute} entity.
 *
 * @author Peter Smith
 */
public class FrontEndRouteVO extends SelfStatusAwareIdentifiableVO<Long, FrontEndRoute> {

    private String routeId;
    private String name;
    private String url;
    private Integer sequenceNumber;
    private FrontEndRouteType type;
    private FrontEndRouteAuthRequirement authRequirement;

    public String getRouteId() {
        return routeId;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public FrontEndRouteType getType() {
        return type;
    }

    public FrontEndRouteAuthRequirement getAuthRequirement() {
        return authRequirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        FrontEndRouteVO that = (FrontEndRouteVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(routeId, that.routeId)
                .append(name, that.name)
                .append(url, that.url)
                .append(sequenceNumber, that.sequenceNumber)
                .append(type, that.type)
                .append(authRequirement, that.authRequirement)
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
                .append(authRequirement)
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
                .append("created", created)
                .append("lastModified", lastModified)
                .append("enabled", enabled)
                .append("id", id)
                .append("authRequirement", authRequirement)
                .toString();
    }

    public static FrontEndRouteVOBuilder getBuilder() {
        return new FrontEndRouteVOBuilder();
    }

    /**
     * Builder for {@link FrontEndRouteVO}.
     */
    public static final class FrontEndRouteVOBuilder {
        private String routeId;
        private Long id;
        private Date created;
        private Date lastModified;
        private boolean enabled;
        private String name;
        private String url;
        private Integer sequenceNumber;
        private FrontEndRouteType type;
        private FrontEndRouteAuthRequirement authRequirement;

        private FrontEndRouteVOBuilder() {
        }

        public FrontEndRouteVOBuilder withRouteId(String routeId) {
            this.routeId = routeId;
            return this;
        }

        public FrontEndRouteVOBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public FrontEndRouteVOBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public FrontEndRouteVOBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public FrontEndRouteVOBuilder withSequenceNumber(Integer sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public FrontEndRouteVOBuilder withType(FrontEndRouteType type) {
            this.type = type;
            return this;
        }

        public FrontEndRouteVOBuilder withCreated(Date created) {
            this.created = created;
            return this;
        }

        public FrontEndRouteVOBuilder withLastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public FrontEndRouteVOBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public FrontEndRouteVOBuilder withAuthRequirement(FrontEndRouteAuthRequirement authRequirement) {
            this.authRequirement = authRequirement;
            return this;
        }

        public FrontEndRouteVO build() {
            FrontEndRouteVO frontEndRouteVO = new FrontEndRouteVO();
            frontEndRouteVO.routeId = this.routeId;
            frontEndRouteVO.sequenceNumber = this.sequenceNumber;
            frontEndRouteVO.lastModified = this.lastModified;
            frontEndRouteVO.enabled = this.enabled;
            frontEndRouteVO.id = this.id;
            frontEndRouteVO.url = this.url;
            frontEndRouteVO.name = this.name;
            frontEndRouteVO.type = this.type;
            frontEndRouteVO.created = this.created;
            frontEndRouteVO.authRequirement = this.authRequirement;
            return frontEndRouteVO;
        }
    }
}
