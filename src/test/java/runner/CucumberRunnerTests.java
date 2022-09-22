package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions (
        features = "src/test/resources/features",
        glue = "definitions",
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
