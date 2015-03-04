package com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;

@RunWith (Suite.class)
@SuiteClasses (FunctionalTestSuiteWithAllFilters.class)
public class AllFunctionalTests {

}

@RunWith (Categories.class)
@IncludeCategory (FunctionalTest.class)
@SuiteClasses (FunctionalTestSuiteWithIgnoredFilter.class)
class FunctionalTestSuiteWithAllFilters {

}

@RunWith (Categories.class)
@ExcludeCategory (IgnoredTest.class)
@SuiteClasses (AllTests.class)
class FunctionalTestSuiteWithIgnoredFilter {

}
