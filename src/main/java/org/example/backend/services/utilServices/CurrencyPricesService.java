package org.example.backend.services.utilServices;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.db.entites.CurrencyPrices;
import org.example.backend.db.repositories.CurrencyPricesRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

@Service
@AllArgsConstructor
public class CurrencyPricesService {
    private final CurrencyPricesRepository currencyPricesRepository;

    public CurrencyPrices getCurrencyPrices() {
        return currencyPricesRepository.findFirstCurrencyPrice();
    }


    @Transactional
    @Scheduled(fixedRate = 3600000)
    public void updateCurrencyPrices() {
        try {
            String html = getHtmlFromApi();

            double usdValue = getCurrencyValue(html, "USD");
            double eurValue = getCurrencyValue(html, "EUR");
            double rubValue = getCurrencyValue(html, "RUB");
            double kgsValue = getCurrencyValue(html, "KGS");
            double gbpValue = getCurrencyValue(html, "GBP");
            double cnyValue = getCurrencyValue(html, "CNY");

            CurrencyPrices currencyPrices = currencyPricesRepository.findFirstCurrencyPrice();
            if (currencyPrices != null) {
                currencyPrices.setUsd(usdValue);
                currencyPrices.setEur(eurValue);
                currencyPrices.setRub(rubValue);
                currencyPrices.setKgs(kgsValue);
                currencyPrices.setGbp(gbpValue);
                currencyPrices.setCny(cnyValue);
                currencyPrices.setUpdateDate(new Date());
                currencyPricesRepository.save(currencyPrices);
                System.out.println(usdValue + " " + eurValue + " " + rubValue + " " + kgsValue);
                System.out.println("Currency prices updated successfully.");
            } else {
                CurrencyPrices newCurrencyPrices = new CurrencyPrices();
                newCurrencyPrices.setUsd(usdValue);
                newCurrencyPrices.setEur(eurValue);
                newCurrencyPrices.setRub(rubValue);
                newCurrencyPrices.setKgs(kgsValue);
                newCurrencyPrices.setGbp(gbpValue);
                newCurrencyPrices.setCny(cnyValue);
                newCurrencyPrices.setCreatedDate(new Date());
                System.out.println(usdValue + " " + eurValue + " " + rubValue + " " + kgsValue);
                System.out.println("New currency prices created.");
            }
        } catch (IOException e) {
            System.out.println("Error updating currency prices: " + e.getMessage());
        }
    }


    private String getHtmlFromApi() throws IOException {
        URL urlObj = new URL("https://mig.kz/api/v1/gadget/html");
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            return sb.toString();
        } else {
            return "Error in sending a GET request";
        }
    }

    private double getCurrencyValue(String html, String currencyCode) {
        Document doc = Jsoup.parse(html);
        Elements rows = doc.select("tr");
        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() == 3) {
                String currency = cells.get(1).text();
                if (currencyCode.equalsIgnoreCase(currency)) {
                    String value = cells.get(2).text();
                    return Double.parseDouble(value);
                }
            }
        }
        return 0.0;
    }
}

