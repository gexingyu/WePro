package com.wechat.app.controller;
 
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.*;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechat.app.dto.SNSUserInfo;
import com.wechat.app.dto.WeiXinOauth2Token;
import com.wechat.app.util.CommonUtil;
import com.wechat.app.util.MyX509TrustManager;
import com.wechat.app.util.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 微信消息的接收和响应
 */
@Controller
public class WeixinServlet  {
	private static Logger log = Logger.getLogger(WeixinServlet.class);
	/**
     * 确认消息来自微信服务器
     * @param request
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    String check(HttpServletRequest request) {
    	try {
			
    		// 微信加密签名
    		String signature = request.getParameter("signature");
    		// 时间戳
    		String timestamp = request.getParameter("timestamp");
    		// 随机数
    		String nonce = request.getParameter("nonce");
    		// 随机字符串
    		String echostr = request.getParameter("echostr");
    		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
    		//String openid = request.getParameter("openId");
    		//System.out.println(openid);
    		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
    			System.out.print("echostr="+echostr);
    			return echostr;
    		}else{
    			return "fail";
    		}
		} catch (Exception e) {
			return "catch faile";
		}
    }

 
    /**
     * 获取网页授权凭证
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeiXinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
    	WeiXinOauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeiXinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInt("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.info("获取网页授权凭证失败 errcode:{} errmsg:{}"+ errorCode+ errorMsg);
            }
        }
        return wat;
    }
    /**
     * 发送https请求
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时：{}", ce);
        } catch (Exception e) {
            log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }
    /**
     * 通过网页授权获取用户信息
     * 
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
    @SuppressWarnings( { "deprecation", "unchecked" })
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(jsonObject.getInt("sex"));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户特权信息
                snsUserInfo.setPrivilegeList(JSONArray.toList(jsonObject.getJSONArray("privilege"), List.class));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取用户信息失败 errcode:{} errmsg:{}"+ errorCode+ errorMsg);
            }
        }
        return snsUserInfo;
    }
    @RequestMapping("gotoIndex")
    public String gotoIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ddddd");
    	request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String appid = "wx26ee1c05ed21b642";
		String appSecret = "d7f2fd2a58993f69f9c786e784a2053e";
        // 用户同意授权后，能获取到code
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        
        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            WeiXinOauth2Token weixinOauth2Token = this.getOauth2AccessToken(appid, appSecret, code);
            // 网页授权接口访问凭证
            String accessToken = weixinOauth2Token.getAccessToken();
            // 用户标识
            String openId = weixinOauth2Token.getOpenId();
            // 获取用户信息
            SNSUserInfo snsUserInfo = this.getSNSUserInfo(accessToken, openId);
            System.out.println(snsUserInfo.toString());
            // 设置要传递的参数
            request.setAttribute("snsUserInfo", snsUserInfo);
            request.setAttribute("state", state);
        }
        // 跳转到index.jsp
        //request.getRequestDispatcher("index.jsp").forward(request, response);
        return "index";
    }
}