package com.hmshop.application.service.impl;

import com.hmshop.application.entity.Color;
import com.hmshop.application.exception.BadRequestException;
import com.hmshop.application.exception.InternalServerException;
import com.hmshop.application.model.mapper.ColorMapper;
import com.hmshop.application.model.request.CreateColorRequest;
import com.hmshop.application.repository.ColorRepository;
import com.hmshop.application.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.hmshop.application.Constant.Constant.LIMIT_BRAND;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    @Override
    public Page<Color> adminGetListColor(String id, String name, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_BRAND, Sort.by("created_at").descending());
        return colorRepository.adminGetListColor(id, name, pageable);
    }

    @Override
    public List<Color> getListColor() {
        return colorRepository.findAll();
    }

    @Override
    public Color getColorById(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tên nhãn hiệu không tồn tại! "));
    }

    @Override
    public void updateColor(CreateColorRequest createColorRequest, Long id) {
        Optional<Color> color = Optional.ofNullable(colorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tên nhãn hiệu không tồn tại! ")));
        Color br = colorRepository.findByName(createColorRequest.getName());
        if (br != null) {
            if (!createColorRequest.getId().equals(br.getId()))
                throw new BadRequestException("Tên màu sắc " + createColorRequest.getName() + " đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
        }
        Color rs = color.get();
        rs.setId(id);
        rs.setName(createColorRequest.getName());
        rs.setCode(createColorRequest.getCode());
        rs.setStatus(createColorRequest.isStatus());
        rs.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        try {
            colorRepository.save(rs);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi chỉnh sửa màu sắc ");
        }
    }

    @Override
    public Color createColor(CreateColorRequest createColorRequest) {
        Color color = colorRepository.findByName(createColorRequest.getName());
        if (color != null) {
            throw new BadRequestException("Tên màu sắc  đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
        }
        color = ColorMapper.toBrand(createColorRequest);
        colorRepository.save(color);
        return color;
    }


}
