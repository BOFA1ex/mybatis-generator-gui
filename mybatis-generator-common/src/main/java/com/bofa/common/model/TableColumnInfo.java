package com.bofa.common.model;

import lombok.Data;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Data
public class TableColumnInfo {
    private int id;
    private String tableName;
    private String columnName;
    private String jdbcType;
    private int jdbcTypeInt;
    private int columnSize;
    private int decimalDigits;
    private String nullable;
    private String remarks;
    private String columnDef;
    private String isAutoincrement;
    private String javaType;
    private String javaTypeShotName;
    private String propertyName;
    private String functionNamePostfix;
    private short primaryKey;
    private short keySeq;
    private String pkName;
}
