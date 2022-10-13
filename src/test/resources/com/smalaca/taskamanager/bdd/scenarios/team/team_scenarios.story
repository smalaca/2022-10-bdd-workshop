Narrative:
Project manager should have the possibility to create, modify and delete teams.
Also they need to have a possibility to add and remove team members from the team.

Scenario: Should create teams
When Project Manager checks what teams exist

Then 0 teams found

When Project Manager creates team Avengers
And Project Manager checks what teams exist

Then team was created
And 1 teams found
And team with name Avengers exist

When Project Manager creates team X-Men
And Project Manager creates team Defenders
And Project Manager creates team Fantastic Four
And Project Manager checks what teams exist

Then 4 teams found
And team with name Avengers exist
And team with name X-Men exist
And team with name Defenders exist
And team with name Fantastic Four exist

When Project Manager creates team X-Men

Then no new team created
And 4 teams found
