package com.wechat.app.util;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

public class SignUtil2 {
	 
    // 与接口配置信息中的Token要一致
    private static String token = "souvcweixin";
 
    public static boolean checkSignature(String signature, String timestamp,String nonce) {
        // 1.将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[] { token, timestamp, nonce };
        Arrays.sort(arr);
 
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        System.out.println(content.toString());
 
        String tmpStr = DigestUtils.sha1Hex(content.toString());
        System.out.println("tmpStr="+tmpStr);
 
        // 3.将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature) : false;
    }
 
    public static void main(String[] args) {
          /*
        signature=95205b721b95f72a1b82b6551c3a40e658e18ceb
        timestamp=1489498977
        nonce=1125364772
        */
        SignUtil2.checkSignature("95205b721b95f72a1b82b6551c3a40e658e18ceb","1489498977","1125364772");
    }
 
}
 