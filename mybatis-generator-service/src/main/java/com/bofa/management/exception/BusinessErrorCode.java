package com.bofa.management.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public enum BusinessErrorCode {
    /**
     *
     */
    Unknown("400", "未知错误!"),
    Parameter_null("400", "请求参数 [{0}] 未传"),
    Parameter_invalid("400", "请求参数 [{0}] 不符合规范"),
    Parameter_com_null("400", "请求参数未传"),
    BAD_REQUEST("400", "无法找到您要的资源"),
    UNAUTHORIZED("401", "对不起, 您无法访问该资源"),
    INTERNAL_SERVER_ERROR("500", "出现无法预知的错误");

    public String code;
    public String message;

    BusinessErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessageAndCompletion(String... strings) {
        if (strings == null) {
            return this.message;
        } else {
            String tmp = this.message;
            for(int i = 0; i < strings.length; ++i) {
                String one = strings[i];
                tmp = StringUtils.replace(tmp, "{" + i + "}", one);
            }
            return tmp;
        }
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
