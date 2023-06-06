package io.tbot.ListBot.parser;

import lombok.Data;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Класс JsonParser предназначен для парсинга текста, переданного из модели распознования vosk.
 */
@Data
@Component
public class JsonParser {
    public String parseToString(String string) {
        JSONObject jsonObject = new JSONObject(string);
        return jsonObject.getString("text");
    }

}
