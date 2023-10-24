
import main.intefacation.Otc;
import model.ClientReviewRecord;
import model.OtcDerivayiveCounterparty;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import sql.ClientReviewRecordMapper;
import sql.CounterpartyOrgMapper;
import sql.CounterpartyProdMonitorFlowMapper;
import sql.OtcDerivayiveCounterpartyMapper;
import sun.util.resources.cldr.CalendarData;
import org.slf4j.Logger;

import javax.lang.model.type.ArrayType;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CreateMonitorFlow {
    static ConnectDba connectDba = new ConnectDba();

    static SqlSession session = connectDba.connecttion();

    static Logger log = LoggerFactory.getLogger(CreateMonitorFlow.class);


    public static void main(String[] args) {

        try {
            String corporateName;
            String customerManager;

//输入客户经理和公司

            Scanner scanner = new Scanner(System.in);
            corporateName = scanner.next();
            customerManager = scanner.next();
            scanner.close();

            CounterpartyProdMonitorFlowMapper counterpartyProdMonitorFlowMapper = session.getMapper(CounterpartyProdMonitorFlowMapper.class);
            OtcDerivayiveCounterpartyMapper otcDerivayiveCounterpartyMapper = session.getMapper(OtcDerivayiveCounterpartyMapper.class);
            ClientReviewRecordMapper clientReviewRecordMapper = session.getMapper(ClientReviewRecordMapper.class);
            OtcDerivayiveCounterparty info = new OtcDerivayiveCounterparty();

//删除回访流程和期权产品监测流程在途流程
            clientReviewRecordMapper.deleteByCorporateName(corporateName);
            counterpartyProdMonitorFlowMapper.deleteByCorporateName(corporateName);

//设置发起期权产品流程的条件

//创建一个Calendar对象，将当前日期传入
            Calendar today = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();

            calendar.add(Calendar.DATE, -181);
            Date nowtime = today.getTime();
            Date newdate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String newdate1 = sdf.format(newdate);
            String now = sdf.format(nowtime);
            log.info("today is :" + newdate1);
            log.info("now is " + now);
            OtcDerivayiveCounterparty otcDerivayiveCounterparty = new OtcDerivayiveCounterparty();
            otcDerivayiveCounterparty.setCustomerManager(customerManager);
            otcDerivayiveCounterparty.setReturnVisitDate(newdate);
            otcDerivayiveCounterparty.setAmlMonitorFlag("true");
            otcDerivayiveCounterparty.setClientQualifyReview("true");
            otcDerivayiveCounterparty.setAllowBusiType("OPTION");
            otcDerivayiveCounterparty.setMasterAgreementDate(newdate);
            otcDerivayiveCounterpartyMapper.updateBycorporateName(otcDerivayiveCounterparty, corporateName);


            List<OtcDerivayiveCounterparty> info1 = new ArrayList<>();
//            clientIdList = otcDerivayiveCounterpartyMapper.selectClientId(corporateName);

//            info1 = otcDerivayiveCounterpartyMapper.selectProdnum(corporateName);
//            List<String> clientIdList = info1.stream().filter(dto -> dto.getIsProdHolder()!="03").map(OtcDerivayiveCounterparty ::getClientId).collect(Collectors.toList());
//            log.info(clientIdList.toString());
//            String url = "http://10.2.145.216:9090/api/test/optionProdMonitor";
//            HttpGet httpGet = new HttpGet(url);
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//
//            List<HttpGet> httpGets = clientIdList.stream()
//                    .map(clientId ->
//                    {
//                        ArrayList<NameValuePair> paramas = new ArrayList<>();
//                        paramas.add(new BasicNameValuePair("clientId",clientId.getClientId()));
//                        paramas.add(new BasicNameValuePair("date",now));
//                        return new HttpGet(String.format(url + "?" +URLEncodedUtils.format(paramas,"UTF-8")));
//                    })
//                    .collect(Collectors.toList());
//            log.info(httpGets.toString());
//            for(HttpGet  httpGet1:httpGets){
//                CloseableHttpResponse response = httpClient.execute(httpGet1);
//                HttpEntity httpEntity = response.getEntity();
//                String msg = EntityUtils.toString(httpEntity);
//                log.info(msg);
//                response.close();
//            }





            List<OtcDerivayiveCounterparty> clientIdList = new ArrayList<>();
            clientIdList = otcDerivayiveCounterpartyMapper.selectClientId(corporateName);



            String url = "http://10.2.145.216:9090/api/test/optionProdMonitor";
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            for(OtcDerivayiveCounterparty client_Id : clientIdList){
                String clientid = client_Id.getClientId();
                ArrayList<NameValuePair> paramas = new ArrayList<>();
                paramas.add(new BasicNameValuePair("clientId",clientid));
                paramas.add(new BasicNameValuePair("date",now));
                log.info(paramas.toString());
                httpGet.setURI(new URI(String.format(url + "?" + URLEncodedUtils.format(paramas,"UTF-8"))));
                CloseableHttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String msg = EntityUtils.toString(httpEntity);
                log.info(msg);
                response.close();

            }



        }
        catch (Exception e){
            log.error("error is " + e);
        }
    }


}
