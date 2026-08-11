package com.demo.jalrakhsa.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
    private String userName;
    private String locationName;
    private Double roofArea;
    private String roofType;
    private Integer dwellers;
    private Double openSpace;
}
