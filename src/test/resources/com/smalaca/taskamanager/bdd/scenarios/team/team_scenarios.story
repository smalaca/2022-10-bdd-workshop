Narrative:
Project manager should have the possibility to create, modify and delete teams.
Also they need to have a possibility to add and remove team members from the team.

!-- Stworzenie zespołu wymaga zdefiniowania Lidera Zespołu
!--     User Story: Stworzenie zespołu wymaga określenia lidera
!--                 - lider musi być istniejącym użytkownik
!--     User Story: Odczytywanie zespołów zwraca również id lidera
!--     User Story: Odczytywanie zespołu zwraca również id lidera???
!--     User Story: Włącz scenariusz "Should create teams"

Scenario: Should create teams
Meta:
@skip
Given User Steve Rogers
And User Eric Magneto
And User Matt Murdock
And User Read Richards

When Project Manager checks what teams exist

Then 0 teams found

When Project Manager creates team Avengers with leader Steve Rogers
And Project Manager checks what teams exist

Then team was created
And 1 teams found
And team with name Avengers and leader Steve Rogers exist

When Project Manager creates team X-Men with leader Eric Magneto
And Project Manager creates team Defenders with leader Matt Murdock
And Project Manager creates team Fantastic Four with leader Read Richards
And Project Manager checks what teams exist

Then 4 teams found
And team with name Avengers and leader Steve Rogers exist
And team with name X-Men and leader Eric Magneto exist
And team with name Defenders and leader Matt Murdock exist
And team with name Fantastic Four and leader Read Richards exist

When Project Manager creates team X-Men with leader Eric Magneto

Then no new team created
And 4 teams found

Given User Charles Xavier
And User Jean Grey
And User Scott Summer
And User Ororo Munroe

When Project Manager adds Charles Xavier to X-Men

Then X-Men have 1 team member
And X-Men contains Charles Xavier

When Project Manager adds Ororo Munroe to X-Men

Then X-Men have 2 team member
And X-Men contains Charles Xavier
And X-Men contains Ororo Munroe
And Avengers have 0 team member
And Defenders have 0 team member
And Fantastic Four have 0 team member
