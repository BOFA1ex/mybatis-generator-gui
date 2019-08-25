package com.bofa.management.bean;

import com.bofa.common.model.TableInfo;
import com.bofa.management.dao.entity.Dbconfig;
import lombok.Data;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Data
public class DatasourceHolder {
    private List<TableInfo> allTables = null;
    private Dbconfig dbconfig;
    private DataSource dataSource;
}
