package com.novicehacks.autobot.executor.ssh;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.novicehacks.autobot.categories.ParameterizedTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnection;

@RunWith (Parameterized.class)
@Category ({ ParameterizedTest.class, UnitTest.class })
public class DefaultSSHConnectionStringParameterizedTest {
	private final String IPAddressRegex = DefaultSSHConnection.IPAddressRegex;

	@Parameters (name = "test({index}) : IPAddressRegex({0}) = {1}")
	public static Collection<Object[]> paramValues() {
		Object[][] data = new Object[][] { { "190.168.123.1", true }, { "192.0.0.123", true },
				{ "sdf.org.com", true }, { "s1df.o2rg", true }, { "192.org", true },
				{ "sdf1.org1.com2", true }, { "190.168.123", true }, { "sdf.org", true },
				{ "1bc.168.123.1", false }, { "abc.168.123.123", false }, { "sdf", false },
				{ "192", false }, { "1:2.123.123.123", false }, { "a,b.234", false } };
		return Arrays.asList (data);
	}

	@Parameter (0)
	public String matcherInput;

	@Parameter (1)
	public boolean matcherOutput;

	@Test
	public void testIPAddressRegex() {
		// given
		boolean expected = this.matcherOutput;
		// when
		boolean actual = this.matcherInput.matches (this.IPAddressRegex);
		// then
		Assert.assertEquals (expected, actual);
	}

}
