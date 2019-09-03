package com.bofa.common.codegen.mybatis.plugin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @project mybatis-generator-gui
 * @since 2019-08-24
 */
public class MybatisMappingPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addAnnotation("@Mapper");
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType("BaseMapper");
        interfaze.addImportedType(new FullyQualifiedJavaType("com.ai.nbs.common.spring.dao.BaseMapper"));
        superInterface.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
        FullyQualifiedJavaType KeyType = null;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            KeyType = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        } else {
            List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
            IntrospectedColumn introspectedColumn;
            for(Iterator var9 = introspectedColumns.iterator(); var9.hasNext(); KeyType = introspectedColumn.getFullyQualifiedJavaType()) {
                introspectedColumn = (IntrospectedColumn)var9.next();
            }
        }
        superInterface.addTypeArgument(KeyType);
        superInterface.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getExampleType()));
        interfaze.addSuperInterface(superInterface);
        interfaze.getMethods().clear();
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
