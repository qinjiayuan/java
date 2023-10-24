import com.sun.corba.se.spi.logging.CORBALogDomains;
import model.AmlBeneficiary;
import model.AmlCounterparty;
import model.AmlCounterpartyExample;
import model.OtcDerivayiveCounterparty;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sql.AmlBeneficiaryMapper;
import sql.AmlCounterpartyMapper;
import sql.OtcDerivayiveCounterpartyMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmailReminddata {
    static ConnectDba connectDba = new ConnectDba();

    static SqlSession session = connectDba.connecttion();

    static Logger log = LoggerFactory.getLogger(EmailReminddata.class);

    public static String corporateName ;

    public static String customer;

    public List<String> getcorporateNameAndcustomer(){
        Scanner scanner = new Scanner(System.in);
        corporateName = scanner.next();
        customer = scanner.next();
        scanner.close();

        List<String> info = new ArrayList<>();
        info.add(corporateName);
        info.add(customer);
        return info ;
    }


    public void CertificateRemindOrg(){
        String corporateName = getcorporateNameAndcustomer().get(0);
        String customer = getcorporateNameAndcustomer().get(1);
        try{


        OtcDerivayiveCounterpartyMapper otcDerivayiveCounterpartyMapper = session.getMapper(OtcDerivayiveCounterpartyMapper.class);
        AmlBeneficiaryMapper amlBeneficiaryMapper = session.getMapper(AmlBeneficiaryMapper.class);
        AmlCounterpartyMapper amlCounterpartyMapper = session.getMapper(AmlCounterpartyMapper.class);
        List<OtcDerivayiveCounterparty> tmd = new ArrayList<>();
        tmd = otcDerivayiveCounterpartyMapper.selectClientId(corporateName);
        if(tmd.size()!=0){
            throw new RuntimeException("查询机构不存在或者存在多个");
        }
        List<String> clientIdList = new ArrayList<>();
        clientIdList = tmd.stream().map(OtcDerivayiveCounterparty::getClientId).collect(Collectors.toList());
        OtcDerivayiveCounterparty otcDerivayiveCounterparty = new OtcDerivayiveCounterparty();
        otcDerivayiveCounterparty.setAmlMonitorFlag("true");
        otcDerivayiveCounterpartyMapper.updateBycorporateName(otcDerivayiveCounterparty,corporateName);
        List<AmlCounterparty> ps2 = amlCounterpartyMapper.selectByCorporateName(corporateName);
        for(String clientid : clientIdList){

        }






        }
        catch(Exception e){
            log.info("error is "+ e);
        }
    }
}
