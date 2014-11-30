package com.novicehacks.autobot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.AutobotConfigManager;
import com.novicehacks.autobot.config.SysConfig;

/**
 * <p>
 * The Application of SysBot is used to execute a set of comands on a server,
 * report the results and also used in monitoring the cpu status.
 * </p>
 * <p>
 * Make sure the system configuration is overriden in the autobot.properties
 * file, and corresponding resources for <em>servers, commands, executables and
 * monitors</em> are in place.
 * </p>
 * 
 * <p>
 * Using monitors is optional and can be controlled by <em>MonitorEnabled</em>
 * setting in autobot.properties.
 * </p>
 * 
 * <p>
 * <strong>TODO: Features for Batch Execution</strong>
 * <ul>
 * <li>Enable overriding executables with arguments.</li>
 * <li>Enable overriding autobot.properties with arguments.</li>
 * <li>Enable logging file selection using arguments</li>
 * <li>Enable remote server output logging to console</li>
 * <li>Enable execution of Executables to a specified no of time in arguments</li>
 * </ul>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class SysBotApp {
	private static Logger logger = LogManager.getLogger(SysBotApp.class);

	public static void main(String[] args) throws InterruptedException,
			ExecutionException, TimeoutException {
		/* Start the ExecutableManager with a delay */
		SysBotApp app = new SysBotApp();
		logger.info("Loading the Configurations");
		AutobotConfigManager.loadSystemConfig();
		AutobotConfigManager.loadResourceConfig();
		logger.info("Starting the ExecutableManager with a scheduled delay");
		app.StartExecutableManager();
		logger.info("Starting the ReportingManager with a scheduled delay");
		app.StartReportManager();
		if (SysConfig.getInstance().MonitoringEnabled()) {
			logger.info("Starting the Monitoring Thread");
			app.StartMonitoringManager();
		}
	}

	private void StartMonitoringManager() {
		// TODO Auto-generated method stub

	}

	private void StartReportManager() {
		// TODO Auto-generated method stub

	}

	private void StartExecutableManager() {
		logger.entry();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		service.scheduleWithFixedDelay(ExecutableManager.getInstance(), 0,
				SysConfig.getInstance().ExecutableDelay(), TimeUnit.HOURS);
		logger.exit();
	}
}
