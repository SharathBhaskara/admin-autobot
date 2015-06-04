package com.novicehacks.autobot.services;

import java.io.IOException;
import java.nio.file.Path;
/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public interface ContentWriterService extends Service {

	public void write(Path path, String content) throws IOException;

}
