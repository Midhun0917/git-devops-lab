
package com.example;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Payment {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(
    System.getenv().getOrDefault("PORT", "8080")
);

HttpServer server = HttpServer.create(
    new InetSocketAddress("0.0.0.0", port), 0
);

        server.createContext("/health", exchange -> {
            byte[] response = "UP".getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(200, response.length);

            try (var os = exchange.getResponseBody()) {
                os.write(response);
            }
        });

        server.createContext("/payment", exchange -> {
            byte[] response =
                "Payment of Rs. 1000 processed successfully!"
                    .getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(200, response.length);

            try (var os = exchange.getResponseBody()) {
                os.write(response);
            }
        });

        server.start();

        System.out.println("Payment application started on port " + port);
    }
}
