package main.java.com.filter.model;


import java.util.Map;

public class FilterWrapper {

    Map<String, String> tags;

    byte[] data;

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
