package com.uplication.hamza.emogiassignment.viewModel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uplication.hamza.emogiassignment.R;
import com.uplication.hamza.emogiassignment.model.Asset;
import com.uplication.hamza.emogiassignment.model.Content;

/**
 * Created by Hamza on 2018-03-15.
 * Contact Hamza on hamza.qureshi@teabot.com
 */

public class GalleryItemViewModel {

  private Content content;

  public GalleryItemViewModel(Content content) {
    this.content = content;
  }

  public String getTags(){
    StringBuilder stringBuilder = new StringBuilder("Tags: ");
    for(String tag: content.getTags()){
      stringBuilder.append("[").append(tag).append("] ");
    }
    return stringBuilder.toString();
  }
  public String getImageUrl(){
    for(Asset asset: content.getAssets()){
      if("full".equals(asset.getSize())){
        return asset.getUrl();
      }
    }
    return "";
  }
  @BindingAdapter({"imageUrl"})
  public static void setImage(ImageView imageView, String imageUrl){
    Glide.with(imageView.getContext()).asGif().load(imageUrl)
        .apply(new RequestOptions().error(R.drawable.error_no_internet))
        .apply(RequestOptions.fitCenterTransform()).into(imageView);
  }
}
