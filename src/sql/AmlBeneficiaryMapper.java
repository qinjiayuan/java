package sql;

import model.AmlBeneficiary;

import java.util.List;

public interface AmlBeneficiaryMapper {
    int deleteByPrimaryKey(String id);

    int insert(AmlBeneficiary record);

    int insertSelective(AmlBeneficiary record);

    AmlBeneficiary selectByPrimaryKey(String id);

    List<AmlBeneficiary> selectByCounterpartyId(String clientName);

    int updateByPrimaryKeySelective(AmlBeneficiary record);

    int updateByPrimaryKey(AmlBeneficiary record);
}