package org.trivia.wetalk;

public class UploadResponseImpl<MediaType> extends ResponseImpl implements UploadResponse<MediaType> {

    private MediaType type;
    private String media_id;
    private int create_at;

    @Override
    public MediaType getType() {
        return type;
    }

    @Override
    public void setType(MediaType type) {
        this.type = type;
    }

    @Override
    public String getMedia_id() {
        return media_id;
    }

    @Override
    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    @Override
    public int getCreate_at() {
        return create_at;
    }

    @Override
    public void setCreate_at(int create_at) {
        this.create_at = create_at;
    }

    @Override
    public UploadResponseImpl setErrcode(int errcode) {
        super.setErrcode(errcode);
        return this;
    }

    @Override
    public UploadResponseImpl setErrmsg(String errmsg) {
        super.setErrmsg(errmsg);
        return this;
    }
}
