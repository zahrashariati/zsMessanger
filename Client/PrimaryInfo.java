package Client;

import java.io.Serializable;
import java.util.ArrayList;

public class PrimaryInfo implements Serializable {
    private ArrayList<String> registeredUserNames = new ArrayList<>();
    private ArrayList<PrivateChat> privateChats = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();
    private ArrayList<Channel> channels = new ArrayList<>();

    public ArrayList<String> getPrivateNames(){
        ArrayList<String> names = new ArrayList<>();
        for (PrivateChat privateChat : privateChats) {
            names.add(privateChat.getContactName());
        }
        return names;
    }

    public ArrayList<String> getGroupNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Group group : groups) {
            names.add(group.getGroupName());
        }
        return names;
    }

    public ArrayList<String> getChannelNames(){
        ArrayList<String> names = new ArrayList<>();
        for (Channel channel : channels) {
            names.add(channel.getChannelName());
        }
        return names;
    }

    public ArrayList<String> getRegisteredUserNames() {
        return registeredUserNames;
    }

    public void setRegisteredUserNames(ArrayList<String> registeredUserNames) {
        this.registeredUserNames = registeredUserNames;
    }

    public ArrayList<PrivateChat> getPrivateChats() {
        return privateChats;
    }

    public void setPrivateChats(ArrayList<PrivateChat> privateChats) {
        this.privateChats = privateChats;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }
}
