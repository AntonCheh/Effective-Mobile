package cucumber.runners;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SuiteDisplayName("Single Feature Test")
@IncludeEngines("cucumber")
@SelectClasspathResource("feature/example.feature") // ← ТОЛЬКО ЭТОТ ФАЙЛ
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "ru.sberbank.sdct.autotest.stepdefs, ru.sberbank.sdct.autotest.hooks")
public class RunSingleFeatureTest {
}