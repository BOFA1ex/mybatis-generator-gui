package com.bofa.common.codegen.mybatis.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;
import java.time.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-01
 */
public class JavaTypeResolverJsr310Impl extends JavaTypeResolverDefaultImpl {

    @Override
    protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer = defaultType;

        switch (column.getJdbcType()) {
            case Types.BIT:
                answer = calculateBitReplacement(column, defaultType);
                break;
            case Types.DECIMAL:
            case Types.NUMERIC:
                answer = calculateBigDecimalReplacement(column, defaultType);
                break;
            case Types.DATE:
                answer = new FullyQualifiedJavaType(LocalDate.class.getName());
                break;
            case Types.TIME:
                answer = new FullyQualifiedJavaType(LocalTime.class.getName());
                break;
            case Types.TIMESTAMP:
                answer = new FullyQualifiedJavaType(LocalDateTime.class.getName());
                break;
            default:
                break;
        }

        return answer;
    }
}
