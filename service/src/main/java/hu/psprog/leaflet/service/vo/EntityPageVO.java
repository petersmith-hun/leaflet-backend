package hu.psprog.leaflet.service.vo;

import java.util.List;

/**
 * @author Peter Smith
 */
public class EntityPageVO<T extends BaseVO> {

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

    public EntityPageVO() {}

    public EntityPageVO(long entityCount, int pageCount, int pageNumber, int pageSize, int entityCountOnPage,
                        List<T> entitiesOnPage, boolean first, boolean last, boolean hasNext, boolean hasPrevious) {
        this.entityCount = entityCount;
        this.pageCount = pageCount;
        this.pageNumber = pageNumber + 1;
        this.pageSize = pageSize;
        this.entityCountOnPage = entityCountOnPage;
        this.entitiesOnPage = entitiesOnPage;
        this.first = first;
        this.last = last;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

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

    public static class Builder {

        private long entityCount;
        private int pageCount;
        private int pageNumber;
        private int pageSize;
        private int entityCountOnPage;
        private List<?> entitiesOnPage;
        private boolean first;
        private boolean last;
        private boolean hasNext;
        private boolean hasPrevious;

        public Builder withEntityCount(long entityCount) {
            this.entityCount = entityCount;
            return this;
        }

        public Builder withPageCount(int pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder withPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        public Builder withPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder withEntityCountOnPage(int entityCountOnPage) {
            this.entityCountOnPage = entityCountOnPage;
            return this;
        }

        public Builder withEntitiesOnPage(List<?> entitiesOnPage) {
            this.entitiesOnPage = entitiesOnPage;
            return this;
        }

        public Builder isFirst(boolean first) {
            this.first = first;
            return this;
        }

        public Builder isLast(boolean last) {
            this.last = last;
            return this;
        }

        public Builder hasNext(boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public Builder hasPrevious(boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
            return this;
        }

        public EntityPageVO createEntityPageVO() {
            return new EntityPageVO(entityCount, pageCount, pageNumber, pageSize, entityCountOnPage, entitiesOnPage,
                    first, last, hasNext, hasPrevious);
        }
    }
}
