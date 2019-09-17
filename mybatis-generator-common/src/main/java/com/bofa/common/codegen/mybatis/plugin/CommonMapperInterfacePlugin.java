package com.bofa.common.codegen.mybatis.plugin;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-01
 */
public class CommonMapperInterfacePlugin extends PluginAdapter {

    static final Logger logger = LoggerFactory.getLogger(CommonMapperInterfacePlugin.class);
    private static final String DEFAULT_DAO_SUPER_CLASS = ".BaseMapper";
    private static final FullyQualifiedJavaType PARAM_ANNOTATION_TYPE = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param");
    private static final FullyQualifiedJavaType LIST_TYPE = FullyQualifiedJavaType.getNewListInstance();

    private List<Method> methods = new ArrayList<>();
    private ShellCallback shellCallback = null;

    public CommonMapperInterfacePlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    /**
     * This method is called after all the setXXX methods are called, but before
     * any other method is called. This allows the plugin to determine whether
     * it can run or not. For example, if the plugin requires certain properties
     * to be set, and the properties are not set, then the plugin is invalid and
     * will not run
     *
     * @param warnings add strings to this list to specify warnings. For example, if
     *                 the plugin is invalid, you should specify why. Warnings are
     *                 reported to users after the completion of the run.
     *
     * @return true if the plugin is in a valid state. Invalid plugins will not
     * *         be called
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * This method can be used to generate additional Java files needed by your
     * implementation that might be related to a specific table. This method is
     * called once for every table in the configuration.
     *
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     *
     * @return a List of GeneratedJavaFiles - these files will be saved
     * *         with the other files from this run.
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> javaFiles = new ArrayList<>();
        String javaFileEncoding = context.getProperty("javaFileEncoding");

        /* base mapper generated */
        String daoTargetDir = context.getJavaClientGeneratorConfiguration().getTargetProject();
        String daoTargetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        String baseClassPackage = context.getProperty("baseClassPackage");
        Interface mapperInterface = new Interface(baseClassPackage + DEFAULT_DAO_SUPER_CLASS);
        if (stringHasValue(daoTargetPackage)) {
            mapperInterface.addImportedType(PARAM_ANNOTATION_TYPE);
            mapperInterface.addImportedType(LIST_TYPE);
            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * " + "Mapper公共基类，由MybatisGenerator自动生成, 请勿修改");
            mapperInterface.addJavaDocLine(" * " + "@param <T> The Model Class 这里是泛型不是Model类");
            mapperInterface.addJavaDocLine(" * " + "@param <I> The Primary Key Class 如果是多主键则是Key类");
            mapperInterface.addJavaDocLine(" * " + "@param <E> The Example Class");
            mapperInterface.addJavaDocLine(" */");
            FullyQualifiedJavaType daoBaseInterfaceJavaType = mapperInterface.getType();
            daoBaseInterfaceJavaType.addTypeArgument(new FullyQualifiedJavaType("T"));
            daoBaseInterfaceJavaType.addTypeArgument(new FullyQualifiedJavaType("I"));
            daoBaseInterfaceJavaType.addTypeArgument(new FullyQualifiedJavaType("E"));

            if (!this.methods.isEmpty()) {
                for (Method method : methods) {
                    mapperInterface.addMethod(method);
                }
            }
            GeneratedJavaFile mapperJavaFile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFileEncoding, javaFormatter);
            try {
                File mapperDir = shellCallback.getDirectory(daoTargetDir, daoTargetPackage);
                File mapperFile = new File(mapperDir, mapperJavaFile.getFileName());
                // 文件不存在
                if (!mapperFile.exists()) {
                    javaFiles.add(mapperJavaFile);
                }
            } catch (ShellException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }
        return javaFiles;
    }


    /**
     * This method is called when the entire client has been generated.
     * Implement this method to add additional methods or fields to a generated
     * client interface or implementation.
     *
     * @param interfaze         the generated interface if any, may be null
     * @param topLevelClass     the generated implementation class if any, may be null
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     *
     * @return true if the interface should be generated, false if the generated
     * *         interface should be ignored. In the case of multiple plugins, the
     * *         first plugin returning false will disable the calling of further
     * *         plugins.
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * generated " + LocalDateTime.now());
        interfaze.addJavaDocLine(" * @author ");
        interfaze.addJavaDocLine(" */");

        String baseClassPackage = context.getProperty("baseClassPackage");
        String daoSuperClass = baseClassPackage + DEFAULT_DAO_SUPER_CLASS;
        FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(daoSuperClass);
        String targetPackage = introspectedTable.getContext().getJavaModelGeneratorConfiguration().getTargetPackage();

        String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
        FullyQualifiedJavaType baseModelJavaType = new FullyQualifiedJavaType(targetPackage + "." + domainObjectName);
        daoSuperType.addTypeArgument(baseModelJavaType);
        FullyQualifiedJavaType primaryKeyTypeJavaType;
        if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
            primaryKeyTypeJavaType = new FullyQualifiedJavaType(targetPackage + "." + domainObjectName + "Key");
        } else if (introspectedTable.hasPrimaryKeyColumns()) {
            primaryKeyTypeJavaType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType();
        } else {
            primaryKeyTypeJavaType = baseModelJavaType;
        }
        daoSuperType.addTypeArgument(primaryKeyTypeJavaType);
        interfaze.addImportedType(primaryKeyTypeJavaType);
        String exampleType = introspectedTable.getExampleType();
        FullyQualifiedJavaType exampleTypeJavaType = new FullyQualifiedJavaType(exampleType);
        daoSuperType.addTypeArgument(exampleTypeJavaType);
        interfaze.addImportedType(exampleTypeJavaType);
        interfaze.addSuperInterface(daoSuperType);
        interfaze.addImportedType(baseModelJavaType);
        return true;
    }

    private void interceptExampleParam(Method method) {
        method.getParameters().clear();
        method.addParameter(new Parameter(new FullyQualifiedJavaType("E"), "example"));
        methods.add(method);
    }

    private void interceptPrimaryKeyParam(Method method) {
        method.getParameters().clear();
        method.addParameter(new Parameter(new FullyQualifiedJavaType("I"), "id"));
        methods.add(method);
    }

    private void interceptModelParam(Method method) {
        method.getParameters().clear();
        method.addParameter(new Parameter(new FullyQualifiedJavaType("T"), "record"));
        methods.add(method);
    }

    private void interceptModelAndExampleParam(Method method) {
        List<Parameter> parameters = method.getParameters();
        if (parameters.size() == 1) {
            interceptExampleParam(method);
        } else {
            method.getParameters().clear();
            Parameter parameter1 = new Parameter(new FullyQualifiedJavaType("T"), "record");
            parameter1.addAnnotation("@Param(\"record\")");
            method.addParameter(parameter1);

            Parameter parameter2 = new Parameter(new FullyQualifiedJavaType("E"), "example");
            parameter2.addAnnotation("@Param(\"example\")");
            method.addParameter(parameter2);
            methods.add(method);
        }
    }

    /**
     * This method is called when the countByExample method has been generated
     * in the client interface.
     *
     * @param method            the generated countByExample method
     * @param interfaze         the partially implemented client interface. You can add
     *                          additional imported classes to the interface if
     *                          necessary
     * @param introspectedTable The class containing information about the table as
     *                          introspected from the database
     *
     * @return true if the method should be generated, false if the generated
     * *       method should be ignored. In the case of multiple plugins, the
     * *       first plugin returning false will disable the calling of further
     * *       plugins
     */
    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptExampleParam(method);
        return false;
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptExampleParam(method);
        return false;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptPrimaryKeyParam(method);
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptPrimaryKeyParam(method);
        method.setReturnType(new FullyQualifiedJavaType("T"));
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptExampleParam(method);
        method.setReturnType(new FullyQualifiedJavaType("List<T>"));
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptExampleParam(method);
        method.setReturnType(new FullyQualifiedJavaType("List<T>"));
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptPrimaryKeyParam(method);
        method.setReturnType(new FullyQualifiedJavaType("T"));
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelAndExampleParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelAndExampleParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelAndExampleParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptModelAndExampleParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptModelAndExampleParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptModelAndExampleParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

}

