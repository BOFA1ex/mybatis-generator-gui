package com.bofa.management.dao.datasource.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Data
public class Dbconfig {
    private Long id;
    private String dbconnectname;
    private String dbschema;
    private String dbtype;
    private String description;
    private String dburl;
    private String dbdrive;
    private String dbname;
    private String dbpassword;
    private Date createDate;
    private String status;
}
