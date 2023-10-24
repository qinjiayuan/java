import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Testupload {
    static Logger Log = LoggerFactory.getLogger(Testupload.class);
    public static String post(String url, Map<String, String> headerMap, Map<String, String> params, Map<String, String> cnParams, File file) {
        Log.info("*****HTTP POST****\n url:[{}],headerParams:[{}], params:[{}]***,cnParams:[{}]", new Object[]{url, headerMap, params, cnParams});
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 兼容性模式

            if (null != headerMap && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            if (null != params && params.size() > 0) {//组装参数
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addTextBody(entry.getKey(), entry.getValue());
                }
            }
            // 解决中文参数乱码
            if (null != cnParams && cnParams.size() > 0) {
                for (Map.Entry<String, String> entry : cnParams.entrySet()) {
                    builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create("text/plain", Consts.UTF_8)));
                }
            }
            if (null != file) {
                builder.addBinaryBody("file", new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, file.getName());
            }
            builder.setCharset(Charset.forName("UTF-8"));
            post.setEntity(builder.build());
            HttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();
            if (null == responseEntity) {
                throw new RuntimeException("post方法返回结果对象为空");
            }
            String resp = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            if (resp.equals("")||resp.isEmpty()) {
                throw new RuntimeException("post方法返回结果对象为空");
            }
            return resp;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("调用post方法发生异常:" + e);
        } finally {
            try {
                if (null != httpClient) {
                    httpClient.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("[post]方法关闭HTTP资源失败！");
            }
        }
    }
}