/**
 * 
 */
package test.com.novicehacks.autobot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * 
 */
public class TestShellInterface {

	private static final String serverIp = "sdf.org";
	private static final String username = "novicehacks";
	private static final String password = "novicehacks";

	public static void main(String[] aa) throws IOException {
		Connection connection = new Connection(serverIp);
		connection.connect();
		try {
			connection.authenticateWithPassword(username, password);
			Session session;
			InputStream _resultOut;
			BufferedReader _buffer;
			StringBuffer _storageBuffer;
			String _outputLine;
			if (connection.isAuthenticationComplete()) {

				String commands[] = { "\b", "\r", "\r", "who", "df -kh", "ls" };
				for (String cmd : commands) {
					session = connection.openSession();
					session.execCommand(cmd);
					_resultOut = new StreamGobbler(session.getStdout());
					_buffer = new BufferedReader(new InputStreamReader(
							_resultOut));
					_storageBuffer = new StringBuffer();
					while (true) {
						try {
							_outputLine = _buffer.readLine();
							System.out.println(_outputLine);
						} catch (IOException e) {
							_outputLine = "CurruptedData";
						}
						if (_outputLine == null || _outputLine.equals(""))
							break;
						_storageBuffer.append(_outputLine);
						_storageBuffer.append("\n");
					}
					System.out.println(_storageBuffer.toString());
					session.close();
				}
			}
		} finally {
			connection.close();
		}

	}
}
