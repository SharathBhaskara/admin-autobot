package com.novicehacks.autobot.logger;

import java.nio.file.Path;

import com.novicehacks.autobot.core.ContentWriterService;

public interface OutputLogger {
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

	/**
	 * File Writer service that is used for writing the formatted content
	 * 
	 * @return
	 */
	public ContentWriterService writerService();

	/**
	 * Path of log, where the content is logged.
	 * 
	 * @return
	 */
	public Path logLocation();
}
