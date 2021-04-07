package ir.amv.snippets.protocolfreemvc.book;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
public class BookEntity {
    @Id
    @GeneratedValue
    private Long bookId;
    private String bookName;
    private LocalDate bookPublishDate;
}
