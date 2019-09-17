package com.bofa.common.codegen.fmk.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bofa1ex
 * @since 2019-09-06
 */
@Data
public class MethodBean {
    private List<String> annotates = new ArrayList();
    private String modifier;
    private String name;
    private String returnContent;
    private String methodContent;
    private List<ParamBean> params = new ArrayList();
    private List<String> exceptions = new ArrayList();

    public void addParam(ParamBean paramBean) {
        params.add(paramBean);
    }

    public void addAnnotate(String annotate){
        annotates.add(annotate);
    }
}
