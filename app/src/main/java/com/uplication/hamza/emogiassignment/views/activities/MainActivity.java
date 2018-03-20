package com.uplication.hamza.emogiassignment.views.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.uplication.hamza.emogiassignment.R;
import com.uplication.hamza.emogiassignment.data.SingletonDataHolder;
import com.uplication.hamza.emogiassignment.model.Content;
import com.uplication.hamza.emogiassignment.views.adapter.GalleryItemAdapter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnQueryTextListener{

  private static final String TAG = "MainActivity";
  private RecyclerView mRecyclerView;
  private LayoutManager mLayoutManager;
  private GalleryItemAdapter mAdapter;
  private TextView searchTagHint;
  private TextView searchTime;
  private int dataType;
  private final static int PROCESSED_DATA = 1;
  private final static int RAW_DATA = 2;
  private static String DATA_TYPE = "DATA_TYPE";


  public static Intent getProcessedDataIntent(Activity activity){
    Intent intent = new Intent(activity,MainActivity.class);
    intent.putExtra(DATA_TYPE,PROCESSED_DATA);
    return intent;
  }

  public static Intent getRawDataIntent(Activity activity){
    Intent intent = new Intent(activity,MainActivity.class);
    intent.putExtra(DATA_TYPE,RAW_DATA);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mRecyclerView = findViewById(R.id.gallery_list_rv);
    searchTagHint = findViewById(R.id.search_tag_hint);
    searchTime = findViewById(R.id.search_time);
    mLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
    mAdapter = new GalleryItemAdapter(MainActivity.this);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);
    dataType = getIntent().getIntExtra(DATA_TYPE,0);
    switch (dataType){
      case PROCESSED_DATA:
        setTitle("Processed Data");
        break;
      case RAW_DATA:
        setTitle("Raw Data");
        break;
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search_view_menu,menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(this);
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    if(dataType == RAW_DATA){
      filterRawRecycler(query);
    } else if(dataType == PROCESSED_DATA){
      filterProcessedDataRecycler(query);
    }

    return true;
  }

  @Override
  public boolean onQueryTextChange(String query) {
    if(dataType == RAW_DATA) {
      filterRawRecycler(query);
    } else if(dataType == PROCESSED_DATA) {
      filterProcessedDataRecycler(query);
    }
    return true;
  }

  public void filterRawRecycler(String query){
    long currentTime = System.nanoTime();
    ArrayList<Content> unFilteredList = new ArrayList<>(SingletonDataHolder.getInstance().getData().getContents().values());
    query = query.toLowerCase();
    List<Content> filteredList = new ArrayList<>();
    for (Content content: unFilteredList){
      if(content.getTags().contains(query)){
        filteredList.add(content);
      }
    }
    if(filteredList.size() > 0) {
      searchTagHint.setVisibility(View.INVISIBLE);
    } else {
      searchTagHint.setText(getString(R.string.no_result_found));
      searchTagHint.setVisibility(View.VISIBLE);
    }

    mAdapter.replaceAll(filteredList);
    mRecyclerView.scrollToPosition(0);
    long timeElapsed = System.nanoTime() - currentTime;
    DecimalFormat df = new DecimalFormat("#.######");
    df.setRoundingMode(RoundingMode.CEILING);
    double timeElapsedInDouble = Long.valueOf(timeElapsed).doubleValue()/100000;
    String timeTaken = filteredList.size() + " matching gifs found in " +df.format(timeElapsedInDouble) + " milliseconds";
    searchTime.setText(timeTaken);
  }

  public void filterProcessedDataRecycler(String query){
    long currentTime = System.nanoTime();
    List<Content> filteredList = SingletonDataHolder.getInstance().getTagByContentMap().get(query);
    if(filteredList == null){
      filteredList = new ArrayList<>();
    }
    if(filteredList.size() > 0) {
      searchTagHint.setVisibility(View.INVISIBLE);
    } else {
      searchTagHint.setText(getString(R.string.no_result_found));
      searchTagHint.setVisibility(View.VISIBLE);
    }
    mAdapter.replaceAll(filteredList);
    mRecyclerView.scrollToPosition(0);
    long timeElapsed = System.nanoTime() - currentTime;
    DecimalFormat df = new DecimalFormat("#.######");
    df.setRoundingMode(RoundingMode.CEILING);
    double timeElapsedInDouble = Long.valueOf(timeElapsed).doubleValue()/100000;
    String timeTaken = filteredList.size() + " matching gifs found in " +df.format(timeElapsedInDouble) + " milliseconds";
    searchTime.setText(timeTaken);
  }

}
