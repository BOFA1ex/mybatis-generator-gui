package com.bofa.cmpt;

import com.bofa.management.exception.BusinessException;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.NullProgressCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Component
public class MybatisGenCmpt {

    public static final Logger logger = LoggerFactory.getLogger(MybatisGenCmpt.class);

    public ContextBuilder newContextBuilder(ModelType modelType) {
        return new ContextBuilder(new Context(modelType));
    }

    public JDBCConnectionConfigBuilder newJdbcConfigBuilder() {
        return new JDBCConnectionConfigBuilder(new JDBCConnectionConfiguration());
    }

    public TableConfigurationBuilder newTableConfigurationBuilder(Context context) {
        return new TableConfigurationBuilder(new TableConfiguration(context));
    }

    public JavaModelGeneratorConfigurationBuilder newJavaModelConfigBuilder() {
        return new JavaModelGeneratorConfigurationBuilder(new JavaModelGeneratorConfiguration());
    }

    public JavaClientGeneratorConfigurationBuilder newJavaClientConfigBuilder() {
        return new JavaClientGeneratorConfigurationBuilder(new JavaClientGeneratorConfiguration());
    }

    public SqlMapGeneratorConfigurationBuilder newSqlMapGeneratorConfigurationBuilder() {
        return new SqlMapGeneratorConfigurationBuilder(new SqlMapGeneratorConfiguration());
    }

    public CommentGeneratorConfigurationBuilder newCommentGeneratorConfigurationBuilder() {
        return new CommentGeneratorConfigurationBuilder(new CommentGeneratorConfiguration());
    }

    public PluginConfigurationBuilder newPluginConfigurationBuilder() {
        return new PluginConfigurationBuilder(new PluginConfiguration());
    }

    public JavaTypeResolverConfigurationBuilder newJavaTypeResolverConfigurationBuilder() {
        return new JavaTypeResolverConfigurationBuilder(new JavaTypeResolverConfiguration());
    }

    public static class ContextBuilder {
        Context context;

        private ContextBuilder(Context context) {
            this.context = context;
        }

        public ContextBuilder setId(String id) {
            context.setId(id);
            return this;
        }

        public ContextBuilder setTargetTime(String targetTime) {
            context.setTargetRuntime(targetTime);
            return this;
        }

        public ContextBuilder addProperty(String name, String value) {
            context.addProperty(name, value);
            return this;
        }

        public ContextBuilder addTableConfiguration(TableConfiguration tableConfig) {
            context.addTableConfiguration(tableConfig);
            return this;
        }

        public ContextBuilder setJavaModelGeneratorConfiguration(JavaModelGeneratorConfiguration modelConfig) {
            context.setJavaModelGeneratorConfiguration(modelConfig);
            return this;
        }

        public ContextBuilder setSqlMapGeneratorConfiguration(SqlMapGeneratorConfiguration mapperXmlConfig) {
            context.setSqlMapGeneratorConfiguration(mapperXmlConfig);
            return this;
        }

        public ContextBuilder setJavaClientGeneratorConfiguration(JavaClientGeneratorConfiguration mapperConfig) {
            context.setJavaClientGeneratorConfiguration(mapperConfig);
            return this;
        }

        public ContextBuilder setCommentGeneratorConfiguration(CommentGeneratorConfiguration commentConfig) {
            context.setCommentGeneratorConfiguration(commentConfig);
            return this;
        }

        public ContextBuilder addPluginConfiguration(PluginConfiguration pluginConfig) {
            context.addPluginConfiguration(pluginConfig);
            return this;
        }

        public ContextBuilder setJdbcConnectionConfiguration(JDBCConnectionConfiguration jdbcConfig) {
            context.setJdbcConnectionConfiguration(jdbcConfig);
            return this;
        }

        public Context build() {
            return context;
        }
    }

    public static class JDBCConnectionConfigBuilder {

        JDBCConnectionConfiguration jdbcConfig;

        private JDBCConnectionConfigBuilder(JDBCConnectionConfiguration jdbcConfig) {
            this.jdbcConfig = jdbcConfig;
        }

