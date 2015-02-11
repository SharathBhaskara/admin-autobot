package test.learning.ssh;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith (Parameterized.class)
public class TestIPAddressRegularExpression {

	private final String	IPAddressRegex	= "[a-zA-Z0-9]{1,}(\\.[a-zA-Z0-9]{1,}){1,2}|(([0-9]{1,3})\\.){3}[0-9]{1,3}";
	private final String	IPAddressRegex2	= "\\w{1,}(\\.\\w{1,}){1,2}|((\\d{1,3})\\.){3}\\d{1,3}";

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
	public String	matcherInput;

	@Parameter (1)
	public boolean	matcherOutput;

	@Test
	public void testIPAddressRegex() {
		boolean status = matcherInput.matches (IPAddressRegex);
		Assert.assertEquals (matcherOutput, status);
	}

	@Test
	public void testIPAddressRegex2() {
		boolean status = matcherInput.matches (IPAddressRegex2);
		Assert.assertEquals (matcherOutput, status);
	}

}
