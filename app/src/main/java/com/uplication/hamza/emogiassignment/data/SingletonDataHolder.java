package com.uplication.hamza.emogiassignment.data;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.uplication.hamza.emogiassignment.model.Content;
import com.uplication.hamza.emogiassignment.model.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hamza on 2018-03-15.
 * Contact Hamza on hamza.qureshi@teabot.com
 */

public class SingletonDataHolder {

  private static final String TAG = "SingletonDataHolder";
  private static SingletonDataHolder singletonDataHolder;
  public static String DATA_AVAILABLE = "DATA_AVAILABLE";
  private Data data;
  private HashMap<String,ArrayList<Content>> tagByContentMap;
  public static SingletonDataHolder getInstance() {
    if (singletonDataHolder == null){
      singletonDataHolder = new SingletonDataHolder();
    }
    return singletonDataHolder;
  }

  private SingletonDataHolder() {
    tagByContentMap = new HashMap<>();
  }

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  public HashMap<String, ArrayList<Content>> getTagByContentMap() {
    return tagByContentMap;
  }

  public void setTagByContentMap(
      HashMap<String, ArrayList<Content>> tagByContentMap) {
    this.tagByContentMap = tagByContentMap;
  }

  public static void loadData(InputStream inputStream){
    LoadDataTask task = new LoadDataTask(inputStream);
    task.execute();
  }

  private static class LoadDataTask extends AsyncTask<Void,Void,Void>{
    InputStream inputStream;

    LoadDataTask(InputStream inputStream) {
      this.inputStream = inputStream;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      Gson gson = new Gson();
      try {
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        String json = new String(buffer,"UTF-8");
        Data data = gson.fromJson(json,Data.class);
        SingletonDataHolder.getInstance().setData(data);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      SingletonDataHolder singletonDataHolder = SingletonDataHolder.getInstance();
      HashMap<String,ArrayList<Content>> tagByContentMap = new HashMap<>();
      for(Content content: singletonDataHolder.getData().getContents().values()){
        for(String tag: content.getTags()){
          Log.d(TAG,"Tag: "+tag);
          ArrayList<Content> contentList = tagByContentMap.get(tag);
          if(contentList == null){
            contentList = new ArrayList<>();
          }
          contentList.add(content);
          tagByContentMap.put(tag,contentList);
        }
      }
      singletonDataHolder.setTagByContentMap(tagByContentMap);
      EventBus.getDefault().postSticky(DATA_AVAILABLE);
    }
  }
}
