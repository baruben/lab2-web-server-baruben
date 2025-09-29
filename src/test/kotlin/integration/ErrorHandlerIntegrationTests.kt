package es.unizar.webeng.lab2

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ErrorHandlerIntegrationTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return custom error page with error 404`() {
        val headers = org.springframework.http.HttpHeaders()
        headers.add("Accept", "text/html")
        val entity = org.springframework.http.HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            "http://localhost:$port/ruta-inexistente",
            org.springframework.http.HttpMethod.GET,
            entity,
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).contains("<!DOCTYPE html>")
        assertThat(response.body).contains("<title>Error</title>")
        assertThat(response.body).contains("<h1>Error")
        assertThat(response.body).contains("404</span>")
        assertThat(response.body).contains("<p>Not Found</p>")
        assertThat(response.body).contains("<a href=\"/\">Volver al inicio</a>")
    }
}