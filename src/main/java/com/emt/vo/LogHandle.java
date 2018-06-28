package com.emt.vo;

public class LogHandle {
	
	private String lhId;
	
	private String lhProject;
	
	private String lhClass;
	
	private String lhMethod;
	
	private long   lhStart;
	
	private long   lhEnd;
	
	//业务请求错误标志 0:正常请求 1：业务错误请求（捕获）2:业务错误异常（抛出）3：系统异常（内存溢出）
	private String lhSuccess;		
	
	private long   lhErrorTime;		//业务错误时间
	
	private String lhErrorMsg;		//业务错误信息
	
	private String lhErrorCode;		//业务错误码
	
	private String lhRequestMsg;	//业务请求信息
	
	private String lhErrorState;	//异常处理标志

	public String getLhErrorState() {
		return lhErrorState;
	}

	public void setLhErrorState(String lhErrorState) {
		this.lhErrorState = lhErrorState;
	}

	public String getLhId() {
		return lhId;
	}

	public void setLhId(String lhId) {
		this.lhId = lhId;
	}

	public String getLhProject() {
		return lhProject;
	}

	public void setLhProject(String lhProject) {
		this.lhProject = lhProject;
	}

	public String getLhClass() {
		return lhClass;
	}

	public void setLhClass(String lhClass) {
		this.lhClass = lhClass;
	}

	public String getLhMethod() {
		return lhMethod;
	}

	public void setLhMethod(String lhMethod) {
		this.lhMethod = lhMethod;
	}

	public String getLhSuccess() {
		return lhSuccess;
	}

	public void setLhSuccess(String lhSuccess) {
		this.lhSuccess = lhSuccess;
	}

	public long getLhStart() {
		return lhStart;
	}

	public void setLhStart(long lhStart) {
		this.lhStart = lhStart;
	}

	public long getLhEnd() {
		return lhEnd;
	}

	public void setLhEnd(long lhEnd) {
		this.lhEnd = lhEnd;
	}

	public long getLhErrorTime() {
		return lhErrorTime;
	}

	public void setLhErrorTime(long lhErrorTime) {
		this.lhErrorTime = lhErrorTime;
	}

	public String getLhErrorMsg() {
		return lhErrorMsg;
	}

	public void setLhErrorMsg(String lhErrorMsg) {
		this.lhErrorMsg = lhErrorMsg;
	}

	public String getLhErrorCode() {
		return lhErrorCode;
	}

	public void setLhErrorCode(String lhErrorCode) {
		this.lhErrorCode = lhErrorCode;
	}

	public String getLhRequestMsg() {
		return lhRequestMsg;
	}

	public void setLhRequestMsg(String lhRequestMsg) {
		this.lhRequestMsg = lhRequestMsg;
	}

	
	
	
	
}
