package io.tbot.ListBot.parser;

import lombok.Data;
import org.json.JSONObject;
@Data
public class JsonParser {
    public String parseToString(String string) {
        JSONObject jsonObject = new JSONObject(string);
        return jsonObject.getString("text");
    }

}
