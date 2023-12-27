package com.bikeleasing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LeasingServiceApplication

fun main(args: Array<String>) {
    runApplication<LeasingServiceApplication>(*args)
}
