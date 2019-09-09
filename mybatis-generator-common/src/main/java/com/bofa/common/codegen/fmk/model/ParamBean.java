package com.bofa.common.codegen.fmk.model;

import lombok.Data;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-06
 */
@Data
public class ParamBean {
    private String annotate;
    private String name;
    private String type;

    public static ParamBean instance(String name, String type, String annotate) {
        ParamBean pb = new ParamBean();
        pb.setName(name);
        pb.setType(type);
        pb.setAnnotate(annotate);
        return pb;
    }
}
