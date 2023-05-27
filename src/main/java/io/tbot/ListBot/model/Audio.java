package io.tbot.ListBot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "audioPathTable")
public class Audio {


    String path;
    @Id
    long chatId;

    public Audio(String path, long chatId) {
        this.path = path;
        this.chatId = chatId;
    }

    public Audio() {

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audio audio = (Audio) o;
        return chatId == audio.chatId && Objects.equals(path, audio.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, chatId);
    }

    @Override
    public String toString() {
        return "Audio{" +
                "path='" + path + '\'' +
                ", chatId=" + chatId +
                '}';
    }
}
