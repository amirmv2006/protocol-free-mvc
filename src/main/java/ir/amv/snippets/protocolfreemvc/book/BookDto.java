package ir.amv.snippets.protocolfreemvc.book;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class BookDto {
    private Long id;
    private String name;
    private LocalDate publishDate;
}
