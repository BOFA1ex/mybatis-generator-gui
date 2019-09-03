package com.bofa.management.service.datasource.constant;

import com.sun.javafx.binding.StringFormatter;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public enum DbType {
    /**
     * Support below database
     * MYSQL
     * Oracle
     * H2
     */
    MySQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s", "//", ":", "/", "3306", null),
    Oracle("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@%s:%s:%s", "@", ":", ":", "1521", "mybatis-generator-gui/lib/ojdbc-6.0.jar"),
    H2("org.h2.Driver", "jdbc:h2://%s:%s/%s", "//", ":", "/", "9092", null);

    private final String driverClass;
    private final String connectionUrlPattern;
    private final String defaultPort;
    private final String hostDelimiter;
    private final String portDelimiter;
    private final String schemaDelimiter;
    private final String libPath;

    public String getDefaultPort() {
        return defaultPort;
    }

    DbType(String driverClass, String connectionUrlPattern, String hostDelimiter,
           String portDelimiter, String schemaDelimiter, String defaultPort, String libPath) {
        this.driverClass = driverClass;
        this.connectionUrlPattern = connectionUrlPattern;
        this.defaultPort = defaultPort;
        this.hostDelimiter = hostDelimiter;
        this.portDelimiter = portDelimiter;
        this.schemaDelimiter = schemaDelimiter;
        this.libPath = libPath;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getConnectionUrlPattern() {
        return connectionUrlPattern;
    }

    public String getHostDelimiter() {
        return hostDelimiter;
    }

    public String getPortDelimiter() {
        return portDelimiter;
    }

    public String getSchemaDelimiter() {
        return schemaDelimiter;
    }

    public String getLibPath() {
        return libPath;
    }

    public static DbType findDbType(String type) {
        DbType[] types = values();
        for (DbType one : types) {
            if (one.toString().equalsIgnoreCase(type)) {
                return one;
            }
        }
        return null;
    }

}
