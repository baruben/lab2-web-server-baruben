package es.unizar.webeng.lab2

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class ErrorController {

    @GetMapping("/test/401")
    fun error401(): Nothing = throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized")

    @GetMapping("/test/403")
    fun error403(): Nothing = throw ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden")

    @GetMapping("/test/500")
    fun error500(): Nothing = throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
}
