package com.uplication.hamza.emogiassignment.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.airbnb.lottie.LottieAnimationView;
import com.uplication.hamza.emogiassignment.R;
import com.uplication.hamza.emogiassignment.data.SingletonDataHolder;
import java.io.InputStream;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashActivity extends AppCompatActivity {


  private LottieAnimationView loadingAnimationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    InputStream inputStream = getResources().openRawResource(R.raw.contents);
    SingletonDataHolder.loadData(inputStream);
    loadingAnimationView = findViewById(R.id.loading_la);
  }

  public void startPreProcessedDataSearch(View view) {
    startActivity(MainActivity.getProcessedDataIntent(SplashActivity.this));
    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
  }

  public void startRawDataSearch(View view) {
    startActivity(MainActivity.getRawDataIntent(SplashActivity.this));
    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
  }

  @Override
  protected void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    String message = EventBus.getDefault().getStickyEvent(String.class);
    if(message != null && message.equals(SingletonDataHolder.DATA_AVAILABLE)){
      loadingAnimationView.clearAnimation();
      loadingAnimationView.animate().translationX(-2000);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
  public void onDataLoaded(String message){
    if(message.equals(SingletonDataHolder.DATA_AVAILABLE)){
      loadingAnimationView.clearAnimation();
      loadingAnimationView.animate().translationX(-2000);
    }
  }

}
