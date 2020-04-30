package com.example.sourcedemoperiodickotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.GenericMessage
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorSupplier
import org.springframework.cloud.stream.annotation.EnableBinding
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import reactor.util.function.Tuples
import java.time.Duration
import java.util.function.Consumer
import java.util.function.Function

@EnableBinding
@SpringBootApplication
class SourceDemoPeriodicKotlinApplication {

//	@Bean
//	fun uppercase() = Function { v: Message<String> ->
//			println("Uppercasing: $v")
//			v.payload.toUpperCase()
//		}
//
//	@Bean
//	fun echo() = Function { v: String ->
//			println("Echo: $v")
//			v
//		}

	@Bean
	fun evenLogger() = Consumer<KStream<String, String>> { v ->
		v.foreach { _, value ->
			println("Even logger: $value")
		}
	}

	@Bean
	fun oddLogger() = Consumer<KStream<String, String>> { v ->
		v.foreach { _, value ->
			println("Odd logger: $value")
		}
	}
}

fun main(args: Array<String>) {
	runApplication<SourceDemoPeriodicKotlinApplication>(*args)
}


fun main0() {
	val uppercase = Function { v: Message<String> ->
		println("Uppercasing: $v")
		v.payload.toUpperCase()
	}

	println(uppercase.apply(GenericMessage("hello")))
}

fun generateFlux(): Flux<Int> {
	return Flux.interval(Duration.ofSeconds(1)).map(Long::toInt)
}

fun main1() {
	generateFlux().filter {
		it % 2 == 0
	}.subscribe {
		println(it)
	}

	System.`in`.read()
}

fun main2() {
	val router = Function { flux: Flux<Int> ->
		val connectedFlux = flux.publish().autoConnect(1)
		val even: UnicastProcessor<String> = UnicastProcessor.create()
		val evenFlux = connectedFlux.filter { number: Int -> number % 2 == 0 }
				.doOnNext { number: Int -> even.onNext("EVEN: $number") }
		Flux.from(even).doOnSubscribe { evenFlux.subscribe() }
	}

	val streams = router.apply(generateFlux())
	streams.subscribe { v: String -> println(v) }
	System.`in`.read()
}

fun main3() {
	val router = Function { flux: Flux<Int> ->
		val connectedFlux = flux.publish().autoConnect(2)
		val even = UnicastProcessor.create<String>()
		val odd = UnicastProcessor.create<String>()
		val evenFlux = connectedFlux.filter { it % 2 == 0 }
				.doOnNext { even.onNext("EVEN: $it") }
		val oddFlux = connectedFlux.filter { it % 2 != 0 }
				.doOnNext { odd.onNext("ODD: $it") }
		Tuples.of<Flux<String>, Flux<String>>(Flux.from(even).doOnSubscribe { evenFlux.subscribe() },
				Flux.from(odd).doOnSubscribe { oddFlux.subscribe() })
	}
	val streams = router.apply(generateFlux())
	streams.t1.subscribe { println(it) }
	streams.t2.subscribe { println(it) }
	System.`in`.read()
}
