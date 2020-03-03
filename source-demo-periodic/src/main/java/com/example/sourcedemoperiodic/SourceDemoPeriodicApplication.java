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
	static Flux<Integer> generateFlux() {
		return Flux.interval(Duration.ofSeconds(1)).map(Long::intValue);
	}

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

	public static void main2(String[] args) throws IOException {
		Function<Flux<Integer>, Tuple2<Flux<String>, Flux<String>>> router = flux -> {
			Flux<Integer> connectedFlux = flux.publish().autoConnect(2);
			UnicastProcessor even = UnicastProcessor.create();
			UnicastProcessor odd = UnicastProcessor.create();

			Flux<Integer> evenFlux = connectedFlux.filter(number -> number % 2 == 0)
					.doOnNext(number -> even.onNext("EVEN: " + number));
			Flux<Integer> oddFlux = connectedFlux.filter(number -> number % 2 != 0)
					.doOnNext(number -> even.onNext("ODD: " + number));

			return Tuples.of(Flux.from(even).doOnSubscribe(x -> evenFlux.subscribe()),
					Flux.from(odd).doOnSubscribe(x -> oddFlux.subscribe()));
		};
		Tuple2<Flux<String>, Flux<String>> streams = router.apply(generateFlux());
		streams.getT1().subscribe(v -> {
			System.out.println(v);
		});
		streams.getT2().subscribe(v -> {
			System.out.println(v);
		});
		System.in.read();
	}

}
