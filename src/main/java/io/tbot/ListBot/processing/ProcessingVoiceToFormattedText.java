package io.tbot.ListBot.processing;

import com.theokanning.openai.edit.EditChoice;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.service.OpenAiService;
import io.tbot.ListBot.command.BotCommands;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * Класс ProcessingVoiceToFormattedText.
 * Реализация интерфейса TextProcessing для обработки голосового текста и преобразования его в форматированный текст.
 * Для преобразования использует api OpenAI.
 */
@Component
@Slf4j
@NoArgsConstructor
public class ProcessingVoiceToFormattedText implements TextProcessing {

    @Value("${open.ai.token}")
    private String openAiToken;

    private static final String INSTRUCTION = "Split text into sentences. Correct punctuation errors.";
    private static final String MODEL = "text-davinci-edit-001";

    /**
     * Метод processText.
     * Обрабатывает голосовой текст и преобразует его в форматированный текст с помощью сервиса OpenAI.
     *
     * @return Преобразованный форматированный текст.
     */
    @Override
    public synchronized String processText(String text) {
        OpenAiService service = new OpenAiService(openAiToken, Duration.ofSeconds(60));
        EditRequest editRequest = EditRequest.builder()
                .model(MODEL)
                .input(text)
                .instruction(INSTRUCTION)
                .n(1)
                .temperature(0.2)
                .build();
        List<EditChoice> editResult = service.createEdit(editRequest).getChoices();
        service.shutdownExecutor();
        return editResult.get(0).getText();
    }

    /**
     * Метод canProcessing.
     * Проверяет, команду которая определяет способ обработки.
     *
     * @return true, если класс может обработать команду(CORRECT); false в противном случае.
     */
    @Override
    public boolean canProcessing(String command) {
        return command.equals(BotCommands.CORRECT_COMMAND.getCommand());
    }
}