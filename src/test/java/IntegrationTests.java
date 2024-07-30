import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        plugin = { "pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json" })
public class IntegrationTests {
}
