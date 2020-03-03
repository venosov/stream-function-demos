package com.example.sourcedemoperiodic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
public class SourceDemoPeriodicApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(SourceDemoPeriodicApplication.class, args);
	}

	@Bean
	public Consumer<String> eventLogger() {
		return v -> System.out.println("Event logger: " + v);
	}

	@Bean
	public Consumer<String> oddLogger() {
		return v -> System.out.println("Odd logger: " + v);
	}
}
