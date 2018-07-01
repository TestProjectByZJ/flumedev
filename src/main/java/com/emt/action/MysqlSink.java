package com.emt.action;

import com.emt.service.LogHandleService;
import com.emt.util.logInfoHandler;
import com.emt.vo.LogHandle;
import com.google.common.base.Throwables;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

 
public class MysqlSink extends AbstractSink implements Configurable {
 
    @Autowired
	private LogHandleService logHandleService ;
    
    private Logger logger = LoggerFactory.getLogger(MysqlSink.class);
    
    private ApplicationContext context;
	
	private logInfoHandler loginfohandler;
	
	private static final int QUEUE_LENGTH = 10000 * 10;
    
    private BlockingQueue<String> Queue = new LinkedBlockingQueue<String>(QUEUE_LENGTH);
    
    private byte[] lock = new byte[0];

	
    public void configure(Context context) {
    }
    
    public MysqlSink() {
    	queueThread();
	}

	@Override
    public void start() {
        super.start();
        try {
        	 context =new FileSystemXmlApplicationContext("classpath*:config/applicationContext.xml");
             logHandleService = (LogHandleService) context.getBean("logHandleService");
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
            	
            	 try {
                     Queue.put(info);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                     transaction.commit();
                     logger.error("put quene error:" + e.getMessage());
                 }
                 synchronized (lock) {
                     lock.notifyAll();
                 }

            }
            transaction.commit();
        } catch (Throwable e) {
            try {
                transaction.rollback();
            } catch (Exception e2) {
            	logger.error("Exception in rollback. Rollback might not have been" + "successful.", e2);
            }
        	logger.error("Failed to commit transaction." + "Transaction rolled back.", e);
            transaction.commit();
            Throwables.propagate(e);
        } finally {
            transaction.close();
        }
 
        return result;
    }
    
    public void doInfo(String info){
    	try {
        	if(info.contains("OperationLog")||info.contains("START")||info.contains("END")){
            	if(info.contains("START")){
            		System.out.println("START-------->"+info);
            		//保存开始时间
            		LogHandle handle = loginfohandler.getStartMsg(info);
            		logHandleService.inserLogHandleInfo(handle);
            	}else if(info.contains("END")){
            		System.out.println("END---------->"+info);
            		//保存结束时间
            		LogHandle handle = loginfohandler.getEndMsg(info);
            		logHandleService.updateLogHandleInfo(handle);
            	}else if(info.contains("OperationLog")){
            		System.out.println("OperationLog->"+info);	
            		LogHandle handle = loginfohandler.getErrorMsg(info);
            		logHandleService.updateLogHandleInfo(handle);
//            		loginfohandler.setHttpConn("http://192.168.25.1:18100/websocket/pushMsg", "");
            	}
            }else if(info.contains("Exception")||info.contains("exception")){
            	System.out.println("Exception---->"+info);
            	LogHandle handle = loginfohandler.getExceptionMsg(info);
            	if(handle.getInserErrorMsgFlag()==1){
            		logHandleService.inserLogHandleInfo(handle);
            	}else if(handle.getInserErrorMsgFlag()==0){
            		logHandleService.updateLogHandleInfo(handle);
            	}
//            	loginfohandler.setHttpConn("http://192.168.25.1:18100/websocket/pushMsg", "");
            }
		} catch (Exception e) {
			
		}
    }
    
    
    private void queueThread() {
    	Runnable race1 = new Runnable() {  
    	    public void run() {  
    	    	synchronized (lock) {
                    try {
                        while (true) {
                    		if (Queue.isEmpty()) {
                                //System.out.println("队列消息处理完毕，进入BLOCKING");
                    			lock.wait();
                                continue;
                            }
                    		doInfo(Queue.poll());
                        }
                    } catch (InterruptedException e) {
                    	logger.error("队列error:" + e.getMessage());
                    }
                }
    	    }  
    	};
        new Thread(race1).start();
    }
    
    
}