package com.novicehacks.autobot.core.services.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.novicehacks.autobot.core.exception.FileNotExistException;
import com.novicehacks.autobot.core.exception.WriteToDirectoryException;
import com.novicehacks.autobot.core.services.ContentWriterService;

public class DefaultContentWriterService implements ContentWriterService {

	@Override
	public void write(Path path, String content) throws IOException {
		checkFileExists (path);
		checkFileOrDirectory (path);
		writeContent (path, content);
	}

	private void checkFileExists(Path filePath) throws FileNotExistException {
		if (Files.notExists (filePath))
			throw new FileNotExistException ();
	}

	private void checkFileOrDirectory(Path filePath) throws WriteToDirectoryException {
		if (Files.isDirectory (filePath))
			throw new WriteToDirectoryException ();
	}

	private void writeContent(Path filePath, String content) throws IOException {
		StandardOpenOption[] options = fileOptions ();
		try (
				ByteChannel _byteChannel = Files.newByteChannel (filePath, options) ) {
			writeToFile (_byteChannel, content);
		}
	}

	private StandardOpenOption[] fileOptions() {
		return new StandardOpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.SYNC,
				StandardOpenOption.APPEND };
	}

	private void writeToFile(ByteChannel byteChannel, String content) throws IOException {
		ByteBuffer contentBuffer;
		contentBuffer = ByteBuffer.wrap (content.getBytes ());
		byteChannel.write (contentBuffer);
	}

}
