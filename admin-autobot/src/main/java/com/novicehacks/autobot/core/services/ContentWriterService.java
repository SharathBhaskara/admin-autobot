package com.novicehacks.autobot.core.services;

import java.io.IOException;
import java.nio.file.Path;

public interface ContentWriterService {

	public void write(Path path, String content) throws IOException;

}
