Compatability
==============

JDK Compatability
------------------
The Mercury project, with the exception of the ``kafka-standalone`` subproject, has been tested with OpenJDK and AdaptOpenJDK version 8 to 16.

Please use Java version 11 or higher to run the kafka-standalone application which is provided as a convenient tool for development and testing. The kafka standalone server would fail due to a known memory mapping bug when running under Java version 1.8.0_292.

Kafka Compatability
--------------------
As of December 2020, Mercury has been tested and deployed with Apache Kafka, Confluent Kafka, IBM Event Streams and Microsoft Azure Event Hubs.

Other Consideration
--------------------

- Timestamp: the Mercury system uses UTC time and ISO-8601 string representation when doing serialization. https://en.wikipedia.org/wiki/ISO_8601

- UTF8 text encoding: we recommend the use of UTF8 for text strings.