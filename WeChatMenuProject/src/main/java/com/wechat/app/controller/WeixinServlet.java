package com.wechat.app.controller;
 
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechat.app.util.SignUtil;
/**
 * 微信消息的接收和响应
 */
@Controller
public class WeixinServlet  {
 
	/**
     * 确认消息来自微信服务器
     * @param request
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    String check(HttpServletRequest request) {

        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            System.out.print("echostr="+echostr);
            return echostr;
        }else{
            return "fail";
        }
    }

 
    /**
     * 接收并处理微信客户端发送的请求
     */
	/*@RequestMapping("/fromWechat")
    public void fromWechat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/xml;charset=utf-8");
        PrintWriter out = response.getWriter();
        try {
            Map<String, String> map = MessageUtil.xmlToMap(request);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
             
            String message = null;
            if ("text".equals(msgType)) {                // 对文本消息进行处理
                TextMeaasge text = new TextMeaasge();
                text.setFromUserName(toUserName);         // 发送和回复是反向的
                text.setToUserName(fromUserName);
                text.setMsgType("text");
                text.setCreateTime(new Date().getTime());
                text.setContent("你发送的消息是：" + content);
                message = MessageUtil.textMessageToXML(text);
                System.out.println(message);            
            }
            out.print(message);                            // 将回应发送给微信服务器
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally{
            out.close();
        }
    }*/
 
}