package com.bofa.codegen.mybatis;

import com.bofa.cmpt.*;
import com.bofa.common.codegen.fmk.model.*;
import com.bofa.common.codegen.mybatis.callback.MyCallBack;
import com.bofa.common.codegen.mybatis.plugin.*;
import com.bofa.common.model.TableColumnInfo;
import com.bofa.common.util.SqlAndJavaTypesMappingUtil;
import com.bofa.codegen.mybatis.dto.MybatisGenerateCodeRequest;
import com.bofa.codegen.fmk.TableDetailInfo;
import com.bofa.management.dao.datasource.DbconfigMapper;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.exception.BusinessException;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.plugins.SerializablePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    public void generateBaseSv(MybatisGenerateCodeRequest request) {
        MyCallBack callBack = new MyCallBack(true);
        File dic = null;
        try {
            dic = callBack.getDirectory(request.getServiceBasePath(), request.getSvPackage());
        } catch (ShellException e) {
            BusinessException.throwBusinessException("生成SV层代码失败", e);
        }

        BaseSvBean baseSvBean = new BaseSvBean();
        baseSvBean.setPackageValue(request.getSvPackage());
        Set<String> imports = new HashSet<>(Arrays.asList(request.getDaoPackage() + ".BaseMapper", request.getEntityPackage() + ".MybatisEntity"));
        baseSvBean.setImports(imports);
        freemarkerCmpt.structure("BaseSv.ftl", baseSvBean, new File(dic, "BaseSv.java"));
    }
