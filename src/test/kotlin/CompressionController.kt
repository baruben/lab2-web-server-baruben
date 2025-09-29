package es.unizar.webeng.lab2

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/small")
    fun smallResponse(): String = "x"

    @GetMapping("/large")
    fun largeResponse(): String = "x".repeat(5000)
}