        public JDBCConnectionConfigBuilder setDriverClass(String driverClass) {
            logger.info("jdbc-config-driverClass - {}", driverClass);
            jdbcConfig.setDriverClass(driverClass);
            return this;
        }

        public JDBCConnectionConfigBuilder setConnectionURL(String connectionURL) {
            logger.info("jdbc-config-connectionURL - {}", connectionURL);
            jdbcConfig.setConnectionURL(connectionURL);
            return this;
        }

        public JDBCConnectionConfigBuilder setUserId(String userId) {
            logger.info("jdbc-config-userId - {}", userId);
            jdbcConfig.setUserId(userId);
            return this;
        }

        public JDBCConnectionConfigBuilder setPassword(String password) {
            jdbcConfig.setPassword(password);
            return this;
        }

        public JDBCConnectionConfigBuilder addProperty(String name, String value) {
            logger.info("jdbc-config-property name - {} value - {}", name, value);
            jdbcConfig.addProperty(name, value);
            return this;
        }

        public JDBCConnectionConfiguration build() {
            return jdbcConfig;
        }
    }

    public static class TableConfigurationBuilder {
        TableConfiguration tableConfig;

        private TableConfigurationBuilder(TableConfiguration tableConfig) {
            this.tableConfig = tableConfig;
        }

        public TableConfigurationBuilder setTableName(String tableName) {
            logger.info("table-config-tableName - {}", tableName);
            tableConfig.setTableName(tableName);
            return this;
        }

        public TableConfigurationBuilder setDomainObjectName(String domainObjectName) {
            logger.info("table-config-domainObjectName - {}", domainObjectName);
            tableConfig.setDomainObjectName(domainObjectName);
            return this;
        }

        public TableConfigurationBuilder setGeneratedKey(GeneratedKey generatedKey) {
            tableConfig.setGeneratedKey(generatedKey);
            return this;
        }

        public TableConfigurationBuilder addProperty(String name, String value) {
            logger.info("table-config-property name - {} value - {}", name, value);
            tableConfig.addProperty(name, value);
            return this;
        }

        public TableConfiguration build() {
            return tableConfig;
        }
    }

    public static class JavaModelGeneratorConfigurationBuilder {
        JavaModelGeneratorConfiguration modelConfig;

        private JavaModelGeneratorConfigurationBuilder(JavaModelGeneratorConfiguration modelConfig) {
            this.modelConfig = modelConfig;
        }

        public JavaModelGeneratorConfigurationBuilder setTargetPackage(String targetPackage) {
            logger.info("javaModel-config-targetPackage - {}", targetPackage);
            modelConfig.setTargetPackage(targetPackage);
            return this;
        }

        public JavaModelGeneratorConfigurationBuilder setTargetProject(String targetProject) {
            logger.info("javaModel-config-targetProject - {}", targetProject);
            modelConfig.setTargetProject(targetProject);
            return this;
        }

        public JavaModelGeneratorConfigurationBuilder addProperty(String name, String value) {
            logger.info("javaModel-config-property name - {} value - {}", name, value);
            modelConfig.addProperty(name, value);
            return this;
        }

        public JavaModelGeneratorConfiguration build() {
            return modelConfig;
        }
    }


    public static class SqlMapGeneratorConfigurationBuilder {
        SqlMapGeneratorConfiguration mapperXmlConfig;

        private SqlMapGeneratorConfigurationBuilder(SqlMapGeneratorConfiguration mapperXmlConfig) {
            this.mapperXmlConfig = mapperXmlConfig;
        }

        public SqlMapGeneratorConfigurationBuilder setTargetPackage(String targetPackage) {
            logger.info("mapperXml-config-targetPackage - {}", targetPackage);
            mapperXmlConfig.setTargetPackage(targetPackage);
            return this;
        }

        public SqlMapGeneratorConfigurationBuilder setTargetProject(String targetProject) {
            logger.info("mapperXml-config-targetProject - {}", targetProject);
            mapperXmlConfig.setTargetProject(targetProject);
            return this;
        }

