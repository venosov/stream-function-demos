package com.example.sourcedemoperiodickotlin

import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import reactor.util.function.Tuples
import java.time.Duration
import java.util.function.Function

//@SpringBootApplication
//class SourceDemoPeriodicKotlinApplication {
////
////	@Bean
////	fun uppercase() = Function { v: Message<String> ->
////			println("Uppercasing: $v")
////			v.payload.toUpperCase()
////		}
////
////	@Bean
////	fun echo() = Function { v: String ->
////			println("Echo: $v")
////			v
////		}
////
//
//	@Bean
//	fun evenLogger() = Consumer { v: String -> println("Even logger: $v") }
//
//	@Bean
//	fun oddLogger() = Consumer { v: String -> println("Odd logger: $v") }
//
//}
//
//fun main(args: Array<String>) {
//	runApplication<SourceDemoPeriodicKotlinApplication>(*args)
//}


//fun main() {
//	val uppercase = Function { v: Message<String> ->
//		println("Uppercasing: $v")
//		v.payload.toUpperCase()
//	}
//
//	println(uppercase.apply(GenericMessage("hello")))
//}

fun generateFlux(): Flux<Int> {
	return Flux.interval(Duration.ofSeconds(1)).map(Long::toInt)
}

//fun main() {
//	generateFlux().subscribe {
//		println(it)
//	}
//
//	System.`in`.read()
//}
//
//fun main() {
//	val router = Function { flux: Flux<Int> ->
//		val connectedFlux = flux.publish().autoConnect(1)
//		val even: UnicastProcessor<String> = UnicastProcessor.create()
//		val evenFlux = connectedFlux.filter { number: Int -> number % 2 == 0 }
//				.doOnNext { number: Int -> even.onNext("EVEN: $number") }
//		Flux.from(even).doOnSubscribe { evenFlux.subscribe() }
//	}
//
//	val streams = router.apply(generateFlux())
//	streams.subscribe { v: String -> println(v) }
//	System.`in`.read()
//}

fun main() {
	val router = Function { flux: Flux<Int> ->
		val connectedFlux = flux.publish().autoConnect(2)
		val even: UnicastProcessor<String> = UnicastProcessor.create()
		val odd: UnicastProcessor<String> = UnicastProcessor.create()
		val evenFlux = connectedFlux.filter { number: Int -> number % 2 == 0 }
				.doOnNext { number: Int -> even.onNext("EVEN: $number") }
		val oddFlux = connectedFlux.filter { number: Int -> number % 2 != 0 }
				.doOnNext { number: Int -> odd.onNext("ODD: $number") }
		Tuples.of<Flux<String>, Flux<String>>(Flux.from(even).doOnSubscribe { evenFlux.subscribe() },
				Flux.from(odd).doOnSubscribe { oddFlux.subscribe() })
	}
	val streams = router.apply(generateFlux())
	streams.t1.subscribe { println(it) }
	streams.t2.subscribe { println(it) }
	System.`in`.read()
}
