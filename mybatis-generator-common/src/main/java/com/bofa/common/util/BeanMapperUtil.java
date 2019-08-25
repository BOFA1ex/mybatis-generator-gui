package com.bofa.common.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public class BeanMapperUtil {

    private static MapperFacade mapper;

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    static {
        MapperFactory mapperFactory = (new DefaultMapperFactory.Builder()).build();
        mapper = mapperFactory.getMapperFacade();
    }
}
