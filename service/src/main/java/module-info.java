module leaflet.app.backend.service {
    requires leaflet.app.backend.persistence;
    requires leaflet.component.mail;
    requires leaflet.component.security.jwt;

    requires java.compiler;
    requires java.persistence;
    requires java.validation;
    requires io.reactivex.rxjava2;
    requires org.apache.commons.lang3;
    requires slf4j.api;
    requires spring.aspects;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.security.core;
    requires spring.tx;

    exports hu.psprog.leaflet.service;
    exports hu.psprog.leaflet.service.common;
    exports hu.psprog.leaflet.service.converter;
    exports hu.psprog.leaflet.service.exception;
    exports hu.psprog.leaflet.service.facade;
    exports hu.psprog.leaflet.service.impl;
    exports hu.psprog.leaflet.service.vo;
}