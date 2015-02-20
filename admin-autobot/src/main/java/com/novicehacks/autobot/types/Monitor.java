package com.novicehacks.autobot.types;

import com.novicehacks.autobot.monitor.MonitorManager;

/**
 * Monitor is a command that has to be monitored.
 * 
 * <p>
 * <strong> Under development</strong>
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see MonitorManager
 */
public class Monitor implements Comparable<Monitor> {

	/**
	 * Monitor Type enum with Disc and CPU constants.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	public enum Type {
		Disk, CPU, Other;

		public static Type fromValue(String name) {
			for (Type type : Type.values()) {
				if (type.name().equalsIgnoreCase(name)) {
					return type;
				}
			}
			return Other;
		}
	}

	private String description;
	private String commandPattern;
	private Boolean hasHeader;
	private Integer row;
	private Integer column;
	private Integer threshold;
	private Type monitorType;
	private String commandLine;

	public Monitor(String line) {
		this.commandLine = line;
	}

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

	public String getCommandLine() {
		return commandLine;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	@Override
	public String toString() {
		return commandLine;
	}

	@Override
	public int compareTo(Monitor o) {
		return this.commandLine.compareTo(o.commandLine);
	}

}
