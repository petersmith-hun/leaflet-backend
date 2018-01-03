package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Informational VO about {@link hu.psprog.leaflet.service.impl.uploader.acceptor.UploadAcceptor} implementations.
 *
 * @author Peter Smith
 */
public class AcceptorInfoVO implements Serializable {

    private String id;
    private String rootDirectoryName;
    private List<String> childrenDirectories;
    private List<String> acceptableMimeTypes;

    public String getId() {
        return id;
    }

    public String getRootDirectoryName() {
        return rootDirectoryName;
    }

    public List<String> getChildrenDirectories() {
        return childrenDirectories;
    }

    public List<String> getAcceptableMimeTypes() {
        return acceptableMimeTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AcceptorInfoVO that = (AcceptorInfoVO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(rootDirectoryName, that.rootDirectoryName)
                .append(childrenDirectories, that.childrenDirectories)
                .append(acceptableMimeTypes, that.acceptableMimeTypes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(rootDirectoryName)
                .append(childrenDirectories)
                .append(acceptableMimeTypes)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("rootDirectoryName", rootDirectoryName)
                .append("childrenDirectories", childrenDirectories)
                .append("acceptableMimeTypes", acceptableMimeTypes)
                .toString();
    }

    public static AcceptorInfoVOBuilder getBuilder() {
        return new AcceptorInfoVOBuilder();
    }

    /**
     * Builder for AcceptorInfoVO.
     */
    public static final class AcceptorInfoVOBuilder {
        private String id;
        private String rootDirectoryName;
        private List<String> childrenDirectories;
        private List<String> acceptableMimeTypes;

        private AcceptorInfoVOBuilder() {
        }

        public AcceptorInfoVOBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public AcceptorInfoVOBuilder withRootDirectoryName(String rootDirectoryName) {
            this.rootDirectoryName = rootDirectoryName;
            return this;
        }

        public AcceptorInfoVOBuilder withChildrenDirectories(List<String> childrenDirectories) {
            this.childrenDirectories = childrenDirectories;
            return this;
        }

        public AcceptorInfoVOBuilder withAcceptableMimeTypes(List<String> acceptableMimeTypes) {
            this.acceptableMimeTypes = acceptableMimeTypes;
            return this;
        }

        public AcceptorInfoVO build() {
            AcceptorInfoVO acceptorInfoVO = new AcceptorInfoVO();
            acceptorInfoVO.id = this.id;
            acceptorInfoVO.rootDirectoryName = this.rootDirectoryName;
            acceptorInfoVO.childrenDirectories = this.childrenDirectories;
            acceptorInfoVO.acceptableMimeTypes = this.acceptableMimeTypes;
            return acceptorInfoVO;
        }
    }
}
