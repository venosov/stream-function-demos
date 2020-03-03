package com.example.sinkdemokotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.util.function.Function

@SpringBootApplication
class SinkDemoKotlinApplication {
	@Bean
	fun router(): Function<Flux<Int>, Tuple2<Flux<String>, Flux<String>>> {
		return Function { flux: Flux<Int> ->
			val connectedFlux = flux.publish().autoConnect(2)
			val even: UnicastProcessor<String> = UnicastProcessor.create<String>()
			val odd: UnicastProcessor<String> = UnicastProcessor.create<String>()
			val evenFlux = connectedFlux.filter { number: Int -> number % 2 == 0 }
					.doOnNext { number: Int -> even.onNext("EVEN: $number") }
			val oddFlux = connectedFlux.filter { number: Int -> number % 2 != 0 }
					.doOnNext { number: Int -> even.onNext("ODD: $number") }
			Tuples.of<Flux<String>, Flux<String>>(Flux.from(even).doOnSubscribe { evenFlux.subscribe() },
					Flux.from(odd).doOnSubscribe { oddFlux.subscribe() })
		}
	}
}

fun main(args: Array<String>) {
	runApplication<SinkDemoKotlinApplication>(*args)
}
