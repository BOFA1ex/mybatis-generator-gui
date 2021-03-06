package com.bofa.codegen.mybatis;

import com.bofa.cmpt.*;
import com.bofa.codegen.fmk.PrepareGenInfo;
import com.bofa.common.codegen.fmk.model.*;
import com.bofa.common.codegen.mybatis.callback.MyCallBack;
import com.bofa.common.codegen.mybatis.plugin.*;
import com.bofa.common.model.TableColumnInfo;
import com.bofa.common.util.SqlAndJavaTypesMappingUtil;
import com.bofa.codegen.mybatis.dto.MybatisGenerateCodeRequest;
import com.bofa.codegen.fmk.TableDetailInfo;
import com.bofa.management.bean.DatasourceHolder;
import com.bofa.management.dao.datasource.DbconfigMapper;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.exception.BusinessException;
import com.bofa.management.service.datasource.constant.DbType;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.plugins.SerializablePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Service
public class MybatisGenerateCodeSv {

    private static final String ORACLE_ENTITY_PRIMARY_KEY_RULE = "seq";
    private static final String JDBC_ENTITY_PRIMARY_KEY_RULE = "gen";

    @Autowired
    private MybatisGenCmpt genCmpt;

    @Autowired
    private FreemarkerCmpt freemarkerCmpt;

    @Autowired
    private DbconfigMapper dbconfigMapper;

    @Autowired
    private DataSourceManager dataSourceManager;

