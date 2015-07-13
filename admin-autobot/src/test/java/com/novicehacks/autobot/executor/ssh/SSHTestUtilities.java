package com.novicehacks.autobot.executor.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SSHTestUtilities {
	public static OutputStream tempOutputStream() {
		OutputStream stream = new OutputStream () {
			@Override
			public void write(int b) throws IOException {}
		};
		return stream;
	}

	public static InputStream tempInputStream() {
		InputStream stream = new InputStream () {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};
		return stream;
	}
}
