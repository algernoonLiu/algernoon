package com.algernoon.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    /**
     * 通过GET方式发起http请求
     */
    public static void doGet(String url){
        //创建默认的httpClient实例
        CloseableHttpClient httpClient = getHttpClient();
        try {
            //用get方法发送http请求
            HttpGet get = new HttpGet(url);
            CloseableHttpResponse httpResponse = null;
            //发送get请求
            httpResponse = httpClient.execute(get);
            try{
                //response实体
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("响应状态码:"+ httpResponse.getStatusLine());
                    System.out.println("-------------------------------------------------");
                    System.out.println("响应内容:" + EntityUtils.toString(entity,"utf-8"));
                    System.out.println("-------------------------------------------------");  
                }
            }
            finally{
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                closeHttpClient(httpClient);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
     
    
    /**
     * POST方式发起http请求
     */
    public static void doPost(String url, Map<String, Object> paramsMap){
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost post = new HttpPost(url);          //这里用上本机的某个工程做测试
            //创建参数列表
            List<NameValuePair> list = handleRequestParams(paramsMap);
            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
            post.setEntity(uefEntity);
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try{
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity){
                    System.out.println("-------------------------------------------------------");
                    System.out.println(EntityUtils.toString(entity));
                    System.out.println("-------------------------------------------------------");
                }
            } finally{
                httpResponse.close();
            }
             
        } catch( UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try{
                closeHttpClient(httpClient);                
            } catch(Exception e){
                e.printStackTrace();
            }
        }
         
    }
	
	private static CloseableHttpClient getHttpClient(){
		return HttpClients.createDefault();
	}
	     
    private static void closeHttpClient(CloseableHttpClient client) throws IOException{
        if (client != null){
            client.close();
        }
    }
    
    private static List<NameValuePair> handleRequestParams(Map<String, Object> requestParams){
    	List<NameValuePair> list = new ArrayList<NameValuePair>();
    	if (requestParams == null) {
	    return list;
	}
    	Iterator<Entry<String, Object>> it = requestParams.entrySet().iterator();
    	while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			list.add(new BasicNameValuePair(key, value != null ? value.toString() : ""));
		}
        return list;
    }
    
    public static void main(String[] args) {
	doPost("http://106.38.38.178:8000/api/sync/getTmsChanels", null);
    }
    
}
