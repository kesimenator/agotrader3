package functional;

import core.Helper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class TestHooks {
    protected static final Level LOGGING_LEVEL = Level.INFO;


    @BeforeClass(alwaysRun = true)
    public void initClass() {
        // Customizing name of the log-file:
        String packageName = getClass().getName();
        String[] segments = packageName.split("\\.");
        String className = segments[segments.length - 1];
        SetLogFilename(className); //create logfileName accurate to a Test Class
    }

    @BeforeMethod(alwaysRun = true)
    public void init(Method method) {
        Helper.initTest();
    }

    protected void _setTestStep(String description, String...values) {
        Helper.setTestStep(description, values);
    }

    public static void SetLogFilename(String className) {
        String fileName = "log/" + className + ".log";

        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(LOGGING_LEVEL);  // the logger debugger
        builder.setConfigurationName("DefaultRollingFileLogger");

        ComponentBuilder triggeringPolicy = builder
                .newComponent("Policies")
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", "50 MB"));

        // logfile appender
        AppenderComponentBuilder appenderBuilder = builder
                .newAppender("LogToRollingFile", "RollingFile")
                .addAttribute("fileName", fileName)
                .addAttribute("filePattern", "log/%d{MMddHHmmss}-fargo.log")
                .addComponent(triggeringPolicy);

        // console appender
        AppenderComponentBuilder appenderBuilderConsole = builder
                .newAppender("LogToConsole", "Console");

        appenderBuilder.add(builder
                .newLayout("PatternLayout")
                .addAttribute("pattern", "%d %p %c [%t] %m%n"));

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.INFO);  // the tests debugger
        builder.add(appenderBuilder);
        builder.add(appenderBuilderConsole);
        rootLogger.add(builder.newAppenderRef("LogToRollingFile"));
        rootLogger.add(builder.newAppenderRef("LogToConsole")); // Console appender
        builder.add(rootLogger);
        Configurator.reconfigure(builder.build());
    }
}
