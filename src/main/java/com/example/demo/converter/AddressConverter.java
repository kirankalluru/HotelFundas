package com.example.demo.converter;

import com.example.demo.entity.common.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AddressConverter implements AttributeConverter<Address, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Address address) {
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Address to JSON", e);
        }
    }

    @Override
    public Address convertToEntityAttribute(String addressJson) {
        try {
            return objectMapper.readValue(addressJson, Address.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to Address", e);
        }
    }
} 