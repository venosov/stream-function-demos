package com.example.demofunctionweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import java.util.function.Function

@SpringBootApplication
class DemoFunctionWebApplication {
	@Bean
	fun reverseString() = Function { it: Message<String> ->
		val result = StringBuilder(it.payload).reverse().toString()
		println(result)
		result
	}
}

fun main(args: Array<String>) {
	runApplication<DemoFunctionWebApplication>(*args)
}
