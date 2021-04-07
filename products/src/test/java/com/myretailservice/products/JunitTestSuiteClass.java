package com.myretailservice.products;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ProductsControllerTests.class, ProductsServiceTests.class })
public class JunitTestSuiteClass {
}
