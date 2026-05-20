package cucumber.runners;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@SuiteDisplayName("Mvideo Cucumber Tests")
@IncludeEngines("cucumber")
@SelectClasspathResource("feature/mvideo")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, " +
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm, " +
                "json:target/cucumber-report/mvideo/report.json, " +
                "html:target/cucumber-report/mvideo/report.html")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,
        value = "cucumber.hooks, cucumber.stepDefs.mvideo")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @excluded")
@ConfigurationParameter(key = ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "false")
public class RunMvideoCucumberTest {
}