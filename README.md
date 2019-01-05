# Mercury - the Post Office for microservices

## Welcome to the Mercury project

The Mercury project is created with one primary objective - `to make software easy to write, read, test, deploy, scale and manage`.

To this end, it introduces the concept of platform abstraction and takes event driven programming to the next level of simplicity.

Everything can be expressed as anonymous functions and they communicate with each other using events. However, event driven and reactive programming can be challenging. The Mercury framework hides all the complexity of event driven and reactive patterns and the magic of inter-service communication.

If you want digital decoupling, this is the technology that you should invest 30 minutes of your time to try it out.

The pre-requisites are very minimal. The foundation technology requires only Java 1.8 JDK or above (Oracle or OpenJDK) and the Maven build system ("mvn"). Docker/Kubernetes are optional. The application modules that you create using the Mercury framework will run in bare metal, VM and any cloud environments.

This project is created by architects and computer scientists who have spent years to perfect software decoupling, scalability and resilience, high performance and massively parallel processing,

With a very high level of decoupling, you can focus in writing business logic without distraction.

Since everything can be expressed as anonymous function, the framework itself is written using this approach. All the cloud connectors and language packs are microservices that are written as anonymous functions. In this way, you can add new connectors, plugins and language packs as you like. The framework is extensible.

The concept is simple. You write your business logic as anonymous functions and packaged them in one or more executables. These executables may be composed as Docker images or alike. You can then deploy them. The containers communicate with each other through an event stream system like Hazelcast or Kafka.

We make the event stream system works as a service mesh. Functions will talk to each other magically without configuration.

If you have your own preference of a different event stream system, you can follow the Hazelcast connector as an example to build your own connector.

Hope you enjoy this journey to improve the world.

Best regards, the Mercury team

December 2018

p.s. This project is originated from the "platformlambda project" and contributed by Accenture.


## Rationale

The microservices movement is gaining a lot of momentum in recent years. Very much inspired with the need to modernize service-oriented architecture and to transform monolithic applications as manageable and reusable pieces, it was first mentioned in 2011 to advocate an architectural style that defines an application as a set of loosely coupled single purpose services.

Classical model of microservices architecture often focuses in the use of REST as interface and the self-containment of data and process. Oftentimes, there is a need for inter-service communication because one service may consume another service. Usually this is done with a service broker. This is an elegant architectural concept. However, many production systems face operational challenges. In reality, it is quite difficult to decompose a solution down to functional level. This applies to both green field development or application modernization. As a result, many microservices modules are indeed smaller subsystems. Within a microservice, business logic is tightly coupled with 3rd party and open sources libraries including cloud platform client components and drivers. This is suboptimal.

## Architecture principles

For simplicity, we advocate 3 architecture principles to write microservices

- minimalist
- event driven
- context bound

Minimalist means we want user software to be as small as possible. The Mercury framework allows you to write business logic down to functional level using simple input-process-output pattern.

Event driven promotes loose coupling. All functions should run concurrently and independently of each other.

Lastly, context bound means high level of encapsulation so that a function only expose API contract and nothing else.

### Platform abstraction

Mercury offers the highest level of decoupling where each piece of business logic can be expressed as an anonymous function. A microservices module is a collection of one or more functions. These functions connect to each other using events.

The framework hides the complexity of event-driven programming and cloud platform integration. For the latter, the service mesh interface is fully encapsulated so that user functions do not need to be aware of network connectivity and details of the cloud platform.

### Simple Input-Process-Output function

This is a best practice in software development. Input is fed into an anonymous function. It will process the input according to some API contract and produce some output.

This simplifies software development and unit tests.

### Post Office

The Mercury framework is a SDK with some cloud connectors and language packs. There are 2 small sets of API for the Platform and the Post Office systems. The Platform API allows you to programmatically register functions with "route names". The Post Office API is used to send events from one service to another.

For example, the following codelet sends the text "hello world" from the caller to the function registered as "v1.some.service".

