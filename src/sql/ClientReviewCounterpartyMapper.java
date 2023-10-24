package sql;

import java.util.List;
import model.ClientReviewCounterparty;
import model.ClientReviewCounterpartyExample;
import org.apache.ibatis.annotations.Param;

public interface ClientReviewCounterpartyMapper {
    int deleteByPrimaryKey(String id);

    int insert(ClientReviewCounterparty record);

    int insertSelective(ClientReviewCounterparty record);

    List<ClientReviewCounterparty> selectByExample(ClientReviewCounterpartyExample example);

    ClientReviewCounterparty selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ClientReviewCounterparty record);

    int updateinfo(@Param("c") ClientReviewCounterparty record ,@Param("r") String recordID);

    int deleteByRecordId(String recordid);

    int insertinfo(ClientReviewCounterparty record );

    List<ClientReviewCounterparty> selectByRecordId(String recordId);

    int updateByPrimaryKey(ClientReviewCounterparty record);
}