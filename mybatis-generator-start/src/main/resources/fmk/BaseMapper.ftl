package ${packageValue};

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author bofa1ex
* @build by fmk -version ${.version}
* @since ${currentDate}
*/
public interface BaseMapper<T,I,E> {

    long countByExample(E var1);

    int deleteByExample(E var1);

    int deleteByPrimaryKey(I var1);

    int insert(T var1);

    int insertSelective(T var1);

    List<T> selectByExample(E var1);

    T selectByPrimaryKey(I var1);

    int updateByExampleSelective(@Param("record") T var1, @Param("example") E var2);

    int updateByExample(@Param("record") T var1, @Param("example") E var2);

    int updateByPrimaryKeySelective(T var1);

    int updateByPrimaryKey(T var1);
}