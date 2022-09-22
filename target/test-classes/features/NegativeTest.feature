Feature: Getting API Errors

  Scenario: negative 1
    Then I get "request_failed" error message for incorrect "incorrectCityName" city

  Scenario: negative 2
    Then I get "missing_query" error message for incorrect "" city

  Scenario: negative 3
    Then I get "invalid_access_key" error message for incorrect "incorrectaccesskey" access key

  Scenario: negative 4
    Then I get "missing_access_key" error message for incorrect "" access key