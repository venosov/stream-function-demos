package com.example.sinkdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Function;

@SpringBootApplication
@EnableBinding(Processor.class)
public class SinkDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SinkDemoApplication.class, args);
	}

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public String echo(String value) {
		System.out.println("Echo " + value);
		return value;
	}

//	@Bean
//	public Function<Message<String>, String> uppercase() {
//		return v -> {
//			System.out.println("Uppercasing: " + v);
//			return v.getPayload().toUpperCase();
//		};
//	}
//
//	@Bean
//	public Function<String, String> echo() {
//		return v -> {
//			System.out.println("Echo: " + v);
//			return v;
//		};
//	}
//
//	@Bean
//	public Function<Flux<Integer>, Flux<Integer>> multiplier() {
//		return flux -> flux.map(v -> v * v);
//	}
//
//	@Bean
//	public Function<Flux<Integer>, Tuple2<Flux<String>, Flux<String>>> router() {
//		return flux -> {
//			Flux<Integer> connectedFlux = flux.publish().autoConnect(2);
//			UnicastProcessor even = UnicastProcessor.create();
//			UnicastProcessor odd = UnicastProcessor.create();
//
//			Flux<Integer> evenFlux = connectedFlux.filter(number -> number % 2 == 0)
//					.doOnNext(number -> even.onNext("EVEN: " + number));
//			Flux<Integer> oddFlux = connectedFlux.filter(number -> number % 2 != 0)
//					.doOnNext(number -> even.onNext("ODD: " + number));
//
//			return Tuples.of(Flux.from(even).doOnSubscribe(x -> evenFlux.subscribe()),
//					Flux.from(odd).doOnSubscribe(x -> oddFlux.subscribe()));
//		};
//	}
}
