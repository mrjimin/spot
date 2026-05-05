package io.github.mrjimin.spot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpotApplication

fun main(args: Array<String>) {
    runApplication<SpotApplication>(*args)
}
