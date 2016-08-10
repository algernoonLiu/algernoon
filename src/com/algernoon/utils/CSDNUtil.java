package com.algernoon.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CSDNUtil implements Runnable{

	public static void doGet(String url) {
		 //创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        try {
        	//用get方法发送http请求
        	HttpGet get = new HttpGet(url);
        	get.setHeader("Host", "blog.csdn.net");
        	get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
        	get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        	get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        	get.setHeader("Accept-Encoding", "gzip, deflate");
        	get.setHeader("Connection", "keep-alive");
        	CloseableHttpResponse httpResponse = null;
        	//发送get请求
        	httpResponse = httpClient.execute(get);
        	
        	try {
        		//response实体
        		HttpEntity entity = httpResponse.getEntity();
        		if (null != entity){
        			System.out.println("响应状态码:"+ httpResponse.getStatusLine());
        			System.out.println("-------------------------------------------------");                    
//        			System.out.println("响应内容:" + EntityUtils.toString(entity));
//        			System.out.println("-------------------------------------------------");                    
        		}
        	} finally {
        		httpResponse.close();
        	}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载资源路径
	 * @param location
	 * @return
	 */
	private static List<String> getUrlList(String location) {
		List<String> urlList = new ArrayList<>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		
		try {
			String str = "";
			fis = new FileInputStream(location);
			// 从文件系统中的某个文件中获取字节
		    isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
		    br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				urlList.add(str);
		    }
		} catch (Exception e){ 
			e.printStackTrace();
		}finally {
			try {
				br.close();
				isr.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return urlList;
	}

	@Override
	public void run() {
		List<String> urlList = getUrlList("E:\\myworkspace\\algernoon\\resource\\csdn-url.txt");
		int urlSize = urlList.size(); // url数量
		while(true){
			Random random = new Random();
			int nextInt = random.nextInt(urlSize); // 产生0~urlSize的随机数（不包括urlSize）
			System.out.println("url:  " + urlList.get(nextInt));
			doGet(urlList.get(nextInt));
			try {
				int sleep  = new Random().nextInt(1000) * 10;
				System.out.println("休息"+sleep/1000+"秒");
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Thread(new CSDNUtil()).start();
	}
}
