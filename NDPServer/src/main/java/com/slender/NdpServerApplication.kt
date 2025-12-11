package com.slender;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class NdpServerApplication

fun main(args: Array<String>) {
    runApplication<NdpServerApplication>(*args)
}

