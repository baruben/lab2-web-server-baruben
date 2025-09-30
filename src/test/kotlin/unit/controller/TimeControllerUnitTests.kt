package es.unizar.webeng.lab2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TimeControllerUnitTests {

    private val timestamp = LocalDateTime.of(2025, 10, 3, 6, 0)
    private val timeController = TimeController(service = TestTimeService(timestamp))

    @Test
    fun `should return time API response as DTO`() {
        val expected = TimeDTO(timestamp)

        val response = timeController.timeV1()

        assertThat(response).isEqualTo(expected)
    }

    @Test
    fun `should return time API response as DTOV2`() {
        val expected = TimeDTOV2(time = timestamp, extra = "extra")

        val response = timeController.timeV2()

        assertThat(response).isEqualTo(expected)
    }
}