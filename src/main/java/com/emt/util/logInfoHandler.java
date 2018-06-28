package com.emt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import com.emt.vo.Alarm;
import com.emt.vo.LogHandle;

  
  
public class logInfoHandler {
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
    public LogHandle getStartTime(String msg) {
    	LogHandle handle = new LogHandle();
    	long time = simpleDateFormat.parse(msg.substring(0,23)).getTime();
    	handle.setLhStart(time);
    	String log = msg.split("OperationLog:")[1];
    	handle.setId(log.split("\\|")[0]);
		return handle;
	}
    
    public LogHandle getEndTime(String msg) {
    	LogHandle handle = new LogHandle();
    	handle.setEndTime(msg.substring(0,23));
		return handle;
	}
    
    public Alarm getAlarmInfo(String msg) {
    	Alarm alarm = new Alarm();
    	String str[] = msg.split("OperationLog:");
    	alarm.setAlarmId(str[1].split("\\|")[0]);
    	alarm.setAlarmName("业务异常");					//告警名称
    	alarm.setAlarmStatus("0");						//告警处理状态，0：未处理
    	alarm.setAlarmTime(str[0].substring(0,23));		//告警触发时间
    	alarm.setAlarmEve("内网");							//告警环境
    	String pro = (str[0].split("]")[1]).replace(" ", ""); 
    	alarm.setAlarmNum("1");
    	alarm.setAlarmProject(pro.split("\\.")[2]);		//项目名称
    	alarm.setAlarmType(str[0].contains("ERROR")?"ERROR":str[0].contains("WARN")?"WARN":"");//告警类型
    	alarm.setAlarmLevel(alarm.getAlarmType().equals("WARN")?"1":"2");//告警等级
    	alarm.setAlarmMsg(str[1].split("\\|")[2]);
		return alarm;
    	
	}
    
    public Alarm getAlarmException(String msg) {
    	Alarm alarm = new Alarm();
    	alarm.setAlarmTime(msg.substring(0,23));		//告警触发时间
    	alarm.setAlarmId("1111");
    	alarm.setAlarmName("Exception");
    	alarm.setAlarmMsg(msg);
    	System.out.println(alarm.getAlarmTime());
    	System.out.println(alarm.getAlarmId());
    	System.out.println(alarm.getAlarmName());
    	System.out.println(alarm.getAlarmMsg());
		return alarm;
	}	 
    
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