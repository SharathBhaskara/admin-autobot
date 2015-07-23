package com.novicehacks.autobot.reporting;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultReportingParser implements ReportingParser {
	private Logger logger = LogManager.getLogger (DefaultReportingParser.class);

	@Override
	public List<CommandOutputBean> readCommandOutput() {
		CommandOutputReader reader = commandOutputReader ();

		return null;
	}

	private CommandOutputReader commandOutputReader() {
		final String commandOutputFilePath = commandOutputFilePath ();
		CommandOutputReader reader = new CommandOutputReader (commandOutputFilePath);
		// TODO incomplete
		return null;
	}

	private String commandOutputFilePath() {
		// TODO incomplete
		return null;
	}

	@Override
	public Set<ReportingBean> reportingBeanList(Collection<CommandOutputBean> outputBeanCollection) {
		// TODO Auto-generated method stub
		return null;
	}

}
