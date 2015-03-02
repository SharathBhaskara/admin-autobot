package com.novicehacks.autobot.executor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ TestCommandExecutorTask.class, TestServerExecutableCommandMap.class })
public class AllExecutorTests {

}
