package io.tbot.ListBot.processing;

import com.theokanning.openai.edit.EditChoice;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;
import java.util.List;

@PropertySource("classpath:application.properties")
public class CorrectionOfErrorsInTheText implements TextProcessing{

    @Getter
    @Setter
    private String TEXT_TO_CORRECT;
    private final OpenAiService service;

    @Value("${ai.key}")
    private String TOKEN;
    private static final String INSTRUCTION = "Correct puncture errors and place points where necessary";
    private static final String MODEL = "ext-davinci-edit-001";


    public CorrectionOfErrorsInTheText(String textToCorrect) {
        TEXT_TO_CORRECT = textToCorrect;
        service = new OpenAiService(TOKEN, Duration.ofSeconds(60));
    }

    @Override
    public String processText(){
        //OpenAiService service = new OpenAiService(TOKEN, Duration.ofSeconds(60));
        //System.out.println("\nCreating completion...");
        EditRequest editRequest = EditRequest.builder()
                .model(MODEL)
                .input(TEXT_TO_CORRECT)
                .instruction(INSTRUCTION)
                .n(1)
                .temperature(0.2)
                .build();
        List<EditChoice> editResult = service.createEdit(editRequest).getChoices();
        service.shutdownExecutor();
        return editResult.get(0).getText();
//        System.out.println(editResult.get(0).getText());
    }
}
