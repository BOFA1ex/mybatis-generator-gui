package com.bofa.common.codegen.fmk.model;

import lombok.Data;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-06
 */
@Data
public class BaseSvBean {
    private String packageValue;
    private Set<String> imports = new HashSet<>();
}
