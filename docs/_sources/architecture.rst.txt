Architecture
=============

Rationale
-------------------------------------------
The microservices movement is gaining a lot of momentum in recent years. Very much inspired with the need t o modernize service-oriented architecture and to transform monolithic applications as manageable and reusable pieces, it was first mentioned in 2011 for an architectural style that defines an application as a set of loosely coupled single purpose services.

Classical model of microservices architecture often focuses in the use of REST as interface and the self-containment of data and process. Oftentimes, there is a need for inter-service communication because one service may consume another service. Usually this is done with a service broker. This is an elegant architectural concept. However, many production systems face operational challenges. In reality, it is quite difficult to decompose a solution down to functional level. This applies to both green field development or application modernization. As a result, many microservices modules are indeed smaller subsystems. Within a traditional microservice, business logic is tightly coupled with 3rd party and open sources libraries including cloud platform client components and drivers. This is suboptimal.

User friendly reactive programming
-------------------------------------------
Summary

Completely and natively event driven
Fully open source - Apache-2.0 license
Simple configuration and simple API hides all the scaffolding code
Code naturally, without worrying about asynchrony
Naturally code in functional style, which makes code very portable
Mix and match languages - currently Java and Python are supported, Node.js and dotnet are WIP
Built-in distributed tracing
Avoid going to the network when running in the same process, a huge performance boost for latency critical applications
Doesn't require all services to adopt, will provide the same benefits to those that do adopt it
Learn and start coding in less than one hour

Benefits

Developer productivity - both authoring and maintenance
Improves latency of calls within the same process
Enables fully reactive implementations
Scalability
Portability of code between containers and clouds
Observability - know who is calling who and when, know where you are in workflows
Improves future-proofing - as better eventing libraries/frameworks/platforms become available, they can be brought in without changing a line of code
Features

Multiple languages supported - currently Java and Python are supported, Node.js and dotnet are WIP
Plug in your favorite event stream. Popular ones are already supported.
Multiple eventing patterns supported - pub/sub, broadcast, command, etc.
REST automation for event-based services
Built in distributed tracing

Gnarly Use Cases solved with Mercury

Workflows with event-based architecture, utilizing Saga pattern
Support of "closed user groups" for added security

Architecture principles
-------------------------------------------
For simplicity, we advocate 3 architecture principles to write microservices

minimalist
event driven
context bound
Minimalist means we want user software to be as small as possible. The Mercury framework allows you to write business logic down to functional level using simple input-process-output pattern.

Event driven promotes loose coupling. All functions should run concurrently and independently of each other.

Lastly, context bound means high level of encapsulation so that a function only expose API contract and nothing else.

Platform abstraction
-------------------------------------------
Mercury offers the highest level of decoupling where each piece of business logic can be expressed as an anonymous function. A microservices module is a collection of one or more functions. These functions connect to each other using events.

The framework hides the complexity of event-driven programming and cloud platform integration. For the latter, the service mesh interface is fully encapsulated so that user functions do not need to be aware of network connectivity and details of the cloud platform.

Simple Input-Process-Output function
-------------------------------------------
This is a best practice in software development. Input is fed into an anonymous function. It will process the input according to some API contract and produce some output.

This simplifies software development and unit tests.
