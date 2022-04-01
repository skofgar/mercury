How-Tos
=============
Microservices and anonymous function
-------------------------------------------
Mercury is all about microservices that are minimalist, event-driven and context bounded.

To this end, we use anonymous functions to encapsulate different domains of business logic and library dependencies.

Under the Mercury framework, business logic wrapped in anonymous functions are callable using a ``route name``. Mercury resolves routing automatically so that you do not need to care whether the calling and called functions are in the same memory space or in different application instances. Mercury will route requests using a high performance memory event bus when the calling and called functions are in the same memory space and route requests through a network event stream system when the calling and called parties reside in different containers.

Writing your first microservices function
-------------------------------------------

Your first function may look like this using Java 1.8 anonymous function syntax:

.. code-block:: java

    LambdaFunction f = (headers, body, instance) -> {
        // do some business logic
        return something
    };

The easiest way to write your first microservices module is to use either the "lambda-example" or "rest-example" as a template.

Let's try with the "rest-example". You should update the application name in both the ``application.properties`` and the ``pom.xml``. Then you can use your favorite IDE to import it as a "maven" project.

Application unit
-----------------

The rest-example is a deployable application unit. Behind the curtain, the mercury framework is using Spring Boot to provide REST and websocket capabilities. For microservices modules that do not need REST endpoints, you can use the "lambda-example" as a template.

Main application
-----------------

For each application unit, you will need a main application. This is the entry of your application unit.

The ``MainApplication`` annotation indicates that this is the main method for the application unit. Main application should also implements the ``EntryPoint`` interface which only has the "start" method. The "args" are optional command line arguments.

In the following example, when the application unit starts, it creates a microservices function and register "hello.world" as its route name. For concurrency, it also specifies 20 worker instances.

Application units are horizontally scalable. Within the application unit, you may specify concurrent "workers". This provides horizontal and verticial scalability respectively.

.. code-block:: java
    :linenos:

    @MainApplication
    public class MainApp implements EntryPoint {
    
        @Override
        public void start(String[] args) throws Exception {
            ServerPersonality.getInstance().setType(ServerPersonality.Type.WEB);
            Platform platform = Platform.getInstance();
            LambdaFunction echo = (headers, body, instance) -> {
                Map<String, Object> result = new HashMap<>();
                result.put("headers", headers);
                result.put("body", body);
                result.put("instance", instance);
                result.put("origin", platform.getOrigin());
                return result;
            };
            platform.register("hello.world", echo, 20);
        }
    }

Alternatively, for typed body and response without casting you can use TypedLambdaFunction<I, O> where I and O stand for input and output class respectively.

.. code-block:: java

    TypedLambdaFunction<String, Map<String, String>> lambda = (headers, body, instance) -> {
        Map<String, String> result = new HashMap<>();
        result.put("body", body);
        return result;
    };

Calling a function
-------------------

Unlike traditional programming, you call a function by sending an event instead of calling its method. Mercury resolves routing automatically so events are delivered correctly no matter where the target function is, in the same memory space or another computer elsewhere in the network.

To make a service call to a function, you may do the following:

.. code-block:: java

    PostOffice po = PostOffice.getInstance();
    EventEnvelope response = po.request("hello.world", 1000, "a test message");
    System.out.println("I got response here..."+response.getBody());

    // the above is an RPC call. For async call, it would be something like this:
    po.send("hello.world", "another message");


You can call the function from another function or a REST endpoint. The latter connects REST API with a microservices function.

The following example forwards a request from the REST endpoint ``(GET /api/hello/world)`` to the "hello.world" service. Note that there are basic performance metrics from the response object.

.. code-block:: java
    :linenos: 

    @Path("/hello")
    public class MyRestEndpoint {

        private static AtomicInteger seq = new AtomicInteger(0);

        @GET
        @Path("/world")
        @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
        public Map<String, Object> hello(@Context HttpServletRequest request) throws IOException, TimeoutException, AppException {

            PostOffice po = PostOffice.getInstance();

            Map<String, Object> forward = new HashMap<>();
            forward.put("time", new Date());

            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String key = headers.nextElement();
                forward.put(key, request.getHeader(key));
            }
            // As a demo, just put the incoming HTTP headers as a payload and a parameter showing the sequence counter.
            // The eco service will return both.
            int n = seq.incrementAndGet();
            EventEnvelope response = po.request("hello.world", 3000, forward, new Kv("seq", n));

            Map<String, Object> result = new HashMap<>();
            result.put("status", response.getStatus());
            result.put("headers", response.getHeaders());
            result.put("body", response.getBody());
            result.put("execution_time", response.getExecutionTime());
            result.put("round_trip", response.getRoundTrip());
            return result;
        }

    }

Massive parallel processing
----------------------------

A function is invoked when an event happens. Before the event arrives, the function is just an entry in a routing table, and it does not consume any additional resources like threads.

All functions are running in parallel without special coding. Behind the curtain, the system uses Java futures and asynchronous event loops for very efficient function execution.

Built-in service mesh
----------------------------
The above demonstrates distributed applications using Kafka as a service mesh.

Built-in pub/sub
----------------------------
You can also use Mercury with other service mesh of your choice. In this case, you can use the built-in pub/sub APIs of Mercury for your app to communicate with Kafka and other event stream systems.

