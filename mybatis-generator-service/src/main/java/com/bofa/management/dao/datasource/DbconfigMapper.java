package com.bofa.management.dao.datasource;

import com.bofa.management.dao.datasource.entity.Dbconfig;
import com.bofa.management.dao.datasource.entity.DbconfigExample;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@Mapper
public interface DbconfigMapper {

    @Select({"select * from dbconfig where dbconnectname = #{dbconnectname}"})
    Dbconfig findByDbconnectName(@Param("dbconnectname") String var1);

    long countByExample(DbconfigExample var1);

    int deleteByExample(DbconfigExample var1);

    int deleteByPrimaryKey(Long var1);

    int insert(Dbconfig var1);

    int insertSelective(Dbconfig var1);

    List<Dbconfig> selectByExample(DbconfigExample var1);

    Dbconfig selectByPrimaryKey(Long var1);

    int updateByExampleSelective(@Param("record") Dbconfig var1, @Param("example") DbconfigExample var2);

    int updateByExample(@Param("record") Dbconfig var1, @Param("example") DbconfigExample var2);

    int updateByPrimaryKeySelective(Dbconfig var1);

    int updateByPrimaryKey(Dbconfig var1);
}
