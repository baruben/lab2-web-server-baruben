package es.unizar.webeng.lab2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.ui.ExtendedModelMap
import org.springframework.ui.Model
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

class TimeControllerUnitTests {
    private lateinit var timeController: TimeController
    private lateinit var timeService: TimeService

    @BeforeEach
    fun setup() {
        timeService = mock()         
        timeController = TimeController(service = timeService)
    }

    @Test
    fun `should return time API response as DTO`() {
        val timestamp = LocalDateTime.now()
        val expected = timestamp.toDTO()
        whenever(timeService.now()).thenReturn(timestamp)
        
        val response = timeController.time()
        assertThat(response).isEqualTo(expected)
    }
}