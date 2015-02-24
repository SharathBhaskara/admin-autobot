package com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.core.CoreUnitTestSuite;
import com.novicehacks.autobot.ssh.SSHUnitTestCategorySuite;

@RunWith (Categories.class)
@SuiteClasses ({ SSHUnitTestCategorySuite.class, CoreUnitTestSuite.class })
public class AllUnitTests {

}
