# Lab 2 Web Server -- Project Report

## Description of Changes
### 1. Custom Error Page
- Added a custom `error.html` to be displayed whenever an error occurs.
- Added **test endpoints** that respond with different HTTP error codes (401, 402, 403, 404, 500).
- **Integration Tests**: verify that the error page correctly displays the error code and message for each response.

### 2. `/time` Endpoint
- Added all necessary components for the new endpoint:
  - **DTO** for response representation.
  - **Provider interface**.
  - **Service implementation**.
  - **REST Controller**.
- Added a **test Service implementation** for deterministic behavior in tests.
- **Unit Tests**: Controller correctly returns the DTO.
- **MVC Tests**: Endpoint returns JSON as expected.

### 3. HTTP/2 and SSL Support
- Created a **PKCS12 keystore** storing the certificate for the project.
- Configured **Spring Boot** for SSL and HTTP/2 using the keystore in `application.yml`.

> **Note:** Secrets such as passwords or certificates should **not** be pushed to GitHub.  
> The keystore and its password are not included in the project.  
> Instead, SSL properties are configured in a separate file:  
> `src/main/resources/application-secrets.properties`
>
>This file must contain the following properties:
>
>```properties
>server.ssl.key-store=<path to the keystore>
>server.ssl.key-store-password=<keystore password>
>```

### 4. HTTP Response Compression
- Configured the application to enable **GZIP compression** for HTTP responses in `application.yml`.
- Added **test endpoints** for:
  - Short responses (not compressed).
  - Large responses (compressed when the `Accept-Encoding: gzip` header is present).
- **Integration Tests**:
  - Short responses are **not compressed**.
  - Large responses **with compression request** are compressed.
  - Responses **without compression request** are not compressed.
  - **Text event stream** (`text/event-stream`) responses are not compressed.

### 5. Content Negotiation in `/time` Endpoint
- `/time` endpoint supports **content negotiation** in both JSON and XML.
- Added **vendor-specific media types** for API versioning:
  - Returns default JSON when no `Accept` header is provided.
  - Returns JSON or XML based on `Accept` header.
  - Returns vendor-specific JSON for `application/vnd.lab2+json` and version 2 for `application/vnd.lab2.v2+json`.
  - Returns **HTTP 406** for unsupported `Accept` headers.
- **Unit Tests**: Controller returns different DTOs for each API version.
- **Integration Tests**:
  - Returns JSON for `application/json`.
  - Returns XML for `application/xml`.
  - Returns vendor-specific JSON when `Accept` matches the custom media type.
  - Returns default JSON when `Accept` header is missing.
  - Returns 406 for unsupported `Accept` headers.

## Technical Decisions

During the assignment I had to make some choices when implementing certain tasks:

- **`/time` endpoint test design**  
  - Applied the design philosophy of creating a Provider interface implementing a Test Service to inject in tests. 

- **SSL and HTTP/2 configuration**  
  - Chose to externalize secrets in `application-secrets.properties` to avoid committing sensitive information to version control.  

- **HTTP response compression**  
  - Enabled GZIP compression in Spring Boot since I didn't know any other type of compression.  
  - Defined a minimum response size of 1024 to only compress long response that would benefit from it.
  - Limited compression to specific MIME types that are used in the project.  
  - Explicitly excluded `text/event-stream` to prevent breaking server-sent events.  

- **Content negotiation and API versioning**  
  - Added API versioning to apply the uses of vendor-specific media types (`application/vnd.lab2+json`).
  - Defaulted to JSON when no `Accept` header is present, as it is the most widely supported format.   

## Learning Outcomes

Through this assignment I learned to:

- **Customize error handling**  
  - How to replace the default Spring Boot error pages with a custom `error.html`.
  - How to use Thymeleafs error variables in said error page.  

- **Design better tests**  
  - How to use dependency injection to provide a test service for deterministic unit tests.  

- **Configure SSL and HTTP/2**  
  - How to create a self-signed certificate (public-private key).
  - How to create and configure a PKCS12 keystore for HTTPS in Spring Boot.  
  - How to separate sensitive information into a `application-secrets.properties` file and avoid committing secrets to version control. 

- **Optimize network performance with compression**  
  - How to enable and configure GZIP compression in Spring Boot.  
  - How to test that compression only applies to large responses and respects content type exclusions (`text/event-stream`).  
  - How to verify headers like `Content-Encoding`, `Vary`, and `Content-Length` for correctness.  

- **Implement content negotiation and API versioning**  
  - How to support multiple formats (JSON, XML) based on the `Accept` header.  
  - How to implement vendor-specific media types for API versioning.  
  - How to handle missing or unsupported `Accept` headers by providing sensible defaults or returning a `406 Not Acceptable`.  

Overall, I improved my understanding of **web performance optimization**, **secure configuration**, and **API design best practices** by combining configuration, implementation, and thorough testing.

## AI Disclosure

### AI Tools Used
- ChatGPT (OpenAI)

### AI-Assisted Work
- Designing the custom error page.
- Explaining key concepts such as `text/event-stream`, vendor-specific media types, and API versioning based on the official Spring documentation.  
- Reviewing my implementation to ensure all assignment requirements were fulfilled.  
- Designing specific tests (e.g., verifying that `text/event-stream` responses are not compressed).  
- Formatting and structuring my report into clear Markdown sections.  

**Estimated AI assistance:** ~55%  
**Modifications:** I adapted, reviewed, and integrated the AI suggestions into my own code and report to ensure correctness and alignment with my assignment.

### Original Work
- Implemented the application features.
- Wrote the majority of the `main/` code.  
- Ran tests, debugged issues, and ensured the application worked as intended.  
- Gained a clear understanding of HTTP compression, SSL/HTTP2 setup, error handling in Spring Boot, content negotiation/versioning, and version control work when secrets are used.  
