package com.timelapse.ashwinxd.model.model.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by ashwinxd on 17/4/17.
 */

public class CustomJacksonConverterFactory extends Converter.Factory {
  private final ObjectMapper mapper;

  public static CustomJacksonConverterFactory create(ObjectMapper objectMapper){
    return new CustomJacksonConverterFactory(objectMapper);
  }

  private CustomJacksonConverterFactory(ObjectMapper mapper) {
    if (mapper == null) throw new NullPointerException("mapper == null");
    this.mapper = mapper;
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    return JacksonConverterFactory.create(mapper).responseBodyConverter(type, null, null);
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    return JacksonConverterFactory.create(mapper).requestBodyConverter(type, null, null, null);
  }
}
