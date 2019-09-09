package com.bofa.common.codegen.mybatis.plugin;

import com.bofa.common.model.TableColumnInfo;
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

    public String adaptJavaType(TableColumnInfo column) {
        FullyQualifiedJavaType answer = null;
        JdbcTypeInformation jdbcTypeInformation = this.typeMap.get(column.getJdbcTypeInt());
        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
            IntrospectedColumn col = new IntrospectedColumn();
            col.setActualColumnName(column.getColumnName());
            col.setJdbcType(column.getJdbcTypeInt());
            col.setJdbcTypeName(column.getJdbcType());
            answer = this.overrideDefaultType(col, answer);
        }
        return answer != null ? answer.getFullyQualifiedName() : null;
    }

    public String adaptSimpleJavaType(TableColumnInfo column) {
        FullyQualifiedJavaType answer = null;
        JdbcTypeInformation jdbcTypeInformation = this.typeMap.get(column.getJdbcTypeInt());
        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
            IntrospectedColumn col = new IntrospectedColumn();
            col.setActualColumnName(column.getColumnName());
            col.setJdbcType(column.getJdbcTypeInt());
            col.setJdbcTypeName(column.getJdbcType());
            answer = this.overrideDefaultType(col, answer);
        }

        return answer != null ? answer.getShortName() : null;
    }

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
