package fr.kitsuapirest.ApiResponse;

import java.util.List;

public class ApiResponse {
    private List<AnimeData> data;

    public List<AnimeData> getData() {
        return data;
    }

    public void setData(List<AnimeData> data) {
        this.data = data;
    }
}
