package com.db.duckbill.currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

@Component
public class AwesomeApiCurrencyProvider implements CurrencyProvider {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${currency.awesomeapi.base-url}")
    private String baseUrl;

    @Value("${currency.timeout-ms:3000}")
    private long timeout;

    @Override
    public BigDecimal convert(String from, String to, BigDecimal amount) {
        if (from == null || to == null || amount == null) {
            throw new IllegalArgumentException("Parâmetros from, to e amount não podem ser nulos");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount deve ser maior que zero");
        }

        try {
            String pair = from.toUpperCase() + "-" + to.toUpperCase();

            String json = webClient.get()
                    .uri(baseUrl + "/last/" + pair)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(timeout))
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            Iterator<Map.Entry<String, JsonNode>> it = root.fields();

            if (!it.hasNext()) {
                throw new RuntimeException("Resposta vazia da AwesomeAPI");
            }

            JsonNode first = it.next().getValue();
            if (!first.has("bid")) {
                throw new RuntimeException("Campo 'bid' não encontrado na resposta da AwesomeAPI");
            }

            BigDecimal rate = new BigDecimal(first.get("bid").asText());
            BigDecimal result = amount.multiply(rate).setScale(2, java.math.RoundingMode.HALF_UP);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao consultar câmbio: " + e.getMessage(), e);
        }
    }

    @Override
    public BigDecimal getQuote(String from, String to, LocalDate date) {
        if (from == null || to == null || date == null) {
            throw new IllegalArgumentException("Parâmetros from, to e date não podem ser nulos");
        }

        try {
            String pair = from.toUpperCase() + "-" + to.toUpperCase();
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            String json = webClient.get()
                    .uri(baseUrl + "/daily/" + pair + "/1?start_date=" + dateStr + "&end_date=" + dateStr)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(timeout))
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            if (!root.isArray() || root.size() == 0) {
                throw new RuntimeException("Resposta vazia da AwesomeAPI para cotação histórica");
            }

            JsonNode quote = root.get(0);
            if (!quote.has("bid")) {
                throw new RuntimeException("Campo 'bid' não encontrado na resposta da AwesomeAPI");
            }

            BigDecimal rate = new BigDecimal(quote.get("bid").asText());
            return rate;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao consultar cotação histórica: " + e.getMessage(), e);
        }
    }
}
