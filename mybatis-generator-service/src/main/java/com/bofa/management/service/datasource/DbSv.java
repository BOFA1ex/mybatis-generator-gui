package com.bofa.management.service.datasource;

import com.bofa.cmpt.DataSourceManager;
import com.bofa.common.model.TableInfo;
import com.bofa.common.util.BeanMapperUtil;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.DbconfigMapper;
import com.bofa.management.dao.entity.Dbconfig;
import com.bofa.management.dao.entity.DbconfigExample;
import com.bofa.management.exception.BusinessErrorCode;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.constant.DbType;
import com.bofa.management.service.datasource.dto.DbconfigRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Service
public class DbSv {

    @Autowired
    private DbconfigMapper dbconfigMapper;

    @Autowired
    private DataSourceManager dataSourceManager;


    public List<Dbconfig> getAllDbConnections(String name) {
        DbconfigExample example = new DbconfigExample();
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andDbnameLike(name);
        }
        return this.dbconfigMapper.selectByExample(example);
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


    public Dbconfig getDbConnection(Long dbId) {
        if (dbId == null) {
            BusinessException.throwBusinessException(BusinessErrorCode.Parameter_null, "dbId");
        }
        Dbconfig dbconfig = this.dbconfigMapper.selectByPrimaryKey(dbId);
        if (dbconfig == null) {
            BusinessException.throwBusinessException(BusinessErrorCode.Parameter_invalid, "dbId");
        }
        return dbconfig;
    }

    public void save(DbconfigRequest req) {
        DbType type = DbType.findDbType(req.getDbtype());
        if (type == null) {
            BusinessException.throwBusinessException("暂不支持该数据库!");
        }

        Dbconfig dbconfig;
        if (req.getId() == null) {
            if (this.dbconfigMapper.findByDbschema(req.getDbschema()) != null) {
                BusinessException.throwBusinessException("该名称已存在!");
            }
            dbconfig = BeanMapperUtil.map(req, Dbconfig.class);
            dbconfig.setCreateDate(new Date());
            dbconfig.setDbdrive(type.getDriverClass());
            dbconfig.setStatus("0");
            this.dbconfigMapper.insert(dbconfig);
        } else {
            dbconfig = this.dbconfigMapper.selectByPrimaryKey(req.getId());
            Dbconfig nameConfig = this.dbconfigMapper.findByDbschema(req.getDbschema());
            if (nameConfig != null && !nameConfig.getId().equals(dbconfig.getId())) {
                BusinessException.throwBusinessException("该名称已存在!");
            }
            if (dbconfig == null) {
                BusinessException.throwBusinessException("数据不符合规范!");
            }
            dbconfig.setDbschema(req.getDbschema());
            dbconfig.setDbtype(req.getDbtype());
            dbconfig.setDescription(req.getDescription());
            dbconfig.setDburl(req.getDburl());
            dbconfig.setDbname(req.getDbname());
            dbconfig.setDbpassword(req.getDbpassword());
            this.dbconfigMapper.updateByPrimaryKey(dbconfig);
        }
    }
}
