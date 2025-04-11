Feature: ToDo API

  Scenario: Get empty ToDo list
    Given no ToDo items exist
    When I GET /todos
    Then I receive 200 OK with empty list
