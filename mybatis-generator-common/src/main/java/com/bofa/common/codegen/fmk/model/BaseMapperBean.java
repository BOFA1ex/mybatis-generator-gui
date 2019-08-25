package com.bofa.common.codegen.fmk.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Data
public class BaseMapperBean {

    String packageValue;

    LocalDate currentDate = LocalDate.now();
}
