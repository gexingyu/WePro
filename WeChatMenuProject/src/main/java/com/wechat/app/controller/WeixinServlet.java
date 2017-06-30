package com.wechat.app.controller;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.wechat.app.dto.TextMeaasge;
import com.wechat.app.util.CheckUtil;
import com.wechat.app.util.MessageUtil;
/**
 * 微信消息的接收和响应
 */
public class WeixinServlet extends HttpServlet {
 
    /**
     * 接收微信服务器发送的4个参数并返回echostr
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        // 接收微信服务器以Get请求发送的4个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
         
        PrintWriter out = response.getWriter();
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);        // 校验通过，原样返回echostr参数内容
        }
    }
 
    /**
     * 接收并处理微信客户端发送的请求
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
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
    }
 
}