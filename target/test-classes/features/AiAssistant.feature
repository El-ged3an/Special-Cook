Feature: AI Assistant Communication

  Scenario: Sending a valid message to the assistant
    Given an AiAssistant with valid API configuration
    When I send the message "Hello"
    Then the assistant should respond with a non-empty message

  Scenario: Sending a message with invalid API configuration
    Given an AiAssistant with invalid API configuration
    When I send the message "Test error"
    Then the assistant should respond with "Request Failed" or "error"
