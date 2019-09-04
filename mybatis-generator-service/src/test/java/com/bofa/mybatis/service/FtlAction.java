package com.bofa.mybatis.service;

import com.bofa.common.codegen.fmk.model.BaseMapperBean;
import freemarker.template.*;
import lombok.Data;
import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import java.util.StringTokenizer;

import static freemarker.template.Configuration.VERSION_2_3_28;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public class FtlAction {

    @Test
    public void gen() throws IOException {
        Configuration cfg = new Configuration(VERSION_2_3_28);
        cfg.setDirectoryForTemplateLoading(Paths.get("/Users/Bofa/mybatis-generator-gui/mybatis-generator-common/src/main/resources").toFile());
        Template temp = null;
        Path path = Paths.get("/Users/Bofa/mybatis-generator-gui/mybatis-generator-common/src/main/resources/BaseMapper.java");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Path targetFile = Files.createFile(path);
        try (Writer out = Files.newBufferedWriter(targetFile)) {
            temp = cfg.getTemplate("/src/main/resources/fmk/BaseMapper.ftl");
            BaseMapperBean mapperBean = new BaseMapperBean();
            mapperBean.setPackageValue(this.getClass().getPackage().getName());
            temp.process(mapperBean, out);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    @Data
    static class StrFormatHolder {
        String pattern;
        String template;
    }

}
