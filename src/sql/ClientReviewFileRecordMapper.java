package sql;

import java.util.List;
import model.ClientReviewFileRecord;
import model.ClientReviewFileRecordExample;
import org.apache.ibatis.annotations.Param;

public interface ClientReviewFileRecordMapper {
    int deleteByPrimaryKey(String s3FileId);

    int insert(ClientReviewFileRecord record);

    int insertSelective(ClientReviewFileRecord record);

    List<ClientReviewFileRecord> selectByExample(ClientReviewFileRecordExample example);

    ClientReviewFileRecord selectByPrimaryKey(String s3FileId);

    int updateByPrimaryKeySelective(ClientReviewFileRecord record);

    int updateS3FileIdInt(@Param("s3") String s3id, @Param("re") String recordid,@Param("fi") String fileName);

    int updateByPrimaryKey(ClientReviewFileRecord record);
}