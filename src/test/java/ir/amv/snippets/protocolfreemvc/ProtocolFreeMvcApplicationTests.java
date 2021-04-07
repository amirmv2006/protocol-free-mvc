package ir.amv.snippets.protocolfreemvc;

import ir.amv.snippets.protocolfreemvc.book.BookDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProtocolFreeMvcApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private BookDto bookDto;

    @Test
    @Order(1)
    void contextLoads() {
    }

    @Test
    @Order(2)
    void theBookShouldBeSavedSuccessfully() {
        bookDto = BookDto.builder()
                .name("Amir")
                .publishDate(LocalDate.now())
                .build();
        ResponseEntity<BookDto> result = testRestTemplate.postForEntity("/books", bookDto, BookDto.class);
        assertThat(result)
                .isNotNull()
                .extracting(HttpEntity::getBody)
                .isNotNull()
                .extracting(BookDto::getId)
                .isNotNull();
        bookDto.setId(result.getBody().getId());
    }

    @Test
    @Order(3)
    void theBooksShouldBeFetchedSuccessfully() {
        ResponseEntity<BookDto[]> result = testRestTemplate.getForEntity("/books", BookDto[].class);
        assertThat(result)
                .isNotNull();
        assertThat(result.getStatusCode())
                .isNotNull()
                .extracting(HttpStatus::is2xxSuccessful)
                .isEqualTo(true);
        assertThat(result.getBody())
                .hasSize(1)
                .containsExactly(bookDto);
    }

    @Test
    @Order(4)
    void theBookShouldBeUpdatedSuccessfully() {
        BookDto toBeUpdated = bookDto.toBuilder()
                .name("Amir2")
                .build();
        testRestTemplate.put("/books", toBeUpdated);
        ResponseEntity<BookDto[]> result = testRestTemplate.getForEntity("/books", BookDto[].class);
        assertThat(result)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isNotNull()
                .isEqualTo(HttpStatus.OK);
        assertThat(result.getBody())
                .isNotNull()
                .hasSize(1)
                .extracting(BookDto::getName)
                .containsExactly("Amir2");
    }

    @Test
    @Order(5)
    void theBookShouldBeDeletedSuccessfully() {
        ResponseEntity<Void> response = testRestTemplate.exchange("/books/{id}", HttpMethod.DELETE, new HttpEntity<>(null), Void.class, bookDto.getId());
        assertThat(response)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isNotNull()
                .isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<BookDto[]> result = testRestTemplate.getForEntity("/books", BookDto[].class);
        assertThat(result)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isNotNull()
                .isEqualTo(HttpStatus.OK);
        assertThat(result.getBody())
                .isNotNull()
                .hasSize(0);
    }
}
