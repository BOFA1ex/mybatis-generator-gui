package com.bofa.common.codegen.mybatis.plugin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @project mybatis-generator-gui
 * @since 2019-08-24
 */
public class LombokPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Data"));
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }
}

