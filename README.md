# Aureo | Bot conversation platform

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

# Overview
This example project is a simple implementation of a ChatBOT platform that handles with bots and their conversations. Even with a small amount of functionality in this first version, the goal of this architecture is to provide a solution that serves large companies and has a large number of calls made over the Internet or Telephone.

# Engineering Challenges
An enterprise company has always a very specific characteristics regarding the technology stack. There are often legacy systems running in production for a long time, sometimes even without simple integration protocols like HTTP. Some are developed by teams using waterfall metodologies, others squads doing agile.

It's important take care of the build & deployment process as well scalability, fault tolerance, tests and more. All those characteristics are related the architecture off the application. 

Nowadays in a new software implementation, all those specific points can be target with specific tecnhologies. This lead us to another very important thing to keep in mind: the team profile, how to find those people and how easy is to find the right professionals for the stack that you have choosen. In my carrer I saw some projects failing because the stack was beautiful but the company did not found professionals in the market to do the job in the needed time schedule.

There are some fast and simple stack choices that I would recommend for startups that need to start selling the product to inject money and then grow to a more complex implementation. For an enterprise company product, the team still have to be productive but there are always more things to think about and handle properly.

# Architectural Considerations
As a new software, this application should be Cloud Native where it's possible to easly adopt cloud providers and change them as desired. As we are talking with a big company, the applications probably will have to be platform agnostic and should not depend on specific implementation of Cloud Providers SDKs.

For this challenge, I'll implement a simple Microservice in Java based on [Microservice Architecture Pattern](https://martinfowler.com/microservices/) using Spring Boot, Spring Cloud and Docker. In this first version the Spring Cloud will not be implemented, but should be done in future versions of the software.

# Functional Services
The Aureo application was decomposed into one core microservices. Independent of the future size, all of them should be independently deployable applications, organized around certain business domains.

Add image here

## Aureo Service
In this service there are two main resources: bots and messages.

####Bots
Stores bots information and the messages that each bot has received or sended.

Method	| Path	| Description	|
------------- | ------------------------- | ------------- |
GET	    | /bots/:id	| Get the specified bot settings
POST	| /bots	    | Create a new bot to handle messages
PUT	    | /bots:id	| Update existing bot with informations
DELETE	| /bots:id	| Delete existing bot

####Messages
Stores messages information. You can retrieve information about messages, grouping by conversations.

Method	| Path	| Description	|
------------- | ------------------------- | ------------- |
GET	    | /messages/:id	| Get the specific message details
GET	    | /messages?conversationId=:conversationId	| Get all messages in a conversation
POST    | /messages	    | Create a new message


#### Notes
- Each microservice should have it's own database, so there is no way to bypass API and access persistance data directly.
- In this project, I use MongoDB as a primary database for the Aureo service. It might also make sense to have a polyglot persistence architecture (сhoose the type of db that is best suited to service requirements).
- Service-to-service communication: Common practice in a real-world systems is to use combination of interaction styles. For example, perform synchronous GET request to retrieve data and use asynchronous approach via Message broker for create/update operations in order to decouple services and buffer messages. However, this brings us to the [eventual consistency](http://martinfowler.com/articles/microservice-trade-offs.html#consistency) world. It would also good idea to implement a DTO layer to handle the exposure of objects and make possible to easly change business data centric logic.