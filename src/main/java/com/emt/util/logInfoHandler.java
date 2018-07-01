package com.emt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.emt.vo.LogHandle;

  
  
public class logInfoHandler {
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
    public LogHandle getStartMsg(String msg) {
    	LogHandle handle = new LogHandle();
    	//根据START将日志默认输出和打印日志分开
		String[] logs = msg.split("START");
		//处理前默认输出日志，截取时间，项目名，线程ID，方法名信息
		String[] topHalfLog = logs[0].split("\\[");
		String time = topHalfLog[0].substring(0, 23);
		String projectName = topHalfLog[1].replace("] ","");
		String threadName = topHalfLog[2].substring(0,32);
		String className = topHalfLog[2].substring(34,topHalfLog[2].length()).replace(" ","").replace(":","");
		//处理后半段信息，截取方法名
		String methodName = logs[1].split("\\|")[1];
    	try {
			handle.setLhStart(simpleDateFormat.parse(time).getTime());
		} catch (ParseException e) {
			handle.setLhStart(0);
		}
    	handle.setLhProject(projectName);
    	handle.setLhId(threadName);
    	handle.setLhClass(className);
    	handle.setLhMethod(methodName);
    	handle.setLhSuccess("0");
		return handle;
	}
    
    public LogHandle getEndMsg(String msg) {
    	LogHandle handle = new LogHandle();
		//处理前默认输出日志，截取时间，线程ID
		String[] topHalfLog = msg.split("\\[");
		String time = topHalfLog[0].substring(0, 23);
		String threadName = topHalfLog[2].substring(0,32);
		handle.setLhId(threadName);
		try {
			handle.setLhEnd(simpleDateFormat.parse(time).getTime());
		} catch (ParseException e) {
			handle.setLhEnd(0);
		}
		return handle;
	}
    
    public LogHandle getErrorMsg(String msg){
    	LogHandle handle = new LogHandle();
    	String[] logs = msg.split("OperationLog");
		//处理前默认输出日志，截取时间，线程ID
		String[] topHalfLog = logs[0].split("\\[");
		String time = topHalfLog[0].substring(0, 23);
		try {
			handle.setLhErrorTime(simpleDateFormat.parse(time).getTime());
		} catch (ParseException e) {
			handle.setLhErrorTime(0);
		}
		String threadName = null;
		String className = null;
		try {
			threadName = topHalfLog[2].substring(0,32);
			className = topHalfLog[2].substring(34,topHalfLog[2].length()).replace(" ","").replace(":","");
		} catch (Exception e) {
			className = null;
		}
		//处理后半段信息，截取错误码，错误信息，请求数据
		String[] bottomHalfLog = logs[1].split("\\|");
		handle.setLhId(threadName);
		handle.setLhClass(className);
		handle.setLhErrorCode(bottomHalfLog[1]);
		handle.setLhErrorMsg(bottomHalfLog[2]);
		handle.setLhRequestMsg(bottomHalfLog[3]);
		handle.setLhErrorState("0");
		handle.setLhSuccess("1");
		handle.setLhErrorName("业务异常");
		return handle;
    }
    
    public LogHandle getExceptionMsg(String msg){
    	LogHandle handle = new LogHandle();
    	handle.setInserErrorMsgFlag(0);
		try {
			Matcher matcher = Pattern.compile(":").matcher(msg);
			int index = 23;
			while(matcher.find()) {
				if (matcher.start()>23) {
					index = matcher.start();
					break;
				}
			}
			// Exception前半段信息，截取时间，线程ID，
			String topHalfLog = msg.substring(0, index);
			String[] errorHalfLog = topHalfLog.split("\\[");
			String time = errorHalfLog[0].substring(0, 23);
			String threadName = errorHalfLog[2].split("\\]")[0];
			try {
				handle.setLhErrorTime(simpleDateFormat.parse(time).getTime());
			} catch (ParseException e) {
				handle.setLhErrorTime(0);
			}
			if(threadName.length()<30){
				threadName = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
				String className = errorHalfLog[3].replace("]", "").replace(" ", "").replace(":", "");
				handle.setLhClass(className);
				handle.setInserErrorMsgFlag(1);
			}
			handle.setLhId(threadName);
			String errorMsg = msg.substring(index + 1, msg.length());
			handle.setLhErrorMsg(errorMsg);
		} catch (Exception e) {
			handle.setInserErrorMsgFlag(2);
			return handle;
		}
		handle.setLhErrorState("0");
		handle.setLhSuccess("2");
		handle.setLhErrorName("系统异常");
		return handle;
    }
    
//    public Alarm getAlarmInfo(String msg) {
//    	Alarm alarm = new Alarm();
//    	String str[] = msg.split("OperationLog:");
//    	alarm.setAlarmId(str[1].split("\\|")[0]);
//    	alarm.setAlarmName("业务异常");					//告警名称
//    	alarm.setAlarmStatus("0");						//告警处理状态，0：未处理
//    	alarm.setAlarmTime(str[0].substring(0,23));		//告警触发时间
//    	alarm.setAlarmEve("内网");							//告警环境
//    	String pro = (str[0].split("]")[1]).replace(" ", ""); 
//    	alarm.setAlarmNum("1");
//    	alarm.setAlarmProject(pro.split("\\.")[2]);		//项目名称
//    	alarm.setAlarmType(str[0].contains("ERROR")?"ERROR":str[0].contains("WARN")?"WARN":"");//告警类型
//    	alarm.setAlarmLevel(alarm.getAlarmType().equals("WARN")?"1":"2");//告警等级
//    	alarm.setAlarmMsg(str[1].split("\\|")[2]);
//		return alarm;
//    	
//	}
//    
//    public Alarm getAlarmException(String msg) {
//    	Alarm alarm = new Alarm();
//    	alarm.setAlarmTime(msg.substring(0,23));		//告警触发时间
//    	alarm.setAlarmId("1111");
//    	alarm.setAlarmName("Exception");
//    	alarm.setAlarmMsg(msg);
//    	System.out.println(alarm.getAlarmTime());
//    	System.out.println(alarm.getAlarmId());
//    	System.out.println(alarm.getAlarmName());
//    	System.out.println(alarm.getAlarmMsg());
//		return alarm;
//	}	 
    
    public void setHttpConn(String url,String sendStr){
    	BufferedReader reader = null;  
    	String strMessage = "";  
	    try {
	        StringBuffer buffer = new StringBuffer();  
	        // 接报文的地址  
	        URL uploadServlet = new URL(url);  
	        HttpURLConnection servletConnection = (HttpURLConnection) uploadServlet.openConnection();  
	        // 设置连接参数  
	        servletConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");  
	        servletConnection.setRequestMethod("POST");  
	        servletConnection.setDoOutput(true);  
	        servletConnection.setDoInput(true);  
	        servletConnection.setAllowUserInteraction(true); 
	        // 开启流，写入XML数据  
	        OutputStream output = servletConnection.getOutputStream();  
	        output.write(sendStr.toString().getBytes());  
	        output.flush();  
	        output.close();  
	        // 获取返回的数据  
	        InputStream inputStream = servletConnection.getInputStream();  
	        reader = new BufferedReader(new InputStreamReader(inputStream));  
	        while ((strMessage = reader.readLine()) != null) {  
	           buffer.append(strMessage);  
	        }  
		} catch (Exception e) {
		}finally{
			if (reader != null) {  
		           try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
		   }  
		}
    }

    
}  