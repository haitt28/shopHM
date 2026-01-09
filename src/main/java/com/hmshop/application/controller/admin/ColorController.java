package com.hmshop.application.controller.admin;

import com.hmshop.application.entity.Brand;
import com.hmshop.application.entity.Color;
import com.hmshop.application.model.mapper.BrandMapper;
import com.hmshop.application.model.mapper.ColorMapper;
import com.hmshop.application.model.request.CreateBrandRequest;
import com.hmshop.application.model.request.CreateColorRequest;
import com.hmshop.application.service.ColorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;
    @GetMapping("/admin/colors")
    public String homePage(Model model,
                           @RequestParam(defaultValue = "", required = false) String id,
                           @RequestParam(defaultValue = "", required = false) String name,
                           @RequestParam(defaultValue = "1", required = false) Integer page) {

        Page<Color> colors = colorService.adminGetListColor(id, name, page);
        model.addAttribute("colors", colors.getContent());
        model.addAttribute("totalPages", colors.getTotalPages());
        model.addAttribute("currentPage", colors.getPageable().getPageNumber() + 1);
        return "admin/color/list";
    }

    @PostMapping("/api/admin/colors")
    public ResponseEntity<Object> createBrand(@Valid @RequestBody CreateColorRequest createBrandRequest) {
        Color color = colorService.createColor(createBrandRequest);
        return ResponseEntity.ok(ColorMapper.toColorDTO(color));
    }

    @PutMapping("/api/admin/colors/{id}")
    public ResponseEntity<Object> updateBrand(@Valid @RequestBody CreateColorRequest createColorRequest, @PathVariable long id) {
        colorService.updateColor(createColorRequest, id);
        return ResponseEntity.ok("Sửa nhãn hiệu thành công!");
    }

    @GetMapping("/api/admin/colors/{id}")
    public ResponseEntity<Object> getBrandById(@PathVariable long id){
        Color color = colorService.getColorById(id);
        return ResponseEntity.ok(color);
    }
}
