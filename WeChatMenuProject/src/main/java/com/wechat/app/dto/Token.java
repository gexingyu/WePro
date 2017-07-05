package com.wechat.app.dto;

import java.io.UnsupportedEncodingException;

import com.wechat.app.util.CommonUtil;

/**
* 类名: Token </br>
* 描述:  凭证  </br>
* 开发人员： souvc </br>
* 创建时间：  2015-11-27 </br>
* 发布版本：V1.0  </br>
 */
public class Token {
    // 接口访问凭证
    private String accessToken;
    // 凭证有效期，单位：秒
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args) {
    	String source="http://gexingyu002.ngrok.cc/gotoIndex";
        System.out.println(CommonUtil.urlEncodeUTF8(source));
	}
}
