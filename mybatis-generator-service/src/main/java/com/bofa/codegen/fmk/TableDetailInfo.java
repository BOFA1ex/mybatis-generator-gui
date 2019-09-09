package com.bofa.codegen.fmk;

import com.bofa.common.model.TableColumnInfo;
import com.bofa.common.model.TableInfo;
import lombok.Data;

import java.util.List;

/**
 * @author bofa1ex
 * @since 2019-09-06
 */
@Data
public class TableDetailInfo {
    private TableInfo tableInfo;
    private List<TableColumnInfo> tableColumnInfo;
    private PrepareGenInfo prepareGenInfo;
}
