package com.bofa.codegen.mybatis;

import com.bofa.common.codegen.mybatis.callback.MyCallBack;
import com.bofa.common.codegen.mybatis.plugin.LombokPlugin;
import com.bofa.common.codegen.mybatis.plugin.MybatisMappingPlugin;
import com.bofa.common.util.SqlAndJavaTypesMappingUtil;
import com.bofa.cmpt.FreemarkerCmpt;
import com.bofa.cmpt.MybatisGenCmpt;
import com.bofa.codegen.mybatis.dto.MybatisGenerateCodeRequest;
import org.mybatis.generator.config.*;
import org.mybatis.generator.plugins.SerializablePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Service
public class MybatisGenerateCodeSv {

    @Autowired
    private MybatisGenCmpt genCmpt;

    @Autowired
    private FreemarkerCmpt freemarkerCmpt;

    public void generateMybatis(MybatisGenerateCodeRequest request) {
        Configuration configuration = new Configuration();
        Context context = genCmpt.newContextBuilder(ModelType.CONDITIONAL)
                .setId("DB2Tables")
                .setTargetTime("MyBatis3")
                .addProperty("javaFileEncoding", "UTF-8")
                .build();
        configuration.addContext(context);
        context.setJdbcConnectionConfiguration(genCmpt.newJdbcConfigBuilder()
                .setDriverClass("com.mysql.cj.jdbc.Driver")
                .setConnectionURL("jdbc:mysql://localhost:3306/test")
                .setUserId("root")
                .setPassword("sunbofan123")
                .addProperty("nullCatalogMeansCurrent", "true")
                .build());
        context.addTableConfiguration(genCmpt.newTableConfigurationBuilder(context)
                .setTableName("test_gen")
//                .setTableName(request.getTableName())
//                .setDomainObjectName(SqlAndJavaTypesMappingUtil.toHumpName(request.getTableName(), 0))
                .setDomainObjectName(SqlAndJavaTypesMappingUtil.toHumpName("test_gen", 0))
                .setGeneratedKey(new GeneratedKey("USER_ID", "JDBC", false, null))
                .addProperty("ignoreQualifiersAtRuntime", "true")
                .addProperty("useActualColumnNames", "true")
                .build());
        context.setJavaModelGeneratorConfiguration(genCmpt.newJavaModelConfigBuilder()
                .setTargetPackage("com.bofa.entity")
                .setTargetProject("/Users/Bofa/mybatis-generator-gui/mybatis-generator-service/src/main/java")
                .addProperty("enableSubPackages", "false")
                .addProperty("trimStrings", "false")
                .addProperty("rootClass", "com.bofa.common.gen.base.MybatisEntity")
                .build());
        context.setSqlMapGeneratorConfiguration(genCmpt.newSqlMapGeneratorConfigurationBuilder()
                .setTargetPackage("com.bofa.mapper.impl")
                .setTargetProject("/Users/Bofa/mybatis-generator-gui/mybatis-generator-service/src/main/java")
                .addProperty("enableSubPackages", "false")
                .build());
        context.setJavaClientGeneratorConfiguration(genCmpt.newJavaClientConfigBuilder()
                .setConfigurationType("XMLMAPPER")
                .setTargetPackage("com.bofa.mapper")
                .setTargetProject("/Users/Bofa/mybatis-generator-gui/mybatis-generator-service/src/main/java")
                .addProperty("useLegacyBuilder", "true")
                .build());
        context.setCommentGeneratorConfiguration(genCmpt.newCommentGeneratorConfigurationBuilder()
                .addProperty("suppressAllComments", "false")
                .addProperty("suppressDate", "false")
                .addProperty("addRemarkComments", "true")
                .addProperty("dateFormat", "yyyy-MM-dd HH:mm:ss")
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(SerializablePlugin.class.getName())
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(LombokPlugin.class.getName())
                .build());
        context.addPluginConfiguration(genCmpt.newPluginConfigurationBuilder()
                .setConfigurationType(MybatisMappingPlugin.class.getName())
                .build());
        genCmpt.generate(configuration, new MyCallBack(true));
    }

}
