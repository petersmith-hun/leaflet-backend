package hu.psprog.leaflet.web.filter.restrictions.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Security configuration model class for client acceptor filter.
 *
 * @author Peter Smith
 */
public class ClientAcceptorConfiguration {

    private String name;
    private List<RestrictionType> restrictions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestrictionType> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(List<RestrictionType> restrictions) {
        this.restrictions = restrictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ClientAcceptorConfiguration that = (ClientAcceptorConfiguration) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(restrictions, that.restrictions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(restrictions)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("restrictions", restrictions)
                .toString();
    }
}
