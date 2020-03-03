package com.example.sourcedemoperiodickotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.GenericMessage
import java.util.function.Function

@SpringBootApplication
class SourceDemoPeriodicKotlinApplication {

	@Bean
	fun uppercase() = Function { v: Message<String> ->
			println("Uppercasing: $v")
			v.payload.toUpperCase()
		}

	@Bean
	fun echo() = Function { v: String ->
			println("Echo: $v")
			v
		}

}

fun main2(args: Array<String>) {
	runApplication<SourceDemoPeriodicKotlinApplication>(*args)
}

fun main(args: Array<String>) {
	val uppercase = Function { v: Message<String> ->
		println("Uppercasing: $v")
		v.payload.toUpperCase()
	}

	println(uppercase.apply(GenericMessage("hello")))
}
