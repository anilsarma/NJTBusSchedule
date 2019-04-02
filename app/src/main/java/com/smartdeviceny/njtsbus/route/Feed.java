package com.smartdeviceny.njtsbus.route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Stores an RSS feed
 */
public class Feed {

    String title;
    String link;
    String description;
    String language;
    String copyright;
    String pubDate;

    final List<FeedMessage> messages = new ArrayList<FeedMessage>();

    public Feed() {

    }

    public Feed(String title, String link, String description, String language, String copyright, String pubDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.copyright = copyright;
        this.pubDate = pubDate;
    }

    public List<FeedMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<FeedMessage> msgs) {
        messages.clear();
        for (FeedMessage msg : msgs) {
            messages.add(msg);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for(FeedMessage item:messages) {
            buffer.append(item.toString() + "\n");
        }
        return "Feed [copyright=" + copyright + ", description=" + description + ", language=" + language + ", link=" + link + ", pubDate=" + pubDate + ", title=" + title + "]"
                + "\n" + buffer.toString() + "\n";

    }

    public void marshall(JSONObject payload) throws JSONException {
        payload.put("title", title);
        payload.put("link", link);
        payload.put("description", description);
        payload.put("language", language);
        payload.put("copyright", copyright);
        payload.put("pubDate", pubDate);

        JSONArray msgs = new JSONArray();
        for (FeedMessage msg : messages) {
            JSONObject obj = new JSONObject();
            msg.marshall(obj);
            msgs.put(msgs.length(), obj);
        }
        payload.put("messages", msgs);
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
        if (payload.has("language")) {
            language = payload.getString("language");
        }
        if (payload.has("copyright")) {
            copyright = payload.getString("copyright");
        }
        if (payload.has("pubDate")) {
            pubDate = payload.getString("pubDate");
        }
        if (payload.has("messages")) {
            JSONArray array = payload.getJSONArray("messages");
            for (int i = 0; i < array.length(); i++) {
                FeedMessage item = new FeedMessage();
                item.unmarshall(array.getJSONObject(i));
                messages.add(item);
            }
        }
    }


}