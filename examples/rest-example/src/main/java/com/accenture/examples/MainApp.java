package com.accenture.examples;

import com.accenture.examples.services.DemoMath;
import org.platformlambda.core.annotations.MainApplication;
import org.platformlambda.core.models.EntryPoint;
import org.platformlambda.core.models.LambdaFunction;
import org.platformlambda.core.system.Platform;
import org.platformlambda.core.system.ServerPersonality;
import org.platformlambda.rest.RestServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@MainApplication
public class MainApp implements EntryPoint {
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {
        RestServer.main(args);
    }

    @Override
    public void start(String[] args) throws Exception {

        // Set personality to WEB - this must be done in the beginning
        ServerPersonality.getInstance().setType(ServerPersonality.Type.WEB);

        Platform platform = Platform.getInstance();
        // you can write simple service using anonymous function
        LambdaFunction echo = (headers, body, instance) -> {
            log.info("echo @"+instance+" received - "+headers+", "+body);

            // your response object can be a Java primitive, hashmap or PoJo. No need to use JSON internally.
            Map<String, Object> result = new HashMap<>();
            result.put("headers", headers);
            result.put("body", body);
            result.put("instance", instance);
            result.put("origin", platform.getOrigin());
            return result;
        };
        // register the above echo service with 10 instances within a single execution/deployment unit.
        // Each deployment unit can be scaled horizontaly by the cloud.
        platform.register("hello.world", echo, 10);
        // Suppose DemoMath is more complex so we write it as a Java class implementing the LambdaFunction interface.
        platform.register("math.addition", new DemoMath(), 5);

        /*
         * for local testing using event node:
         *
         * 1. set these parameters in application.properties
         *    cloud.connector=event.node
         *    event.node.path=ws://127.0.0.1:8080/ws/events/
         *
         * 2. platform.connectToCloud() - this will connect to event node
         *
         * for deployment to IBM bluemix
         *
         * 1. set these parameters in application.properties
         *    cloud.connector=message.hub
         *    cloud.com.accenture.examples.services=bluemix.redis,bluemix.reporter
         *
         * 2. platform.startCloudServices() - this will start MessageHub, Redis and Service Reporter
         *
         * 3. platform.connectToCloud() - this will connect to Bluemix
         *
         */

//        platform.startCloudServices();

        // connect to the network event streams so it can automatically discover other services
        platform.connectToCloud();

    }

}