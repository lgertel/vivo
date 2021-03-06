# Aureo | Bot conversation platform

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

## Aureo Service
In this service there are two main resources: bots and messages.

#### Bots
Stores bots information and the messages that each bot has received or sended.

Method	| Path	| Description	|
------------- | ------------------------- | ------------- |
GET	    | /bots/:id	| Get the specified bot settings
POST	| /bots	    | Create a new bot to handle messages
PUT	    | /bots:id	| Update existing bot with informations
DELETE	| /bots:id	| Delete existing bot

#### Messages
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

# What is missing
See below my recomendations for future implementations, and the things related to deploy, integration and scalability.

## Infrastructure services
There's a bunch of common patterns in distributed systems, which could help us to make described core services work. [Spring cloud](http://projects.spring.io/spring-cloud/) provides powerful tools that enhance Spring Boot applications behaviour to implement those patterns. I'll cover them briefly.
<img width="880" alt="Infrastructure services" src="https://cloud.githubusercontent.com/assets/6069066/13906840/365c0d94-eefa-11e5-90ad-9d74804ca412.png">
### Config service
[Spring Cloud Config](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) is horizontally scalable centralized configuration service for distributed systems. It uses a pluggable repository layer that currently supports local storage, Git, and Subversion. 

In this project, I could `native profile`, which simply loads config files from the local classpath.

##### Client side usage
Just build Spring Boot application with `spring-cloud-starter-config` dependency, autoconfiguration will do the rest.

Now you don't need any embedded properties in your application. Just provide `bootstrap.yml` with application name and Config service url:
```yml
spring:
  application:
    name: aureo-service
  cloud:
    config:
      uri: http://config:8888
      fail-fast: true
```

##### With Spring Cloud Config, you can change app configuration dynamically. 
First, change required properties in Config server. Then, perform refresh request to Aureo service:
`curl -H "Authorization: Bearer #token#" -XPOST http://127.0.0.1:8000/aureo/refresh`

