package sql;

import java.util.List;
import model.OtcDerivayiveCounterparty;
import model.OtcDerivayiveCounterpartyExample;
import org.apache.ibatis.annotations.Param;

public interface OtcDerivayiveCounterpartyMapper {
    int deleteByPrimaryKey(String id);

    int insert(OtcDerivayiveCounterparty record);

    int insertSelective(OtcDerivayiveCounterparty record);

    List<OtcDerivayiveCounterparty> selectByExample(OtcDerivayiveCounterpartyExample example);

    OtcDerivayiveCounterparty selectByPrimaryKey(String id);

    OtcDerivayiveCounterparty selectBycorporateName(String corporateName);
    List<OtcDerivayiveCounterparty> selectAllbyClientid();

    int updateByPrimaryKeySelective(OtcDerivayiveCounterparty record);

    int updateByPrimaryKey(OtcDerivayiveCounterparty record);

    List<OtcDerivayiveCounterparty> selectProdnum(String corporateName);

    List<OtcDerivayiveCounterparty> selectOrg(String corporateName);

    List<OtcDerivayiveCounterparty> selectClientId(String corporateName);

    List<OtcDerivayiveCounterparty> selectByCorporateName(String corporateName);

    int updateBycorporateName(@Param("s") OtcDerivayiveCounterparty record ,@Param("w") String corporateName );

    int updateByclientId(OtcDerivayiveCounterparty record);
}