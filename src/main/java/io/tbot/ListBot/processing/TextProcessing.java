package io.tbot.ListBot.processing;

/**
 * Интерфейс TextProcessing.
 * Определяет методы для обработки текста.
 */
public interface TextProcessing {

    /**
     * Метод processText.
     * Обрабатывает текст и возвращает результат обработки.
     *
     * @return Результат обработки текста.
     */
    String processText(String text);

    /**
     * Метод canProcessing.
     * Проверяет, каким способом будет обработан текст.
     *
     * @param command Команда для обработки.
     * @return true, если класс может обработать команду; false в противном случае.
     */
    boolean canProcessing(String command);
}

