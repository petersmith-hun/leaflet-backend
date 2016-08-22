package hu.psprog.leaflet.web.config;

public class ConfigurationProperty {

    // fixed properties
    public static final String REPOSITORY_PACKAGE = "hu.psprog.leaflet.persistence.repository";
    public static final String ENTITY_PACKAGE = "hu.psprog.leaflet.persistence.entity";

    // properties from external property source
    public static final String JNDI_JDBC_SOURCE = "${jndi.jdbc.source}";
    public static final String DATASOURCE_SHOW_SQL = "${datasource.showSql}";
    public static final String DATASOURCE_GENERATE_DDL = "${datasource.generateDdl}";
    public static final String HIBERNATE_DDL_AUTO = "${hibernate.hbm2ddl.auto}";
    public static final String HIBERNATE_DIALECT = "${hibernate.dialect}";
    public static final String RUN_LEVEL = "${runLevel:MAINTENANCE}";
}
