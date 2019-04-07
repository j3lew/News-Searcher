package com.uwaterloo.jinhwan.vidme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<VideoStatus> mVideoStatus;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView videoTitleView;
        public ImageView thumbnailView;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnailView = (ImageView) itemView.findViewById(R.id.thumbnail_view);
            videoTitleView = (TextView) itemView.findViewById(R.id.video_title_view);
        }
    }

    public VideoListAdapter(List<VideoStatus> videoStatuses) {
        this.mVideoStatus = videoStatuses;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.video_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    public void setData(List<VideoStatus> videoStatus) {
        this.mVideoStatus = videoStatus;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        VideoStatus video = mVideoStatus.get(position);

        // Set thumbnail for the video list
        ImageView thumbnailView = viewHolder.thumbnailView;
        Bitmap myBitmap = getBitmapFromURL(video.getDefaultThumbnailURL());
        if (myBitmap != null) {
            thumbnailView.setImageBitmap(myBitmap);
        } else {
//            TODO: put progress bar
        }

        // Set title for the video list
        TextView videoTitleView = viewHolder.videoTitleView;
        videoTitleView.setText(video.getTitle());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mVideoStatus == null ? 0 : mVideoStatus.size();
    }

    private Bitmap getBitmapFromURL(String address) {
        try {
            URL url = new URL(address);
            Log.d("Charlie", ""+url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
