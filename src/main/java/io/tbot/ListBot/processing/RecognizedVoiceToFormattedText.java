package io.tbot.ListBot.processing;

import com.theokanning.openai.edit.EditChoice;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@NoArgsConstructor
public class RecognizedVoiceToFormattedText implements TextProcessing{

    @Value("${open.ai.token}")
    private String TOKEN;
    private static final String INSTRUCTION = "Split text into sentences. Correct punctuation errors.";
    private static final String MODEL = "text-davinci-edit-001";


    @Override
    public synchronized String processText(String text){
        OpenAiService service = new OpenAiService(TOKEN, Duration.ofSeconds(60));
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
}
