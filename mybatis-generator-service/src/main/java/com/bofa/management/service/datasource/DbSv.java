package com.bofa.management.service.datasource;

import com.bofa.cmpt.DataSourceManager;
import com.bofa.common.model.TableInfo;
import com.bofa.common.util.BeanMapperUtil;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.datasource.DbconfigMapper;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.dao.datasource.entity.DbconfigExample;
import com.bofa.management.exception.BusinessErrorCode;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.constant.DbType;
import com.bofa.management.service.datasource.dto.DbconfigRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Transactional
@Service
public class DbSv {

    @Autowired
    private DbconfigMapper dbconfigMapper;

    @Autowired
    private DataSourceManager dataSourceManager;

    public void testDbConnect(DbconfigRequest dto) {
        Dbconfig config = BeanMapperUtil.map(dto, Dbconfig.class);
        config.setCreateDate(new Date());
        dataSourceManager.testConnect(config);
    }

    public void delDbconfig(Dbconfig dbconfig) {
        dbconfigMapper.deleteByPrimaryKey(dbconfig.getId());
    }

    public DatasourceHolder getDatasourceHolder(Dbconfig dbconfig) {
        DatasourceHolder holder = dataSourceManager.getDatasourceHolder(dbconfig);
        // need to synchronize the tables info
        // so must be requested to get tables resultSet when invoke this method each time.
//        List<TableInfo> allTables = holder.getAllTables();
//        if (allTables == null || allTables.size() == 0) {
        List<TableInfo> allTables = dataSourceManager.getAllTables(holder.getDataSource(), dbconfig.getDbschema(), DbType.findDbType(dbconfig.getDbtype()));
//        }
        holder.setAllTables(allTables);
        return holder;
    }

    public List<Dbconfig> getAllDbConnections() {
        return this.dbconfigMapper.selectByExample(new DbconfigExample());
    }
//    public List<TableInfo> tableList(Long dbId, final String tableName) throws SQLException {
//        if (dbId == null) {
//            return null;
//        } else {
//            Dbconfig dbconfig = this.dbconfigMapper.selectByPrimaryKey(dbId);
//            if (dbconfig == null) {
//                return null;
//            } else {
//                DatasourceHolder dbh = this.dataSourceManager.getDatasourceHolder(dbconfig);
//                List<TableInfo> tables = null;
//                if (dbh.getAllTables() == null || dbh.getAllTables().size() == 0) {
//                    tables = this.sqlManager.getAllTables(dbh.getDataSource(), dbh.getDbconfig().getDbname(), DbType.findDbType(dbh.getDbconfig().getDbtype()));
//                    dbh.setAllTables(tables);
//                }
//                tables = dbh.getAllTables();
//                List<TableInfo> result = tables;
//                if (StringUtils.isNotBlank(tableName)) {
//                    result = ListUtils.select(tables, new Predicate<TableInfo>() {
//                        public boolean evaluate(TableInfo object) {
//                            return StringUtils.containsIgnoreCase(object.getTableName(), tableName);
//                        }
//                    });
//                }
//                return result;
//            }
//        }
//    }

//
//    public Dbconfig getDbConnection(Long dbId) {
//        if (dbId == null) {
//            BusinessException.throwBusinessException(BusinessErrorCode.Parameter_null, "dbId");
//        }
//        Dbconfig dbconfig = this.dbconfigMapper.selectByPrimaryKey(dbId);
//        if (dbconfig == null) {
//            BusinessException.throwBusinessException(BusinessErrorCode.Parameter_invalid, "dbId");
//        }
//        return dbconfig;
//    }

    public Dbconfig saveDbConfig(DbconfigRequest req) throws BusinessException {
        Optional.ofNullable(DbType.findDbType(req.getDbtype())).
                orElseThrow(() -> BusinessException.mapperBusinessException("暂不支持该数据库!"));
        Dbconfig dbconfig;
        if (req.getId() == null) {
            if (this.dbconfigMapper.findByDbconnectName(req.getDbconnectname()) != null) {
                BusinessException.throwBusinessException("该名称已存在!");
            }
            dbconfig = BeanMapperUtil.map(req, Dbconfig.class);
            dbconfig.setStatus("0");
            dbconfig.setCreateDate(new Date());
            this.dbconfigMapper.insert(dbconfig);
            dbconfig = this.dbconfigMapper.findByDbconnectName(req.getDbconnectname());
        } else {
            dbconfig = BeanMapperUtil.map(req, Dbconfig.class);
            this.dbconfigMapper.updateByPrimaryKey(dbconfig);
        }
        return dbconfig;
    }
}
