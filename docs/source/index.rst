Welcome to the Mercury project!
===================================

The Mercury project is created with one primary objective - ``to make software easy to write, read, test, deploy, scale and manage.``

If you want digital decoupling, this is the technology that you should invest 30 minutes of your time to get familiar with.

This project is created by architects and computer scientists who have spent years to perfect software decoupling, scalability and resilience, high performance and massively parallel processing, with a very high level of decoupling, you can focus in writing business logic without distraction.

It introduces the concept of platform abstraction and takes event driven programming to the next level of simplicity and sophistication.

Everything can be expressed as anonymous functions and they communicate with each other using events. This includes turning synchronous HTTP requests and responses into async events using the REST automation system. However, event driven and reactive programming can be challenging. The Mercury framework hides all the complexity of event driven and reactive patterns and the magic of inter-service communication.

Since everything can be expressed as anonymous functions, the framework itself is written using this approach, including the cloud connectors and language pack in the project. In this way, you can add new connectors, plugins and language packs as you like. The framework is extensible.

Mercury supports unlimited service route names on top of event stream and messaging systems such as Kafka, Hazelcast, Tibco EMS and ActiveMQ artemis. While we make the event stream system works as a service mesh, Mercury can be used in standalone mode for applications that use pub/sub directly.

In fact, you can encapsulate other event stream or even enterprise service bus (ESB) with Mercury. Just use the connectors as examples. It would make your ESB runs like an event stream system for RPC, async, callback, streaming, pipeline and pub/sub use cases.

The pre-requisites are minimal. The foundation technology requires only Java (OpenJDK 8 or higher) and the Maven build system ("mvn"). Docker/Kubernetes are optional. The application modules that you create using the Mercury framework will run in bare metal, VM and any cloud environments.

Best regards, the Mercury team, Accenture

January 2022


Check out the Developer Guide section for further information.

.. note::

   This project is under active development.

Contents
--------

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Introduction

   Getting Started <start>
   Architecture <architecture>
   Libraries and Components <library>
   Compatability <compatability>
   Terminology <terminology>

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Developer Guide

   Pragmatic Guide <How-tos>
   Application Properties <app-properties>
   Reserved Route Names <route-names>

.. toctree::
   :maxdepth: 2
   :hidden:
   :caption: Features

   Platform API <platform-api>
   Post Office API <post-office-api> 
   Rest Automation <rest-automation>
   JAX-RS and WebServlets <jax-rs>
   Cloud Connectors <connectors>
   Multicast <multicast>
   Additonal Features <additonal-features>
   Version Control <version-control>
   
Mercury has its documentation hosted on Read the Docs.
