package ir.amv.snippets.protocolfreemvc.adapters.http;

import ir.amv.snippets.protocolfreemvc.book.BookConverters;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private MvcDtoResolver mvcDtoResolver;

    public MvcConfig(List<HttpMessageConverter<?>> converters) {
        mvcDtoResolver = new MvcDtoResolver(converters, ApplicationConversionService.getSharedInstance());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(0, mvcDtoResolver);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(0, mvcDtoResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        BookConverters.registger(registry);
        BookConverters.registger((ConverterRegistry) ApplicationConversionService.getSharedInstance());
    }
}
