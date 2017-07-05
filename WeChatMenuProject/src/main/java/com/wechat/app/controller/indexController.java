package com.wechat.app.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.swetake.util.Qrcode;
import com.wechat.app.dto.WeixinUserInfo;
import com.wechat.app.util.CommonUtil;
import com.wechat.app.util.HttpRequestor;

import net.sf.json.JSONObject;

@Controller
public class indexController {
	private Logger log = Logger.getLogger(indexController.class);

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		return "index";
	}
	@RequestMapping("/getinfo")
	@ResponseBody
	public String getinfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String appid = "wx26ee1c05ed21b642";
		String appSecret = "d7f2fd2a58993f69f9c786e784a2053e";
		String url ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appSecret+"";  
		String backData=sendGet(url, "utf-8", 10000);
		//getUserInfo(backData,);
		
		
		return backData;
	}
	@RequestMapping("/getopenid")
	@ResponseBody
	public String getopenid(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		String directUrl = this.urlEncodeUTF8("http://gexingyu002.ngrok.cc/getinfo");
//		String appid = "wx26ee1c05ed21b642";
//		String appSecret = "d7f2fd2a58993f69f9c786e784a2053e";
//		String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?"
//				+ "appid="+appid+"&redirect_uri="+directUrl+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
//		
		String openid = request.getParameter("openId");
		return "";
	}
	@RequestMapping("/erweima")
	public void erweima(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// String result=
		// this.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx26ee1c05ed21b642&secret=d7f2fd2a58993f69f9c786e784a2053e",
		// null);
		// request.setAttribute("index", result);
		OutputStream os = null;
		//String directUrl = this.urlEncodeUTF8("http://gexingyu002.ngrok.cc/getinfo");
		String directUrl = "http://gexingyu002.ngrok.cc/check";
		String appid = "wx26ee1c05ed21b642";
		String appSecret = "d7f2fd2a58993f69f9c786e784a2053e";
		String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?"
				+ "appid="+appid+"&redirect_uri="+directUrl+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		System.out.println(requestUrl);
		try {
			os = response.getOutputStream();
			OutputStream outputStream = produce(requestUrl);
			ByteArrayOutputStream byt = (ByteArrayOutputStream) outputStream;
			byte[] bt = byt.toByteArray();
			os.write(bt);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			;
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	private static BufferedImage createQrCodeBuff(String url, int width, int height) throws Exception {
		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeErrorCorrect('M');
		qrcode.setQrcodeEncodeMode('B');
		qrcode.setQrcodeVersion(7);

		byte[] bstr = url.getBytes("UTF-8");
		BufferedImage bi = new BufferedImage(width, height, 1);
		Graphics2D g = bi.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		if ((bstr.length > 0) && (bstr.length < 123)) {
			boolean[][] b = qrcode.calQrcode(bstr);
			for (int i = 0; i < b.length; ++i) {
				for (int j = 0; j < b.length; ++j) {
					if (b[j][i]) {
						g.fillRect((int) (width / 139.0D * (j * 3 + 2)), (int) (height / 139.0D * (i * 3 + 2)),
								(int) (width / 139.0D * 3.0D), (int) (height / 139.0D * 3.0D));
					}
				}
			}
		}

		g.dispose();
		bi.flush();
		return bi;
	}

	private static OutputStream produce(String encoderContent) throws Exception {
		BufferedImage bi = createQrCodeBuff(encoderContent, 139, 139);
		OutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", outputStream);
		System.out.println("========decoder success!!!");
		return outputStream;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 *            接口访问凭证
	 * @param openId
	 *            用户标识
	 * @return WeixinUserInfo
	 */
	private WeixinUserInfo getUserInfo(String accessToken, String openId) {
		WeixinUserInfo weixinUserInfo = null;
		// 拼接请求地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		// 获取用户信息
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				weixinUserInfo = new WeixinUserInfo();
				// 用户的标识
				weixinUserInfo.setOpenId(jsonObject.getString("openid"));
				// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
				weixinUserInfo.setSubscribe(jsonObject.getInt("subscribe"));
				// 用户关注时间
				weixinUserInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
				// 昵称
				weixinUserInfo.setNickname(jsonObject.getString("nickname"));
				// 用户的性别（1是男性，2是女性，0是未知）
				weixinUserInfo.setSex(jsonObject.getInt("sex"));
				// 用户所在国家
				weixinUserInfo.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				weixinUserInfo.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				weixinUserInfo.setCity(jsonObject.getString("city"));
				// 用户的语言，简体中文为zh_CN
				weixinUserInfo.setLanguage(jsonObject.getString("language"));
				// 用户头像
				weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
			} catch (Exception e) {
				if (0 == weixinUserInfo.getSubscribe()) {
					log.info("用户{}已取消关注" + weixinUserInfo.getOpenId());
				} else {
					int errorCode = jsonObject.getInt("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					log.info("获取用户信息失败 errcode:{} errmsg:{}" + errorCode + errorMsg);
				}
			}
		}
		return weixinUserInfo;
	}

	public static String sendGet(String url, String charset, int timeout) {
		String result = "";
		try {
			URL u = new URL(url);
			try {
				URLConnection conn = u.openConnection();
				conn.connect();
				conn.setConnectTimeout(timeout);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
				String line = "";
				while ((line = in.readLine()) != null) {

					result = result + line;
				}
				in.close();
			} catch (IOException e) {
				return result;
			}
		} catch (MalformedURLException e) {
			return result;
		}
		return result;
	}
	public static String urlEncodeUTF8(String source){
        String result = source;
        try {
                result = URLEncoder.encode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
        return result;
}
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String code = request.getParameter("code");
		String appid = "wx26ee1c05ed21b642";
		String secret = "d7f2fd2a58993f69f9c786e784a2053e";

        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
        //第一次请求 获取access_token 和 openid
		//String requestUrl="asas";
		String  oppid = new HttpRequestor().doGet(requestUrl);
        JSONObject oppidObj =JSONObject.fromObject(oppid);
        String access_token = (String) oppidObj.get("access_token");
        String openid = (String) oppidObj.get("openid");
        String requestUrl2 = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        String userInfoStr = new HttpRequestor().doGet(requestUrl2);
        JSONObject wxUserInfo =JSONObject.fromObject(userInfoStr); 
}


}
