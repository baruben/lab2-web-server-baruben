package es.unizar.webeng.lab2

import java.time.LocalDateTime

class TestTimeService(private val fixedTime: LocalDateTime) : TimeProvider {
    override fun now(): LocalDateTime = fixedTime
}