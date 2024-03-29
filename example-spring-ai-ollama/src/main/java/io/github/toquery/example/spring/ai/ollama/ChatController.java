package io.github.toquery.example.spring.ai.ollama;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final OllamaChatClient chatClient;

    @Autowired
    public ChatController(OllamaChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping("/ai/generate-prompt")
    public Map generatePrompt(@RequestParam(value = "userMessage", defaultValue = "Tell me a joke") String userMessage,
                              @RequestParam(value = "assistantMessage", required = false) String assistantMessage,
                              @RequestParam(value = "systemMessage", required = false) String systemMessage,
                              @RequestParam(value = "functionMessage", required = false) String functionMessage) {

        List<Message> messages = new ArrayList<>();
        if (userMessage != null) {
            messages.add(new UserMessage(userMessage));
        }
        if (assistantMessage != null) {
            messages.add(new AssistantMessage(assistantMessage));
        }
        if (systemMessage != null) {
            messages.add(new SystemMessage(systemMessage));
        }
        if (functionMessage != null) {
            messages.add(new FunctionMessage(functionMessage));
        }
        Prompt prompt = new Prompt(messages);
        return Map.of("messages", messages, "generation", chatClient.call(prompt));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
    }

}
