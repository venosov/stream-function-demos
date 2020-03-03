package com.example.sourcedemoperiodickotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
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

fun main(args: Array<String>) {
	runApplication<SourceDemoPeriodicKotlinApplication>(*args)
}
