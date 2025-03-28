import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",  // Path to your feature files
    glue = "",  // No specific package since you're using the default package for step definitions
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class TestRunner {
}
