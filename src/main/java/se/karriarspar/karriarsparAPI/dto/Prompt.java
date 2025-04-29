package se.karriarspar.karriarsparAPI.dto;

import java.util.List;

public class Prompt {
    private List<Content> contents;

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
}
