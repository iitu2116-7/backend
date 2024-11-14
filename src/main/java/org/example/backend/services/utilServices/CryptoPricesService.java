package org.example.backend.services.utilServices;

import lombok.RequiredArgsConstructor;
import org.example.backend.db.entites.CryptoPrices;
import org.example.backend.db.repositories.CryptoPricesRepository;
import org.example.backend.dto.dtos.CryptoPricesDTO;
import org.example.backend.dto.responses.BinancePriceResponse;
import org.example.backend.utils.CryptoPricesMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CryptoPricesService {

    private final CryptoPricesRepository cryptoPricesRepository;
    private final CryptoPricesMapper cryptoPricesMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/24hr";

    @Scheduled(fixedRate = 3600000)
    public void updateCryptoPrices() {
        try {
            List<CryptoPrices> cryptoPricesList = cryptoPricesRepository.findAll();
            if (cryptoPricesList.isEmpty()) {
                System.err.println("No crypto symbols found in the database to update.");
                return;
            }

            BinancePriceResponse[] responses = restTemplate.getForObject(BINANCE_API_URL, BinancePriceResponse[].class);

            if (responses != null) {
                for (BinancePriceResponse response : responses) {
                    cryptoPricesList.stream()
                            .filter(cryptoPrice -> cryptoPrice.getSymbol().equals(response.getSymbol()))
                            .forEach(cryptoPrice -> {
                                cryptoPrice.setLastPrice(parseDoubleOrNull(response.getLastPrice()));
                                cryptoPrice.setPriceChange(parseDoubleOrNull(response.getPriceChange()));
                                cryptoPrice.setPriceChangePercent(parseDoubleOrNull(response.getPriceChangePercent()));
                                cryptoPrice.setHighPrice(parseDoubleOrNull(response.getHighPrice()));
                                cryptoPrice.setLowPrice(parseDoubleOrNull(response.getLowPrice()));
                                cryptoPrice.setVolume(parseDoubleOrNull(response.getVolume()));
                                cryptoPrice.setUpdateDate(new Date());

                                cryptoPricesRepository.save(cryptoPrice);
                                System.out.println("Updated price");
                            });
                }
            } else {
                System.err.println("Failed to fetch prices from Binance API.");
            }
        } catch (Exception e) {
            System.err.println("Error while updating crypto prices: " + e.getMessage());
        }
    }

    private Double parseDoubleOrNull(String value) {
        return (value != null && !value.trim().isEmpty()) ? Double.parseDouble(value.trim()) : null;
    }


    public Page<CryptoPricesDTO> getCurrentCryptoPricesByFilter(String filter, Pageable pageable) {
        Page<CryptoPrices> page = switch (filter.toLowerCase()) {
            case "popular" -> {
                List<String> popularSymbols = List.of("BTCUSDT", "ETHUSDT", "BNBUSDT", "XRPUSDT", "ADAUSDT");
                yield cryptoPricesRepository.findAllBySymbolIn(popularSymbols, pageable);
            }
            case "highest" ->
                    cryptoPricesRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "lastPrice")));
            case "lowest" ->
                    cryptoPricesRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "lastPrice")));
            case "newest" ->
                    cryptoPricesRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "updateDate")));
            case "recent_changes" ->
                    cryptoPricesRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "priceChangePercent")));
            default ->
                    cryptoPricesRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "symbol")));
        };

        return page.map(cryptoPricesMapper::toDto);
    }
}
