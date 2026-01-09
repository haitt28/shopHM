package com.hmshop.application.service;

import com.hmshop.application.entity.Brand;
import com.hmshop.application.entity.Color;
import com.hmshop.application.model.request.CreateBrandRequest;
import com.hmshop.application.model.request.CreateColorRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ColorService {

    Page<Color> adminGetListColor(String id, String name, Integer page);

    List<Color> getListColor();

    Color createColor(CreateColorRequest createColorRequest);

    void updateColor(CreateColorRequest createColorRequest, Long id);

    Color getColorById(Long id);
}
