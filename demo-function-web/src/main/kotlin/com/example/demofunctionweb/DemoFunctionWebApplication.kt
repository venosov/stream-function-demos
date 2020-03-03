package com.example.demofunctionweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.function.Function

@SpringBootApplication
class DemoFunctionWebApplication {
	@Bean
	fun reverseString() = Function<String, String> {
		StringBuilder(it).reverse().toString()
	}
}

fun main(args: Array<String>) {
	runApplication<DemoFunctionWebApplication>(*args)
}
