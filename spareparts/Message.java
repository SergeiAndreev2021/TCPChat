package TCPChat.spareparts;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String sender;
    private String text;
    private LocalDateTime dateTime;
    private int senderId;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.senderId = sender.hashCode()+(int)(Math.random()*100);
    }
    public String getSender() {
        return sender;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getText() {
        return text;
    }

    public void setDateTime(){
        dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
    public static Message getMessage(String sender, String text){
       return new Message(sender,text);
    }
}
