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



Scenario: Should add team members to the team
Given existing team X-Force
And existing team New Mutants
And User Charles Xavier
And User Jean Grey
And User Scott Summer
And User Ororo Munroe

When Project Manager adds Charles Xavier to X-Force

Then X-Force have 1 team member
And X-Force contains Charles Xavier

When Project Manager adds Ororo Munroe to X-Force

Then X-Force have 2 team member
And X-Force contains Charles Xavier
And X-Force contains Ororo Munroe
Then New Mutants have 0 team member
