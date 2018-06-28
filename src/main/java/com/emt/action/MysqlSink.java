package com.emt.action;

import com.emt.service.AlarmService;
import com.emt.service.LogHandleService;
import com.emt.util.logInfoHandler;
import com.emt.vo.Alarm;
import com.emt.vo.LogHandle;
import com.google.common.base.Throwables;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

 
public class MysqlSink extends AbstractSink implements Configurable {
 
    private Logger LOG = LoggerFactory.getLogger(MysqlSink.class);
    
    @Autowired
	private LogHandleService logHandleService ;
	
	@Autowired
	private AlarmService alarmService;
	
	private ApplicationContext context;
	
	private logInfoHandler loginfohandler;
	
    public void configure(Context context) {
    }
 
    @Override
    public void start() {
        super.start();
        try {
        	 context =new FileSystemXmlApplicationContext("classpath*:config/applicationContext.xml");
             logHandleService = (LogHandleService) context.getBean("logHandleService");
             alarmService = (AlarmService) context.getBean("alarmService");
             loginfohandler = new logInfoHandler();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
 
    }
 
    @Override
    public void stop() {
        super.stop();
    }
 
    public Status process() throws EventDeliveryException {
        Status result = Status.READY;
        Channel channel = getChannel();
        Transaction transaction = channel.getTransaction();
        Event event;
        String info = null;
        transaction.begin();
        try {
            event = channel.take();
            if (event != null) {
            	info = new String(event.getBody());
            } else {
                result = Status.BACKOFF;
            }
            if (info!=null&&info!="") {
            	if(info.contains("OperationLog:")||info.contains("START")||info.contains("END")){
                	if(info.contains("START")){
                		//保存开始时间
                		LogHandle handle = loginfohandler.getStartTime(info);
                		logHandleService.inserLogHandleInfo(handle);
                	}else if(info.contains("END")){
                		//保存结束时间
                		LogHandle handle = loginfohandler.getEndTime(info);
                		logHandleService.updateLogHandleInfo(handle);
                	}else if(info.contains("ERROR")||info.contains("WARN")){
                		Alarm alarm =loginfohandler.getAlarmInfo(info);
                		alarmService.insertAlarmInfo(alarm);
                		//统计发生次数+更新
                		int happenTimes = alarmService.getHappenTimes(alarm);
                		alarm.setAlarmNum(happenTimes+"");
                		alarmService.updateHappenTimes(alarm);
                		loginfohandler.setHttpConn("http://192.168.25.1:18100/websocket/pushMsg", "");
                	}
                }else if(info.contains("Exception")||info.contains("exception")){
                	//TODO exception告警监控
                	Alarm alarm = loginfohandler.getAlarmException(info);
                	alarmService.insertAlarmInfo(alarm);
                	loginfohandler.setHttpConn("http://192.168.25.1:18100/websocket/pushMsg", "");
                }
            }
            transaction.commit();
        } catch (Throwable e) {
            try {
                transaction.rollback();
            } catch (Exception e2) {
                LOG.error("Exception in rollback. Rollback might not have been" + "successful.", e2);
            }
            LOG.error("Failed to commit transaction." + "Transaction rolled back.", e);
            Throwables.propagate(e);
        } finally {
            transaction.close();
        }
 
        return result;
    }
    
}