package com.bofa.management.service.datasource.dto;

import lombok.Data;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Data
public class DbconfigRequest {
    private Long id;
    private String dbschema;
    private String dbtype;
    private String description;
    private String dburl;
    private String dbname;
    private String dbpassword;
}