To enable Kafka pub/sub without using it as a service mesh, use these parameters in application.properties

cloud.connector=none
cloud.services=kafka.pubsub
This means the system encapsulates the original pub/sub feature of the underlying event stream system. The built-in publishers and listeners will do the heavy lifting for you in a consistent manner. Note that Kafka supports rewinding read "offset" so that your application can read older messages. In Hazelcast, the older events are dropped after delivery.

Example:

.. code-block:: java 

    // setup your subscriber function
    LambdaFunction myFunction = (headers, body, instance) -> {
    log.info("Got ---> {}", body);
    return true;
    };

.. code-block:: java

    PubSub ps = PubSub.getInstance();
    /*
    * Pub/sub service starts asynchronously.
    * If your runs pub/sub code before the container is completely initialized, 
    * you may want to "waitForProvider" for a few seconds.
    */
    ps.waitForProvider(10); 
    // this creates a topic with one partition
    ps.createTopic("some.kafka.topic", 1); 
    // this subscribe the topic with your function
    ps.subscribe("some.kafka.topic", myFunction, "client-101", "group-101");
    // this publishes an event to the topic
    ps.publish("some.kafka.topic", null, "my test message");

If you run this application for the first time and you do not see the test message, the kafka topic has just been created when your application starts. Due to racing condition, Kafka would skip the offset and you cannot see the first message. Just restart the application and you will see your test message.

However, if you create the topic administratively before running this test app, your app will always show the first test message. This is a normal Kafka behavior.

You may also notice that the Kafka client sets the read offset to the latest pointer. To read from the beginning, you may reset the read offset by adding a parameter "0" after the clientId and groupId in the subscribe statement above.

Work nicely with reactive frameworks
-------------------------------------

Mercury provides a stream abstraction that can be used with reactive frameworks.

For example, developers using Spring reactor with Mercury may setup a stream between two app modules within the same container or in different containers like this:

.. code-block: java

    // at the producer app container
    ObjectStreamIO producer = new ObjectStreamIO(TIMEOUT_IN_SECONDS);
    ObjectStreamWriter out = producer.getOutputStream();
    out.write("hello"); // you can send text, bytes, Map or PoJo
    out.write("world");
    out.close(); // to indicate end-of-stream
            
    String streamId = producer.getRoute();
    // deliver the streamId to the consumer using PostOffice
    //
    // at the consumer app container
    ObjectStreamIO consumer = new ObjectStreamIO(streamId);
    ObjectStreamReader in = consumer.getInputStream(TIMEOUT_IN_MILLISECONDS);
    Flux.fromIterable(in).log()
        .doOnNext((d) -> {
            // handle data block
        }).doOnError((e) -> {
            // handle exception
        }).doOnComplete(() -> {
            // handle completion
            in.close(); // close I/O stream
        }).subscribeOn(Schedulers.parallel()).subscribe();

Write your own microservices
----------------------------

You may use the lambda-example and rest-example as templates to write your own applications.

Please update pom.xml and application.properties for application name accordingly.

Cloud Native
----------------------------

The Mercury framework is Cloud Native. While it uses the local file system for buffering, it expects local storage to be transient.

If your application needs to use the local file system, please consider it to be transient too, i.e., you cannot rely on it to persist when your application restarts.

If there is a need for data persistence, use external databases or cloud storage.

Dockerfile
----------------------------
Creating a docker image from the executable is easy. First you need to build your application as an executable with the command mvn clean package. The executable JAR is then available in the target directory.

The Dockerfile may look like this:

.. code-block:: dockerfile

    FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine
    EXPOSE 8083
    WORKDIR /app
    COPY target/your-app-name.jar .
    ENTRYPOINT ["java","-jar","your-app-name.jar"]

To build a new docker image locally:

.. code-block:: docker

    docker build -t your-app-name:latest .

To run the newly built docker image, try this:

.. code-block:: docker

    docker run -p 8083:8083 -i -t your-app-name:latest

Change the exposed port numnber and application name accordingly. Then build the docker image and publish it to a docker registry so you can deploy from there using Kubernetes or alike.

For security reason, you may want to customize the docker file to use non-priliveged Unix user account. Please consult your company's enterprise architecture team for container management policy.

VM or bare metal deployment
----------------------------

If you are deploying the application executables in a VM or bare metal, we recommend using a cross-platform process manager. The system has been tested with "pm2" (https://www.npmjs.com/package/pm2).

A sample process.json file is shown below. Please edit the file accordingly. You may add "-D" or "-X" parameters before the "-jar" parameter. To start the application executable, please do pm2 start my-app.json.

You may create individual process.json for each executable and start them one-by-one. You can then monitor the processes with pm2 list or pm2 monit.

To deploy using pm2, please browse the pm2-example folder as a starting point.

Distributed tracing
----------------------------
Microservices are likely to be deployed in a multi-tier environment. As a result, a single transaction may pass through multiple services.

Distributed tracing allows us to visualize the complete service path for each transaction. This enables easy trouble shooting for large scale applications.

With the Mercury framework, distributed tracing does not require code at application level. To enable this feature, you can simply set "tracing=true" in the rest.yaml configuration of the rest-automation application.
