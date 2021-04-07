package ir.amv.snippets.protocolfreemvc.adapters.http;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class MvcDtoResolver extends RequestResponseBodyMethodProcessor {

    private final ConversionService conversionService;

    public MvcDtoResolver(List<HttpMessageConverter<?>> messageConverters, ConversionService conversionService) {
        super(messageConverters);
        this.conversionService = conversionService;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), MapDto.class) ||
                returnType.hasMethodAnnotation(MapDto.class));
    }

    @Override
    public void handleReturnValue(
            Object returnValue,
            MethodParameter returnType,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest
    ) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        // Try even with null return value. ResponseBodyAdvice could get involved.
        MapDto mapDto = returnType.getMethodAnnotation(MapDto.class);
        Object converted = doConvert(returnValue, mapDto);
        writeWithMessageConverters(converted, returnType, inputMessage, outputMessage);
    }

    private Object doConvert(Object source, MapDto mapDto) {
        if (source == null) {
            return null;
        } else if (source instanceof List<?>) {
            List<?> sourceList = (List<?>) source;
            return sourceList.stream()
                    .map(i -> doConvert(i, mapDto))
                    .collect(Collectors.toList());
        } else {
            return conversionService.convert(source, mapDto.value());
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MapDto.class);
    }

    @Override
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        binder.validate();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object deserializedDto = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return conversionService.convert(deserializedDto, parameter.getParameterType());
    }

    @Override
    protected Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        for (Annotation ann : parameter.getParameterAnnotations()) {
            MapDto dtoType = AnnotationUtils.getAnnotation(ann, MapDto.class);
            if (dtoType != null) {
                return super.readWithMessageConverters(inputMessage, parameter, dtoType.value());
            }
        }
        throw new RuntimeException();
    }

}
