Terminology
============

1. ``Microservices`` \- Generally, we use this term to refer to an architectural pattern where business logic is encapsulated in a bounded context, the unit of service is minimalist and services are consumed in an event driven manner.

2. ``Microservices function`` or simply ``function`` \- This refers to the smallest unit that encapsulates business logic or 3rd party library. As a best practice, we advocate clear separation of business logic from proprietary libraries. By wrapping 3rd party libaries as functions, we can keep the event API intact when switching to an alternative library.

3. ``Microservices module`` or ``microservice`` \- This refers to a single unit of deployment that contains at least one public function and optionally some private functions. All functions communicate with each others through a memory based event stream system within the module. Each microservices module can be independently deployed and scaled.

4. ``Microservices application`` \- It is a collection of microservices modules running together as a single application.

5. ``User facing endpoints`` \- This refers to public API for REST, websocket or other communication protocols.

6. ``Event API`` \- Microservices functions expose event APIs internally for inter-service communications. For example, a user facing endpoint may use REST protocol. The HTTP request is then converted to an event to a microservice.

7. ``Real-time events`` \- Messages that are time sensitive. e.g. RPC requests and responses.

8. ``Store-n-forward events`` \- Events that are not time sensitive. First, a calling function can send an event without waiting for a response. Second, the called function may not even available when the event is delivered. e.g. data pipeline applications.

9. ``Streaming events`` \- This refers to continuous flow of events from one function to another. Note that streaming can be either real-time or store-n-forward.

10. ``public function`` \- This means the function is visible to all application containers and instances in the system.

11. ``private function`` \- This means the function is visible only to functions in the same memory space inside an application instance.

12. ``route name`` \- Each service can be uniquely identified with a name.

13. ``closed user groups`` \- Conceptually, we treat the underlying event stream system as a conduit where one group of application instances can be logically separated from another group.