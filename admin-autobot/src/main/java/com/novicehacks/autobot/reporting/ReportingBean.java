package com.novicehacks.autobot.reporting;

import java.util.Calendar;

public class ReportingBean {
	private String commandId;
	private String serverId;
	private String commandStr;
	private String serverName;

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getCommandStr() {
		return commandStr;
	}

	public void setCommandStr(String commandStr) {
		this.commandStr = commandStr;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Calendar getExecutionTimestamp() {
		return executionTimestamp;
	}

	public void setExecutionTimestamp(Calendar executionTimestamp) {
		this.executionTimestamp = executionTimestamp;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	private Calendar executionTimestamp;
	private String output;
}
