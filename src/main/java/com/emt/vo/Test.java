package com.emt.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) throws Exception {
		/*
		 * 研究分析Exception 
		 * 1:业务异常{
		 * 		1：空thredId，空项目名，有时间
		 * 		2：有id,有时间
		 * }
		 * 2：系统异常{
		 * 		1：无时间打印，无ID，项目名，信息归类到下一条信息，（启动虚拟机，进行异常模拟）内存栈溢出
		 * }
		 */		
		getIndex();
	}
	
	public static void getIndex(){
		 String msg = "2018-06-28 13:16:43.157 INFO [wechat] [958161E3B0234105BA09918BA1AA656C] com.emt.monitor.aop.LogAspect : ss:ll";
			Matcher matcher = Pattern.compile(":").matcher(msg);
			int index = 20;
			while(matcher.find()) {
				if(matcher.start()>index){
					index = matcher.start();
					System.out.println(index);
					break;
				}
				
			}
			// Exception前半段信息，截取时间，线程ID，
			String topHalfLog = msg.substring(0, index);
			String[] errorHalfLog = topHalfLog.split("\\[");
			String time = errorHalfLog[0].substring(0, 23);
			String threadName = errorHalfLog[2].split("\\]")[0];
			String errorMsg = msg.substring(index + 1, msg.length());
		 System.out.println(time);
		 System.out.println(threadName);
		 System.out.println(errorMsg);
	}
	
	public static void GetErrorMsg(){
		String msg = "2018-06-28 13:00:58.244 INFO [wechat] [CD9B5D8817824C0786A00D54F40CCC02] com.emt.monitor.controller.CollectController 	: OperationLog|EQ0001|必填信息不能为空|message+X ";
		String[] logs = msg.split("OperationLog");
		//处理前默认输出日志，截取时间，线程ID
		String[] topHalfLog = logs[0].split("\\[");
		String time = topHalfLog[0].substring(0, 23);
		String threadName = topHalfLog[2].substring(0,32);
		//处理后半段信息，截取错误码，错误信息，请求数据
		String[] bottomHalfLog = logs[1].split("\\|");
		for (int i = 0; i < bottomHalfLog.length; i++) {
			System.out.println(bottomHalfLog[i]);
		}
		String errorCode = bottomHalfLog[1];
		String errorMsg  = bottomHalfLog[2];
		String requestMsg = bottomHalfLog[3];
		System.out.println(time);
		System.out.println(threadName);
	}
	
	public static void End(){
		String msg = "2018-06-28 13:00:58.257 INFO [wechat] [CD9B5D8817824C0786A00D54F40CCC02] com.emt.monitor.aop.LogAspect 					: END|getInfo|(后面字段无要求，按照个人需求添加)请求结束XXXXX.......... ";
		//处理前默认输出日志，截取时间，线程ID
		String[] topHalfLog = msg.split("\\[");
		String time = topHalfLog[0].substring(0, 23);
		String threadName = topHalfLog[2].substring(0,32);
		System.out.println(time);
		System.out.println(threadName);
		
	}
	
	public static void Start(){
		String msg = "2018-06-28 13:59:45.502  INFO  [wechat] [83F38F88162E411B89309B16C00CCF98] com.emt.monitor.aop.LogAspect                      : START|getInfo|(后面字段无要求，按照个人需求添加)请求开始XXXX.......... ";
		//根据START将日志默认输出和打印日志分开
		String[] logs = msg.split("START");
		//处理前默认输出日志，截取时间，项目名，线程ID，方法名信息
		String[] topHalfLog = logs[0].split("\\[");
		String time = topHalfLog[0].substring(0, 23);
		String projectName = topHalfLog[1].replace("] ","");
		String thredName = topHalfLog[2].substring(0,32);
		String className = topHalfLog[2].substring(34,topHalfLog[2].length()).replace(" ","").replace(":","");
		//处理后半段信息，截取方法名
		String methodName = logs[1].split("\\|")[1];
		System.out.println(time);
		System.out.println(projectName);
		System.out.println(thredName);
		System.out.println(className);
		System.out.println(methodName);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        long time = simpleDateFormat.parse(msg.substring(0,23)).getTime();
//        System.out.println(time);
	}
	
}
