package se.karriarspar.karriarsparAPI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.karriarspar.karriarsparAPI.dto.Content;
import se.karriarspar.karriarsparAPI.dto.Part;
import se.karriarspar.karriarsparAPI.dto.Prompt;

import java.util.List;
//Service
@Service
public class GeminiAIService {

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String PREDEFINED_PROMPT = "Extrahera alla nyckelord ur denna jobbannonsen: ";

    private final RestTemplate restTemplate;

    public GeminiAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callGeminiAPI(String userInput) {

        String fullPrompt = PREDEFINED_PROMPT + userInput;

        Prompt prompt = new Prompt();
        Content content = new Content();
        Part part = new Part();
        part.setText(fullPrompt);
        content.setParts(List.of(part));
        prompt.setContents(List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Prompt> requestEntity = new HttpEntity<>(prompt, headers);

        String urlWithKey = apiUrl + "?key=" + apiKey;
        ResponseEntity<String> response = restTemplate.exchange(
                urlWithKey,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getBody();
    }
}
