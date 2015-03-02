package com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.IgnoredTest;

@RunWith (Suite.class)
@SuiteClasses (IgnoredTestSuiteWithAllFilters.class)
public class AllIgnoredTests {

}

@RunWith (Categories.class)
@IncludeCategory (IgnoredTest.class)
@SuiteClasses (AllTests.class)
class IgnoredTestSuiteWithAllFilters {

}
