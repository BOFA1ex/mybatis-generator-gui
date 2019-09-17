package com.bofa.cmpt;

import com.bofa.common.codegen.mybatis.plugin.JavaTypeResolverJsr310Impl;
import com.bofa.common.model.TableColumnInfo;
import com.bofa.common.model.TableInfo;
import com.bofa.common.util.SqlAndJavaTypesMappingUtil;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.constant.DbType;
import com.zaxxer.hikari.util.DriverDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Component
public class DataSourceManager {

    static final Logger logger = LoggerFactory.getLogger(DataSourceManager.class);

    public DatasourceHolder getDatasourceHolder(Dbconfig dbconfig) {
        DatasourceHolder instance = null;
        instance = this.createDatasourceHolder(dbconfig);
        logger.info("datasourceHolder -> {}", instance);
        return instance;
    }

    public List<TableColumnInfo> getTableColumnInfos(DataSource dataSource, String schema, DbType dbType, String tableName) {
        ResultSet rs = null;
        List<TableColumnInfo> columns = new ArrayList();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData dbmd = conn.getMetaData();
            if (dbType == DbType.Oracle) {
                rs = dbmd.getColumns(null, schema != null ? schema.toUpperCase() : null, tableName, null);
            } else {
                rs = dbmd.getColumns(null, null, tableName, null);
            }
            JavaTypeResolverJsr310Impl jtm = new JavaTypeResolverJsr310Impl();
            int i = 1;
            while (rs.next()) {
                TableColumnInfo column = new TableColumnInfo();
                column.setId(i++);
                column.setTableName(tableName);
                column.setColumnName(rs.getString("COLUMN_NAME"));
                column.setJdbcTypeInt(rs.getInt("DATA_TYPE"));
                column.setJdbcType(rs.getString("TYPE_NAME"));
                column.setColumnSize(rs.getInt("COLUMN_SIZE"));
                column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
                column.setNullable(rs.getString("NULLABLE"));
                column.setRemarks(rs.getString("REMARKS"));
                column.setColumnDef(rs.getString("COLUMN_DEF"));
                column.setPropertyName(SqlAndJavaTypesMappingUtil.toHumpName(column.getColumnName(), 1));
                column.setFunctionNamePostfix(SqlAndJavaTypesMappingUtil.toHumpName(column.getColumnName(), 0));
                column.setJavaType(jtm.adaptJavaType(column));
                column.setJavaTypeShotName(jtm.adaptSimpleJavaType(column));
                columns.add(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columns;
    }

    public List<TableInfo> getAllTables(DataSource dataSource, String schema, DbType dbType) {
        ResultSet rs = null;
        List<TableInfo> tableInfos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData dbmd = conn.getMetaData();
            if (dbType == DbType.Oracle) {
                rs = dbmd.getTables(null, schema != null ? schema.toUpperCase() : null, null, new String[]{"TABLE"});
            } else {
                rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});
            }
            int var15 = 1;
            while (rs.next()) {
                TableInfo info = new TableInfo();
                info.setId(var15++);
                info.setDbname(rs.getString(1));
                info.setTableName(rs.getString(3));
                info.setType(rs.getString(4));
                ResultSet keysRs = dbmd.getPrimaryKeys(null, null, info.getTableName());
                while (keysRs.next()) {
                    String primaryKeyColumnName = keysRs.getString("COLUMN_NAME");
                    if (StringUtils.isBlank(primaryKeyColumnName)) {
                        BusinessException.throwBusinessException("该表[ " + info.getTableName() + " ] 没有主键");
                    }
                    info.setPrimaryKeyColumnName(primaryKeyColumnName);
                    break;
                }
                tableInfos.add(info);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableInfos;
    }

    private DataSource getDataSource(String dbType, String url, String username, String password) {
        Properties pro = new Properties();
        /*
         * Note:
         * mysql is not supported catalog and schema
         * so need to add this property below that.
         * TODO 2019-08-27 bofa1ex http://www.mybatis.org/generator/usage/mysql.html
         */
        DbType dt = DbType.findDbType(dbType);
        if (dt == null) {
            BusinessException.throwBusinessException("dbType is not found");
        } else if (dt == DbType.MySQL) {
            pro.setProperty("nullCatalogMeansCurrent", "true");
        }
        return new DriverDataSource(url, dt.getDriverClass(), pro, username, password);
    }

    /**
     * create datasourceHolder by create datasource
     *
     * @param dbconfig
     *
     * @return
     *
     * @see #createDataSource(com.bofa.management.dao.datasource.entity.Dbconfig)
     */
    private DatasourceHolder createDatasourceHolder(Dbconfig dbconfig) {
        DatasourceHolder holder = new DatasourceHolder();
        DataSource ds = this.createDataSource(dbconfig);
        holder.setDataSource(ds);
        holder.setDbconfig(dbconfig);
        return holder;
    }

    public DataSource createDataSource(Dbconfig dbconfig) {
        DataSource dataSource = getDataSource(dbconfig.getDbtype(), dbconfig.getDburl(), dbconfig.getDbname(), dbconfig.getDbpassword());
        try (Connection conn = dataSource.getConnection()) {
            logger.info("建立连接 {}", conn);
        } catch (SQLException e) {
            BusinessException.throwBusinessException("根据" + dbconfig + "尝试连接数据库失败! \n原因 " + e.getMessage());
        }
        return dataSource;
    }

    public void testConnect(Dbconfig dbconfig) {
        DataSource dataSource = getDataSource(dbconfig.getDbtype(), dbconfig.getDburl(), dbconfig.getDbname(), dbconfig.getDbpassword());
        try (Connection conn = dataSource.getConnection()) {
            logger.info("建立连接 {}", conn);
        } catch (SQLException e) {
            BusinessException.throwBusinessException("根据" + dbconfig + "尝试连接数据库失败! \n原因 " + e.getMessage());
        }
    }

}
