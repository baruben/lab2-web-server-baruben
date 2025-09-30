package es.unizar.webeng.lab2

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import java.time.LocalDateTime

@WebMvcTest(TimeController::class)
class TimeControllerMVCTests {

    private val timestamp = LocalDateTime.of(2025, 10, 3, 6, 0)
    @TestConfiguration
    class Config {
        @Bean
        fun timeProvider(): TimeProvider = TestTimeService(LocalDateTime.of(2025, 10, 3, 6, 0))
    }
    
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return time API response as JSON`() {
        mockMvc.perform(get("/time"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.time").value("${timestamp.toString()}:00"))
    }
}