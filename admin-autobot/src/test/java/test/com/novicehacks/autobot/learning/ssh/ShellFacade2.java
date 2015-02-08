/**
 * 
 */
package test.com.novicehacks.autobot.learning.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * 
 */
public class ShellFacade2 {

	/* Init Commands */
	String command[] = { "\b", " ", "ls", "pwd", "df -k", "ps", "exit" };
	/* Console Output */
	String TempFilePrefix = "RemoteConsole_";
	String TempFileSuffix = ".log";
	Path ConsolePath;
	/* Remote Machine Info */
	final String Remote = "sdf.org";
	final String Username = "novicehacks";
	final String Password = "novicehacks";
	/* Logger */
	Logger logger = LogManager.getLogger("ShellFacade2");
	/* Shell Types */
	Connection connection;
	Session session;
	InputStream in;
	OutputStream out;
	/* Locking to synchronize the input and output */
	Boolean CommandStarted = false;
	Lock shellLock = new ReentrantLock(true);
	Condition SendInput = shellLock.newCondition();
	Condition InputComplete = shellLock.newCondition();

	/**
	 * Creates a connection and authenticates it with the username and password
	 * supplied, in Remote Machine Information.
	 * 
	 * @throws IOException
	 */
	public void createConnection() throws IOException {
		connection = new Connection(Remote);
		connection.connect();
		connection.authenticateWithPassword(Username, Password);
		if (!connection.isAuthenticationComplete()) {

		}
		/* Signals Input Complete for the first time */
	}

	/**
	 * Starts a RemoteConsumer and invokes the executeCommands to send the
	 * executables.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void startShell() throws IOException, InterruptedException {
		logger.entry();
		session = connection.openSession();
		try {
			// int x_width = 90;
			// int y_width = 30;
			// session.requestPTY("dumb", x_width, y_width, 0, 0, null);
			session.requestDumbPTY();
			session.startShell();
			in = session.getStdout();
			out = session.getStdin();
			ConsolePath = Files.createTempFile(TempFilePrefix, TempFileSuffix);
			logger.debug("Temp File Path : {}", ConsolePath.toAbsolutePath());
			Thread consumer = new Thread(new RemoteConsumer());
			/* Write data to the remote machine */
			consumer.start();
			/* Write data to the remote machine */
			executeCommands();
			consumer.join();
		} catch (InterruptedException ex) {
			logger.info("Exiting the shell");
		} finally {
			session.close();

		}

		/*
		 * Executing more commands in different sessions on the same server
		 * session = connection.openSession(); InputStream ins =
		 * session.getStdout(); session.execCommand("who"); byte[] b = new
		 * byte[8192]; System.out.println("--------------------"); while
		 * (ins.read(b) != -1) { System.out.print(new String(b)); }
		 * connection.close();
		 */
		logger.exit();
	}

	/**
	 * Loops for the commands to be executed, and writes them to the remote
	 * channel.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * 
	 */
	private void executeCommands() throws IOException, InterruptedException {
		logger.entry();
		/* Start execution of the commands */
		try (PrintStream remoteWriter = new PrintStream(out, true);) {
			for (String cmd : command) {
				/* Check for Exit command, if found then send an interrupt */
				if (cmd.equalsIgnoreCase("exit")) {
					Thread.currentThread().interrupt();
				}
				/* Create the char[] from the command */
				char[] data = cmd.toCharArray();
				/*
				 * Acquire the lock on the terminal, to send the input to remote
				 * and release once done
				 */
				shellLock.lockInterruptibly();
				CommandStarted = true;
				logger.debug("Lock Acquired by Command Executor");
				try {
					/*
					 * Wait till all available data is consumed by
					 * RemoteConsumer
					 */
					logger.debug("Wait for Send Input Signal and acquire lock");
					SendInput.awaitUninterruptibly();
					logger.debug("Send Input Received");

					remoteWriter.println(data);
					// addText(data);
					Thread.sleep(1000);
					InputComplete.signal();
					logger.debug("Signalling Input Complete");
				} finally {
					shellLock.unlock();
					logger.debug("Lock Released by Command Executor");
				}
			}
		} finally {
			shellLock.lockInterruptibly();
			SendInput.awaitUninterruptibly();
			logger.debug("Final Output from the remote received, hence interrupting");
			shellLock.unlock();
			Thread.currentThread().getThreadGroup().interrupt();
			out.close();
		}
		logger.exit();
	}

	/**
	 * @param buff
	 * @param len
	 * @throws IOException
	 */
	private void addText(byte[] buff) throws IOException {
		String line = new String(buff);
		line += "\n";
		line = line.trim();
		logger.trace(line);
		Files.write(ConsolePath, line.getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.WRITE, StandardOpenOption.APPEND,
				StandardOpenOption.SYNC);
	}

	/**
	 * @param buff
	 * @param len
	 * @throws IOException
	 */
	private void addText(char[] buff) throws IOException {
		String data = String.copyValueOf(buff);
		addText(data.getBytes());
	}

	class RemoteConsumer implements Runnable {

		/**
		 * <p>
		 * Reads the output from the remote machine, as and when available. And
		 * submits the data to write to temp file.
		 * </p>
		 * 
		 * <p>
		 * <strong>Inherited Doc:</strong> {@inheritDoc}
		 * </p
		 * 
		 * @see RemoteConsumer#addText
		 * 
		 */
		@Override
		public void run() {
			byte[] buff = new byte[8192];
			/* Wait for Command Executor to Start */
			while (!CommandStarted) {
				// do nothing...
			}
			try {
				String line;
				/* Loop for ever */
				while (true) {
					shellLock.lockInterruptibly();
					logger.debug("Lock acquired by Remote Consumer");
					if (in.available() == 0) {
						/* Signalling the Output Complete for Command invocation */
						SendInput.signal();
						logger.debug("Signalling Output Complete & Awaiting for Input Complete Signal");
						InputComplete.awaitUninterruptibly();
						logger.debug("Input Complete Received");
					}
					try {
						buff = new byte[8192];
						int len = in.read(buff);
						if (len == -1)
							return;
						addText(buff);
					} finally {
						shellLock.unlock();
						logger.debug("Lock released by Remote Consumer");
					}
				}
			} catch (Exception e) {
				if (e instanceof InterruptedException) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	@Test
	public void testShellFacade() throws IOException, InterruptedException {
		ShellFacade2 facade = new ShellFacade2();
		facade.createConnection();
		facade.startShell();
	}
}
