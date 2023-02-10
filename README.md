# Dock

## Why and Learnings

I wanted to practice API building using Java, Spring Boot and Docker, so I searched for "Java api challenge" on Google and this challenge was brought to me. Read the [instructions](./challenge-specs.md), the challenge seemed good enough since it would allow me to practice clean code; SOLID principles as well as the use of design patterns.

A Trello card, in the board I created to record every project I develop to practice, was created, so I'd be able to use the kanban methodology to track the development. Since it is a personal project, with no obligations to deliver to a client or anyone else, I felt like I could go on my own pace and get as deep as I wanted in every tool.

This project helped me better understand the use of DTOs (incoming and outgoing); of mappers (MapStruct was used, but now I can build my own); of the JpaInterface (which let me create functions by only defining their names, IntelliJ helped a lot with this); the breaking of functions, good variable names and so on.

Even though I tried to write the best code I could, I'm not fully satisfied with it. I'll finish this project and re-try it with a predefined architecture, because I learned how hard it can be to make big changes in the code if the architecture was not well thought. Maybe I'll use the next shot to use a microservice architecture applied using the concept of [Evolutionary Architecture](https://www.youtube.com/watch?v=6hbKLQo0PUM).

## Methodologies and Tools

- Kanban;
- IntelliJ;
- [Semantic Commits](https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716);
- Gradle;
- Java 11;
- SpringBoot v2.7.1;
  - MapStruct;
  - Hibernate;
  - Lombok;
  - Mockito;
- Jacoco.
- Postgres;
- Docker.

## Road Map

- [X] Endpoint to create entity `Portador`, which is composed by the properties bellow;
  - Full name;
  - CPF (must be unique and formatted as the Brazilian Government commands);
  - UUID.
- [X] Endpoint to remove a specific `Portador`;
- [X] Endpoint to create a `Agencia`,  which is composed by the properties bellow;
  - Id;
  - Name (unique).
- [X] Endpoint to create remove a specific `Agencia`;
- [X] Endpoint to create a `Conta`,  which is composed by the properties bellow;
  - Portador`s CPF;
  - Account balance;
  - Account number (UUID);
  - Agencia's id.
- [X] Enpoint to create a `Transacao`, which is composed by the properties bellow;
  - Date;
  - Time;
  - Conta's id;
  - Type of transact (withdraw, deposit);
  - Amount to be processed.
- [X] Bind the act of posting a new Transaction to that of updating a CONTA balance;
- [X] Add the rules listed bellow to the withdrawal type transactions;
  - The final balance cannot be negative;
  - The amount to be processed can not be higher than the balance;
  - Only 2k can be withdrawn per day;
  - The CONTA must be activated;
  - The CONTA must not be blocked.
- [X] Add a Dockercompose file;
- [X] Add a postgres database in the Dockercompose file;
- [ ] Endpoint to get the bank statement (a list of all transactions made);
- [ ] Endpoint for the client (Portador) to deactivate the CONTA associated to them;
- [ ] Endpoint for the bank (Dock) to block any CONTA;
- [ ] Create a standard object to be returned by all endpoints;
- [ ] Add the Java image to Dockercompose file;
