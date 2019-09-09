package com.bofa.common.util;

import com.bofa.common.codegen.mybatis.plugin.JavaTypeResolverJsr310Impl;
import com.project.tool.component.bean.TableColumnInfo;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-09-09
 */
public class MybatisJavaTypeMappingUtil extends JavaTypeResolverDefaultImpl {
    public String adaptJavaType(TableColumnInfo column) {
        FullyQualifiedJavaType answer = null;
        JdbcTypeInformation jdbcTypeInformation = this.typeMap.get(column.getJdbcTypeInt());
        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
            answer = this.overrideDefaultType(column, answer);
        }

        return answer != null ? answer.getFullyQualifiedName() : null;
    }

    public String adaptSimpleJavaType(TableColumnInfo column) {
        FullyQualifiedJavaType answer = null;
        JdbcTypeInformation jdbcTypeInformation = this.typeMap.get(column.getJdbcTypeInt());
        if (jdbcTypeInformation != null) {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
            answer = this.overrideDefaultType(column, answer);
        }

        return answer != null ? answer.getShortName() : null;
    }

    protected FullyQualifiedJavaType overrideDefaultType(TableColumnInfo column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer = defaultType;
        switch(column.getJdbcTypeInt()) {
            case -7:
                if (column.getColumnSize() > 1) {
                    answer = new FullyQualifiedJavaType("byte[]");
                } else {
                    answer = defaultType;
                }
                break;
            case 2:
            case 3:
                if (column.getDecimalDigits() <= 0 && column.getColumnSize() <= 18) {
                    if (column.getColumnSize() > 9) {
                        answer = new FullyQualifiedJavaType(Long.class.getName());
                    } else if (column.getColumnSize() > 4) {
                        answer = new FullyQualifiedJavaType(Integer.class.getName());
                    } else {
                        answer = new FullyQualifiedJavaType(Short.class.getName());
                    }
                } else {
                    answer = defaultType;
                }
        }

        return answer;
    }
}
