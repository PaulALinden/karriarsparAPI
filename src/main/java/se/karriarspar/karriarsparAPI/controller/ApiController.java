package se.karriarspar.karriarsparAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.karriarspar.karriarsparAPI.service.GeminiAIService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final GeminiAIService geminiAIService;

    ApiController(GeminiAIService geminiAIService){
        this.geminiAIService = geminiAIService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> getResponseFromGemini(@RequestBody String message) {

        String response = geminiAIService.callGeminiAPI(message);
        return ResponseEntity.ok(response);
    }
}
