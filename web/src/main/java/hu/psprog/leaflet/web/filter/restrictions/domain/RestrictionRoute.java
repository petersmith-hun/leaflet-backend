package hu.psprog.leaflet.web.filter.restrictions.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.http.HttpMethod;

/**
 * Route with method wrapper for configuring security restriction validation strategies.
 *
 * @author Peter Smith
 */
public class RestrictionRoute {

    private HttpMethod method;
    private String path;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RestrictionRoute that = (RestrictionRoute) o;

        return new EqualsBuilder()
                .append(method, that.method)
                .append(path, that.path)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(method)
                .append(path)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("method", method)
                .append("path", path)
                .toString();
    }
}