Also, you could use Repository [webhooks to automate this process](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html#_push_notifications_and_spring_cloud_bus)

##### Notes
- There are some limitations for dynamic refresh though. `@RefreshScope` doesn't work with `@Configuration` classes and doesn't affect `@Scheduled` methods
- `fail-fast` property means that Spring Boot application will fail startup immediately, if it cannot connect to the Config Service.

### Auth service
Authorization responsibilities are completely extracted to separate server, which grants [OAuth2 tokens](https://tools.ietf.org/html/rfc6749) for the backend resource services. Auth Server is used for user authorization as well as for secure machine-to-machine communication inside a perimeter.

Spring Cloud Security provides convenient annotations and autoconfiguration to make this really easy to implement from both server and client side.

From the client side, everything works exactly the same as with traditional session-based authorization. You can retrieve `Principal` object from request, check user's roles and other stuff with expression-based access control and `@PreAuthorize` annotation.


### API Gateway
As you can see, there are three core services, which expose external API to client. In a real-world systems, this number can grow very quickly as well as whole system complexity. Actually, hundreds of services might be involved in rendering of one complex webpage.

In theory, a client could make requests to each of the microservices directly. But obviously, there are challenges and limitations with this option, like necessity to know all endpoints addresses, perform http request for each peace of information separately, merge the result on a client side. Another problem is non web-friendly protocols which might be used on the backend.

Usually a much better approach is to use API Gateway. It is a single entry point into the system, used to handle requests by routing them to the appropriate backend service or by invoking multiple backend services and [aggregating the results](http://techblog.netflix.com/2013/01/optimizing-netflix-api.html). Also, it can be used for authentication, insights, stress and canary testing, service migration, static response handling, active traffic management.

Netflix opensourced [such an edge service](http://techblog.netflix.com/2013/06/announcing-zuul-edge-service-in-cloud.html), and now with Spring Cloud we can enable it with one `@EnableZuulProxy` annotation. In this project, I use Zuul to store static content (ui application) and to route requests to appropriate microservices. Here's a simple prefix-based routing configuration for Notification service:

```yml
zuul:
  routes:
    notification-service:
        path: /notifications/**
        serviceId: notification-service
        stripPrefix: false

```

That means all requests starting with `/notifications` will be routed to Notification service. There is no hardcoded address, as you can see. Zuul uses Service discovery, described below.

### Service discovery

Another commonly known architecture pattern is Service discovery. It allows automatic detection of network locations for service instances, which could have dynamically assigned addresses because of auto-scaling, failures and upgrades.

The key part of Service discovery is Registry. I use Netflix Eureka in this project. Eureka is a good example of the client-side discovery pattern, when client is responsible for determining locations of available service instances (using Registry server) and load balancing requests across them.

With Spring Boot, you can easily build Eureka Registry with `spring-cloud-starter-eureka-server` dependency, `@EnableEurekaServer` annotation and simple configuration properties.

Client support enabled with `@EnableDiscoveryClient` annotation an `bootstrap.yml` with application name:
``` yml
spring:
  application:
    name: notification-service
```

Now, on application startup, it will register with Eureka Server and provide meta-data, such as host and port, health indicator URL, home page etc. Eureka receives heartbeat messages from each instance belonging to a service. If the heartbeat fails over a configurable timetable, the instance will be removed from the registry.

Also, Eureka provides a simple interface, where you can track running services and a number of available instances: `http://localhost:8761`

### Load balancer, Circuit breaker and Http client

Netflix OSS provides another great set of tools. 

#### Ribbon
Ribbon is a client side load balancer which gives you a lot of control over the behaviour of HTTP and TCP clients. Compared to a traditional load balancer, there is no need in additional hop for every over-the-wire invocation - you can contact desired service directly.

Out of the box, it natively integrates with Spring Cloud and Service Discovery. Eureka-client provides a dynamic list of available servers so Ribbon could balance between them.

#### Hystrix
Hystrix is the implementation of [Circuit Breaker pattern](http://martinfowler.com/bliki/CircuitBreaker.html), which gives a control over latency and failure from dependencies accessed over the network. The main idea is to stop cascading failures in a distributed environment with a large number of microservices. That helps to fail fast and recover as soon as possible - important aspects of fault-tolerant systems that self-heal.

Besides circuit breaker control, with Hystrix you can add a fallback method that will be called to obtain a default value in case the main command fails.

Moreover, Hystrix generates metrics on execution outcomes and latency for each command, that we can use to monitor system behavior.

#### Feign
Feign is a declarative Http client, which seamlessly integrates with Ribbon and Hystrix. Actually, with one `spring-cloud-starter-feign` dependency and `@EnableFeignClients` annotation you have a full set of Load balancer, Circuit breaker and Http client with sensible ready-to-go default configuration.


- Everything you need is just an interface
- You can share `@RequestMapping` part between Spring MVC controller and Feign methods
- Above example specifies just desired service id - `statistics-service`, thanks to autodiscovery through Eureka (but obviously you can access any resource with a specific url)

### Monitor dashboard
In this project configuration, each microservice with Hystrix on board pushes metrics to Turbine via Spring Cloud Bus (with AMQP broker). The Monitoring project is just a small Spring boot application with [Turbine](https://github.com/Netflix/Turbine) and [Hystrix Dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard).

### Log analysis
Centralized logging can be very useful when attempting to identify problems in a distributed environment. Elasticsearch, Logstash and Kibana stack lets you search and analyze your logs, utilization and network activity data with ease.

### Distributed tracing
Analyzing problems in distributed systems can be difficult, for example, tracing requests that propagate from one microservice to another. It can be quite a challenge to try to find out how a request travels through the system, especially if you don't have any insight into the implementation of a microservice. Even when there is logging, it is hard to tell which action correlates to a single request.

[Spring Cloud Sleuth](https://cloud.spring.io/spring-cloud-sleuth/) solves this problem by providing support for distributed tracing. It adds two types of IDs to the logging: traceId and spanId. The spanId represents a basic unit of work, for example sending an HTTP request. The traceId contains a set of spans forming a tree-like structure. For example, with a distributed big-data store, a trace might be formed by a PUT request. Using traceId and spanId for each operation we know when and where our application is as it processes a request, making reading our logs much easier. 

## Security

An advanced security configuration is beyond the scope of this proof-of-concept project. For a more realistic simulation of a real system, consider to use https, JCE keystore to encrypt Microservices passwords and Config server properties content (see [documentation](http://cloud.spring.io/spring-cloud-config/spring-cloud-config.html#_security) for details).

## Infrastructure automation

Deploying microservices, with their interdependence, is much more complex process than deploying monolithic application. It is important to have fully automated infrastructure. We can achieve following benefits with Continuous Delivery approach:

- The ability to release software anytime
- Any build could end up being a release
- Build artifacts once - deploy as needed

Here is a simple Continuous Delivery workflow example:

<img width="880" src="https://cloud.githubusercontent.com/assets/6069066/14159789/0dd7a7ce-f6e9-11e5-9fbb-a7fe0f4431e3.png">

In this configuration, Travis CI builds tagged images for each successful git push. So, there are always `latest` image for each microservice on DockerHub and older images, tagged with git commit hash. It's easy to deploy any of them and quickly rollback, if needed.

## How to run all the things?

#### Before you start
- Install Docker and Docker Compose.
- Export environment variables: `MONGODB_PASSWORD` (make sure they were exported: `printenv`)
- Make sure to build the project: `mvn package [-DskipTests]`

#### Development mode
run `docker-compose up --build`