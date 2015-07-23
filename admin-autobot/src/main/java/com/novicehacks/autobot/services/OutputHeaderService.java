package com.novicehacks.autobot.services;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public interface OutputHeaderService extends Service {

	/**
	 * returns header content, wrapped with header separator in
	 * {@link #headerSeparator()}
	 * 
	 * @return
	 */
	public String header();

	/**
	 * returns the separator string used to wrap the header content in
	 * {@link #header()}
	 * 
	 * @return
	 */
	public String headerSeparator();
}
