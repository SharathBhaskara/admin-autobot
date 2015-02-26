package com.novicehacks.autobot.logger;

public interface OutputLoggerTask extends Runnable {
	/**
	 * Final content that is about to be written by this thread.
	 * 
	 * @return
	 */
	public String getFormattedContent();

	/**
	 * Header service that is used for formatting the content
	 * 
	 * @return
	 */
	public OutputHeaderService headerService();

	/**
	 * Footer service that is used for formatting the content
	 * 
	 * @return
	 */
	public OutputFooterService footerService();
}
