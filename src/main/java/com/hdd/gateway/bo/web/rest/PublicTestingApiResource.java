package com.hdd.gateway.bo.web.rest;

import io.swagger.annotations.ApiParam;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public")
public class PublicTestingApiResource {

    private final Logger log = LoggerFactory.getLogger(PublicTestingApiResource.class);

    private final String SHARED_FOLDER_PATH = "/test-volume/";

    private String uniqueID = null;

    @GetMapping("/hello-world")
    public Mono<ResponseEntity<String>> getHelloWorld() {
        String uid = getUniqueId();
        try (BufferedReader reader = new BufferedReader(new FileReader(SHARED_FOLDER_PATH + "hello-world"))) {
            String data = uid + reader.lines().collect(Collectors.joining("\n"));
            return Mono.just(ResponseEntity.ok().body(data));
        } catch (IOException e) {
            log.error("Failed to read file", e);
            return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
        }
    }

    // use get here for easier testing
    @GetMapping("/write-hello-world")
    public Mono<ResponseEntity<String>> writeHelloWorld(@ApiParam String content) {
        String uid = getUniqueId();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SHARED_FOLDER_PATH + "hello-world"))) {
            writer.write(content);
            return Mono.just(ResponseEntity.ok().body(uid + "OK"));
        } catch (IOException e) {
            log.error("Failed to write file", e);
            return Mono.just(ResponseEntity.badRequest().body(uid + "NOK"));
        }
    }

    private String getUniqueId() {
        if (uniqueID == null) {
            synchronized (this) {
                if (uniqueID == null) {
                    uniqueID = "From Unique ID: " + UUID.randomUUID().toString() + "\n";
                }
            }
        }
        return uniqueID;
    }
}
