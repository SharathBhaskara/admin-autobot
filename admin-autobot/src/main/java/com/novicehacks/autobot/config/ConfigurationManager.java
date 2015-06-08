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
	 * Shared Singleton implementation
	 */

	ConfigurationManager () {}

	private static class ConfigurationManagerSingleton {
		private static final ConfigurationManager instance = new ConfigurationManager ();

		private static ConfigurationManager getInstance() {
			return instance;
		}
	}

	/**
	 * Gets the singleton instance of the object. Do not guarantee only one
	 * instance of this object exist, as constructor is not private.
	 * 
	 * @return
	 */
	public static ConfigurationManager getSharedInstance() {
		return ConfigurationManagerSingleton.getInstance ();
	}

	/**
	 * Loads the configuration properties from config file to
	 * {@link ApplicationConfig} bean.
	 * 
	 * <p>
	 * uses {@link ApplicationConfigParser} to fetch the configurations from file and uses
	 * {@link ApplicationConfigLoader} to load them to ApplicationConfig bean.
	 * 
	 * @see ApplicationConfigParser
	 * @see ApplicationConfigLoader
	 */
	public ApplicationConfig loadSystemConfig() {
		logger.entry ();
		ApplicationConfigParser parser = getConfigParser ();
		Properties configProperties = parser.getConfigProperties ();
		ApplicationConfigLoader loader = getConfigLoader ();
		loader.loadApplicationConfig (configProperties);
		AppConfigNotLoaded.set (false);
		logger.exit ();
		return ApplicationConfig.getInstance ();
	}

	private ApplicationConfigLoader getConfigLoader() {
		ApplicationConfigLoader loader;
		loader = new ApplicationConfigLoader ();
		return loader;
	}

	protected ApplicationConfigParser getConfigParser() {
		ApplicationConfigParser parser;
		parser = ApplicationConfigParser.getIntance ();
		return parser;
	}

	/**
	 * Loads the resources configurations from corresponding files, and loads
	 * them in {@link ResourceConfig} bean.
	 * 
	 * @see ResourceConfigLoader
	 */
	public ResourceConfig loadResourceConfig() {
		if (AppConfigNotLoaded.get ())
			loadSystemConfig ();
		loadConfigAndHandleExceptionsIfAny ();
		return ResourceConfig.getInstance ();
	}

	private void loadConfigAndHandleExceptionsIfAny() {
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
