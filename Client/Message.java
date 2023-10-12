package Client;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
    public String sender;
    public String receiver;
    public String type;    // it can be "private" , "group" , "channel"
    public String message="";
    public String fileName = "";
    public String fileAddress = "";
    public File file = null;
    public Date date;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    // constructor : message without file
    public Message(String senderName, String receiverName, String type, String text) {
        this.sender = senderName;
        this.receiver = receiverName;
        this.type = type;
        this.message = text;
        date = new Date();
        dateFormat.format(date);
    }
    // constructor : message with file
    public Message(String senderName, String receiverName, String type , String fileName , String filePath) {
        this.sender = senderName;
        this.receiver = receiverName;
        this.type = type;
        this.fileName = fileName + filePath.substring(filePath.indexOf("."));
        this.fileAddress = filePath;
        file = new File(filePath);
        date = new Date();
        dateFormat.format(date);
    }

    public Message() {

    }

    public String getFilePath() {
        return fileAddress;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public String getSenderName() {
        return sender;
    }

    public String getReceiverName() {
        return receiver;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return message;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
