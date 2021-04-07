package ir.amv.snippets.protocolfreemvc.book;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

public class BookConverters {

    public static void registger(ConverterRegistry registry) {
        registry.addConverter(new EntityToDtoConverter());
        registry.addConverter(new DtoToEntityConverter());
    }

    private static class EntityToDtoConverter implements Converter<BookEntity, BookDto> {

        @Override
        public BookDto convert(BookEntity source) {
            return BookDto.builder()
                    .id(source.getBookId())
                    .name(source.getBookName())
                    .publishDate(source.getBookPublishDate())
                    .build();
        }
    }

    private static class DtoToEntityConverter implements Converter<BookDto, BookEntity> {

        @Override
        public BookEntity convert(BookDto source) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setBookId(source.getId());
            bookEntity.setBookName(source.getName());
            bookEntity.setBookPublishDate(source.getPublishDate());
            return bookEntity;
        }
    }
}
