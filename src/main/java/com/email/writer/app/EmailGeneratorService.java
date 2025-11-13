////package com.email.writer.app;
////
////import com.fasterxml.jackson.databind.JsonNode;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.stereotype.Service;
////import org.springframework.web.reactive.function.client.WebClient;
//////import org.springframework.web.reactive.function.client.WebClient;
////
////import java.net.http.HttpRequest;
////import java.util.Map;
////
////@Service
////
////public class EmailGeneratorService {
////
////    private final WebClient webClient;
////
////
////    @Value("${gemini.api.url}")
////    private String  geminiApiUrl;
////    @Value("${gemini.api.key}")
////    private  String  geminiApiKey;
////
////    public EmailGeneratorService(WebClient.Builder webClient) {
////        this.webClient = WebClient.builder().build();
////    }
////
////    public String generateEmailReply(EmailRequest emailRequest){
////        //build the prompt
////        String prompt =buildPrompt(emailRequest);
////        // craft a request
////        Map<String,Object> requestBody =Map.of(
////                "contents", new Object[]{
////                        Map.of("parts",  new Object[]{
////                                Map.of("text",prompt)
////                        })
////                }
////        );
////        // do request then we get the response
////// WE ARE GOING TO USE WEBCLIENT
////String response = webClient.post()
//////.uri(geminiApiUrl + geminiApiKey)
////        .uri(geminiApiUrl + geminiApiKey)
////                .header("Content-Type", "application/json")
////        .bodyValue(requestBody)
////                .retrieve()
////                .bodyToMono(String.class)
////                .block();
////        // extract Nd return response
////        return extractResponseContent(response);
////
////    }
////
////
////    private  String     extractResponseContent(String response) {
////        try{
////            ObjectMapper mapper= new ObjectMapper();
////            JsonNode rootNode = mapper.readTree(response);  //readtree converts json text
////            return rootNode.path("candidates")
////                    .get(0)
////                    .path("content")
////                    .path("parts")
////                    .get(0)
////                    .path("text")
////                    .asText();
////
////        } catch (Exception e) {
////            return "Error processing request: " +e.getMessage();
////        }
////    }
////
////    private String buildPrompt(EmailRequest emailRequest) {
////        StringBuilder prompt =new StringBuilder();
////        prompt.append("Generate a professional email reply for the following email content .Please don't generate a subject line ");
////        if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
////            prompt.append("use a ").append(emailRequest.getTone()).append("tone. ");
////        }
////        prompt.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
////        return prompt.toString();
////    }
////
////}
//package com.email.writer.app;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class EmailGeneratorService {
//
//    private final WebClient webClient;
//
//    @Value("${gemini.api.url}")
//    private String geminiApiUrl;
//
//    @Value("${gemini.api.key}")
//    private String geminiApiKey;
//
//    public EmailGeneratorService(WebClient.Builder builder) {
//        this.webClient = builder.build();
//    }
//
//    public String generateEmailReply(EmailRequest emailRequest) {
//        String prompt = buildPrompt(emailRequest);
//
//        Map<String, Object> requestBody = Map.of(
//                "contents", new Object[]{
//                        Map.of("role", "user", "parts", new Object[]{
//                                Map.of("text", prompt)
//                        })
//                }
//        );
//
//        try {
//            String url = geminiApiUrl + "?key=" + geminiApiKey;
//
//            String response = webClient.post()
//                    .uri(url)
//                    .header("Content-Type", "application/json")
//                    .bodyValue(Map.of(
//                            "contents", List.of(Map.of(
//                                    "parts", List.of(Map.of("text", prompt))
//                            ))
//                    ))
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//        } catch (WebClientResponseException e) {
//            return "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    private String extractResponseContent(String response) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(response);
//            return rootNode.path("candidates")
//                    .get(0)
//                    .path("content")
//                    .path("parts")
//                    .get(0)
//                    .path("text")
//                    .asText();
//        } catch (Exception e) {
//            return "Error processing request: " + e.getMessage();
//        }
//    }
//
//    private String buildPrompt(EmailRequest emailRequest) {
//        StringBuilder prompt = new StringBuilder();
//        prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line. ");
//        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
//            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
//        }
//        prompt.append("\nOriginal email:\n").append(emailRequest.getEmailContent());
//        return prompt.toString();
//
//    }
//}

package com.email.writer.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public EmailGeneratorService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public String generateEmailReply(EmailRequest emailRequest) {
        String prompt = buildPrompt(emailRequest);

        try {
            // Construct final URL with key
            String url = geminiApiUrl + "?key=" + geminiApiKey;

            // Build request body
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(Map.of("text", prompt))
                    ))
            );

            // Make API call
            String response = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Extract and return clean text
            return extractResponseContent(response);

        } catch (WebClientResponseException e) {
            return "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line. ");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
        }
        prompt.append("\nOriginal email:\n").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
