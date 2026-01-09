package com.hmshop.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ColorDTO {
    private Long id;
    private String name;
    private String code;
    private boolean status;
}