    public void generateMybatis(MybatisGenerateCodeRequest request) throws Exception {
        Dbconfig dbconfig = dbconfigMapper.selectByPrimaryKey(request.getDbId());
        if (dbconfig == null) {
            BusinessException.throwBusinessException("根据dbId " + request.getDbId() + ", 找不到对应config");
        }
        Configuration configuration = new Configuration();
        Context context = genCmpt.newContextBuilder(ModelType.CONDITIONAL)
                .setId("DB2Tables")
                .setTargetTime("MyBatis3")
                .addProperty("javaFileEncoding", "UTF-8")
                .build();
        configuration.addContext(context);
        context.setJdbcConnectionConfiguration(genCmpt.newJdbcConfigBuilder()
                .setDriverClass(dbconfig.getDbdrive())
                .setConnectionURL(dbconfig.getDburl())
                .setUserId(dbconfig.getDbname())
                .setPassword(dbconfig.getDbpassword())
                .addProperty("nullCatalogMeansCurrent", "true")
                .build());
        GeneratedKey gkey = null;
        if (ORACLE_ENTITY_PRIMARY_KEY_RULE.equalsIgnoreCase(request.getEntityPrimaryKeyRule())) {
            gkey = new GeneratedKey(request.getEntityPrimaryKey(), this.getOracleGeneratedKey(request.getEntitySeq()), false, null);
        } else if (JDBC_ENTITY_PRIMARY_KEY_RULE.equalsIgnoreCase(request.getEntityPrimaryKeyRule())) {
            gkey = new GeneratedKey(request.getEntityPrimaryKey(), "JDBC", false, null);
        }
        context.addTableConfiguration(genCmpt.newTableConfigurationBuilder(context)
                .setTableName(request.getTableName())
                .setDomainObjectName(SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 0))
                .setGeneratedKey(gkey)
                .addProperty("ignoreQualifiersAtRuntime", "true")
//                .addProperty("useActualColumnNames", "true")
                .build());
        String baseClassPackage = StringUtils.isBlank(request.getBaseClassPackage()) ? request.getBusiPackage() + ".base" : request.getBaseClassPackage();
        request.setBaseClassPackage(baseClassPackage);
        context.addProperty("baseClassPackage", baseClassPackage);
        context.setJavaModelGeneratorConfiguration(genCmpt.newJavaModelConfigBuilder()
                .setTargetPackage(request.getEntityPackage())
                .setTargetProject(request.getDaoBasePath())
                .addProperty("enableSubPackages", "false")
                .addProperty("trimStrings", "false")
                .addProperty("rootClass", request.getRootEntityClass())
                .build());
        context.setSqlMapGeneratorConfiguration(genCmpt.newSqlMapGeneratorConfigurationBuilder()
                .setTargetPackage(request.getDaoPackage())
                .setTargetProject(request.getDaoBasePath())
                .addProperty("enableSubPackages", "false")
                .build());
        context.setJavaClientGeneratorConfiguration(genCmpt.newJavaClientConfigBuilder()
                .setConfigurationType("XMLMAPPER")
                .setTargetPackage(request.getDaoPackage())
                .setTargetProject(request.getDaoBasePath())
                .addProperty("useLegacyBuilder", "true")
                .build());
        context.setCommentGeneratorConfiguration(genCmpt.newCommentGeneratorConfigurationBuilder()
                .addProperty("suppressAllComments", "false")
                .addProperty("suppressDate", "false")
                .addProperty("addRemarkComments", "true")
                .addProperty("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .build());
        context.setJavaTypeResolverConfiguration(genCmpt.newJavaTypeResolverConfigurationBuilder()
                .setConfigurationType(JavaTypeResolverJsr310Impl.class.getName())
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(SerializablePlugin.class.getName())
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(LombokPlugin.class.getName())
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(CommonEntityClassPlugin.class.getName())
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(CommonMapperInterfacePlugin.class.getName())
                .build());
        genCmpt.generate(configuration, new MyCallBack(true));
        TableDetailInfo tableDetailInfo = null;
        /* mapper tableDetailInfo when svGen or controllerGen is true */
        if ("on".equalsIgnoreCase(request.getSvGenFlag()) || "on".equalsIgnoreCase(request.getControllerGenFlag())) {
            DatasourceHolder holder = dataSourceManager.getDatasourceHolder(dbconfig);
            List<TableColumnInfo> tableColumnInfos = dataSourceManager.getTableColumnInfos(holder.getDataSource(), dbconfig.getDbschema(), DbType.findDbType(dbconfig.getDbtype()), request.getTableName());
            tableDetailInfo = new TableDetailInfo();
            PrepareGenInfo prepareGenInfo = new PrepareGenInfo();
            prepareGenInfo.setEntityPrimaryKey(request.getEntityPrimaryKey());
            prepareGenInfo.setEntitySeq(request.getEntitySeq());
            tableDetailInfo.setPrepareGenInfo(prepareGenInfo);
            tableDetailInfo.setTableColumnInfo(tableColumnInfos);
            if ("on".equalsIgnoreCase(request.getSvGenFlag())) {
                this.generateBaseSv(request);
                this.generateSvJava(tableDetailInfo, request);
            }
            if ("on".equalsIgnoreCase(request.getControllerGenFlag())) {
                this.generateBaseDtoJsonBean(request);
                this.generateMybatisController(tableDetailInfo, request);
            }
        }
    }


    public void generateBaseSv(MybatisGenerateCodeRequest request) {
        MyCallBack callBack = new MyCallBack(true);
        File dic = null;
        try {
            dic = callBack.getDirectory(request.getServiceBasePath(), request.getBaseClassPackage());
        } catch (ShellException e) {
            BusinessException.throwBusinessException("生成SV层代码失败", e);
        }
        BaseSvBean baseSvBean = new BaseSvBean();
        baseSvBean.setPackageValue(request.getBaseClassPackage());
        Set<String> imports = new HashSet<>(Arrays.asList(FullyQualifiedJavaType.getNewListInstance().getFullyQualifiedName(),
                "org.slf4j.Logger", "org.slf4j.LoggerFactory", "org.springframework.beans.factory.annotation.Autowired"));
        baseSvBean.setImports(imports);
        File baseSvFile = new File(dic, "BaseSv.java");
        if (!baseSvFile.exists()) {
            freemarkerCmpt.structure("BaseSv.ftl", baseSvBean, baseSvFile);
        }
    }

    public void generateSvJava(TableDetailInfo tableInfo, MybatisGenerateCodeRequest request) {
        MyCallBack callback = new MyCallBack(true);
        String tableName = SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 0);
        String className = tableName + "Sv";
        String filename = className + ".java";
        Map<String, TableColumnInfo> colMap = new HashMap(16);
        Iterator var11 = tableInfo.getTableColumnInfo().iterator();
        File dic = null;
        try {
            dic = callback.getDirectory(request.getServiceBasePath(), request.getSvPackage());
        } catch (ShellException var29) {
            BusinessException.throwBusinessException("生成SV层代码失败", var29);
        }
        File targetFile = new File(dic, filename);
        while (var11.hasNext()) {
            TableColumnInfo colInfo = (TableColumnInfo) var11.next();
            colMap.put(colInfo.getColumnName(), colInfo);
        }
        ClassBean serviceBean = new ClassBean();
        serviceBean.setPackageValue(request.getSvPackage());
        serviceBean.addAnnotate("@Service");
        serviceBean.addImport("org.springframework.stereotype.Service");
        serviceBean.addAnnotate("@Transactional");
        serviceBean.addImport("org.springframework.transaction.annotation.Transactional");
        serviceBean.setModifier("public");
        serviceBean.setSimpleName(className);
        String tablePkName = tableInfo.getPrepareGenInfo().getEntityPrimaryKey();
        FullyQualifiedJavaType tablePkjavaType = new FullyQualifiedJavaType(colMap.get(tablePkName) == null ? "java.lang.Long" : colMap.get(tablePkName).getJavaType());
        if (tablePkjavaType.isExplicitlyImported()) {
            serviceBean.addImport(tablePkjavaType.getFullyQualifiedNameWithoutTypeParameters());
        }
        String mapperName = tableName + "Mapper";
        String entityExampleName = tableName + "Example";
        serviceBean.setSuperClass("BaseSv<" + mapperName + ", " + tableName + ", " + tablePkjavaType.getShortName() + ", " + entityExampleName + ">");
        serviceBean.addImport(request.getBaseClassPackage() + ".BaseSv");
        serviceBean.addImport(request.getDaoPackage() + "." + mapperName);
        serviceBean.addImport(request.getEntityPackage() + "." + tableName);
        serviceBean.addImport(request.getEntityPackage() + "." + entityExampleName);
        List<TableColumnInfo> listPageCols = new ArrayList();
        String colName;
        if (request.getQueryParams() != null) {
            for (String col : request.getQueryParams()) {
                listPageCols.add(colMap.get(col));
            }
        }
        /* create pojo */
        File dto = new File(dic, "dto");
        colName = tableName + "QueryReq";
        File targetRequestFile = new File(dto, colName + ".java");
        this.createPojo(listPageCols, request.getSvPackage() + ".dto", colName, targetRequestFile, request.isValidate());
        MethodBean listBean = new MethodBean();
        listBean.setModifier("public");
        listBean.setReturnContent("List<" + tableName + ">");
        serviceBean.addImport(FullyQualifiedJavaType.getNewListInstance().getFullyQualifiedName());
        listBean.setName("list");
        serviceBean.addImport(request.getSvPackage() + ".dto." + colName);
        listBean.addParam(this.createParam(colName, "params"));
        StringBuilder listMethod = new StringBuilder();
        listMethod.append("\n");
        listMethod.append("\t\t").
                append(entityExampleName).
                append(" exmp = new ").
                append(entityExampleName).
                append("();\n");
        if (listPageCols.size() > 0) {
            listMethod.append("\t\t").
                    append(entityExampleName).
                    append(".Criteria cs = exmp.createCriteria();\n\n");
        }

        for (TableColumnInfo col : listPageCols) {
            FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(col.getJavaType());
            if (String.class.getName().equals(javaType.getFullyQualifiedName())) {
                serviceBean.addImport("org.apache.commons.lang3.StringUtils");
                listMethod.append("\t\tif(StringUtils.isNotBlank(params.").append(SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", col.getPropertyName())).append("())){\n");
            } else {
                listMethod.append("\t\tif(params.").append(SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", col.getPropertyName())).append("() != null){\n");
            }
            listMethod.append("\t\t\tcs.").append(SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("and", col.getPropertyName())).append("EqualTo(params.").append(SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", col.getPropertyName())).append("());\n");
            listMethod.append("\t\t}\n");
        }

        listMethod.append("\t\treturn super.list(exmp);");
        listBean.setMethodContent(listMethod.toString());
        serviceBean.addMethod(listBean);

        this.freemarkerCmpt.structure("Class", serviceBean, targetFile);
    }

    private ParamBean createParam(String type, String name) {
        ParamBean param = new ParamBean();
        param.setName(name);
        param.setType(type);
        return param;
    }

    private void createPojo(List<TableColumnInfo> listPageCols, String packageName, String className, File targetFile, boolean validate) {
        try {
            Path dtoDic = targetFile.getParentFile().toPath();
            if (!Files.exists(dtoDic)) {
                Files.createDirectory(dtoDic);
            }
            Files.createFile(targetFile.toPath());
        } catch (IOException var11) {
            if (var11 instanceof FileAlreadyExistsException) {
                //ignore
            } else {
                BusinessException.throwBusinessException("生成SV层代码失败", var11);
            }
        }

        ClassBean pojoBean = new ClassBean();
        pojoBean.setPackageValue(packageName);
        pojoBean.addAnnotate("@Data");
        pojoBean.addImport("lombok.Data");
        pojoBean.setModifier("public");
        pojoBean.setSimpleName(className);

        for (TableColumnInfo col : listPageCols) {
            FieldBean fieldBean = new FieldBean();
            fieldBean.setModifier("private");
            FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(col.getJavaType());
            if (javaType.isExplicitlyImported()) {
                pojoBean.addImport(javaType.getFullyQualifiedNameWithoutTypeParameters());
                fieldBean.setType(javaType.getShortName());
            } else {
                fieldBean.setType(javaType.getShortName());
            }

            if (validate) {
                if (String.class.getName().equals(javaType.getFullyQualifiedName())) {
                    if ("0".equals(col.getNullable())) {
                        pojoBean.addImport("javax.validation.constraints.NotNull");
                        fieldBean.addAnnotate("@NotNull(message = \"" + col.getPropertyName() + "不能为空\")");
                    }

                    pojoBean.addImport("org.hibernate.validator.constraints.Length");
                    fieldBean.addAnnotate("@Length(max=" + col.getColumnSize() + ", message = \"" + col.getPropertyName() + "不能超过" + col.getColumnSize() + "个字节\")");
                } else if ("0".equals(col.getNullable())) {
                    pojoBean.addImport("javax.validation.constraints.NotNull");
                    fieldBean.addAnnotate("@NotNull(message = \"" + col.getPropertyName() + "不能为空\")");
                }
            }

            fieldBean.setName(col.getPropertyName());
            pojoBean.addField(fieldBean);
        }

        this.freemarkerCmpt.structure("Class", pojoBean, targetFile);
    }

    private void generateBaseDtoJsonBean(MybatisGenerateCodeRequest request) {
        MyCallBack callBack = new MyCallBack(true);
        File dic = null;
        try {
            dic = callBack.getDirectory(request.getControllerBasePath(), request.getBaseClassPackage());
        } catch (ShellException e) {
            BusinessException.throwBusinessException("生成Controller层代码失败", e);
        }
        BaseFmkBean jsonBean = new BaseFmkBean();
        jsonBean.setPackageValue(request.getBaseClassPackage());
        jsonBean.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        File jsonBeanFile = new File(dic, "JsonBean.java");
        if (!jsonBeanFile.exists()) {
            freemarkerCmpt.structure("JsonBean.ftl", jsonBean, jsonBeanFile);
        }
    }

    private void generateMybatisController(TableDetailInfo tableDetailInfo, MybatisGenerateCodeRequest request) {
        MyCallBack callback = new MyCallBack(true);
        String tableName = SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 0);
        String className = tableName + "Controller";
        String filename = className + ".java";
        Map<String, TableColumnInfo> colMap = new HashMap();
        Iterator var11 = tableDetailInfo.getTableColumnInfo().iterator();
        File dic = null;
        try {
            dic = callback.getDirectory(request.getControllerBasePath(), request.getControllerPackage());
        } catch (ShellException var31) {
            BusinessException.throwBusinessException("生成Controller层代码失败", var31);
        }
        File targetFile = new File(dic, filename);
        while (var11.hasNext()) {
            TableColumnInfo colInfo = (TableColumnInfo) var11.next();
            colMap.put(colInfo.getColumnName(), colInfo);
        }

        String tablePkName = tableDetailInfo.getPrepareGenInfo().getEntityPrimaryKey();
        FullyQualifiedJavaType tablePkjavaType = new FullyQualifiedJavaType(colMap.get(tablePkName) == null ? "java.lang.Long" : ((TableColumnInfo) colMap.get(tablePkName)).getJavaType());
        ClassBean controllerBean = new ClassBean();
        controllerBean.setPackageValue(request.getControllerPackage());
        controllerBean.addAnnotate("@Controller");
        controllerBean.addImport("org.springframework.stereotype.Controller");
        controllerBean.setModifier("public");
        controllerBean.setSimpleName(className);
        String svHandlerName = SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 1) + "Sv";
        FieldBean fieldBean = new FieldBean();
        fieldBean.addAnnotate("@Autowired");
        controllerBean.addImport("org.springframework.beans.factory.annotation.Autowired");
        controllerBean.addImport(request.getSvPackage() + "." + tableName + "Sv");
        fieldBean.setModifier("private");
        fieldBean.setType(tableName + "Sv");
        fieldBean.setName(svHandlerName);
        controllerBean.addField(fieldBean);
        String listUrl = request.getBaseUrlPath() + "/list";
        String listRequestName = tableName + "QueryReq";
        MethodBean listMethodBean = new MethodBean();
        controllerBean.addMethod(listMethodBean);
        controllerBean.addImport("org.springframework.web.bind.annotation.RequestMapping");
        controllerBean.addImport("org.springframework.web.bind.annotation.RequestMethod");
        listMethodBean.addAnnotate("@RequestMapping(value = \"" + listUrl + "\", method=RequestMethod.GET)");
        listMethodBean.setModifier("public");
        controllerBean.addImport("org.springframework.web.bind.annotation.ResponseBody");
        controllerBean.addImport(request.getBaseClassPackage() + ".JsonBean");
        listMethodBean.setReturnContent("@ResponseBody JsonBean");
        listMethodBean.setName("list");
        controllerBean.addImport("org.springframework.web.bind.annotation.RequestParam");
        controllerBean.addImport(request.getSvPackage() + ".dto." + listRequestName);
        listMethodBean.addParam(this.createParam(listRequestName, "params"));
        final String listMethodContent = "JsonBean jsonReturn = new JsonBean();\n" +
                "\t\tjsonReturn.setData(" +
                svHandlerName +
                ".list(params));\n" +
                "\t\treturn jsonReturn;";
        listMethodBean.setMethodContent(listMethodContent);
        if (tablePkjavaType.isExplicitlyImported()) {
            controllerBean.addImport(tablePkjavaType.getFullyQualifiedNameWithoutTypeParameters());
        }

