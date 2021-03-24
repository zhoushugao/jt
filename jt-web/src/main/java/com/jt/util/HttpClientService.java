package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {

	//通过spring容器注入对象
	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private RequestConfig requestConfig;


	/**
	 *    构建get请求工具类
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * 
	 * 	目的:	
	 * 	  利用用户传递url,帮助用户发起http get请求.
	 *  
	 *      是否有参数:
	 *  	没有参数:     http://manage.jt.com/xxx
	 *  	    有参数:    http://manage.jt.com?key1=value1&key2=value2....
	 *      利用httpClient发起请求返回结果即可.    
	 */
	public String doGet(String url,Map<String,String> params,String charset) {
		//1.检验字符集编码格式
		if(StringUtils.isEmpty(charset)) {
			//表示用户没有指定字符类型 指定默认值
			charset = "UTF-8";
		}

		//2.校验map集合是否为null
		//Set<Map.Entry<K, V>> entrySet();
		if(params != null) {
			url = url + "?"; //http://manage.jt.com?
			//有参数:  ?key1=value1&key2=value2....
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				url = url+key+"="+value+"&";
			}
			//经过循环之后多&符
			url = url.substring(0,url.length()-1);
		}

		//定义请求对象
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);	//设定超时时间
		String result = null;
		try {
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200) {

				result = EntityUtils.toString(httpResponse.getEntity(),charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return result;
	}

	public String doGet(String url) {

		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params) {

		return doGet(url, params, null);
	}


	//实现httpClient POST提交
	public String doPost(String url,Map<String,String> params,String charset){
		String result = null;

		//1.定义请求类型
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);  	//定义超时时间

		//2.判断字符集是否为null
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}

		//3.判断用户是否传递参数
		if(params !=null){
			//3.2准备List集合信息
			List<NameValuePair> parameters = new ArrayList<>();

			//3.3将数据封装到List集合中
			for (Map.Entry<String,String> entry : params.entrySet()) {

				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			//3.1模拟表单提交
			try {
				UrlEncodedFormEntity formEntity = 
						new UrlEncodedFormEntity(parameters,charset); //采用u8编码
				//3.4将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}

		//4.发送请求
		try {
			CloseableHttpResponse response = 
					httpClient.execute(post);

			//4.1判断返回值状态
			if(response.getStatusLine().getStatusCode() == 200) {

				//4.2表示请求成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	public String doPost(String url){

		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){

		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){

		return doPost(url, null, charset);
	}

}
