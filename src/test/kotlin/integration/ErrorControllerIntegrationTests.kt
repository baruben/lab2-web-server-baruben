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
public class ErrorControllerIntegrationTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun testErrorPage(path: String, expectedStatus: HttpStatus, expectedMessage: String) {
        val headers = org.springframework.http.HttpHeaders()
        headers.add("Accept", "text/html")
        val entity = org.springframework.http.HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            "http://localhost:$port$path",
            org.springframework.http.HttpMethod.GET,
            entity,
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(expectedStatus)
        assertThat(response.body).contains("<!DOCTYPE html>")
        assertThat(response.body).contains("<title>Error</title>")
        assertThat(response.body).contains("<h1>Error")
        assertThat(response.body).contains("${expectedStatus.value()}")
        assertThat(response.body).contains("<p>$expectedMessage</p>")
        assertThat(response.body).contains("<a href=\"/\">Volver al inicio</a>")
    }

    @Test
    fun `should return custom 401 error page`() {
        testErrorPage("/test/401", HttpStatus.UNAUTHORIZED, "Unauthorized")
    }
    
    @Test
    fun `should return custom 403 error page`() {
        testErrorPage("/test/403", HttpStatus.FORBIDDEN, "Forbidden")
    }

    @Test
    fun `should return custom 404 error page`() {
        testErrorPage("/test/404", HttpStatus.NOT_FOUND, "Not Found")
    }

    @Test
    fun `should return custom 500 error page`() {
        testErrorPage("/test/500", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")
    }
}