package com.bofa.common.codegen.fmk.model;

import lombok.Data;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-06
 */
@Data
public class ClassBean {
    private String packageValue;
    private List<String> annotates = new ArrayList();
    private String superClass;
    private String modifier;
    private String simpleName;
    private Set<String> interfaceClasses = new HashSet();
    private Set<String> imports = new HashSet();
    private List<FieldBean> fields = new ArrayList();
    private List<MethodBean> methods = new ArrayList();

    public void addAnnotate(String annotate) {
        annotates.add(annotate);
    }

    public void addImport(String importValue) {
        imports.add(importValue);
    }

    public void addField(FieldBean fieldBean) {
        fields.add(fieldBean);
    }

    public void addMethod(MethodBean methodBean) {
        methods.add(methodBean);
    }
}
