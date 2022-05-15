package com.github.konstantin.suspitsyn.ecommercebackend.checkout;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CheckoutService {

    public static final String USERNAME = "907925";
    public static final String API_KEY = "test_9RNcjcDxi7TKzUFt4KUhAKNOvkb6HWfMNwOspQmagew";
    private final String YOOKASSA = "https://api.yookassa.ru/v3/payments";
    private final String CURRENCY = "RUB";

    final JsonNodeFactory factory = JsonNodeFactory.instance;

    private ObjectNode jsonContent = factory.objectNode();
    private ObjectNode amount = factory.objectNode();
    private ObjectNode confirmation = factory.objectNode();

    private final String RETURN_URL = "https://localhost:8080";
    private final String ORDER_NO = "Заказ № %s";




    public ObjectNode sendAndReturn(String orderNo, Long price) {
        // Creates JSON for YOOKASSA to create payment

        confirmation.put("type", "redirect");
        confirmation.put("return_url", RETURN_URL);

        amount.put("value", String.valueOf(price)+".00");
        amount.put("currency", CURRENCY);

        jsonContent.putIfAbsent("confirmation", confirmation);
        jsonContent.putIfAbsent("amount", amount);

        jsonContent.put("capture", true);
        jsonContent.put("description", String.format(ORDER_NO, orderNo));

        return jsonContent;

    }

    private Map<String, String> parseJson(String jsonString) {
        JSONObject obj = new JSONObject(jsonString);
        Map<String, String> infoFromJson = new HashMap<>();
        infoFromJson.put("id", obj.getString("id"));
        infoFromJson.put("status", obj.getString("status"));
        if (obj.has("confirmation")) {
            if (obj.getJSONObject("confirmation").has("confirmation_url")) {
                infoFromJson.put("confirmation_url", obj.getJSONObject("confirmation").getString("confirmation_url"));
            }
        }

        return infoFromJson;
    }

    public Map<String, String> sendPaymentYookassa(Long orderNo, Long price) {

        RestTemplate restTemplate = new RestTemplate();

        ObjectNode jsonPost = this.sendAndReturn(String.valueOf(orderNo), price);

        HttpEntity request = createRequest(jsonPost, orderNo);

        // Send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(YOOKASSA, request, String.class);
//        System.out.println(response.getBody());
//        System.out.println(this.parseJson(response.getBody()));

        if (response.getStatusCode()!=HttpStatus.OK) {
            throw new IllegalStateException("Что-то пошло не так");
        }

        return this.parseJson(response.getBody());

    }

    public Map<String, String> checkPaymentStatusYookassa(String yookassaId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(USERNAME, API_KEY);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(YOOKASSA + "/" + yookassaId, HttpMethod.GET, request, String.class);

        if (response.getStatusCode()!=HttpStatus.OK) {
            throw new IllegalStateException("Что-то пошло не так");
        }

        return this.parseJson(response.getBody());

    }



    private HttpEntity createRequest(ObjectNode json, Long orderNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Idempotence-Key", "nas" + String.valueOf(orderNo));
        // Add basic authentication
        headers.setBasicAuth(USERNAME, API_KEY);
        // Build the request
        HttpEntity request = new HttpEntity<>(json, headers);

        return request;
    }

    public static void main(String[] args) {
        CheckoutService cs = new CheckoutService();
//        cs.sendPaymentYookassa(123L, 100L);
//        cs.checkPaymentStatusYookassa("2a11f790-000f-5000-9000-1f5a02ea919d");
    }

}
