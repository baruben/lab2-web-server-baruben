package es.unizar.webeng.lab2

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class TestController {

    @GetMapping("/small")
    fun smallResponse(): String = "x"

    @GetMapping("/large")
    fun largeResponse(): String = "x".repeat(5000)

    @GetMapping("/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamResponse(): SseEmitter {
        val emitter = SseEmitter()
        Thread {
            try {
                repeat(5) { i ->
                    emitter.send(SseEmitter.event().data("event-$i"))
                    Thread.sleep(100) 
                }
                emitter.complete()
            } catch (ex: Exception) {
                emitter.completeWithError(ex)
            }
        }.start()
        return emitter
    }
}