package com.emt.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		
		String msg = "2018-06-28 13:59:45.502  INFO  [wechat] [83F38F88162E411B89309B16C00CCF98] com.emt.monitor.aop.LogAspect                      : START|getInfo|(后面字段无要求，按照个人需求添加)请求开始XXXX.......... ";
		String s = "2018-06-28 13:59:45.502";
		System.out.println(msg.substring(0,23));
		System.out.println(msg.split("["));
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        long time = simpleDateFormat.parse(msg.substring(0,23)).getTime();
//        System.out.println(time);
		
	}
	
	
}
