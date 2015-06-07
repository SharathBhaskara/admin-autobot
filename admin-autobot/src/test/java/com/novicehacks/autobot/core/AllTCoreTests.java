package com.novicehacks.autobot.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ TestThreadManager.class, /*TestBotUtils.class,*/
		TestGenericUncaughtExceptionHandler.class })
public class AllTCoreTests {

}
