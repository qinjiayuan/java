package sql;

import java.util.List;
import model.CounterpartyOrg;
import model.CounterpartyOrgExample;
import org.apache.ibatis.annotations.Param;

public interface CounterpartyOrgMapper {
    int deleteByPrimaryKey(String id);

    int insert(CounterpartyOrg record);

    int insertSelective(CounterpartyOrg record);

    List<CounterpartyOrg> selectByExample(CounterpartyOrgExample example);

    CounterpartyOrg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CounterpartyOrg record);

    List<CounterpartyOrg> selectByCorporateName(String corporateName);

    int updateBycorporateName(@Param("s") CounterpartyOrg record ,@Param("w") String corporateName);

    int deleteByCorporateName(String corporateName);

    int updateByPrimaryKey(CounterpartyOrg record);
}