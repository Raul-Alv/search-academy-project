package co.empathy.academy.search.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Akas {
    private String titleId;
    private String title;
    private String region;
    private String language;

    @JsonProperty("isOriginalTitle")
    private boolean isOriginalTitle;

    public boolean isOriginalTitle() {
        return isOriginalTitle;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean getIsOriginalTitle() {
        return isOriginalTitle;
    }

    public void setIsOriginalTitle(boolean isOriginalTitle) {
        this.isOriginalTitle = isOriginalTitle;
    }

}
