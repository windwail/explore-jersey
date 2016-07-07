package com.example;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/myapp/";

    public static HttpServer server;

    public static ResourceConfig config;

    public static Set<Class<?>> classes = new HashSet<>();

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static void startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package

        classes.add(MyResource.class);

        config = new ResourceConfig(classes);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        running = false;
    }

    private static volatile boolean running = true;

    private static Thread runner = new Thread() {
        @Override
        public void run() {
            while(running);

            System.out.println("Stopping...");
            if(server!=null && server.isStarted()) {
                server.shutdownNow();
            }

        }
    };

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {

        startServer();

        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));

        runner.start();
    }
}

