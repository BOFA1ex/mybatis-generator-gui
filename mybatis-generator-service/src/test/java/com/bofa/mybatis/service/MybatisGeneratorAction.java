package com.bofa.mybatis.service;

import com.bofa.common.codegen.mybatis.callback.MyCallBack;
import com.bofa.common.codegen.mybatis.plugin.LombokPlugin;
import com.bofa.common.codegen.mybatis.plugin.MybatisMappingPlugin;
import com.bofa.common.util.SqlAndJavaTypesMappingUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.NullProgressCallback;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;

import static freemarker.template.Configuration.VERSION_2_3_28;

/**
 * @author bofa1ex
 * @version 1.0
 * @project mybatis-generator-gui
 * @since 2019-08-24
 */
public class MybatisGeneratorAction {
    /**
     * more xml configuration references vide infra from official website
     * http://www.mybatis.org/generator/configreference/xmlconfig.html
     * javaModel, sqlMapMod
     *
     * @throws InvalidConfigurationException
     * @throws InterruptedException
     * @throws SQLException
     * @throws IOException
     */
    @Test
    public void generateTest() throws InvalidConfigurationException, InterruptedException, SQLException, IOException {
        Configuration config = new Configuration();
        Context context = new Context(ModelType.CONDITIONAL);
        config.addContext(context);
        /* context base-properties */
        context.setId("DB2Tables");
        context.setTargetRuntime("MyBatis3");
        context.addProperty("javaFileEncoding", "UTF-8");
        /* jdbc-connection-config */
        JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
        jdbcConfig.setDriverClass("com.mysql.cj.jdbc.Driver");
        jdbcConfig.setConnectionURL("jdbc:mysql://localhost:3306/test");
        jdbcConfig.setUserId("root");
        jdbcConfig.setPassword("sunbofan123");
        jdbcConfig.addProperty("nullCatalogMeansCurrent", "true");
        /*
         * MySql does not properly support SQL catalogs and schema. If you run the create schema command in MySql,
         * it actually creates a database - and the JDBC driver reports it back as a catalog. But MySql syntax does not support the standard catalog..table SQL syntax.
         * For this reason, it is best to not specify either catalog or schema in generator configurations. Just specify table names and specify the database in the JDBC URL.
         * If you are using version 8.x of Connector/J you may notice that the generator attempts to generate code for tables in the MySql information schemas (sys, information_schema, performance_schema, etc.)
         * This is probably not what you want! To disable this behavior, add the property "nullCatalogMeansCurrent=true" to your JDBC URL.
         */
        context.setJdbcConnectionConfiguration(jdbcConfig);
        // jdbc-table-config
        TableConfiguration tableConfig = new TableConfiguration(context);
        tableConfig.setTableName("test_gen");
        /*
            TODO 2019-08-24 00:22 问题解决, 不再需要指定catalog和schema, mysql-connector-java v8以上的版本会有这个问题, 看上面的官方说明
            tableConfig.setCatalog("chat");
            when using version 8.x of Connector/J, the generator attempts to generate code for tables in the MySql information schemas (sys, information_schema, performance_schema, etc.)
            add the property "nullCatalogMeansCurrent=true" to your JDBC URL to resolve this problem.
            sample:
            org.mybatis.generator.internal.db.DatabaseIntrospector - Found column "status", data type 4, in table "chat..user"
            org.mybatis.generator.internal.db.DatabaseIntrospector - Found column "Host", data type 1, in table "mysql..user"
            TODO 2019-08-24 19:03 这里如果不指定catalog，会与其他catalog的表重名, 导致javaModel生成后的实体类与预计不符
         */
        tableConfig.setDomainObjectName(SqlAndJavaTypesMappingUtil.toHumpName("test_gen", 0));
        /* 默认为false，如果设置为true，在生成的SQL中，table名字不会加上catalog或schema */
        tableConfig.addProperty("ignoreQualifiersAtRuntime", "true");
        /* 默认为false，如果设置为true，则使用表中实际字段名, 如果使用默认false，userId -> userid */
        /* 因此建议表中字段不要设置成USER_ID */
        tableConfig.addProperty("useActualColumnNames", "true");
        context.addTableConfiguration(tableConfig);
        /* jdbc-generateKey-config */
        GeneratedKey gkey;
//        if ("seq".equalsIgnoreCase(request.getEntityPrimaryKeyRule())) {
//            gkey = new GeneratedKey(request.getEntityPrimaryKey(), this.getOracleGeneratedKey(request.getEntitySeq()), false, (String)null);
//            tableConfig.setGeneratedKey(gkey);
//        } else if ("gen".equalsIgnoreCase(request.getEntityPrimaryKeyRule())) {
//            gkey = new GeneratedKey(request.getEntityPrimaryKey(), "JDBC", false, (String)null);
//            tableConfig.setGeneratedKey(gkey);
//        } else if ("ipt".equalsIgnoreCase(request.getEntityPrimaryKeyRule())) {
//        }
//        gkey = new GeneratedKey("primaryKeyColumnName", getOracleGeneratedKey("seqName"), false, null);
        gkey = new GeneratedKey("USER_ID", "JDBC", false, null);
        tableConfig.setGeneratedKey(gkey);

        /* java-model-generator-config 生成实体类 */
        JavaModelGeneratorConfiguration modelConfig = new JavaModelGeneratorConfiguration();
        modelConfig.setTargetPackage("com.bofa.entity");
        modelConfig.setTargetProject("src/main/java");
        modelConfig.addProperty("enableSubPackages", "false");
        modelConfig.addProperty("trimStrings", "false");
        modelConfig.addProperty("rootClass", "com.bofa.common.gen.base.MybatisEntity");
        context.setJavaModelGeneratorConfiguration(modelConfig);
        /* sql-map-generator-config 生成Mapper xml */
        SqlMapGeneratorConfiguration mapperConfig = new SqlMapGeneratorConfiguration();
        mapperConfig.setTargetPackage("com.bofa.mapper.impl");
        mapperConfig.setTargetProject("src/main/java");
        mapperConfig.addProperty("enableSubPackages", "false");
        context.setSqlMapGeneratorConfiguration(mapperConfig);
        // java-client-generator-config 生成Mapper接口
        JavaClientGeneratorConfiguration daoConfig = new JavaClientGeneratorConfiguration();
        daoConfig.setConfigurationType("XMLMAPPER");
        daoConfig.setTargetPackage("com.bofa.mapper");
        daoConfig.setTargetProject("src/main/java");
        daoConfig.addProperty("useLegacyBuilder", "true");
        context.setJavaClientGeneratorConfiguration(daoConfig);
        /* comment-generator-config */
        CommentGeneratorConfiguration commentConfig = new CommentGeneratorConfiguration();
        commentConfig.addProperty("suppressAllComments", "false");
        commentConfig.addProperty("suppressDate", "false");
        commentConfig.addProperty("addRemarkComments", "true");
        commentConfig.addProperty("dateFormat", "yyyy-MM-dd HH:mm:ss");
        context.setCommentGeneratorConfiguration(commentConfig);
        /* plugin-config */
        PluginConfiguration serializablePluginConfiguration = new PluginConfiguration();
        serializablePluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
        context.addPluginConfiguration(serializablePluginConfiguration);
        PluginConfiguration lombokPlugin = new PluginConfiguration();
        lombokPlugin.setConfigurationType(LombokPlugin.class.getName());
        context.addPluginConfiguration(lombokPlugin);
        PluginConfiguration mybatisMappingPlugin = new PluginConfiguration();
        mybatisMappingPlugin.setConfigurationType(MybatisMappingPlugin.class.getName());
        context.addPluginConfiguration(mybatisMappingPlugin);
        try {
            List<String> warnings = new ArrayList<>();
            MyCallBack callback = new MyCallBack(true);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
//            File dic = callback.getDirectory("src/main/java", "com.bofa.mapper");
//            fmkGen(dic, "com.bofa.mapper");
            myBatisGenerator.generate(new NullProgressCallback());
        } catch (Exception var18) {
        }
    }

    @Test
    public void toHumpNameTest() {
        System.out.println("toHumpName('user_common',0) -> " + SqlAndJavaTypesMappingUtil.toHumpName("user_common", 0));
    }

    private String getOracleGeneratedKey(String seqName) {
        return "SELECT " + seqName + ".NEXTVAL AS ID " + "FROM DUAL";
    }
}
