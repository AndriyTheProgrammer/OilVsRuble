package ua.com.oilversusruble.oilversusruble;

import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by User on 17.05.2015.
 */
public class ChatMessage {
    ParseUser author;
    String message = "null";
    Date date;

    public ChatMessage(ParseUser authorId, String message, Date date) {
        this.author = authorId;
        this.message = message;
        this.date = date;
    }

    public ParseUser getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
