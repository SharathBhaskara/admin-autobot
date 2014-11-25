package com.novicehacks.autobot.types;

/**
 * Monitor is a command that has to be monitored.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class Monitor {

	/**
	 * Monitor Type enum with Disc and CPU constants.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	public enum Type {
		Disk, CPU;
	}

	private String description;
	private String commandPattern;
	private Boolean hasHeader;
	private Integer row;
	private Integer column;
	private Type monitorType;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommandPattern() {
		return commandPattern;
	}

	public void setCommandPattern(String commandPattern) {
		this.commandPattern = commandPattern;
	}

	public Boolean getHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(Boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public Type getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(Type monitorType) {
		this.monitorType = monitorType;
	}

}
