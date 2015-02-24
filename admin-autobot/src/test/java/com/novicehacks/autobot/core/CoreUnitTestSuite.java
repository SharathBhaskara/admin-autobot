package com.novicehacks.autobot.core;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.UnitTest;

@RunWith (Categories.class)
@IncludeCategory (UnitTest.class)
@SuiteClasses ({ TestThreadManager.class, TestBotUtils.class,
		TestGenericUncaughtExceptionHandler.class })
public class CoreUnitTestSuite {

}
