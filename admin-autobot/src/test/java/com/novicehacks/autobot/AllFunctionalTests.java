package com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.FunctionalTest;

@RunWith (Suite.class)
@SuiteClasses (FunctionalTestSuiteWithAllFilters.class)
public class AllFunctionalTests {

}

@RunWith (Categories.class)
@IncludeCategory (FunctionalTest.class)
@SuiteClasses (AllTests.class)
class FunctionalTestSuiteWithAllFilters {

}
