package com.uwaterloo.jinhwan.vidme;

public class VideoStatus {
    private String title;
    private String videoId;
    private String publishedDate;
    private String videoDescription;
    private String channelTitle;
    private String defaultThumbnailURL;

    public VideoStatus (String title, String videoId, String publishedDate,
                      String videoDescription, String channelTitle, String defaultThumbnailURL) {
        this.title = title;
        this.videoId = videoId;
        this.publishedDate = publishedDate;
        this.videoDescription = videoDescription;
        this.channelTitle = channelTitle;
        this.defaultThumbnailURL = defaultThumbnailURL;
    }

    public String getDefaultThumbnailURL() {
        return defaultThumbnailURL;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }
}
