package com.novicehacks.autobot.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.novicehacks.autobot.core.exception.FileNotExistException;
import com.novicehacks.autobot.core.exception.WriteToDirectoryException;

public class DefaultFileWriter implements FileWriter {
	private Path filePath;

	public DefaultFileWriter (Path path) {
		validateParams (path);
		this.filePath = path;
	}

	public DefaultFileWriter (String path) {
		validateParams (path);
		this.filePath = Paths.get (path);
	}

	private void validateParams(Object path) {
		if (BotUtils.HasNullReferences (path)) {
			throw new IllegalArgumentException ("Invalid Parameter : " + path);
		}
	}

	@Override
	public void write(String content) throws IOException {
		checkFileExists ();
		checkFileOrDirectory ();
		writeContent (content);
	}

	private void checkFileExists() throws FileNotExistException {
		if (Files.notExists (this.filePath))
			throw new FileNotExistException ();
	}

	private void checkFileOrDirectory() throws WriteToDirectoryException {
		if (Files.isDirectory (this.filePath))
			throw new WriteToDirectoryException ();
	}

	private void writeContent(String content) throws IOException {
		StandardOpenOption[] options = fileOptions ();
		try (
				ByteChannel _byteChannel = Files.newByteChannel (this.filePath, options) ) {
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
