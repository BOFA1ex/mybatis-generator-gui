package com.bofa.common.model;

import lombok.Data;

/**
 * @author bofa1ex
 * @since 2019-08-25
 */
@Data
public class TableInfo {
    private int id;
    private String dbname;
    private String tableName;
    private String tableEntityName;
    private String primaryKeyColumnName;
    private String remarks;
    private String type;
}
