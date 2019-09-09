package com.bofa.management.service.datasource;

import com.bofa.cmpt.DataSourceManager;
import com.bofa.common.model.TableColumnInfo;
import com.bofa.common.model.TableInfo;
import com.bofa.common.util.BeanMapperUtil;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.datasource.DbconfigMapper;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.dao.datasource.entity.DbconfigExample;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.constant.DbType;
import com.bofa.management.service.datasource.dto.DbconfigRequest;
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

    public List<TableColumnInfo> getTableColumnInfos(TableInfo tableInfo, DatasourceHolder holder) {
        Dbconfig dbconfig = holder.getDbconfig();
        return dataSourceManager.getTableColumnInfos(holder.getDataSource(), dbconfig.getDbschema(), DbType.findDbType(dbconfig.getDbtype()), tableInfo.getTableName());
    }

    public void testDbConnect(DbconfigRequest dto) {
        Dbconfig config = BeanMapperUtil.map(dto, Dbconfig.class);
        config.setCreateDate(new Date());
        dataSourceManager.testConnect(config);
    }

    public void delDbconfig(Dbconfig dbconfig) {
        dbconfigMapper.deleteByPrimaryKey(dbconfig.getId());
    }

    public DatasourceHolder getDatasourceHolderWithTableInfo(Dbconfig dbconfig) {
        DatasourceHolder holder = dataSourceManager.getDatasourceHolder(dbconfig);
        List<TableInfo> allTables = dataSourceManager.getAllTables(holder.getDataSource(), dbconfig.getDbschema(), DbType.findDbType(dbconfig.getDbtype()));
        holder.setAllTables(allTables);
        return holder;
    }

    public DatasourceHolder getDatasourceHolder(Dbconfig dbconfig) {
        return dataSourceManager.getDatasourceHolder(dbconfig);
    }

    public List<Dbconfig> getAllDbConnections() {
        return this.dbconfigMapper.selectByExample(new DbconfigExample());
    }

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
