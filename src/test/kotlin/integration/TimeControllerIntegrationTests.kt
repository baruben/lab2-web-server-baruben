package es.unizar.webeng.lab2

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.*
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeControllerTests {
    
    @LocalServerPort
    private var port: Int = 0

    private val timestamp = LocalDateTime.of(2025, 10, 3, 6, 0)
    @TestConfiguration
    class Config {
        @Bean
        @Primary
        fun timeProvider(): TimeProvider = TestTimeService(LocalDateTime.of(2025, 10, 3, 6, 0))
    }

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val objectMapper = jacksonObjectMapper()
    private val xmlMapper = XmlMapper()

    @Test
    fun `should return JSON when Accept is application-json`() {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_JSON)
        }
        val response = restTemplate.exchange(
            "/time",
            HttpMethod.GET,
            HttpEntity<String>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)

        val json = jacksonObjectMapper().readTree(response.body)
        assertThat(json["time"].asText()).isEqualTo("${timestamp.toString()}:00")
    }

    @Test
    fun `should return XML when Accept is application-xml`() {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.APPLICATION_XML)
        }
        val response = restTemplate.exchange(
            "/time",
            HttpMethod.GET,
            HttpEntity<String>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_XML)

        val xml = XmlMapper().readTree(response.body)
        assertThat(xml["time"].asText()).isEqualTo("${timestamp.toString()}:00")
    }

    @Test
    fun `should return vendor-specific JSON when Accept is vnd lab2`() {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.valueOf("application/vnd.lab2.time+json"))
        }
        val response = restTemplate.exchange(
            "/time",
            HttpMethod.GET,
            HttpEntity<String>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType)
            .isEqualTo(MediaType.valueOf("application/vnd.lab2.time+json"))
        
        val json = jacksonObjectMapper().readTree(response.body)
        assertThat(json["time"].asText()).isEqualTo("${timestamp.toString()}:00")
    }

    @Test
    fun `should return vendor-specific JSON v2 when Accept is vnd lab2 v2`() {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.valueOf("application/vnd.lab2.time-v2+json"))
        }
        val response = restTemplate.exchange(
            "/time",
            HttpMethod.GET,
            HttpEntity<String>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType)
            .isEqualTo(MediaType.valueOf("application/vnd.lab2.time-v2+json"))

        val json = jacksonObjectMapper().readTree(response.body)
        assertThat(json["time"].asText()).isEqualTo("${timestamp.toString()}:00")
        assertThat(json["extra"].asText()).isEqualTo("extra")
    }

    @Test
    fun `should return default JSON when no Accept header is provided`() {
        val response = restTemplate.getForEntity("/time", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)
        
        val json = jacksonObjectMapper().readTree(response.body)
        assertThat(json["time"].asText()).isEqualTo("${timestamp.toString()}:00")
    }

    @Test
    fun `should return 406 for unsupported Accept header`() {
        val headers = HttpHeaders().apply {
            accept = listOf(MediaType.IMAGE_PNG)
        }
        val response = restTemplate.exchange(
            "/time",
            HttpMethod.GET,
            HttpEntity<String>(headers),
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_ACCEPTABLE)
    }
}
