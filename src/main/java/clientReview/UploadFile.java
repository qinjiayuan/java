package clientReview;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
public class UploadFile {
    static Logger log = LoggerFactory.getLogger(UploadFile.class);

    public  List<String> gets3filed() {



            String url = "http://10.2.145.216:9090/clientreview/file/upload";
            CloseableHttpClient httpClient = HttpClients.createDefault();
            List<String> s3id = new ArrayList<>();
            try{

                HttpPost httpPost = new HttpPost(url);
                File file = new File("src/main/intefacation/20220318144757.png");
                log.info("file :"+file.getName());
                if(!file.exists()){
                    log.info("File is no exists");
                }else{
                    log.info("File is exisits");
                }
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("fileBelong","主体/管理人文件"));
                nameValuePairs.add(new BasicNameValuePair("productName","主体/管理人文件"));

                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.setCharset(Consts.UTF_8);
                for(NameValuePair nameValuePair:nameValuePairs){
                    multipartEntityBuilder.addTextBody(nameValuePair.getName(),nameValuePair.getValue(),ContentType.create("text/plain", Consts.UTF_8));
                }
                for(int i = 0; i < 13 ; i++) {
                    multipartEntityBuilder.addBinaryBody("files", Files.newInputStream(file.toPath()), ContentType.MULTIPART_FORM_DATA, file.getName());
                }
                httpPost.setHeader("name","sunbin");
                HttpEntity httpEntity = multipartEntityBuilder.build();
                httpPost.setEntity(httpEntity);

                CloseableHttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String msg1 = EntityUtils
                        .toString(response.getEntity());
                log.info(msg1);

                JSONObject jsonObject = new JSONObject(msg1);
                String content = String.valueOf(jsonObject.getJSONArray("data"));
                //该方法是将一个字符串转换成一个json数组对象
                JSONArray arr = JSONArray.parseArray(content);


                for(int i = 0 ; i< arr.size();i++){
                    com.alibaba.fastjson.JSONObject obj = arr.getJSONObject(i);
                    String s3fileid = obj.getString("s3FileId");
                    s3id.add(s3fileid);
                }
                log.info(s3id.toString());
                return s3id;


            } catch (Exception e) {
                throw new RuntimeException(e);
            }finally{
                if(httpClient !=null){
                    httpClient.getConnectionManager().shutdown();
                }

            }
        }


    }






