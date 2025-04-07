package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;

    public class testLogin {
        public static void main(String[] args) throws Exception {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/user/login"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString("{\"username\": \"root\", \"password\": \"$2a$10$7Qjz8X1x/5H9N7orO3n5UuT1oG0IlXc9S9YkY2wyXr1JzG9nF2I9e\"}"))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        }
    }

