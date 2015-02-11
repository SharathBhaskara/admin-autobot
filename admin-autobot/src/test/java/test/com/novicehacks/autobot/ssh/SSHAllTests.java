package test.com.novicehacks.autobot.ssh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ SSHIntegrationTestSuite.class, SSHUnitTestSuite.class,
		SSHParameterizedTestSuite.class })
public class SSHAllTests {

}
