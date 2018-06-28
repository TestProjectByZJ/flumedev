package com.emt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emt.dao.LogHandleMapper;
import com.emt.vo.LogHandle;

@Service
public class LogHandleService{
	
	@Autowired
	private LogHandleMapper logHandleMapper;
	
	public int inserLogHandleInfo(LogHandle logHandle) {
		
		return logHandleMapper.insertLogHandInfo(logHandle);
		
	}
	public int updateLogHandleInfo(LogHandle logHandle) {
		
		return logHandleMapper.updateLogHandleInfo(logHandle);
		
	}
	
}
