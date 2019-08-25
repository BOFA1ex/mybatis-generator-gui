package com.bofa.management.service.datasource.constant;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public enum DbType {
    /**
     * MYSQL
     * Oracle
     * H2
     */
    MySQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://%s:%s/%s?useUnicode=true&useSSL=false&characterEncoding=%s"),
    Oracle("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@%s:%s:%s"),
    H2("org.h2.Driver", "jdbc:h2:%s%s%s");

    private final String driverClass;
    private final String connectionUrlPattern;

    DbType(String driverClass, String connectionUrlPattern) {
        this.driverClass = driverClass;
        this.connectionUrlPattern = connectionUrlPattern;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getConnectionUrlPattern() {
        return connectionUrlPattern;
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
