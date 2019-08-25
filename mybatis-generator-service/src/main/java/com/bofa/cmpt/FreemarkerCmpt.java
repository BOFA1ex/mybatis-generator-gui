package com.bofa.cmpt;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Lazy
@Component
public class FreemarkerCmpt {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String FREEMARKER_DIRECTORY = "/src/main/resources/fmk";
    public static final String FREEMARKER_SUFFIX = ".ftl";

    @Autowired
    private Configuration cfg;

    public void structure(String ftlName, Object root, String fileName) {
        File targetFile = null;
        if (fileName != null) {
            targetFile = new File(fileName);
        }
        this.structure(ftlName, root, targetFile);
    }

    public void structure(String ftlName, Object root, File targetFile) {
        try {
            if (ftlName == null) {
                return;
            }

            ftlName = this.checkName(ftlName);
            Template temp = this.cfg.getTemplate(ftlName);
            Writer out = null;
            if (targetFile == null) {
                out = new OutputStreamWriter(System.out);
            } else {
                out = Files.newBufferedWriter(targetFile.toPath(), StandardCharsets.UTF_8);
            }
            temp.process(root, out);
        } catch (Exception var6) {
            logger.error("根据模板构建内容出错 " + ftlName + " " + root, var6);
        }

    }

    public void addStringTemplate(String ftlName, String content) {
        if (ftlName != null) {
            ftlName = this.checkName(ftlName);
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate(ftlName, content);
            this.cfg.setTemplateLoader(stringLoader);
        }
    }

    public void removeTemplate(String ftlName) {
        if (ftlName != null) {
            ftlName = this.checkName(ftlName);

            try {
                this.cfg.removeTemplateFromCache(ftlName);
            } catch (IOException var3) {
                logger.error("删除模板缓存出错 " + ftlName, var3);
            }

        }
    }

    private String checkName(String ftlName) {
        if (!FREEMARKER_SUFFIX.endsWith(ftlName)) {
            ftlName = ftlName + FREEMARKER_SUFFIX;
        }
        return ftlName;
    }
}
