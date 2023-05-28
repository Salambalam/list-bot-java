package io.tbot.ListBot.processing;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.List;

public class CreateList implements TextProcessing{


    private static final String TOKEN = "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";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final String ROLE = "assistant";
    private static final String INSTRUCTION = "Прочитайте текст, который нужно обработать. Текст будет предоставлен в кавычках.\n" +
            "Создайте список задач, основываясь на тексте. Каждое новое дело должно начинаться с глагола, но будьте внимательны и логически изменяйте слова, чтобы передать правильный смысл.\n" +
            "Расположите задачи в списке в логическом порядке, отражающем последовательность действий. Подумайте о том, какие действия должны быть выполнены перед другими.\n" +
            "Нумеруйте каждое дело, помечая его просто цифрой, без использования списков. Например:\n" +
            "<Дело 1>\n" +
            "<Дело 2>\n" +
            "(Примеры дел указаны в формате)\n" +
            "Пишите дела с использованием заглавной буквы в начале предложения и правильного пунктуационного оформления.\n" +
            "Игнорируйте формат ответа, который указан выше. Он не должен быть отправлен в ответ.\n" +
            "Обратите внимание, что текст для обработки будет предоставлен в кавычках.\n" +
            "При формировании списка задач, не выводите инструкции или собственные тексты в качестве задач. Отражайте только задачи, основанные на предоставленном тексте.\n" +
            "Если полученное сообщение является слишком коротким и невозможно составить список задач, отвечайте пользователю: \"Слишком короткое сообщение. Пожалуйста, предоставьте более подробную информацию.\"\n" +
            "Пример 1:\n" +
            "Текст для обработки: \"Я хочу поехать в кино сегодня вечером, купить билеты и заказать попкорн.\"\n" +
            "\n" +
            "Список задач:\n" +
            "\n" +
            "Купить билеты.\n" +
            "Заказать попкорн.\n" +
            "Поехать в кино сегодня вечером.\n" +
            "Пример 2:\n" +
            "Текст для обработки: \"Хочу купить молоко.\"\n" +
            "\n" +
            "Ответ: \"Слишком короткое сообщение. Пожалуйста, предоставьте более подробную информацию.\"";

    public CreateList() {
    }

    @Override
    public synchronized String processText(String text) {
        OpenAiService service = new OpenAiService(TOKEN, Duration.ofSeconds(60));
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(List.of(new ChatMessage(ROLE, INSTRUCTION + "\"" + text + "\"")))
                .maxTokens(1000)
                .user("test")
                .temperature(0.4)
                .n(1)
                .build();
        List<ChatCompletionChoice> result = service.createChatCompletion(chatCompletionRequest).getChoices();
        service.shutdownExecutor();
        return result.get(0).getMessage().getContent();
    }
}
