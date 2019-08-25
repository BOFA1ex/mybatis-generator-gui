package com.bofa.common.codegen.mybatis.callback;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-24
 */
public class MyCallBack extends DefaultShellCallback {
    /**
     * Instantiates a new default shell callback.
     *
     * @param overwrite the overwrite
     */
    public MyCallBack(boolean overwrite) {
        super(overwrite);
    }

    @Override
    public boolean isMergeSupported() {
        return true;
    }

    @Override
    public String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) throws ShellException {
        try {
            return StringUtils.endsWithIgnoreCase(existingFile.getName(), "mapper") ?
                    Files.asCharSource(existingFile, Charset.forName(fileEncoding)).read() : newFileSource;
        } catch (IOException var6) {
            throw new ShellException("读已存在的mapper文件失败", var6);
        }
    }
}
