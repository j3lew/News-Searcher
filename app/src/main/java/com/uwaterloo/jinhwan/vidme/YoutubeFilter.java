package com.uwaterloo.jinhwan.vidme;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class YoutubeFilter {
    public static JSONObject getYoutubeData(String searchKeyWord, int itemCount) {
        HttpGet httpGet = new HttpGet(
                "https://www.googleapis.com/youtube/v3/search?"
                        + "part=snippet&q=" + searchKeyWord
                        + "&key=" + DeveloperKey.DEVELOPER_KEY
                        + "&maxResults=" + itemCount);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while((b = stream.read()) != -1)
            {
                stringBuilder.append((char) b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static List<VideoStatus> parseJsonData(List<VideoStatus> list, JSONObject jsonObject) throws JSONException{

        JSONArray videoList = jsonObject.getJSONArray("items");
        for (int i = 0; i < videoList.length(); i++) {
            String title;
            String videoId;
            String publishedDate;
            String description;
            String channelTitle;
            JSONObject currentVideo = videoList.getJSONObject(i);
            String type = currentVideo.getJSONObject("id").getString("kind");
            if(type.equals("youtube#video")) {
                videoId = currentVideo.getJSONObject("id").getString("videoId");
            } else {
                videoId = currentVideo.getJSONObject("id").getString("playlistId");
            }
            title = currentVideo.getJSONObject("snippet").getString("title");
            description = currentVideo.getJSONObject("snippet").getString("description");
            channelTitle = currentVideo.getJSONObject("snippet").getString("channelTitle");

            String changedTitle = "";
            String changedDescription = "";
            String changedChannelTitle = "";
            try {
                changedTitle = new String(title.getBytes("8859_1"), "utf-8");
                changedDescription = new String(description.getBytes("8859_1"), "utf-8");
                changedChannelTitle = new String(channelTitle.getBytes("8859_1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            publishedDate = currentVideo.getJSONObject("snippet").getString("publishedAt").substring(0,10);
            String imgUrl = currentVideo.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url");

            list.add(new VideoStatus(changedTitle, videoId, publishedDate, changedDescription, changedChannelTitle, imgUrl));
        }

        return list;
    }
}
