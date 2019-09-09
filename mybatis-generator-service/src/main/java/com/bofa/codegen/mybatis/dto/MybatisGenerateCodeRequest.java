package com.bofa.codegen.mybatis.dto;

import lombok.Data;

import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Data
public class MybatisGenerateCodeRequest {
    private Long dbId;
    private String tableName;
    private String rootEntityClass = "MybatisEntity";
    private String entityPrimaryKey;
    private String entityPrimaryKeyRule;
    private String entitySeq;
    private String projectBasePath;
    private String busiPackage;
    private String busiName;
    private String daoBasePath;
    private String entityPackage;
    private String daoPackage;
    private String BaseClassPackage;
    private String serviceBasePath;
    private String svGenFlag;
    private String svPackage;
    private List<String> queryParams;
    private String controllerBasePath;
    private String controllerGenFlag;
    private String controllerPackage;
    private String baseUrlPath;
    private boolean validate = true;
}
