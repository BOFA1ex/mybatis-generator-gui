package com.bofa.codegen.mybatis;

import com.bofa.cmpt.*;
import com.bofa.common.codegen.mybatis.callback.MyCallBack;
import com.bofa.common.codegen.mybatis.plugin.*;
import com.bofa.common.util.SqlAndJavaTypesMappingUtil;
import com.bofa.codegen.mybatis.dto.MybatisGenerateCodeRequest;
import com.bofa.management.dao.datasource.DbconfigMapper;
import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.exception.BusinessException;
import org.mybatis.generator.config.*;
import org.mybatis.generator.plugins.SerializablePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

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
    }

    private String getOracleGeneratedKey(String seqName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(seqName).append(".NEXTVAL AS ID ").append("FROM DUAL");
        return sb.toString();
    }

}
