package com.wechat.app.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.swetake.util.Qrcode;




@Controller
public class indexController {
	private Logger logger = Logger.getLogger(indexController.class);
	@RequestMapping("/index")
	public String index(HttpServletRequest request,HttpServletResponse response)  throws IOException{
		return "index";
	}
	@RequestMapping("/erweima")
	public void erweima(HttpServletRequest request,HttpServletResponse response)  throws IOException{
		//String result= this.doGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx26ee1c05ed21b642&secret=d7f2fd2a58993f69f9c786e784a2053e", null);
		//request.setAttribute("index", result);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			OutputStream outputStream = produce();
			ByteArrayOutputStream byt= (ByteArrayOutputStream) outputStream;
			byte[] bt = byt.toByteArray();
			os.write(bt);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);;
		}finally{
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
				boolean[][] b= qrcode.calQrcode(bstr);
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
	 private static OutputStream  produce() throws Exception{
	        String encoderContent = "http://www.baidu.com";  
	        BufferedImage bi =  createQrCodeBuff(encoderContent,139, 139);
	        OutputStream outputStream = new ByteArrayOutputStream();
	        ImageIO.write(bi, "png", outputStream);
	        System.out.println("========decoder success!!!"); 
	        return outputStream;
	    }
}
