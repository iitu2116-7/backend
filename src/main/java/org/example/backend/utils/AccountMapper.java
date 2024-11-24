package org.example.backend.utils;

import org.example.backend.db.entites.AssetAccount;
import org.example.backend.dto.dtos.AccountDTO;
import org.example.backend.dto.dtos.AssetInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountMapper {

    public AccountDTO toDto(BigDecimal balanceInKzt, List<AssetAccount> assets) {
        List<AssetInfo> assetInfoList = assets.stream()
                .map(asset -> new AssetInfo(
                        asset.getCrypto().getName(),
                        asset.getQuantity(),
                        asset.getProfit()
                ))
                .toList();

        return new AccountDTO(
                balanceInKzt,
                assetInfoList
        );
    }
}
