package eCare.model.PO;

import java.util.List;

/**
 * Created by echerkas on 26.04.2018.
 */
public class MessagesList {

    private List<String> messageList;

    private Feature messageFeature;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessagesList(){}

    public Feature getMessageFeature() {
        return messageFeature;
    }

    public void setMessageFeature(Feature messageFeature) {
        this.messageFeature = messageFeature;
    }

    public List<String> getMessageList() {
        return messageList;

    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "MessagesList{" +
                "messageList=" + messageList +
                '}';
    }
}
