package clientReview;//import main.Way.Counterpartyway;

import model.Aorg;
import model.CounterpartyOrg;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sql.AorgMapper;
import sql.CounterpartyOrgMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class Test {

    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws Exception {

        InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        SqlSession session = factory.openSession(true);
        Scanner scan = new Scanner(System.in);
        String corporatename =scan.next();
        scan.close();
//  查询是否存在机构
        CounterpartyOrgMapper counterpartyOrgMapper = session.getMapper(CounterpartyOrgMapper.class);
        List<CounterpartyOrg> counterpartyOrgs = counterpartyOrgMapper.selectByCorporateName(corporatename);
//查询客户经理
        AorgMapper aorgMapper = session.getMapper(AorgMapper.class);
        List<Aorg> aorgs = aorgMapper.selectExists("孙滨");
        System.out.println(aorgs.get(0).getDeptCode());




//        CounterpartyOrgMapper counterpartyOrgMapper = session.getMapper(CounterpartyOrgMapper.class);
//        CounterpartyOrg client_id = counterpartyOrgMapper.selectLastestClientIdInt();
//        System.out.println(client_id.getc);
//        OtcDerivayiveCounterpartyMapper otcDerivayiveCounterpartyMapper = session.getMapper(OtcDerivayiveCounterpartyMapper.class);
//测试查询
       /* List<OtcDerivayiveCounterparty> info = otcDerivayiveCounterpartyMapper.selectAllbyClientid();
        System.out.println(info.toString());
        for(OtcDerivayiveCounterparty dbn : info){

            System.out.println(dbn.toString());
        }

        CounterpartyOrgMapper counterpartyOrgMapper = session.getMapper(CounterpartyOrgMapper.class);
        String counterparty = String.valueOf(counterpartyOrgMapper.selectByPrimaryKey("F867F04C6AEB4EFDB9188885ED7F5E33"));
        System.out.println(counterparty.toString());*/
//测试更新
        /*OtcDerivayiveCounterpartyMapper  otcDerivayiveCounterpartyMapper = session.getMapper(OtcDerivayiveCounterpartyMapper.class);
        OtcDerivayiveCounterparty recored = new OtcDerivayiveCounterparty();
        recored.setClientId("DEV1100106355");
        recored.setClientQualifyReview("true");
        recored.setAllowBusiType("TRS");
        otcDerivayiveCounterpartyMapper.updateByclientId(recored);
        System.out.println("更新成功");*/
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        HttpPost httpPost = new HttpPost("http://10.128.8.49:5050/createflow");
//        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//        File file = new File("src/main/intefacation/20220318144757.png");
//        multipartEntityBuilder.addPart("file",new FileBody(file));
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("CORPORATE_NAME","测试产品关注类"));
//        params.add(new BasicNameValuePair("USER","孙滨"));
//        params.add(new BasicNameValuePair("isNewflow","0"));
//        params.add(new BasicNameValuePair("ENVIRONMENT","216"));
//        params.add(new BasicNameValuePair("PORT","9090"));
//
//        log.info(params.toString());
//        httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
//        httpPost.setEntity(multipartEntityBuilder.build());
//
//        CloseableHttpResponse response = httpClient.execute(httpPost);
//        log.info(response.toString());












//        OtcDerivayiveCounterparty record = new OtcDerivayiveCounterparty();
//        record.setCorporateName(info.getCorporateName());
//        record.setClientId(info.getClientId());
//        record.setClientId(info.getAbbreviation());
//        record.setAmlMonitorFlag("true");
//        System.out.println(record);

    }
}