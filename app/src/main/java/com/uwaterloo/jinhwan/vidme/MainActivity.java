package com.uwaterloo.jinhwan.vidme;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout homeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        homeLayout = findViewById(R.id.home_layout);

        final NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        homeLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                findViewById(R.id.video_list).setVisibility(View.GONE);
                                break;
                            case R.id.nav_breaking_news:
                                findViewById(R.id.video_list).setVisibility(View.VISIBLE);
                                VideoListFragment.setSearchKeyWord(getResources().getString(R.string.search_breaking_news));
                                break;
                            case R.id.nav_sports_news:
                                findViewById(R.id.video_list).setVisibility(View.VISIBLE);
                                VideoListFragment.setSearchKeyWord(getResources().getString(R.string.search_sports_news));
                                break;
                            case R.id.nav_health_news:
                                findViewById(R.id.video_list).setVisibility(View.VISIBLE);
                                VideoListFragment.setSearchKeyWord(getResources().getString(R.string.search_health_news));
                                break;
                            case R.id.nav_education_news:
                                findViewById(R.id.video_list).setVisibility(View.VISIBLE);
                                VideoListFragment.setSearchKeyWord(getResources().getString(R.string.search_education_news));
                                break;
                            default:
                                break;
                        }

                        // Update the recycler view according to the search category
                        VideoListFragment videoListFragment = new VideoListFragment();
                        FragmentManager fragManager = getSupportFragmentManager();
                        fragManager.beginTransaction()
                                .replace(R.id.video_list_fragment, videoListFragment)
                                .addToBackStack(null)
                                .commit();

                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                homeLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
