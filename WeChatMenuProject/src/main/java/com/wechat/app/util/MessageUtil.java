/*package com.wechat.app.util;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.wechat.app.dto.TextMeaasge;
 
*//**
 * 实现消息的格式转换(Map类型和XML的互转)
 *//*
public class MessageUtil {
 
    *//**
     * 将XML转换成Map集合
     *//*
    public static Map<String, String>xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
         
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();            // 使用dom4j解析xml
        InputStream ins = request.getInputStream(); // 从request中获取输入流
        Document doc = reader.read(ins);
         
        Element root = doc.getRootElement();         // 获取根元素
        List<Element> list = root.elements();        // 获取所有节点
         
        for (Element e : list) {
            map.put(e.getName(), e.getText()); 
            System.out.println(e.getName() + "--->" + e.getText());
        }
        ins.close();
        return map;
    }
     
    *//**
     * 将文本消息对象转换成XML
     *//*
    public static String textMessageToXML(TextMeaasge textMessage){
         
        XStream xstream = new XStream();              // 使用XStream将实体类的实例转换成xml格式    
        xstream.alias("xml", textMessage.getClass()); // 将xml的默认根节点替换成“xml”
        return xstream.toXML(textMessage);
         
    }
     
}*/