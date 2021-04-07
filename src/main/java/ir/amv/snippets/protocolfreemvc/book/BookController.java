package ir.amv.snippets.protocolfreemvc.book;

import ir.amv.snippets.protocolfreemvc.adapters.http.MapDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(
        value = "/books",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public interface BookController {

    @GetMapping(consumes = MediaType.ALL_VALUE)
    @MapDto(BookDto.class)
    List<BookEntity> allBooks();

    @PostMapping
    @MapDto(BookDto.class)
    BookEntity saveBook(@MapDto(BookDto.class) BookEntity entity);

    @PutMapping
    void updateBook(@MapDto(BookDto.class) BookEntity entity);

    @DeleteMapping(
            value = "/{id}",
            consumes = "*/*"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBook(@PathVariable("id") Long id);
}
