Narrative:
- Nowa inicjatywa wymaga właściciela
- Odrzucenie inicjatywy powoduje wysłanie notyfikacji do właściciela
- Zaakceptowana inicjatywa wymaga potwierdzenia budżetu
- Po akceptacji budżetu tworzymy projekt na podstawie inicjatywy
- Budżet można korygować na każdym etapie
- Właściciel może usunąć inicjatywę na każdym etapie

Nowa inicjatywa wymaga właściciela

Scenario: scenario description
Given user Sebastian Malaca

When Manager creates new initative BDD Workshop with start date 14.10.2022 owned by Sebastian Malaca

Then BDD Workshop exists
And BDD Workshop is owned by Sebastia Malaca
And BDD Workshop has start date 14.10.2022