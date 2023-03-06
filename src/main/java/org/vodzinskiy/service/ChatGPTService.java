package org.vodzinskiy.service;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Service
public class ChatGPTService {

    public static final String CHOICES = "choices";
    public static final String CONTENT = "content";
    @Value("${bot.text.temperature}")
    Double temperature;
    @Value("${bot.text.model}")
    String textModel;
    @Value("${bot.text.top-p}")
    Double topP;
    @Value("${bot.text.frequency-penalty}")
    Double freqPenalty;
    @Value("${bot.text.presence-penalty}")
    Double presPenalty;
    @Value("${api.token}")
    String apiToken;
    @Value("${bot.text.max-tokens}")
    Integer maxTokens;
    @Value(value = "${api.url.completions}")
    String urlCompletions;


    public String askChatGPTText(String msg) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = setHeaders();

        JSONArray messages = new JSONArray();
        JSONObject sys = new JSONObject();
        sys.put("role", "system");
        sys.put("content", "You are a chatGPT");
        messages.put(sys);

        JSONObject userMessages = new JSONObject();
        userMessages.put("role", "user");
        userMessages.put("content", msg);
        messages.put(userMessages);

        JSONObject request = new JSONObject();
        request.put("model", textModel);
        request.put("messages", messages);
        request.put("temperature", temperature);
        request.put("max_tokens", maxTokens);
        request.put("top_p", topP);
        request.put("frequency_penalty", freqPenalty);
        request.put("presence_penalty", presPenalty);

        HttpEntity<String> requestEntity = new HttpEntity<>(request.toString(), headers);

        URI chatGptUrl = URI.create(urlCompletions);
        ResponseEntity<String> responseEntity = restTemplate.
                postForEntity(chatGptUrl, requestEntity, String.class);

        JSONObject responseJson = new JSONObject(Objects.requireNonNull(responseEntity.getBody()));
        JSONArray choices = (JSONArray) responseJson.get(CHOICES);

        JSONObject firstChoice = (JSONObject) choices.get(0);
        JSONObject message = (JSONObject) firstChoice.get("message");
        return String.valueOf(message.get(CONTENT));
    }

    private HttpHeaders setHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", apiToken);
        return headers;
    }
}