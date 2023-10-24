import model.*;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.LoggerFactory;
import sql.*;

import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;

public class AutoCreateReviewFlow {

    static ConnectDba connectDba = new ConnectDba();
    static SqlSession session = connectDba.connecttion();
    static Logger log = LoggerFactory.getLogger(AutoCreateReviewFlow.class);

//获取客户和客户经理的信息




    public static void main(String[] args) {
        AutoCreateReviewFlow autoCreateReviewFlow = new AutoCreateReviewFlow();
        try{
            String corporatename ;
            String user ;
            Scanner scanner = new Scanner(System.in);
            corporatename = scanner.next();
            user = scanner.next();
            scanner.close();
//  查询是否存在机构
            GetId getId = new GetId();
            CounterpartyOrgMapper counterpartyOrgMapper = session.getMapper(CounterpartyOrgMapper.class);
            List<CounterpartyOrg> counterpartyOrgs = counterpartyOrgMapper.selectByCorporateName(corporatename);
//查询客户经理
            AorgMapper aorgMapper = session.getMapper(AorgMapper.class);
            List<Aorg> aorgs = aorgMapper.selectExists(user);
            if(aorgs.size()==0){
                throw new IllegalArgumentException("请输入正确的客户经理的中文名称");
            }
            String deptcode = aorgs.get(0).getDeptCode();
            String customerManager = aorgs.get(0).getUserId();

            if(counterpartyOrgs.size() != 0){
//查询是否存在交易对手
                String unifilesocialcode =counterpartyOrgs.get(0).getUnifiedsocialCode();
                CounterpartyOrg record1 = new CounterpartyOrg();
                record1.setAmlMonitorFlag("true");
                counterpartyOrgMapper.updateBycorporateName(record1,corporatename);
                OtcDerivayiveCounterpartyMapper otcDerivayiveCounterpartyMapper = session.getMapper(OtcDerivayiveCounterpartyMapper.class);
                List<OtcDerivayiveCounterparty> result1 = otcDerivayiveCounterpartyMapper.selectByCorporateName(corporatename);
                if(result1.size()!=0){
                    Date date = new Date();
                    log.info("今天的时间是" + date);
                    OtcDerivayiveCounterparty derivayiveCounterparty = new OtcDerivayiveCounterparty();
//设置起流程的条件以及更新客户经理
                    derivayiveCounterparty.setAmlMonitorFlag("true");
                    derivayiveCounterparty.setReturnVisitDate(date);
                    derivayiveCounterparty.setNoAutoVisit("false");
                    derivayiveCounterparty.setCustomerManager(customerManager);
                    derivayiveCounterparty.setIntroductionDepartment(deptcode);
                    derivayiveCounterparty.setAllowBusiType("OPTION,PRODUCT");
                    log.info(derivayiveCounterparty.toString());
                    otcDerivayiveCounterpartyMapper.updateBycorporateName(derivayiveCounterparty,corporatename);
                    log.info("已经成功更新");


                    ClientReviewRecordMapper clientReviewRecordMapper = session.getMapper(ClientReviewRecordMapper.class);
                    List<ClientReviewRecord> recordId = clientReviewRecordMapper.selectByClientName(corporatename);
                    if(recordId.size() != 0){
                        ClientReviewCounterpartyMapper counterpartyMapper = session.getMapper(ClientReviewCounterpartyMapper.class);
                        for(ClientReviewRecord rd : recordId){
                            counterpartyMapper.deleteByRecordId(rd.getRecordId());
                            clientReviewRecordMapper.deleteByRecordIdInt(rd.getRecordId());
                        }
                        log.info("在途流程已经清除");
                    }
                    List<OtcDerivayiveCounterparty> multipleProd = otcDerivayiveCounterpartyMapper.selectProdnum(corporatename);
//发起客户流程
                    if(multipleProd.size()!=1){
//发起产品客户流程
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        String url = "http://10.2.145.216:9090/clientreview/checkMultipleClient";

                        HttpPost httpPost = new HttpPost(url);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedate = sdf.format(date);
                        ArrayList<NameValuePair> multiple_params = new ArrayList<>();
                        multiple_params.add(new BasicNameValuePair("checkDateEnd",formattedate));
                        multiple_params.add(new BasicNameValuePair("checkDateStart",formattedate));
                        multiple_params.add(new BasicNameValuePair("uniCodeList",unifilesocialcode));
                        log.info(multiple_params.toString());
                        httpPost.setEntity(new UrlEncodedFormEntity(multiple_params));
                        CloseableHttpResponse response = httpClient.execute(httpPost);
                        HttpEntity httpEntity = response.getEntity();
                        String msg = EntityUtils.toString(httpEntity);
                        log.info("多产品回访 "+msg);
                        response.close();
//单独针对机构客户发起流程

                        String singleUrl = "http://10.2.145.216:9090/clientreview/checkSingleClient";

                        HttpPost httpPost1 = new HttpPost(singleUrl);
                        List<NameValuePair> single_params = new ArrayList<>();
                        single_params.add(new BasicNameValuePair("checkDateEnd",formattedate));
                        single_params.add(new BasicNameValuePair("checkDateStart",formattedate));
                        single_params.add(new BasicNameValuePair("uniCodeList",unifilesocialcode));
                        log.info(single_params.toString());
                        httpPost1.setEntity(new UrlEncodedFormEntity(single_params));
                        CloseableHttpResponse response1 = httpClient.execute(httpPost1);

                        HttpEntity httpEntity1 = response1.getEntity();
                        String ms = EntityUtils.toString(httpEntity1);
                        log.info("单产品回访"+ms);
                        response1.close();

                        List<String> s3fileid = new ArrayList<>();
                        UploadFile uploadFile = new UploadFile();

                        List<String> file_name = Arrays.asList("主体/管理人文件", "32", "CSRC", "QCC_CREDIT_RECORD", "CEIDN", "QCC_ARBITRATION", "QCC_AUDIT_INSTITUTION", "CCPAIMIS", "CC", "P2P", "OTHERS", "NECIPS", "CJO");
                        log.info(file_name.get(1));
                        List<ClientReviewRecord> allrecord = clientReviewRecordMapper.selectByClientName(corporatename);

                        ClientReviewFileRecordMapper clientReviewFileRecordMapper = session.getMapper(ClientReviewFileRecordMapper.class);
//上传附件
                        for(ClientReviewRecord re :allrecord){
                            String new_record_id = re.getRecordId();
                            s3fileid = uploadFile.gets3filed();

                            for(int i = 0 ; i < 13 ; i++){
                                clientReviewFileRecordMapper.updateS3FileIdInt(s3fileid.get(i),new_record_id,file_name.get(i));
                            }
                        }
//设置基本信息页
                        ClientReviewDetailMapper clientReviewDetailMapper = session.getMapper(ClientReviewDetailMapper.class);
                        ClientReviewDetail reviewDetail = new ClientReviewDetail();
                        ClientReviewCounterpartyMapper clientReviewCounterpartyMapper = session.getMapper(ClientReviewCounterpartyMapper.class);
                        ClientReviewRecord clientReviewRecord = new ClientReviewRecord();
                        for(ClientReviewRecord record :allrecord) {
                            reviewDetail.setId(getId.getid());
                            reviewDetail.setRecordId(record.getRecordId());
                            reviewDetail.setClientName("11");
                            reviewDetail.setClientPosition("老师");
                            reviewDetail.setEmail("123@qq.com");
                            reviewDetail.setPhone("13112345678");
                            reviewDetail.setReviewLog("123");
                            reviewDetail.setSuitability("N");
                            reviewDetail.setSuitabilityLog("123");
                            reviewDetail.setCreatedDatetime(date);
                            clientReviewDetailMapper.insertSelective(reviewDetail);
                            ClientReviewCounterparty clientReviewCounterparty = new ClientReviewCounterparty();
                            clientReviewCounterparty.setAgreeInfo("Y");
                            clientReviewCounterparty.setBenefitOverFlag("1");
                            clientReviewCounterpartyMapper.updateinfo(clientReviewCounterparty,record.getRecordId());
                            clientReviewRecord.setSalePerson("renyu");
                            clientReviewRecord.setAccountingFirmName("测试专用");
                            clientReviewRecordMapper.updateByPrimaryKeySelective(clientReviewRecord,record.getRecordId());


                        }
                        log.info("已更新基本信息");





                    }








//






                }
                else{
                    throw new IllegalArgumentException("{error:该机构不存在交易对手}");
                }

            }else{
                throw new IllegalArgumentException("{error:该机构不存在");
            }


        }catch (Exception e){
            log.info("error is " + e);
        }

    }
        }
