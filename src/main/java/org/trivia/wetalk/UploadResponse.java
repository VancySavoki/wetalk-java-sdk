package org.trivia.wetalk;

public interface UploadResponse<T> extends Response {
    T getType();

    void setType(T type);

    String getMedia_id();

    void setMedia_id(String media_id);

    int getCreate_at();

    void setCreate_at(int create_at);
}
