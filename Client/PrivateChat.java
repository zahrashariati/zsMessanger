package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateChat implements Serializable {
    private String contactName;
    private ArrayList<Message> messages = new ArrayList<Message>();
    public PrivateChat(){}
    public PrivateChat(String contactName, ArrayList<Message> messages) {
        this.contactName = contactName;
        this.messages = messages;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
