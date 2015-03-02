package com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.EnvironmentalTest;

@RunWith (Suite.class)
@SuiteClasses (EnvironmentalTestSuiteWithAllFilters.class)
public class AllEnvironmentalTests {

}

@RunWith (Categories.class)
@IncludeCategory (EnvironmentalTest.class)
@SuiteClasses (AllTests.class)
class EnvironmentalTestSuiteWithAllFilters {

}
