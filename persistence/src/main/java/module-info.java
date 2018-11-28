module leaflet.app.backend.persistence {
    requires java.persistence;
    requires java.validation;
    requires org.apache.commons.lang3;
    requires org.hibernate.orm.core;
    requires spring.beans;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.tx;

    exports hu.psprog.leaflet.persistence.dao;
    exports hu.psprog.leaflet.persistence.entity;
    exports hu.psprog.leaflet.persistence.repository.specification;
}