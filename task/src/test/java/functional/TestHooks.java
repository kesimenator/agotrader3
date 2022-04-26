package functional;

import core.Helper;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class TestHooks {

    @BeforeMethod(alwaysRun = true)
    public void Init(Method method) {
        Helper.initTest();
    }

    protected void _setTestStep(String description, String...values) {
        Helper.setTestStep(description, values);
    }
}
