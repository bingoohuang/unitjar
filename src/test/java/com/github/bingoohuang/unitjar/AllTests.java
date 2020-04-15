package com.github.bingoohuang.unitjar;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
//import com.googlecode.junittoolbox.WildcardPatternSuite;
//import com.googlecode.junittoolbox.SuiteClasses;
//import com.googlecode.junittoolbox.ExcludeCategories;

//@RunWith(WildcardPatternSuite.class)
@RunWith(Suite.class)
@SuiteClasses({UnitJarTest.class})
//@SuiteClasses("com/github/bingoohuang/unitjar/*Test.class")
//@ExcludeCategories({AllTests.class, MainAppTest.class})
public class AllTests {

}

