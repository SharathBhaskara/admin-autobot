package com.novicehacks.autobot.config;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.ExitStatusCode;

public class ConfigurationManager {
	private static final AtomicBoolean AppConfigNotLoaded = new AtomicBoolean (true);
	private static Logger logger = LogManager.getLogger (ConfigurationManager.class);

	/**
	 * Loads the configuration properties from config file to
	 * {@link ApplicationConfig} bean.
	 * 
	 * <p>
	 * uses {@link ConfigParser} to fetch the configurations from file and uses
	 * {@link ConfigLoader} to load them to ApplicationConfig bean.
	 * 
	 * @see ConfigParser
	 * @see ConfigLoader
	 */
	public static ApplicationConfig loadSystemConfig() {
		logger.entry ();
		ConfigParser parser = ConfigParser.getIntance ();
		Properties configProperties = parser.getConfigProperties ();
		ConfigLoader loader = new ConfigLoader (configProperties);
		loader.loadApplicationConfig ();
		AppConfigNotLoaded.set (false);
		logger.exit ();
		return ApplicationConfig.getInstance ();
	}

	/**
	 * Loads the resources configurations from corresponding files, and loads
	 * them in {@link ResourceConfig} bean.
	 * 
	 * @see ResourceConfigLoader
	 */
	public static ResourceConfig loadResourceConfig() {
		if (AppConfigNotLoaded.get ())
			loadSystemConfig ();
		loadConfigAndHandleExceptionsIfAny ();
		return ResourceConfig.getInstance ();
	}

	private static void loadConfigAndHandleExceptionsIfAny() {
		logger.entry ();
		ResourceConfigLoader configLoader = new ResourceConfigLoader ();
		try {
			configLoader.loadResourceConfig ();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.fatal ("Unable to load resource configurations, SYSTEM will exit", e);
			e.printStackTrace ();
			System.exit (ExitStatusCode.ResourceLoadingFailed.value ());
		}
		logger.exit ();
	}
}
