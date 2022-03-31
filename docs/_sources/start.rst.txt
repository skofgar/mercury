Getting Started
================

Before You Start
-----------------

If you haven't already, please start a terminal and clone the repository:

.. code-block:: console

    git clone https://github.com/Accenture/mercury.git
    cd mercury

To get the system up and running, you should compile and build the foundation libraries from sources. This will install the libraries into your ".m2/repository/org/platformlambda" folder.

``Important`` \- please close all Java applications and web servers in your PC if any. The build process will invoke unit tests that simulate HTTP and websocket servers.

.. code-block:: console

    # start a terminal and go to the mercury sandbox folder
    mvn clean install

The platform-core, rest-spring, hazelcast-connector and kafka-connector are libraries and you can rebuild each one individually using ``mvn clean install``

The rest of the subprojects are executables that you can rebuild each one with ``mvn clean package``.

Getting Started
----------------
You can compile the rest-example as a microservices executable like this:

.. code-block:: console

    cd mercury/examples
    cd rest-example
    mvn clean package
    java -jar target/rest-example-2.3.2.jar
    # this will run the rest-example without a cloud connector

Try http://127.0.0.1:8083/api/hello/world with a browser.

It will respond with some sample output like this:

.. code-block:: xml

    {
    "body" : {
        "body" : {
        "accept" : "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "accept-encoding" : "gzip, deflate, br",
        "accept-language" : "en-US,en;q=0.9",
        "cache-control" : "max-age=0",
        "connection" : "keep-alive",
        "host" : "127.0.0.1:8083",
        "time" : "2018-12-21T16:39:33.546Z",
        "upgrade-insecure-requests" : "1",
        "user-agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) ..."
        },
        "headers" : {
        "seq" : "4"
        },
        "instance" : 4,
        "origin" : "2018122170c4d0e80ef94b459763636da74d6b5f"
    },
    "execution_time" : 0.298,
    "headers" : { },
    "round_trip" : 0.862,
    "status" : 200
    }

The REST endpoint makes a request to the "hello.world" service that echoes back the headers of the HTTP request. The underlying Spring Boot HTTP server has been pre-configured with HTML, JSON and XML message serializers and exception handlers.

If you change the "accept" header to "application/xml", the output will be shown in XML.

To demonstrate concurrent processing, you may visit http://127.0.0.1:8083/api/hello/concurrent

Now try http://127.0.0.1:8083/api/hello/pojo/1

This will return a HTTP-500 "Route hello.pojo not found" error. This is expected behavior because the "hello.pojo" service is not available in the rest-example container. Let's walk thru distributed and scalable application in the next section.

Scalable application using Kafka
---------------------------------

For development and testing, you can start a standalone Kafka server.

.. code-block:: console

    # start a terminal and go to the mercury sandbox folder, then go to the kafka-standalone folder
    cd mercury/connectors/kafka/kafka-standalone/
    mvn clean package
    java -jar target/kafka-standalone-2.3.2.jar
    # this will run a standalone Kafka server in the foreground

The next step is to start the "presence-monitor" application.

.. code-block:: console

    # start another terminal and go to kafka-presence folder
    cd mercury/connectors/kafka/kafka-presence/
    mvn clean package
    java -jar target/kafka-presence-2.3.2.jar
    # this will run the presence monitor at port 8080 in the foreground

    # when an application instance is started, it connects to the presence monitor to get topic.
    # you will see log similar to the following:
    Adding member 20210405aa0220674e404169a5ec158a99714da6
    Monitor (me) 20210405209f8e20ed3f4c0a80b035a50273b922 begins RSVP
    multiplex.0001-001 assigned to 20210405aa0220674e404169a5ec158a99714da6 lambda-example, 2.1.1
    Monitor (me) 20210405209f8e20ed3f4c0a80b035a50273b922 finished RSVP

Optionally, if you want to test resilience of the presence monitor, you can start another instance like this:

.. code-block:: console

    # start another terminal and go to kafka-presence folder
    cd mercury/connectors/kafka/kafka-presence/
    mvn clean package
    java -Dserver.port=8081 -jar target/kafka-presence-2.3.2.jar
    # this will run the presence monitor at port 8081 in the foreground

You can then run the lambda-example and rest-example applications

