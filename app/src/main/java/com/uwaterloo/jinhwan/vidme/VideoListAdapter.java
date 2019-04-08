package com.uwaterloo.jinhwan.vidme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<VideoStatus> mVideoStatus;
    private Map<YouTubeThumbnailView, YouTubeThumbnailLoader> mThumbnailViewYouTubeThumbnailLoaderMap =
            new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
    private LayoutInflater mLayoutInflater;
    private ThumbnailListener mThumbnailListener = new ThumbnailListener();
    private Context context;

    public VideoListAdapter(List<VideoStatus> videoStatuses) {
        this.mVideoStatus = videoStatuses;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        context = parent.getContext();
        mLayoutInflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = mLayoutInflater.inflate(R.layout.video_list_item, parent, false);

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

        ((ViewHolder)viewHolder).bind(video);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView videoTitleView;
        private YouTubeThumbnailView youTubeThumbnailView;
        private VideoStatus mVideoStatusHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.thumbnail_view);
            videoTitleView = (TextView) itemView.findViewById(R.id.video_title_view);
            itemView.setOnClickListener(this);
        }

        void bind(final VideoStatus videoStatus) {

            mVideoStatusHolder = videoStatus;

            // Set thumbnail for the video list
            youTubeThumbnailView.setTag(videoStatus.getVideoId());
            youTubeThumbnailView.initialize(DeveloperKey.DEVELOPER_KEY, mThumbnailListener);

            // Set title for the video list
            videoTitleView.setText(videoStatus.getTitle());
        }

        @Override
        public void onClick(View v) {
            YoutubeFragment youtubeFragment = new YoutubeFragment();
            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.youtube_layout, youtubeFragment)
                    .addToBackStack(null)
                    .commit();

            YoutubeFragment.mVideoStatus.setValue(mVideoStatusHolder);
//            Log.d("Charlie", mVideoStatusHolder.getTitle());
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mVideoStatus == null ? 0 : mVideoStatus.size();
    }

//    Thumbnail: tried to use bitmap
//    private Bitmap getBitmapFromURL(String address) {
//        try {
//            URL url = new URL(address);
//            Log.d("Charlie", ""+url);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            mThumbnailViewYouTubeThumbnailLoaderMap.put(view, loader);
            view.setImageResource(R.drawable.ic_error_outline_black_24dp);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {}

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }
}