```
//-------------------------------------------------

// SERVICE in one container
Platform platform = Platform.getInstance();

LambdaFunction f = (headers, body, instance) -> {
	// do some business logic
	System.out.println("I got your request..."+body);
	return something
};
platform.register("v1.some.service", f, 1);

//-------------------------------------------------

// USER in another container
PostOffice po = PostOffice.getInstance();
EventEnvelope response = po.request("v1.some.service", 1000, "hello world");
System.out.println("I got response here..."+response.getBody());

// the above is a RPC call. For async call, it would be something like this:
po.send("v1.some.service", "hello world");

//-------------------------------------------------

```

### Inter-service communication patterns

- RPC `“Request-response”, best for interactivity`
- Asynchronous `e.g. Drop-n-forget and long queries`
- Call back `e.g. Progressive rendering`
- Pipeline `e.g. Work-flow application`
- Streaming `e.g. Data ingest`
- Broadcast `e.g. Concurrent processing of the same dataset with different outcomes`


### How this repository is organized

The Mercury framework includes the following:
1. system `platform-core, rest-core and rest-spring`
2. connectors `event-node, hazelcast, kafka, etc.`
3. language packs `python, node.js, etc.`
4. documentation `architecture and user guides`
5. examples `code samples / templates for creating microservices with or without REST endpoints`


## Libraries and components

`platform core`

With Mercury, any software module can be expressed as a Java anonymous functions or a Java class that implements the LambdaFunction interface.

The platform core library enables the following:
1. High level of decoupling - You may write business logic as anonymous functions that run concurrently and independently. In addition to business logic, you may also encapsulate platform components and databases as anonymous functions.
2. Event driven programming - Each function is addressable with a unique route name and they communicate by sending events.
You make request from one function to another by making a service call through some simple Mercury Post Office API.
3. One or more functions can be packaged together as a microservice executable, usually deployed as a Docker image or similar container technology.

`rest-core`

The rest-core library encapsulates JAX-RS.

`rest-spring`

The rest-spring library customizes and simplifies the use of Spring Boot as an application container to hold functions. This includes preconfigured message serializers and exception handlers.

`hazelcast-connector`

This connector library is designed to work with Hazelcast version 3.11.1 out of the box. To use this connector, you can download Hazelcast from https://hazelcast.org/download/

`hazelcast-presence`

This project can be compiled into an executable JAR. This is the "presence monitor" for Hazelcast. Your application instances will report to one of the presence monitors (2 to 3 monitor instances are good enough for large installations), the monitors use Hazelcast to cluster themselves. The Post Office in the platform-core will use the Hazelcast-connector to send events to other application instances thru the Hazelcast cluster.

The hazelcast-connector and hazelcast-presence is fully scalable.

`kafka-connector` and `kafka-presence` - these are upcoming cloud connection modules for higher performance needs.

`event-node`

For ease of software development, the Event Node project can be compiled into an executable JAR. It emulates the event stream system (i.e. the event stream itself such as Kafka and Hazelcast) and the presence monitor.

The Event Node is not designed for production purpose. It is a convenient tool for software development in a single laptop.

`lambda-example`

This is an example project to illustrate writing microservices without using a HTTP application server.

`rest-example`

This is an example project that may be used as a template to write microservices container that provides REST endpoints using a light-weight application server.


## Before you start

To get the system up and running, you should compile and build the foundation libraries from sources. This will install the libraries into your ".m2/repository/org/platformlambda" folder. For your convenience, we will be publishing these libraries into a public repository at a later time.

```
# start a terminal and go the the mercury sandbox folder
cd mercury/system
cd platform-core
mvn clean install
cd ..
cd rest-core
mvn clean install
cd ..
cd rest-spring
mvn clean install
cd ..
cd ..
cd connectors
cd hazelcast/hazelcast-connector
mvn clean install
# close the terminal
```

## Getting started


You can compile the rest-example as a microservices executable like this:

