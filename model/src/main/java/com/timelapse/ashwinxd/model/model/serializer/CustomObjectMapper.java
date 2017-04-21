package com.timelapse.ashwinxd.model.model.serializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ashwinxd on 17/4/17.
 */

@Singleton
public class CustomObjectMapper extends ObjectMapper {

  @Inject
  public CustomObjectMapper() {
    enable(SerializationFeature.INDENT_OUTPUT);
    configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
    configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);

  }



}