        String getUrl = request.getBaseUrlPath() + "/get";
        MethodBean getMethodBean = new MethodBean();
        controllerBean.addMethod(getMethodBean);
        getMethodBean.addAnnotate("@RequestMapping(value = \"" + getUrl + "\", method=RequestMethod.GET)");
        getMethodBean.setModifier("public");
        getMethodBean.setReturnContent("@ResponseBody JsonBean");
        getMethodBean.setName("get");
        getMethodBean.addParam(this.createParam(tablePkjavaType.getShortName(), "id"));
        final String getMethodContent = "JsonBean jsonReturn = new JsonBean();\n" +
                "\t\tjsonReturn.setData(" + svHandlerName + ".get(id));\n" +
                "\t\treturn jsonReturn;";
        getMethodBean.setMethodContent(getMethodContent);

        String delUrl = request.getBaseUrlPath() + "/del";
        MethodBean delMethodBean = new MethodBean();
        controllerBean.addMethod(delMethodBean);
        delMethodBean.addAnnotate("@RequestMapping(value = \"" + delUrl + "\", method=RequestMethod.POST)");
        delMethodBean.setModifier("public");
        delMethodBean.setReturnContent("@ResponseBody JsonBean");
        delMethodBean.setName("del");
        delMethodBean.addParam(this.createParam(tablePkjavaType.getShortName(), "id"));
        final String delMethodContent = "" + svHandlerName + ".del(id);\n" +
                "\t\treturn JsonBean.success;";
        delMethodBean.setMethodContent(delMethodContent);
        this.freemarkerCmpt.structure("Class", controllerBean, targetFile);
    }

    private String getOracleGeneratedKey(String seqName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(seqName).append(".NEXTVAL AS ID ").append("FROM DUAL");
        return sb.toString();
    }

}
