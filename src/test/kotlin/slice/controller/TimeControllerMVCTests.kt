package es.unizar.webeng.lab2

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(TimeController::class)
class TimeControllerMVCTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var timeService: TimeService

    private val objectMapper = ObjectMapper()

    @Test
    fun `should return time API response as JSON`() {
        val timestamp = LocalDateTime.now()
        whenever(timeService.now()).thenReturn(timestamp)

        mockMvc.perform(get("/time"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            // Convert json value $.time into DateTime (DateTime -> JSON != DateTime -> String)
            .andExpect { result ->
                val json = result.response.contentAsString
                val node = objectMapper.readTree(json)
                val actual = LocalDateTime.parse(node.get("time").textValue())
                assertThat(actual, equalTo(timestamp))
            }
    }
}