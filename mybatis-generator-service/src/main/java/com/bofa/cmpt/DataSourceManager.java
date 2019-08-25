package com.bofa.cmpt;

import com.bofa.common.model.TableInfo;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.entity.Dbconfig;
import com.bofa.management.service.datasource.constant.DbType;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Component
public class DataSourceManager {

    static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    @Value("${driver.login.timeout:#{5}}")
    private Integer loginTimeOut;

    private ConcurrentHashMap<Long, DatasourceHolder> datasourceHolderMap = new ConcurrentHashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public static final QueryRunner RUNNER = new QueryRunner();

    public List<TableInfo> getAllTables(DataSource dataSource, String schema, DbType dbType) {
        ResultSet rs = null;
        List<TableInfo> tableInfos = null;
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData dbmd = conn.getMetaData();
            if (dbType == DbType.Oracle) {
                rs = dbmd.getTables(null, schema != null ? schema.toUpperCase() : null, null, new String[]{"TABLE"});
            } else {
                rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
            }
            BeanListHandler<TableInfo> handler = new BeanListHandler<>(TableInfo.class);
            tableInfos = handler.handle(rs);
            tableInfos.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableInfos;
    }

    public static void main(String[] args) {
        ResultSet rs = null;
        List<TableInfo> tableInfos = new ArrayList<>();
        DataSourceManager manager = new DataSourceManager();
        try (Connection conn = manager.getConnection("jdbc:mysql://localhost:3306/test", "root", "sunbofan123")) {
            DatabaseMetaData dbmd = conn.getMetaData();
//            if (dbType == DbType.Oracle) {
//                rs = dbmd.getTables(null, schema != null ? schema.toUpperCase() : null, null, new String[]{"TABLE"});
//            } else {
            rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
//            }
            int var15 = 1;
            while (rs.next()) {
                TableInfo info = new TableInfo();
                info.setId(var15++);
                info.setDbname(rs.getString(2));
                info.setTableName(rs.getString(3));
                info.setType(rs.getString(4));
                tableInfos.add(info);
            }
            tableInfos.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection(String url, String username, String password) throws SQLException {
//        DriverManager.setLoginTimeout(loginTimeOut);
        Properties pro = new Properties();
        pro.setProperty("user", username);
        pro.setProperty("password", password);
        // mysql is not supported catalog and schema
        // so need to add this property below that.
        pro.setProperty("nullCatalogMeansCurrent", "true");
        StringBuilder sbKey = new StringBuilder(url);
        sbKey.append("§");
        sbKey.append(pro.stringPropertyNames().stream().map((k) -> k + "=" + pro.getProperty(k)).collect(Collectors.joining(", ", "{", "}")));
        System.out.println(sbKey.toString());
        return DriverManager.getConnection(url, pro);
    }

    public DatasourceHolder getDatasourceHolder(Dbconfig dbconfig) {
        DatasourceHolder instance = null;
        try {
            readLock.lock();
            instance = this.datasourceHolderMap.get(dbconfig.getId());
            logger.info("datasourceHolder -> {}", instance);
        } finally {
            readLock.unlock();
        }
        try {
            if (instance == null) {
                instance = this.createDatasourceHolder(dbconfig);
                logger.info("datasourceHolder -> {}", instance);
                writeLock.lock();
                this.datasourceHolderMap.put(dbconfig.getId(), instance);
            }
        } finally {
            writeLock.unlock();
        }
        return instance;
    }

    /**
     * create datasourceHolder by create datasource
     *
     * @param dbconfig
     *
     * @return
     *
     * @see #createDataSource(com.bofa.management.dao.entity.Dbconfig)
     */
    private DatasourceHolder createDatasourceHolder(Dbconfig dbconfig) {
        DatasourceHolder holder = new DatasourceHolder();
        DataSource ds = this.createDataSource(dbconfig);
        holder.setDataSource(ds);
        holder.setDbconfig(dbconfig);
        return holder;
    }

    public DataSource createDataSource(Dbconfig dbconfig) {
        try (Connection conn = getConnection(dbconfig.getDburl(), dbconfig.getDbname(), dbconfig.getDbpassword())) {
        } catch (SQLException e) {
            throw new RuntimeException("根据" + dbconfig + "尝试连接数据库失败!");
        }
        return DataSourceBuilder.create().url(dbconfig.getDburl())
                .driverClassName(dbconfig.getDbdrive())
                .username(dbconfig.getDbname())
                .password(dbconfig.getDbpassword()).build();
    }

}
