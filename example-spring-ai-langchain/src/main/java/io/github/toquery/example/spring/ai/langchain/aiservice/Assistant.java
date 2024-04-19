package io.github.toquery.example.spring.ai.langchain.aiservice;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
interface Assistant {

    @SystemMessage("You are a polite assistant")
    String chat(String userMessage);
}
