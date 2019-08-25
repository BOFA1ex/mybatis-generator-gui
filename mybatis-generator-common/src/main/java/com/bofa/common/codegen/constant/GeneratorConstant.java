package com.bofa.common.codegen.constant;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-24
 */
public interface GeneratorConstant {

    /* ------------- JAVA_CLIENT-GENERATOR_CONSTANT -------------*/
    /** 会生成Mapper接口，接口完全依赖XML */
    String JAVA_CLIENT_GENERATOR_CONFIGURATION_XMLMAPPER = "XMLMAPPER";

    /** 会生成使用Mapper接口+Annotation的方式创建（SQL生成在annotation中），不会生成对应的XML */
    String JAVA_CLIENT_GENERATOR_CONFIGURATION_ANNOTATEDMAPPER = "ANNOTATEDMAPPER";

    /** 使用混合配置，会生成Mapper接口，并适当添加合适的Annotation，但是XML会生成在XML中 */
    String JAVA_CLIENT_GENERATOR_CONFIGURATION_MIXEDMAPPER = "MIXEDMAPPER";
    /* ------------- JAVA_CLIENT-GENERATOR_CONSTANT -------------*/



}
