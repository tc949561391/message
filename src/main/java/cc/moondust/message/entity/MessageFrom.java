package cc.moondust.message.entity;

/**
 * Created by j0 on 2016/8/2.
 */
public class MessageFrom {

    private String from;

    private String to;

    private String receverType;

    private String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getReceverType() {
        return receverType;
    }

    public void setReceverType(String receverType) {
        this.receverType = receverType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
