Narrative:
- Nowa inicjatywa wymaga właściciela
- Odrzucenie inicjatywy powoduje wysłanie notyfikacji do właściciela
- Zaakceptowana inicjatywa wymaga potwierdzenia budżetu
- Po akceptacji budżetu tworzymy projekt na podstawie inicjatywy
- Budżet można korygować na każdym etapie
- Właściciel może usunąć inicjatywę na każdym etapie

Odrzucenie inicjatywy powoduje wysłanie notyfikacji do właściciela

Scenario: scenario description
Given user Sebastian Malaca with mail sebastian.malaca<at>gmail.com

When Manager creates new initative TDD Workshop with start date 14.10.2022 owned by Sebastian Malaca

Then TDD Workshop exists
And TDD Workshop is owned by Sebastia Malaca
And TDD Workshop has start date 14.10.2022

When Manager rejects TDD Workshop due to reason: Piotrek already is doing it

Then Sebastian Malaca receives mail about rejection of the TDD Workshop initiative due to reason: Piotrek already is doing it