        public SqlMapGeneratorConfigurationBuilder addProperty(String name, String value) {
            logger.info("mapperXml-config-property name - {} value - {}", name, value);
            mapperXmlConfig.addProperty(name, value);
            return this;
        }

        public SqlMapGeneratorConfiguration build() {
            return mapperXmlConfig;
        }
    }

    public static class JavaClientGeneratorConfigurationBuilder {
        JavaClientGeneratorConfiguration mapperConfig;

        private JavaClientGeneratorConfigurationBuilder(JavaClientGeneratorConfiguration mapperConfig) {
            this.mapperConfig = mapperConfig;
        }

        public JavaClientGeneratorConfigurationBuilder setConfigurationType(String configurationType) {
            logger.info("mapper-config-configurationType - {}", configurationType);
            mapperConfig.setConfigurationType(configurationType);
            return this;
        }

        public JavaClientGeneratorConfigurationBuilder setTargetPackage(String targetPackage) {
            logger.info("mapper-config-targetPackage - {}", targetPackage);
            mapperConfig.setTargetPackage(targetPackage);
            return this;
        }

        public JavaClientGeneratorConfigurationBuilder setTargetProject(String targetProject) {
            logger.info("mapper-config-targetProject - {}", targetProject);
            mapperConfig.setTargetProject(targetProject);
            return this;
        }

        public JavaClientGeneratorConfigurationBuilder addProperty(String name, String value) {
            logger.info("mapper-config-property name - {} value - {}", name, value);
            mapperConfig.addProperty(name, value);
            return this;
        }

        public JavaClientGeneratorConfiguration build() {
            return mapperConfig;
        }
    }

    public static class CommentGeneratorConfigurationBuilder {
        CommentGeneratorConfiguration commentConfig;

        private CommentGeneratorConfigurationBuilder(CommentGeneratorConfiguration commentConfig) {
            this.commentConfig = commentConfig;
        }

        public CommentGeneratorConfigurationBuilder addProperty(String name, String value) {
            logger.info("comment-config-property name - {} value - {}", name, value);
            commentConfig.addProperty(name, value);
            return this;
        }

        public CommentGeneratorConfigurationBuilder setConfigurationType(String configurationType) {
            logger.info("comment configurationType {}", configurationType);
            commentConfig.setConfigurationType(configurationType);
            return this;
        }

        public CommentGeneratorConfiguration build() {
            return commentConfig;
        }
    }

    public static class PluginConfigurationBuilder {
        PluginConfiguration pluginConfig;

        private PluginConfigurationBuilder(PluginConfiguration pluginConfig) {
            this.pluginConfig = pluginConfig;
        }

        public PluginConfigurationBuilder setConfigurationType(String configurationType) {
            logger.info("plugin-config-configurationType {}", configurationType);
            pluginConfig.setConfigurationType(configurationType);
            return this;
        }

        public PluginConfiguration build() {
            return pluginConfig;
        }
    }

    public static class JavaTypeResolverConfigurationBuilder {
        JavaTypeResolverConfiguration resolverConfig;

        private JavaTypeResolverConfigurationBuilder(JavaTypeResolverConfiguration resolverConfig) {
            this.resolverConfig = resolverConfig;
        }

        public JavaTypeResolverConfigurationBuilder setConfigurationType(String configurationType) {
            logger.info("javaType-resolve-configurationType {}", configurationType);
            resolverConfig.setConfigurationType(configurationType);
            return this;
        }

        public JavaTypeResolverConfiguration build() {
            return resolverConfig;
        }
    }

    public void generate(Configuration config, DefaultShellCallback callback) throws Exception {
        List<String> warnings = new ArrayList<>();
        try {
            MyBatisGenerator gen = new MyBatisGenerator(config, callback, warnings);
            logger.info("mybatis generate start...");
            gen.generate(new NullProgressCallback());
            logger.info("mybatis generate finished...");
        } catch (InvalidConfigurationException | InterruptedException | IOException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            logger.warn("warnings {}", warnings);
            if (warnings.size() != 0) {
                StringBuilder sb = new StringBuilder();
                warnings.forEach(sb::append);
                BusinessException.throwBusinessException(sb.toString());
            }
        }
    }
}
