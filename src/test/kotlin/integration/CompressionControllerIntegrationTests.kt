package es.unizar.webeng.lab2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompressionIntegrationTests {

    @LocalServerPort
    private var port: Int = 0

    @Autowired 
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should not compress small response`() {
        val headers = HttpHeaders().apply {
            set("Accept-Encoding", "gzip")
        }
        val entity = HttpEntity<String>(headers)

        val response: ResponseEntity<String> =
            restTemplate.exchange("/small", HttpMethod.GET, entity, String::class.java)

        assertThat(response.headers["Content-Encoding"]).isNull()
        assertThat(response.headers["Content-Length"]).isNotNull()
        assertThat(response.body).isEqualTo("x")
    }

    @Test
    fun `should compress large response with gzip request header`() {
        val headers = HttpHeaders().apply {
            set("Accept-Encoding", "gzip")
        }
        val entity = HttpEntity<String>(headers)

        val response: ResponseEntity<ByteArray> =
            restTemplate.exchange("/large", HttpMethod.GET, entity, ByteArray::class.java)

        assertThat(response.headers["Content-Encoding"]).contains("gzip")
        assertThat(response.headers["Vary"]).contains("accept-encoding")
        assertThat(response.headers["Content-Length"]).isNull()

        val compressedBytes = response.body!!
        assertThat(compressedBytes.size).isLessThan(5000)
    }

    @Test
    fun `should not compress large response without gzip request header`() {
        val response: ResponseEntity<String> =
            restTemplate.getForEntity("/large", String::class.java)

        assertThat(response.headers["Content-Encoding"]).isNull()
        assertThat(response.headers["Content-Length"]).isNotNull()
    }

    @Test
    fun `should not compress text event stream`() {
        val headers = HttpHeaders().apply {
            set("Accept-Encoding", "gzip")
        }
        val entity = HttpEntity<String>(headers)

        val response: ResponseEntity<String> =
            restTemplate.exchange("/sse", HttpMethod.GET, entity, String::class.java)

        assertThat(response.headers["Content-Encoding"]).isNull()
        assertThat(response.headers["Content-Type"]).contains("text/event-stream")
    }
}
