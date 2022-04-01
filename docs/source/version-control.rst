Version Control
================

Sometimes, it would be useful to deploy more than one version of the same service in an environment.

This allows us to test new versions without impacting existing functionality.

Version control is supported in the REST automation system and the Post Office event core engine.

To do that, we can add a prefix or suffix to a service route name.

For example, the service route name is originally "hello.world". The versioned services would be:

.. code-block::

    v1.hello.world - the current version
    v2.hello.world - the next version of the service with functional improvement or bug fix

There are two ways to do versioning:

1. REST automation
2. Route substitution

Let's examine how to do versioning from the REST automation system.