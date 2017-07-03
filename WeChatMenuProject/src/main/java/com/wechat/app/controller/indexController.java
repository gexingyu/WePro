package com.wechat.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wechat.app.util.SSLClient;
import com.wechat.app.util.TwoDimensionCode;

@Controller
public class indexController {
	@RequestMapping("/index")
	public String index(HttpServletRequest request,HttpServletResponse response){
		String result= this.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx26ee1c05ed21b642&secret=d7f2fd2a58993f69f9c786e784a2053e", null);
		request.setAttribute("index", result);
		String url = TwoDimensionCode.produce();
		request.setAttribute("url", url);
		return "index";
	}
	/** 
     * 发送get请求 
     * @param url       链接地址 
     * @param charset   字符编码，若为null则默认utf-8 
     * @return 
     */  
    public String doGet(String url,String charset){  
        if(null == charset){  
            charset = "utf-8";  
        }  
        HttpClient httpClient = null;  
        HttpGet httpGet= null;  
        String result = null;  
          
        try {  
            httpClient = new SSLClient();  
            httpGet = new HttpGet(url);  
            HttpResponse response = httpClient.execute(httpGet);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        return result;  
    }  

}
