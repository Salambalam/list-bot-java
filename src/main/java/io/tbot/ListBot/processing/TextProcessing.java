package io.tbot.ListBot.processing;

public interface TextProcessing {
    String processText(String text);

    boolean canProcessing(String command);

}

