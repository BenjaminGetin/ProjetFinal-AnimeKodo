package fr.kitsuapirest.ApiResponse;

public class AnimeData {
    private String id;
    private String type;
    private AnimeAttributes attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AnimeAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(AnimeAttributes attributes) {
        this.attributes = attributes;
    }
}