package sql;

import java.util.List;

import com.sun.security.ntlm.Client;
import model.ClientReviewCounterparty;
import model.ClientReviewRecord;
import model.ClientReviewRecordExample;
import org.apache.ibatis.annotations.Param;

public interface ClientReviewRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(ClientReviewRecord record);

    int insertSelective(ClientReviewRecord record);

    List<ClientReviewRecord> selectByExample(ClientReviewRecordExample example);

    ClientReviewRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(@Param("s") ClientReviewRecord record,@Param("w") String recordId);

    List<ClientReviewRecord> selectByClientName(String clientName);

    int deleteByRecordIdInt(String record);

    int deleteByCorporateName(String corporateName);

    int updateByPrimaryKey(ClientReviewRecord record);
}