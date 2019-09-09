package com.bofa.common.util;

import com.bofa.common.model.TableColumnInfo;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-24
 */
public class SqlAndJavaTypesMappingUtil {
    private static Map<Integer, String> sqlTypes = new HashMap<>();

    private static void init() {
        Field[] var0 = Types.class.getDeclaredFields();
        int var1 = var0.length;

        for (Field field : var0) {
            try {
                Integer v = (Integer) field.get(Field.class);
                if (v != null) {
                    sqlTypes.put(v, field.getName());
                }
            } catch (Exception ignored) {
            }
        }

    }

    public static String findByType(int type) {
        String typeName = sqlTypes.get(type);
        return typeName == null ? "" + type : typeName;
    }


    public static String toHumpName(String name, int startIndex) {
        String colName = name.toLowerCase(Locale.ROOT);
        String[] values = StringUtils.split(colName, "_");
        if (values != null && values.length != 0) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < values.length; ++i) {
                String v = values[i];
                if (i < startIndex) {
                    buf.append(v);
                } else {
                    char vchar = Character.toUpperCase(v.charAt(0));
                    buf.append(vchar);
                    buf.append(v.substring(1));
                }
            }
            return buf.toString();
        } else {
            return null;
        }
    }

    public static String getOrSetHumpMethodName(String prefix, String name) {
        if (name != null && name.length() != 0) {
            StringBuilder buf = new StringBuilder();
            buf.append(prefix);
            char vchar = Character.toUpperCase(name.charAt(0));
            buf.append(vchar);
            buf.append(name.substring(1));
            return buf.toString();
        } else {
            return null;
        }
    }

    public static String adaptJavaType(TableColumnInfo column) {
        if (3 == column.getJdbcTypeInt()) {
            int columnSize = column.getColumnSize();
            int decimalDigits = column.getDecimalDigits();
            if (decimalDigits == 0) {
                if (columnSize <= 4) {
                    return "short";
                }

                if (columnSize <= 9) {
                    return "int";
                }

                if (columnSize <= 18) {
                    return "long";
                }

                return BigDecimal.class.getName();
            }
        } else {
            if (12 == column.getJdbcTypeInt()) {
                return String.class.getName();
            }

            if (93 == column.getJdbcTypeInt()) {
                return Date.class.getName();
            }
        }

        return "";
    }

    static {
        init();
    }
}
