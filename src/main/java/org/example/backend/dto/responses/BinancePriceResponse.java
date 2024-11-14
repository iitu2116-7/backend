package org.example.backend.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinancePriceResponse {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("volume")
    private String volume;

    @JsonProperty("lastPrice")
    private String lastPrice;

    @JsonProperty("priceChange")
    private String priceChange;

    @JsonProperty("priceChangePercent")
    private String priceChangePercent;

    @JsonProperty("highPrice")
    private String highPrice;

    @JsonProperty("lowPrice")
    private String lowPrice;
}
