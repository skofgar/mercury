Libraries and Components
=========================

``platform core``

With Mercury, any software module can be expressed as an anonymous functions or a Java class that implements the LambdaFunction interface.

The platform core library enables the following:

1. High level of decoupling \- You may write business logic as anonymous functions that run concurrently and independently. In addition to business logic, you may also encapsulate platform components and databases as anonymous functions.

2. Event driven programming \- Each function is addressable with a unique "route name" and they communicate by sending events. You make request from one function to another by making a service call through some simple Mercury Post Office API.

3. One or more functions can be packaged together as a microservice executable, usually deployed as a Docker image or similar container technology.

-------------

``rest-spring``

The rest-spring library customizes and simplifies the use of Spring Boot as an application container to hold functions. This includes preconfigured message serializers and exception handlers.

-------------

``cloud-connector`` and ``service-monitor``

The cloud-connector and service-monitor are the core libraries to support implementations of various cloud connectors.

Mercury supports Kafka, Hazelcast, ActiveMQ-artemis and TIBCO-EMS out of the box.

``kafka-connector``

-------------

This is the kafka specific connector library is designed to work with Kafka version 2.7.0 out of the box that you add it into your application's pom.xml. A convenient standalone Kafka server application is available in the kafka-standalone project under the connector/kafka directory. The standalone Kafka server is a convenient tool for application development and tests in the developer's laptop.

``kafka-presence``

-------------

This "presence monitor" manages mapping of Kafka topics and detects when an application instance is online or offline. Your application instances will report to the presence monitors (2 monitor instances are enough for large installations). Since version 2.0, it uses a single partition of a topic to serve an application instance. To avoid deleting topics, the system can re-use topics and partitions automatically.

The kafka-presence system is fully scalable.

``language-connector``

-------------

The python language pack is available in https://github.com/Accenture/mercury-python

``rest-automation``

-------------

This extension package is a system that automates the creation of REST endpoints by configuration instead of code. Not only it eliminates the repetitive work of writing REST endpoints, it makes HTTP completely non-blocking.

``distributed-tracer``

-------------

This extension package is an example application that consolidates distributed trace metrics.

``lambda-example``

-------------

This is an example project to illustrate writing microservices without any HTTP application server.

``rest-example``

-------------

If your application module needs info and health admin endpoints, you may want to use this example project as a template.

It encapsulates Spring Boot, automates JSON/XML/PoJo serialization and simplfies application development. It also allows you to write custom REST endpoints using WebServlet and JAX-RS REST annotations.

For rapid application development, we do recommend you to use the REST automation system to define REST endpoints by configuration rather than code.