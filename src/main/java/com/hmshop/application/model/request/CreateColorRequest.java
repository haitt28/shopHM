package com.hmshop.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateColorRequest {
    private Long id;
    private String code;
    @NotBlank(message = "Tên danh mục trống")
    @Size(max = 50, message = "Tên danh mục có độ dài tối đa 50 ký tự!")
    private String name;

    private boolean status;
}
