package hu.psprog.leaflet.service.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * Pagination information wrapper.
 *
 * @author Peter Smith
 */
public class EntityPageVO<T extends BaseVO> implements Serializable {

    private long entityCount;
    private int pageCount;
    private int pageNumber;
    private int pageSize;
    private int entityCountOnPage;
    private List<T> entitiesOnPage;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    public long getEntityCount() {
        return entityCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getEntityCountOnPage() {
        return entityCountOnPage;
    }

    public List<T> getEntitiesOnPage() {
        return entitiesOnPage;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isLast() {
        return last;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EntityPageVO)) return false;

        EntityPageVO<?> that = (EntityPageVO<?>) o;

        return new EqualsBuilder()
                .append(entityCount, that.entityCount)
                .append(pageCount, that.pageCount)
                .append(pageNumber, that.pageNumber)
                .append(pageSize, that.pageSize)
                .append(entityCountOnPage, that.entityCountOnPage)
                .append(first, that.first)
                .append(last, that.last)
                .append(hasNext, that.hasNext)
                .append(hasPrevious, that.hasPrevious)
                .append(entitiesOnPage, that.entitiesOnPage)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(entityCount)
                .append(pageCount)
                .append(pageNumber)
                .append(pageSize)
                .append(entityCountOnPage)
                .append(entitiesOnPage)
                .append(first)
                .append(last)
                .append(hasNext)
                .append(hasPrevious)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entityCount", entityCount)
                .append("pageCount", pageCount)
                .append("pageNumber", pageNumber)
                .append("pageSize", pageSize)
                .append("entityCountOnPage", entityCountOnPage)
                .append("first", first)
                .append("last", last)
                .append("hasNext", hasNext)
                .append("hasPrevious", hasPrevious)
                .toString();
    }

    public static EntityPageVOBuilder getBuilder() {
        return new EntityPageVOBuilder();
    }

    /**
     * Builder for {@link EntityPageVO}.
     */
    public static final class EntityPageVOBuilder {
        private long entityCount;
        private int pageCount;
        private int pageNumber;
        private int pageSize;
        private int entityCountOnPage;
        private List entitiesOnPage;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;

        private EntityPageVOBuilder() {
        }

        public EntityPageVOBuilder withEntityCount(long entityCount) {
            this.entityCount = entityCount;
            return this;
        }

        public EntityPageVOBuilder withPageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public EntityPageVOBuilder withPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public EntityPageVOBuilder withPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public EntityPageVOBuilder withEntityCountOnPage(int entityCountOnPage) {
            this.entityCountOnPage = entityCountOnPage;
            return this;
        }

        public EntityPageVOBuilder withEntitiesOnPage(List<? extends BaseVO> entitiesOnPage) {
            this.entitiesOnPage = entitiesOnPage;
            return this;
        }

        public EntityPageVOBuilder withFirst(boolean first) {
            this.first = first;
            return this;
        }

        public EntityPageVOBuilder withLast(boolean last) {
            this.last = last;
            return this;
        }

        public EntityPageVOBuilder withHasNext(boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public EntityPageVOBuilder withHasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
            return this;
        }

        public <S extends BaseVO> EntityPageVO<S> build(Class<S> targetClass) {
            EntityPageVO<S> entityPageVO = new EntityPageVO<>();
            entityPageVO.entityCount = this.entityCount;
            entityPageVO.pageSize = this.pageSize;
            entityPageVO.last = this.last;
            entityPageVO.pageNumber = this.pageNumber;
            entityPageVO.entitiesOnPage = this.entitiesOnPage;
            entityPageVO.hasPrevious = this.hasPrevious;
            entityPageVO.pageCount = this.pageCount;
            entityPageVO.entityCountOnPage = this.entityCountOnPage;
            entityPageVO.hasNext = this.hasNext;
            entityPageVO.first = this.first;
            return entityPageVO;
        }
    }
}
