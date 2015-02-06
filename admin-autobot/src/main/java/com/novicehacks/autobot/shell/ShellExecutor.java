package com.novicehacks.autobot.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ServerCredential;

/**
 * Will connect to the server and execute all the commands that are passed to
 * it. Once the execution is completed the results will be logged into a file.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ShellExecutor implements Runnable {
	private static final String	CurruptedData	= "Corrupted Data";
	private Server				server;
	private Command[]			commands;
	private Connection			connection;
	private Logger				logger			= LogManager
														.getLogger (ShellExecutor.class);

	/**
	 * Will instantiate the ShellExecutor with the server and an array of
	 * commands to be executed on the server.
	 * 
	 * @param server
	 * @param commands
	 */
	public ShellExecutor (Server server, Command... commands) {
		if (BotUtils.HasNullReferences (server, commands)) {
			throw new IllegalArgumentException (
					"Invalid server and command passed to ShellExecutor");
		}
		this.server = server;
		this.commands = commands;
	}

	/**
	 * <p>
	 * Will instantiate the ShellExecutor with the server and an array of
	 * commands to be executed on the server.
	 * </p>
	 * <p>
	 * Alternate for the command array constructor, be cautious not to send a
	 * null collection, as it will throw an NPE.
	 * </p>
	 * 
	 * @param server
	 * @param commands
	 * @throws NullPointerException
	 *         if commands collections is null.
	 */
	public ShellExecutor (Server server, Collection<Command> commands) {
		this (server, commands.toArray (new Command[] { }));
	}

	@Override
	public void run() {
		logger.entry ();
		logger.debug ("Executing commands on server : {}", this.server.name ());
		/*
		 * Starts the Connection to the server, and authenticates with
		 * credentials
		 */
		try {
			StartConnection ();
			/*
			 * Once authenticated we can start executing commands in seperate
			 * sessions
			 */
			if (!this.connection.isAuthenticationComplete ()) {
				logger.warn (
						"Authentication failed to server [{}] hence quitting the thread.",
						this.server.name ());
				return;
			}
			/* Execute the Init Commands if exists */
			// TODO needs to evaluate on how to execute these commands..

			/*
			 * if (server.initCommands() != null) {
			 * executeInitCommand(server.initCommands());
			 * }
			 */
			ExecutorService service = Executors.newCachedThreadPool ();
			/* Executing the commands */
			for (Command command : commands) {
				/* Execute Command and log results in asynchronous way */
				executeCommand (service, command);
			}
			service.shutdown ();
			boolean status = service.awaitTermination (SysConfig.getInstance ()
					.longTimeoutInMinutes (), TimeUnit.MINUTES);
			logger.info ("Status of the threads executing commands {}", status);
			if (!service.isTerminated ()) {
				logger.warn ("Threads not completed, after 5 minutes so terminating them for performance issues");
			}

		} catch (InterruptedException e) {
			logger.warn (
					"Thread terminated while executing the commands on the server [{}]",
					this.server.name ());
		} catch (IOException e) {
			logger.error (
					"Unable to initiate the connection to the server : {}",
					this.server.name (), e);
			logger.warn (
					"Termintating the thread as the connection was unsuccessful to server {}",
					this.server.name ());
			return;
		} finally {
			CloseConnection ();
		}

		logger.exit ();
	}

	/**
	 * @param initCommand
	 * @throws IOException
	 */
	private void executeInitCommand(String[] initCommands) throws IOException {
		Session session = null;
		BufferedReader _buffer = null;
		InputStream _is = null;
		StringBuffer _storageBuffer;
		String _outputLine;
		logger.entry ();
		try {
			/*
			 * Session will be opened and the init command will be executed.
			 */
			logger.debug ("Creating a new Session for executing the command");
			session = connection.openSession ();
			for (String command : initCommands) {
				logger.debug ("Executing Server Init Command :  {}", command);
				// TODO some problem in executing initializing commands
				session.execCommand (command);
				_is = new StreamGobbler (session.getStdout ());
				_buffer = new BufferedReader (new InputStreamReader (_is));
				_storageBuffer = new StringBuffer ();
				/*
				 * Reads from the remote stream and and append to the
				 * StringBuffer, Breaks when there is nothing to read from.
				 */
				while (true) {
					_outputLine = _buffer.readLine ();
					logger.trace ("Output for Command Id : {} is :  {}",
							command, _outputLine);
					if (_outputLine == null || _outputLine.equals (""))
						break;
					_storageBuffer.append (_outputLine);
					_storageBuffer.append ("\n");
				}
				logger.debug ("Output for Command {} is : \n", command,
						_storageBuffer.toString ());
			}
		} finally {
			/* Closing the resources for preventing memory leaks */
			if (_buffer != null)
				_buffer.close ();
			if (_is != null)
				_is.close ();
			if (session != null)
				session.close ();
		}
		logger.exit ();
	}

	/**
	 * Will execute each command in a separate session.
	 * <ul>
	 * <li>Creates a thread to execute</li>
	 * <ul>
	 * <li>Will open a new session on the connection object</li>
	 * <li>Execute the command in the session</li>
	 * <li>Collect the Streamed output from StreamGobbler</li>
	 * <li>Create a new thread to write the results</li>
	 * </ul>
	 * <li>Submits the thread to the executor service passed</li> </ul>
	 * 
	 * @param service
	 * @param command
	 * @throws IOException
	 */
	private void executeCommand(ExecutorService service, Command command) {
		Executor executor = new Executor (command);
		service.submit (executor);
		return;
	}

	/**
	 * Will create an SSH Connection to the IPAddress. And authenticates the
	 * user for starting the sessions.
	 * 
	 * @throws IOException
	 */
	private void StartConnection() throws IOException {
		logger.entry ();
		Connection connection;
		logger.debug ("Connecting to server with address : {}",
				this.server.ipaddress ());
		connection = new Connection (this.server.ipaddress ());
		connection.connect (null, SysConfig.getInstance ()
				.serverConnectionTimeout (), 10 * 30 * 1000);
		logger.debug ("Authenticating the connection with credentials");
		Boolean authenticated = false;
		StringBuilder buffer = new StringBuilder ();
		/**
		 * Authenticates the user with the credentials defined in the server.
		 */
		for (ServerCredential credential : this.server.credentials ()) {
			authenticated = connection.authenticateWithPassword (
					credential.getLoginid (), credential.getPassword ());
			if (authenticated) {
				break;
			}
			buffer.append ("User : ");
			buffer.append (credential.getLoginid ());
			buffer.append (" Methods : {");
			buffer.append (connection.getRemainingAuthMethods (credential
					.getLoginid ()));
			buffer.append ("}");
		}
		if (!authenticated) {
			logger.warn (
					"Unable to authenticate the server [{}] with the credentials provided",
					this.server.name ());
			logger.info (
					"Make sure user based authentication listed and supported by the server below : {}",
					buffer.toString ());
		}
		this.connection = connection;
		logger.exit ();
	}

	/**
	 * Closes the connection on the server if its still connected.
	 */
	private void CloseConnection() {
		logger.entry (this.connection);
		if (this.connection != null)
			this.connection.close ();
		logger.exit ();
	}

	/**
	 * Private Inner class for creation the threads for command execution.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private class Executor implements Runnable {
		private Command	command;

		private Executor (Command command) {
			this.command = command;
		}

		/**
		 * Executing the Command in a new Session object and closes it when
		 * completed.
		 * 
		 * @return
		 * @throws IOException
		 */
		private String execute() throws IOException {
			Session session = null;
			BufferedReader _buffer = null;
			InputStream _is = null;
			StringBuffer _storageBuffer;
			String _outputLine;
			logger.entry ();
			try {
				/*
				 * Session will be opened and the command will be executed.
				 */
				logger.debug ("Creating a new Session for executing the command");
				session = connection.openSession ();
				if (command != null && command.command () != null) {
					logger.debug ("Executing the command : {}", command.id ());
					session.execCommand (command.command ());
				} else {
					logger.warn ("Invalid command submitted : ");
					return CurruptedData;
				}

				_is = new StreamGobbler (session.getStdout ());
				_buffer = new BufferedReader (new InputStreamReader (_is));
				_storageBuffer = new StringBuffer ();
				/*
				 * Reads from the remote stream and and append to the
				 * StringBuffer, Breaks when there is nothing to read from.
				 */
				logger.debug ("Logging the results after the command : {}",
						command.id ());
				while (true) {
					_outputLine = _buffer.readLine ();
					logger.trace ("Output for Command Id : {} is :  {}",
							command.id (), _outputLine);
					if (_outputLine == null || _outputLine.equals (""))
						break;
					_storageBuffer.append (_outputLine);
					_storageBuffer.append ("\n");
				}
				logger.exit ();
				return _storageBuffer.toString ();
			} finally {
				/* Closing the resources for preventing memory leaks */
				if (_buffer != null)
					_buffer.close ();
				if (_is != null)
					_is.close ();
				if (session != null)
					session.close ();
			}

		}

		/**
		 * Will execute the command, and log the results in ShellConsole
		 */
		@Override
		public void run() {
			String _output;
			try {
				_output = execute ();
				/* Logging the results in the shell console */
				Thread t = new Thread (new ShellConsole (_output, server,
						command));
				t.start ();
				/*
				 * Preventing the Thread being killed before completion of
				 * logging to console
				 */
				t.join ();
			} catch (IOException | InterruptedException e) {
				logger.error ("Execution of command failed", e);
			}
		}
	}

}
