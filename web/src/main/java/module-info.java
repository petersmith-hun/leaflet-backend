open module leaflet.app.backend.web {
    requires leaflet.app.backend.persistence;
    requires leaflet.app.backend.service;
    requires leaflet.component.bridge.api;
    requires leaflet.component.bridge.implementation;
    requires leaflet.component.bridge.integration.spring;
    requires leaflet.component.rest.backend.api;
    requires leaflet.component.rest.recaptcha.api;
    requires leaflet.component.rest.recaptcha.client;
    requires leaflet.component.security.jwt;
    requires leaflet.component.tlp.appender;

    requires java.annotation;
    requires java.compiler;
    requires java.management;
    requires java.validation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;
    requires io.reactivex.rxjava2;
    requires jdk.management;
    requires metrics.annotation;
    requires metrics.core;
    requires metrics.graphite;
    requires metrics.jvm;
    requires metrics.spring;
    requires org.apache.commons.lang3;
    requires org.aspectj.weaver;
    requires slf4j.api;
    requires spring.aspects;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot.configuration.processor;
    requires spring.context;
    requires spring.core;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.web;
    requires spring.webmvc;
    requires thymeleaf.spring5;
    requires tomcat.embed.core;
}