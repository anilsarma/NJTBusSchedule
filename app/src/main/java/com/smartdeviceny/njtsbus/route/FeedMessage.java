package com.smartdeviceny.njtsbus.route;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedMessage {

    public String title;
    public String description;
    public String link;
    public String author;
    public String guid;
    public String pubDate;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description + ", link=" + link + ", author=" + author + ", guid=" + guid + "]";
    }

    public void marshall(JSONObject payload) throws JSONException {
        payload.put("title", title);
        payload.put("description", description);
        payload.put("link", link);
        payload.put("author", author);
        payload.put("guid", guid);
        payload.put("pubDate", pubDate);
    }

    public void unmarshall(JSONObject payload) throws JSONException {
        if (payload.has("title")) {
            title = payload.getString("title");
        }
        if (payload.has("description")) {
            description = payload.getString("description");
        }
        if (payload.has("link")) {
            link = payload.getString("link");
        }
        if (payload.has("author")) {
            author = payload.getString("author");
        }
        if (payload.has("guid")) {
            guid = payload.getString("guid");
        }
        if (payload.has("pubDate")) {
            pubDate = payload.getString("pubDate");
        }

    }
}