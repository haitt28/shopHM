package com.hmshop.application.model.mapper;

import com.hmshop.application.entity.Color;
import com.hmshop.application.model.dto.ColorDTO;
import com.hmshop.application.model.request.CreateColorRequest;

import java.sql.Timestamp;

public class ColorMapper {

    public static ColorDTO toColorDTO(Color color){
        ColorDTO colorDTO = new ColorDTO();
        colorDTO.setId(color.getId());
        colorDTO.setName(color.getName());
        colorDTO.setStatus(color.isStatus());
        return colorDTO;
    }

    public static Color toBrand(CreateColorRequest createColorRequest){
        Color color = new Color();
        color.setName(createColorRequest.getName());
        color.setCode(createColorRequest.getCode());
        color.setStatus(createColorRequest.isStatus());
        color.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return color;
    }
}
