# Aplikacija za kupovinu avionskih karata
Ispitni zadatak iz predmeta Interakcija covek racunar za rok Februar 2023.

## Tehnologije
Za izradu aplikacije koriscena je mikroservisna arhitektura sa sledeceim tehnologijama:

- Java Spring Boot - Backend
- Vue 3 - Frontend
- Rasa - Python

## Mikroservisi
Aplikacija se sastoji od sledecih mikroservisa:

- [Aplikacija za kesiranje letova](https://github.com/Pequla/flight-cache)
- [Aplikacija za upravljanje korisnicima](https://github.com/Pequla/ticket-app)
- [Aplikacija sa konverzacijskim agentom](https://github.com/Pequla/ticket-agent)
- [Aplikacija za prikaz podataka - Frontend](https://github.com/Pequla/vue-tickets)

> Komunikacija izmedju aplikacija se vrsi upotrebom REST poziva u JSON formatu

## Baza podataka
Aplikacija koristi 2 baze podataka. Jedna za samo kesiranje letova a druga za potrebe upravljanja i cuvanja podataka korisnika. Koristi se MariaDB baza podataka

## Produkcijsko okruzenje

Za potrebe ispita aplikacija je instalirana na virtualni privatni server (eng. VPS) na kome se nalazi unapred instaliran operativni sistem Ubuntu 22.10

Neophodan a podrazumevan softver je `Apache2` web server koji se koristi u potrebe reverse proxy-a kao i Lets Encrypt Cert Bot za besplatne TLS serfikate 