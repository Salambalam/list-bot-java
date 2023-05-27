package io.tbot.ListBot.processing;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.List;

public class CreateList implements TextProcessing{

    @Getter
    @Setter
    private String TEXT_TO_LIST;
    private final OpenAiService service;

    @Value("${ai.key}")
    private String TOKEN;
    private static final String MODEL = "gpt-3.5-turbo";
    private static final String ROLE = "system";
    private static final String INSTRUCTION = "из текста ниже создай список дел в таком формате, новое дело, скорее всего начинается с глагола, " +
            "но будь внимателен. Номер дела указывай не списком а помечай просто в виде цифры. Текст в кавычках\n" +
            "Дела на сегодня:\n" +
            "1 - <Дело один;>\n" +
            "2 - <Дело два;>";

    public CreateList(String TEXT_TO_LIST) {
        this.TEXT_TO_LIST = TEXT_TO_LIST;
        service = new OpenAiService(TOKEN, Duration.ofSeconds(60));
    }

    @Override
    public String processText() {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(List.of(new ChatMessage(ROLE, INSTRUCTION + TEXT_TO_LIST)))
                .maxTokens(1000)
                .user("test")
                .temperature(0.2)
                .n(1)
                .build();
        List<ChatCompletionChoice> result = service.createChatCompletion(chatCompletionRequest).getChoices();
        //System.out.println(result.get(0).getMessage().getContent());
        service.shutdownExecutor();
        return result.get(0).getMessage().getContent();
    }
}
