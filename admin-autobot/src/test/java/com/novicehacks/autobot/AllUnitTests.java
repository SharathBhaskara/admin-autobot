package com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.EnvironmentalTest;
import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;

@RunWith (Suite.class)
@SuiteClasses (UnitTestSuiteWithAllFilters.class)
public class AllUnitTests {

}

@RunWith (Categories.class)
@IncludeCategory (UnitTest.class)
@SuiteClasses (UnitTestSuiteWithFunctionalFilter.class)
class UnitTestSuiteWithAllFilters {

}

@RunWith (Categories.class)
@ExcludeCategory (FunctionalTest.class)
@SuiteClasses (UnitTestSuiteWithEnvironmentFilter.class)
class UnitTestSuiteWithFunctionalFilter {

}

@RunWith (Categories.class)
@ExcludeCategory (EnvironmentalTest.class)
@SuiteClasses (AllTests.class)
class UnitTestSuiteWithEnvironmentFilter {

}
