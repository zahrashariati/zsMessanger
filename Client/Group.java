package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {
    private String groupName;
    private ArrayList<String> members;
    private ArrayList<Message> messages = new ArrayList<Message>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