```
cd mercury/examples
cd rest-example
mvn clean package
java -Dcloud.connector=none -jar target/rest-example-1.11.28.jar
# this will run the rest-example without a cloud connector
```

Try http://127.0.0.1:8083/api/hello/world with a browser.

It will respond with some sample output like this:

```
{
  "body" : {
    "body" : {
      "accept" : "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
      "accept-encoding" : "gzip, deflate, br",
      "accept-language" : "en-US,en;q=0.9",
      "cache-control" : "max-age=0",
      "connection" : "keep-alive",
      "cookie" : "_ga=GA1.4.1745506134.1545067415",
      "host" : "127.0.0.1:8083",
      "time" : "2018-12-21T16:39:33.546Z",
      "upgrade-insecure-requests" : "1",
      "user-agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36"
    },
    "headers" : {
      "seq" : "4"
    },
    "instance" : 4,
    "origin" : "2018122170c4d0e80ef94b459763636da74d6b5f"
  },
  "execution_time" : 0.298636,
  "headers" : { },
  "round_trip" : 0.862446,
  "status" : 200
}
```

The REST endpoint makes a request to the "hello.world" service that echoes back the headers of the HTTP request. The underlying Spring Boot HTTP server has been pre-configured with HTML, JSON and XML message serializers and exception handlers.

If you change the "accept" header to "application/xml", the output will be shown in XML.

To demonstrate concurrent processing, you may visit http://127.0.0.1:8083/api/hello/concurrent

Now try http://127.0.0.1:8083/api/hello/pojo/1

This will return a HTTP-500 "Route hello.pojo not found" error. This is expected behavior because the "hello.pojo" service is not available in the rest-example container.

### Scalable application using Event Node for prototyping

Now it is time to build a scalable application using an event stream system. 
For simplicity, we are going to use the Event Node system to emulate a cloud environment.

```
cd mercury/connectors
cd event-node
mvn clean package
java -jar target/event-node-1.11.28.jar
# the Event Node system will run. It emulates an event stream system.

# Open another terminal and go to the project root
cd mercury/examples
cd lambda-example
mvn clean package
java -jar target/lambda-example-1.11.28.jar
# the lambda-example microservices module will run and connect to the event node

# Go to the terminal that runs the rest-example earlier
CTL-C to quit the rest-example application
# Then run the rest-example again
java -jar target/rest-example-1.11.28.jar
# without the "-Dcloud.connector" parameter override, the rest-example will run and connect to the event node.

```

Try http://127.0.0.1:8083/api/hello/pojo/1 again. You will see response like this:

```
{
  "address" : "100 World Blvd, Planet Earth",
  "date" : "2018-12-21T16:56:29.774Z",
  "id" : 1,
  "instance" : 1,
  "name" : "Simple PoJo class",
  "origin" : "2018122125cea36ffffc42729cffd416d35f92b4"
}
```

Congratulations! You have just created a network of microservices and consume the "hello.pojo" service thru a network event stream system.

If you run another instance of the lambda-example in a different terminal and try the endpoint http://127.0.0.1:8083/api/hello/pojo/1 again, you will see the "origin" of the response changes. This illustrates that the system is load balancing your request to multiple containers.

### Truly scalable application

The Event Node is designed for rapid prototyping so the event stream system is emulated in one laptop.

Now CTL-C all terminals to quit the demo. We are going to do it again with Hazelcast as the event stream system.

- Download Hazelcast from https://hazelcast.org/download/
- Go to the "bin" folder of the unzipped folder.
- start hazelcast with "start.bat" for windows or "./start.sh" for Mac and Linux. The Hazelcast system will start.

You will see member list like this in the terminal.

```
Members {size:1, ver:1} [
	Member [10.227.179.104]:5701 - 3c253cd0-13eb-4919-aa67-b75d22c86408 this
]
```

- copy the hazelcast folder to another folder.
- start the second copy of hazelcast. A cluster of 2 hazelcast nodes will form right away. They serve ports 5701 and 5702. The member list will have 2 entries.