//    public void generateFmk(TableDetailInfo tableInfo, MybatisGenerateCodeRequest request) {
//        MyCallBack callback = new MyCallBack(true);
//        String tableName = SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 0);
//        String className = tableName + "Sv";
//        String filename = className + ".java";
//        Map<String, TableColumnInfo> colMap = new HashMap();
//        Iterator var11 = tableInfo.getTableColumnInfo().iterator();
//        File dic = null;
//
//        try {
//            dic = callback.getDirectory(request.getServiceBasePath(), request.getSvPackage());
//        } catch (ShellException var29) {
//            BusinessException.throwBusinessException("生成SV层代码失败", var29);
//        }
//        File targetFile = new File(dic, filename);
//        while (var11.hasNext()) {
//            TableColumnInfo colInfo = (TableColumnInfo) var11.next();
//            colMap.put(colInfo.getColumnName(), colInfo);
//        }
//        ClassBean serviceBean = new ClassBean();
//        serviceBean.setPackageValue(request.getSvPackage());
//        serviceBean.addAnnotate("@Service");
//        serviceBean.addImport("org.springframework.stereotype.Service");
//        serviceBean.addAnnotate("@Transactional");
//        serviceBean.addImport("org.springframework.transaction.annotation.Transactional");
//        serviceBean.setModifier("public");
//        serviceBean.setSimpleName(className);
//        String tablePkName = tableInfo.getPrepareGenInfo().getEntityPrimaryKey();
//        TableColumnInfo tablePkCol = colMap.get(tablePkName);
//        FullyQualifiedJavaType tablePkjavaType = new FullyQualifiedJavaType(colMap.get(tablePkName) == null ? "java.lang.Long" : ((TableColumnInfo) colMap.get(tablePkName)).getJavaType());
//        if (tablePkjavaType.isExplicitlyImported()) {
//            serviceBean.addImport(tablePkjavaType.getFullyQualifiedNameWithoutTypeParameters());
//        }
//        String mapperName = tableName + "Mapper";
//        String entityExampleName = tableName + "Example";
//        serviceBean.setSuperClass("BaseSv<" + mapperName + ", " + tableName + ", " + tablePkjavaType.getShortName() + ", " + entityExampleName + ">");
//        serviceBean.addImport("com.ai.nbs.common.spring.service.BaseSv");
//        serviceBean.addImport(request.getDaoPackage() + "." + mapperName);
//        serviceBean.addImport(request.getEntityPackage() + "." + tableName);
//        serviceBean.addImport(request.getEntityPackage() + "." + entityExampleName);
//        List<TableColumnInfo> listPageCols = new ArrayList();
//        String colName;
//        if (request.getQueryParams() != null) {
//            Iterator var19 = request.getQueryParams().iterator();
//
//            while (var19.hasNext()) {
//                colName = (String) var19.next();
//                listPageCols.add(colMap.get(colName));
//            }
//        }
//        File dtoDic = new File(dic, "dto");
//        colName = tableName + "QueryReq";
//        File targetPageRequestFile = new File(dtoDic, colName + ".java");
//        this.createPojo(listPageCols, request.getSvPackage() + ".dto", colName, targetPageRequestFile, false);
//        MethodBean pageListBean = new MethodBean();
//        pageListBean.setModifier("public");
//        pageListBean.setReturnContent("Page<" + tableName + ">");
//        serviceBean.addImport("com.ai.nbs.common.mybatis.domain.Page");
//        pageListBean.setName("list");
//        pageListBean.addParam(this.createParam("int", "pageNumber"));
//        pageListBean.addParam(this.createParam("int", "pageSize"));
//        serviceBean.addImport(request.getSvPackage() + ".dto." + colName);
//        pageListBean.addParam(this.createParam(colName, "params"));
//        StringBuilder pageListMethod = new StringBuilder();
//        pageListMethod.append("\n");
//        pageListMethod.append("\t\t" + entityExampleName + " exmp = new " + entityExampleName + "();\n");
//        if (listPageCols.size() > 0) {
//            pageListMethod.append("\t\t" + entityExampleName + ".Criteria cs = exmp.createCriteria();\n\n");
//        }
//
//        Iterator var24 = listPageCols.iterator();
//
//        while (var24.hasNext()) {
//            TableColumnInfo col = (TableColumnInfo) var24.next();
//            FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(col.getJavaType());
//            if (String.class.getName().equals(javaType.getFullyQualifiedName())) {
//                serviceBean.addImport("org.apache.commons.lang3.StringUtils");
//                pageListMethod.append("\t\tif(StringUtils.isNotBlank(params." + SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", col.getPropertyName()) + "())){\n");
//            } else {
//                pageListMethod.append("\t\t\tif(params." + SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", col.getPropertyName()) + "() != null){\n");
//            }
//
//            pageListMethod.append("\t\t\tcs." + SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("and", col.getPropertyName()) + "EqualTo(params."
//                    + SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", col.getPropertyName()) + "());\n");
//            pageListMethod.append("\t\t}\n\n");
//
//            pageListMethod.append("\t\treturn super.list(pageNumber, pageSize, exmp);\n");
//            pageListBean.setMethodContent(pageListMethod.toString());
//            serviceBean.addMethod(pageListBean);
//            List<TableColumnInfo> saveMethodCols = tableInfo.getTableColumnInfo();
//            String targetSaveRequestName = tableName + "Req";
//            File targetSaveRequestFile = new File(dtoDic, targetSaveRequestName + ".java");
//            this.createPojo(saveMethodCols, request.getSvPackage() + ".dto", targetSaveRequestName, targetSaveRequestFile, true);
//            MethodBean saveBean = new MethodBean();
//            saveBean.setModifier("public");
//            saveBean.setReturnContent("void");
//            saveBean.setName("save");
//            serviceBean.addImport(request.getSvPackage() + ".dto." + targetSaveRequestName);
//            saveBean.addParam(this.createParam(targetSaveRequestName, "request"));
//            serviceBean.addImport("com.ai.nbs.common.spring.service.exception.NbsException");
//            serviceBean.addImport("com.ai.nbs.common.java.validator.ValidatatorUtil");
//            serviceBean.addImport("com.ai.nbs.common.spring.service.exception.NbsErrorCode");
//            serviceBean.addImport("com.ai.nbs.common.util.mapper.BeanMapper");
//            StringBuilder saveMethod = new StringBuilder();
//            saveMethod.append("\n");
//            saveMethod.append("\t\tValidatatorUtil.validate(request);\n\n");
//            saveMethod.append("\t\tif(request." + SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", tablePkCol.getPropertyName()) + "() == null ){\n");
//            saveMethod.append("\t\t\t" + tableName + " bean" + " = BeanMapper.map(request, " + tableName + ".class) ;\n");
//            saveMethod.append("\t\t\tsuper.mapper.insert(bean);\n");
//            saveMethod.append("\t\t}else{\n");
//            saveMethod.append("\t\t\t" + tableName + " bean" + " = super.mapper.selectByPrimaryKey(request." + SqlAndJavaTypesMappingUtil.getOrSetHumpMethodName("get", tablePkCol.getPropertyName()) + "());\n");
//            saveMethod.append("\t\t\tif(bean == null){;\n");
//            saveMethod.append("\t\t\t\tNbsException.throwNbsException(NbsErrorCode.Parameter_invalid);;\n");
//            saveMethod.append("\t\t\t}\n");
//            saveMethod.append("\t\t\t//TODO 将需要修改的字段修改!\n");
//            saveMethod.append("\t\t\tsuper.mapper.updateByPrimaryKey(bean);\n");
//            saveMethod.append("\t\t}\n");
//            saveBean.setMethodContent(saveMethod.toString());
//            serviceBean.addMethod(saveBean);
//        }
//        this.freemarkerCmpt.structure("mybatis/Class", serviceBean, targetFile);
//    }

    private ParamBean createParam(String type, String name) {
        ParamBean param = new ParamBean();
        param.setName(name);
        param.setType(type);
        return param;
    }

    private void createPojo(List<TableColumnInfo> listPageCols, String packageName, String className, File targetFile, boolean valvalidate) {
        try {
            Files.createFile(targetFile.toPath());
        } catch (IOException var11) {
            BusinessException.throwBusinessException("生成SV层代码失败", var11);
        }

        ClassBean pojoBean = new ClassBean();
        pojoBean.setPackageValue(packageName);
        pojoBean.addAnnotate("@Data");
        pojoBean.addImport("lombok.Data");
        pojoBean.setModifier("public");
        pojoBean.setSimpleName(className);
        Iterator var7 = listPageCols.iterator();

        while (var7.hasNext()) {
            TableColumnInfo col = (TableColumnInfo) var7.next();
            FieldBean fieldBean = new FieldBean();
            fieldBean.setModifier("private");
            FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(col.getJavaType());
            if (javaType.isExplicitlyImported()) {
                pojoBean.addImport(javaType.getFullyQualifiedNameWithoutTypeParameters());
                fieldBean.setType(javaType.getShortName());
            } else {
                fieldBean.setType(javaType.getShortName());
            }

            if (valvalidate) {
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

        this.freemarkerCmpt.structure("mybatis/Class", pojoBean, targetFile);
    }

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
            gkey = new GeneratedKey(request.getEntityPrimaryKey(), this.getOracleGeneratedKey(request.getEntitySeq()), false, (String) null);
        } else if (JDBC_ENTITY_PRIMARY_KEY_RULE.equalsIgnoreCase(request.getEntityPrimaryKeyRule())) {
            gkey = new GeneratedKey(request.getEntityPrimaryKey(), "JDBC", false, null);
        }
        context.addTableConfiguration(genCmpt.newTableConfigurationBuilder(context)
                .setTableName(request.getTableName())
                .setDomainObjectName(SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 0))
                .setGeneratedKey(gkey)
                .addProperty("ignoreQualifiersAtRuntime", "true")
                .addProperty("useActualColumnNames", "true")
                .build());
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
        if ("on".equals(request.getSvGenFlag())){
            if ("on".equalsIgnoreCase(request.getSvGenFlag())) {
                this.generateBaseSv(request);
            }
//
//            if ("on".equalsIgnoreCase(request.getControllerGenFlag())) {
//                this.genMybatisController(tableInfo, request, config);
//            }
        }
    }

    private String getOracleGeneratedKey(String seqName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(seqName).append(".NEXTVAL AS ID ").append("FROM DUAL");
        return sb.toString();
    }

}
