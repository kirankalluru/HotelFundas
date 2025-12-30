package com.example.demo.util;



import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.example.demo.dto.common.AddressDTO;

import com.example.demo.entity.common.Address;


public class EntityDTOConverter {

    public static AddressDTO convertToAddressDTO(Address address) {
        if (address == null) {
            return null;
        }
        AddressDTO dto = new AddressDTO();
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPincode(address.getPincode());
        return dto;
    }



   
}