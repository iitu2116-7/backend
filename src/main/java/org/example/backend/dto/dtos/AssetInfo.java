package org.example.backend.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AssetInfo {
    private String assetName;
    private BigDecimal quantity;
    private BigDecimal profit;
}
