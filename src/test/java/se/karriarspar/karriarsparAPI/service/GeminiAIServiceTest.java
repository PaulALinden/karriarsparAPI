package se.karriarspar.karriarsparAPI.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import se.karriarspar.karriarsparAPI.dto.Content;
import se.karriarspar.karriarsparAPI.dto.Part;
import se.karriarspar.karriarsparAPI.dto.Prompt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeminiAIServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiAIService geminiAIService;

    private static final String API_URL = "https://api.gemini.com/v1/test";
    private static final String API_KEY = "test-api-key";
    private static final String PREDEFINED_PROMPT = "Extrahera alla nyckelord ur denna jobbannonsen: ";

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(geminiAIService, "apiUrl", API_URL);
        ReflectionTestUtils.setField(geminiAIService, "apiKey", API_KEY);
    }

    @Test
    void testCallGeminiAPI_Success() {

        String userInput = "Software Engineer position";
        String expectedPrompt = PREDEFINED_PROMPT + userInput;
        String expectedResponse = "{\"keywords\": [\"Software\", \"Engineer\"]}";


        Prompt expectedPrompts = new Prompt();
        Content content = new Content();
        Part part = new Part();
        part.setText(expectedPrompt);
        content.setParts(List.of(part));
        expectedPrompts.setContents(List.of(content));

        ResponseEntity<String> mockResponse = ResponseEntity.ok(expectedResponse);
        when(restTemplate.exchange(
                eq(API_URL + "?key=" + API_KEY),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);


        String result = geminiAIService.callGeminiAPI(userInput);


        assertEquals(expectedResponse, result);
        verify(restTemplate, times(1)).exchange(
                eq(API_URL + "?key=" + API_KEY),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void testCallGeminiAPI_EmptyInput() {

        String userInput = "";
        String expectedPrompt = PREDEFINED_PROMPT;
        String expectedResponse = "{\"keywords\": []}";

        Prompt expectedPrompts = new Prompt();
        Content content = new Content();
        Part part = new Part();
        part.setText(expectedPrompt);
        content.setParts(List.of(part));
        expectedPrompts.setContents(List.of(content));

        ResponseEntity<String> mockResponse = ResponseEntity.ok(expectedResponse);
        when(restTemplate.exchange(
                eq(API_URL + "?key=" + API_KEY),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        String result = geminiAIService.callGeminiAPI(userInput);

        assertEquals(expectedResponse, result);
        verify(restTemplate, times(1)).exchange(
                eq(API_URL + "?key=" + API_KEY),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void testCallGeminiAPI_ApiFailure() {

        String userInput = "Software Engineer position";
        String expectedPrompt = PREDEFINED_PROMPT + userInput;

        when(restTemplate.exchange(
                eq(API_URL + "?key=" + API_KEY),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("API failure"));

        try {
            geminiAIService.callGeminiAPI(userInput);
        } catch (RuntimeException e) {
            assertEquals("API failure", e.getMessage());
        }

        verify(restTemplate, times(1)).exchange(
                eq(API_URL + "?key=" + API_KEY),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }
}