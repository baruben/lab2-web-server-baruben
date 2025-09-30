package es.unizar.webeng.lab2

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType
import jakarta.servlet.http.HttpServletRequest
import java.time.LocalDateTime


data class TimeDTO(val time: LocalDateTime)
data class TimeDTOV2(val time: LocalDateTime, val extra: String)

interface TimeProvider {
    fun now(): LocalDateTime
}

@Service
class TimeService : TimeProvider {
    override fun now(): LocalDateTime = LocalDateTime.now()
}

fun LocalDateTime.toDTO(): TimeDTO = TimeDTO(time = this)
fun LocalDateTime.toDTOV2(): TimeDTOV2 = TimeDTOV2(time = this, extra = "extra")

@RestController
class TimeController(private val service: TimeProvider) {

    @GetMapping(
        value = ["/time"],
        produces = [
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            "application/vnd.lab2.time+json"
        ]
    )
    fun timeV1(): TimeDTO = service.now().toDTO()

    @GetMapping(
        value = ["/time"],
        produces = ["application/vnd.lab2.time-v2+json"]
    )
    fun timeV2(): TimeDTOV2 = service.now().toDTOV2()
}