.. code-block:: console

    # go to the lambda-example project folder in one terminal
    java -Dcloud.connector=kafka -jar target/lambda-example-2.3.2.jar
    # the lambda-example will connect to the "presence monitor", obtain a topic and connect to Kafka

    # go to the rest-example project folder in another terminal
    java -Dcloud.connector=kafka -jar target/rest-example-2.3.2.jar
    # the rest-example will also connect to the "presence monitor", obtain a topic and connect to Kafka

    # the lambda-example and rest-example apps will show the topic assignment like this
    presence.monitor, partition 1 ready
    multiplex.0001, partition 0 ready

You may visit http://127.0.0.1:8083/api/hello/pojo/1 with a browser, you will see output like this:

.. code-block:: xml

    {
    "id": 1,
    "name": "Simple PoJo class",
    "address": "100 World Blvd, Planet Earth",
    "date": "2021-04-05T22:13:38.351Z",
    "instance": 1,
    "origin": "20210405aa0220674e404169a5ec158a99714da6"
    }

This means the rest-example app accepts the HTTP request and sends the request event to the lambda-example app which in turns return the response event as shown above.

The "cloud.connector=kafka" parameter overrides the application.properties in the rest-example and lambda-example projects so they can select Kafka as the service mesh.

If you use Jetbrains Idea ("IntelliJ") to run your project, please edit the start up config, click 'Modify options' and tick 'add VM options'. This allows you to set default run-time parameters. e.g. "-Dcloud.connector=kafka". Eclipse and other IDE would have similar run-time parameter options.

Get additional info from the presence monitor
You may visit http://127.0.0.1:8080 and select "info". It may look like this:

.. code-block:: xml

    {
    "app": {
        "name": "kafka-presence",
        "description": "Presence Monitor",
        "version": "2.1.1"
    },
    "memory": {
        "max": "8,524,922,880",
        "free": "470,420,400",
        "allocated": "534,773,760"
    },
    "personality": "RESOURCES",
    "additional_info": {
        "total": {
        "virtual_topics": 3,
        "topics": 2,
        "connections": 3
        },
        "virtual_topics": [
        "multiplex.0001-000 -> 2021082260f0b955a9ad427db14a9db751340d61, rest-example v2.1.1",
        "multiplex.0001-001 -> 20210822371949df090644428cd98ae0ba91a69b, lambda-example v2.1.1",
        "multiplex.0001-002 -> 2021082295c030edc07a4a4eb5cb78b6421f5fb7, lambda-example v2.1.1"
        ],
        "topics": [
        "multiplex.0001 (32)",
        "service.monitor (11)"
        ],
        "connections": [
        {
            "created": "2021-08-22T17:29:45Z",
            "origin": "20210822371949df090644428cd98ae0ba91a69b",
            "name": "lambda-example",
            "topic": "multiplex.0001-001",
            "monitor": "20210822c750f194403241c49ef8fae113beff75",
            "type": "APP",
            "updated": "2021-08-22T17:29:53Z",
            "version": "2.1.1",
            "seq": 2,
            "group": 1
        },
        {
            "created": "2021-08-22T17:29:55Z",
            "origin": "2021082295c030edc07a4a4eb5cb78b6421f5fb7",
            "name": "lambda-example",
            "topic": "multiplex.0001-002",
            "monitor": "20210822c750f194403241c49ef8fae113beff75",
            "type": "APP",
            "updated": "2021-08-22T17:30:04Z",
            "version": "2.1.1",
            "seq": 2,
            "group": 1
        },
        {
            "created": "2021-08-22T17:29:30Z",
            "origin": "2021082260f0b955a9ad427db14a9db751340d61",
            "name": "rest-example",
            "topic": "multiplex.0001-000",
            "monitor": "20210822c750f194403241c49ef8fae113beff75",
            "type": "WEB",
            "updated": "2021-08-22T17:30:01Z",
            "version": "2.1.1",
            "seq": 3,
            "group": 1
        }
        ],
        "monitors": [
        "20210822c750f194403241c49ef8fae113beff75 - 2021-08-22T17:30:01Z"
        ]
    },
    "vm": {
        "java_vm_version": "11.0.10+9",
        "java_runtime_version": "11.0.10+9",
        "java_version": "11.0.10"
    },
    "streams": {
        "count": 0
    },
    "origin": "20210822c750f194403241c49ef8fae113beff75",
    "time": {
        "current": "2021-08-22T17:30:06.712Z",
        "start": "2021-08-22T17:28:56.128Z"
    }
    }