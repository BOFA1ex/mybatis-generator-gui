package com.bofa.common.codegen.fmk.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bofa1ex
 * @since 2019-09-06
 */
@Data
public class FieldBean {
    private List<String> annotates = new ArrayList();
    private String modifier;
    private String name;
    private String type;

    public void addAnnotate(String annotate) {
        annotates.add(annotate);
    }
}
