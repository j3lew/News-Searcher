package com.uwaterloo.jinhwan.vidme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListFragment extends Fragment {

    private MutableLiveData<List<VideoStatus>> mVideoStatuses = new MutableLiveData<>();
    private List<VideoStatus> videoStatusList = new ArrayList<>();
    private VideoListAdapter mVideoListAdapter;
    private static String mSearchKeyWord;

    public static void setSearchKeyWord(String searchKeyWord) {
        mSearchKeyWord = searchKeyWord;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Network operations on the UI thread give NetworkOnMainThreadException.
        // So networking process must be done in a new thread (Async task)
        if (mSearchKeyWord != null) {
            new VideoRequestTask().execute();
        }

        View rootView = inflater.inflate(R.layout.video_list_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.video_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        mVideoListAdapter = new VideoListAdapter(videoStatusList);
        recyclerView.setLayoutManager(linearLayoutManager);

        final Observer<List<VideoStatus>> videoStatusObserver = new Observer<List<VideoStatus>>() {
            @Override
            public void onChanged(List<VideoStatus> videoStatuses) {
                // Update the UI, in this case, a TextView.
                if (videoStatuses != null || videoStatuses.size() > 0){
                    videoStatusList = videoStatuses;
                    mVideoListAdapter.setData(videoStatusList);
                }
            }
        };

        mVideoStatuses.observe(this, videoStatusObserver);
        recyclerView.setAdapter(mVideoListAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoListAdapter.notifyDataSetChanged();
    }

    private class VideoRequestTask extends AsyncTask<Void, Void, List<VideoStatus>> {
        @Override
        protected List<VideoStatus> doInBackground(Void... voids) {

            List<VideoStatus> videoStatusList = new ArrayList<>();

            JSONObject jsonObject = YoutubeFilter.getYoutubeData(mSearchKeyWord, 10);
            try {
                videoStatusList = YoutubeFilter.parseJsonData(videoStatusList, jsonObject);
            } catch (JSONException e){
                e.printStackTrace();
            }
            return videoStatusList;
        }

        @Override
        protected void onPostExecute(List<VideoStatus> videoStatuses) {
            // Refresh recycler view when data is loaded/changed
//            Attempt: 2
//            Use Livedata to notify when there is a change in data --> trigger observer onChanged
            mVideoStatuses.setValue(videoStatuses);

//            Attempt: 1
//            List<VideoStatus> tempList = new ArrayList<>();
//
//            Iterator<VideoStatus> iterator = videoStatuses.iterator();
//            while (iterator.hasNext()) {
//                VideoStatus status = iterator.next();
//                if (status == videoStatuses.get(videoStatuses.size() - 1)){
//                    tempList.add(status);
//                }
//            }
//
//            videoStatusList.addAll(tempList);
//            for (int i = 0; i < videoStatusList.size(); i++) {
//                Log.d("hello", videoStatusList.get(i).getTitle());
//            }
//            mVideoListAdapter.notifyDataSetChanged();
        }
    }
}
