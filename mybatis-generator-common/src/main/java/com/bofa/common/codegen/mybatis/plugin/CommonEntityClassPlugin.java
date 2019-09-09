package com.bofa.common.codegen.mybatis.plugin;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-02
 */
public class CommonEntityClassPlugin extends PluginAdapter {

    static final Logger logger = LoggerFactory.getLogger(CommonEntityClassPlugin.class);

    private ShellCallback shellCallback;

    public CommonEntityClassPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> javaFiles = new ArrayList<>();
        String javaFileEncoding = context.getProperty("javaFileEncoding");

        /* base entity generated */
        String entityTargetDir = context.getJavaModelGeneratorConfiguration().getTargetProject();
        String entityTargetPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        String baseClassPackage = context.getProperty("baseClassPackage");
        String rootEntityClass = context.getJavaModelGeneratorConfiguration().getProperty("rootClass");
        TopLevelClass baseEntity = new TopLevelClass(baseClassPackage + "." + rootEntityClass);
        if (stringHasValue(entityTargetPackage)) {
            baseEntity.setVisibility(JavaVisibility.PUBLIC);
            baseEntity.addJavaDocLine("/**");
            baseEntity.addJavaDocLine("/ * " + "Entity公共基类, 由MybatisGenerator自动生成, 请勿修改");
            baseEntity.addJavaDocLine(" * " + "@author");
            baseEntity.addJavaDocLine(" */");
            GeneratedJavaFile entityJavaFile = new GeneratedJavaFile(baseEntity, entityTargetDir, javaFileEncoding, javaFormatter);
            try {
                File mapperDir = shellCallback.getDirectory(entityTargetDir, entityTargetPackage);
                File mapperFile = new File(mapperDir, entityJavaFile.getFileName());
                // 文件不存在
                if (!mapperFile.exists()) {
                    javaFiles.add(entityJavaFile);
                }
            } catch (ShellException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return javaFiles;
    }


    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String rootEntityClass = introspectedTable.getContext().getJavaModelGeneratorConfiguration().getProperty("rootClass");
        String baseClassPackage = context.getProperty("baseClassPackage");
        topLevelClass.addImportedType(baseClassPackage + "." + rootEntityClass);
        return true;
    }
}