- Build the hazelcast-presence project

```
cd mercury/connectors
cd hazelcast/hazelcast-presence
mvn clean package
java -jar target/hazelcast-presence-1.11.28.jar
# this will start the "presence monitor" that will connect to the hazelcast cluster.
```

- Run the lambda-example and rest-example again

```
# go to the lambda-example project folder in one terminal
java -Dcloud.connector=hazelcast -jar target/lambda-example-1.11.28.jar
# the lambda-example will connect to the hazelcast cluster and the "presence monitor"

# go to the rest-example project folder in another terminal
java -Dcloud.connector=hazelcast -jar target/rest-example-1.11.28.jar
# the rest-example will also connect to the hazelcast cluster and the "presence monitor"

```

You may visit http://127.0.0.1:8083/api/hello/pojo/1. The output is exactly the same as the earlier demo.

The "cloud.connector=hazelcast" parameter overrides the application.properties in the rest-example and lambda-example projects.

- Get additional info from the presence monitor

You may visit http://127.0.0.1:8080/info to see connection info. It may look like this:


```
{
  "additional.info" : {
    "connections" : {
      "201812213aed6381e8b543d48f3f288f64207019" : {
        "created" : "2018-12-21T17:09:54Z",
        "monitor" : "20181221012a5f4614c3443280936b78ff032e51",
        "name" : "lambda-example",
        "seq" : 123,
        "type" : "APP",
        "updated" : "2018-12-21T17:51:01Z",
        "version" : "1.11.28"
      },
      "201812215ff40bbc36004637ac8cd18debf5cf95" : {
        "created" : "2018-12-21T17:11:49Z",
        "monitor" : "20181221012a5f4614c3443280936b78ff032e51",
        "name" : "rest-example",
        "seq" : 117,
        "type" : "WEB",
        "updated" : "2018-12-21T17:50:55Z",
        "version" : "1.11.28"
      }
    },
    "topics" : [ "201812215ff40bbc36004637ac8cd18debf5cf95", "201812213aed6381e8b543d48f3f288f64207019" ],
    "total" : {
      "connections" : 2,
      "topics" : 2
    }
  },
  "app" : {
    "description" : "Presence Monitor",
    "name" : "hazelcast-presence",
    "version" : "1.11.28"
  },
  "memory" : {
    "allocated" : "737,673,216",
    "free" : "326,572,008",
    "max" : "3,817,865,216",
    "total" : "3,406,764,008"
  },
  "origin" : "20181221012a5f4614c3443280936b78ff032e51",
  "time" : "2018-12-21T17:51:08.808Z",
  "vm" : {
    "java_runtime_version" : "1.8.0_171-b11",
    "java_version" : "1.8.0_171",
    "java_vm_version" : "25.171-b11"
  }
}
```

You may also check the health status of the presence monitor by visiting http://127.0.0.1:8080/health

```
{
  "status" : "UP",
  "upstream" : [ {
    "cluster" : [ "127.0.0.1:5701", "127.0.0.1:5702" ],
    "message" : "Loopback test took 1 ms",
    "namespace" : "connector",
    "required" : true,
    "route" : "cloud.connector.health",
    "service" : "hazelcast",
    "statusCode" : 200
  } ]
}
```


## Write your own microservices

You may use the lambda-example and rest-example as templates to write your own applications.

Please update pom.xml and application.properties for application name accordingly.

## Cloud Native

The Mercury framework is Cloud Native. While it uses the local file system for buffering, it expects local storage to be transcient.

If your application needs to use the local file system, please consider it to be transcient too. i.e. you cannot rely on it to persist when your application restarts.

If there is a need for data persistence, use external databases or cloud storage.

## Other consideration

- Timestamp: the Mercury system uses UTC time and ISO-8601 string representation when doing serialization. https://en.wikipedia.org/wiki/ISO_8601

- UTF8 text encoding: we recommend the use of UTF8 for text strings.