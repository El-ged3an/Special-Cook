import static org.junit.jupiter.api.Assertions.*;
import io.cucumber.java.en.*;

public class AiAssistantSteps {

    private AiAssistant assistant;
    private String response;
    private static String api_key= "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";
    private static String ai_url="https://openrouter.ai/api/v1";
    private static String ai_model = "qwen/qwq-32b:free";

    @Given("an AiAssistant with valid API configuration")
    public void valid_ai_assistant() {
        assistant = new AiAssistant(ai_url,api_key,ai_model);
    }

    @Given("an AiAssistant with invalid API configuration")
    public void invalid_ai_assistant() {
        assistant = new AiAssistant("http://localhost:12345", "invalid", "bad-model");
    }

    @When("I send the message {string}")
    public void i_send_message(String message) {
        response = assistant.Send_Recieve(message);
    }

    @Then("the assistant should respond with a non-empty message")
    public void assistant_should_respond_non_empty() {
        assertNotNull(response);
        assertFalse(response.isEmpty());
    }

    @Then("the assistant should respond with {string} or {string}")
    public void assistant_should_respond_with_failure(String option1, String option2) {
        assertTrue(response.equals(option1) || response.equals(option2));
    }
}
