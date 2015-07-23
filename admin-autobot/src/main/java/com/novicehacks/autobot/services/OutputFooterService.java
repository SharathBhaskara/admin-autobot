package com.novicehacks.autobot.services;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public interface OutputFooterService extends Service {
	/**
	 * returns footer content wrapped with {@link #footerSeparator()}
	 * 
	 * @return
	 */
	public String footer();

	/**
	 * returns seperator used to wrap the content in {@link #footer()}
	 * 
	 * @return
	 */
	public String footerSeparator();
}
