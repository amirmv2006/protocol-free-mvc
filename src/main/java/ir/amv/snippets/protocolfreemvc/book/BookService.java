package ir.amv.snippets.protocolfreemvc.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements BookController {

    private final BookRepository bookRepository;

    @Override
    public List<BookEntity> allBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookEntity saveBook(BookEntity entity) {
        return bookRepository.save(entity);
    }

    @Override
    public void updateBook(BookEntity entity) {
        bookRepository.save(entity);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
