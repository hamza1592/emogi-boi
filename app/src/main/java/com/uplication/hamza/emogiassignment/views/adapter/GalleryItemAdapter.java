package com.uplication.hamza.emogiassignment.views.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.SortedList;
import android.support.v7.util.SortedList.Callback;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.uplication.hamza.emogiassignment.R;
import com.uplication.hamza.emogiassignment.databinding.ItemGalleryBinding;
import com.uplication.hamza.emogiassignment.model.Content;
import com.uplication.hamza.emogiassignment.viewModel.GalleryItemViewModel;
import com.uplication.hamza.emogiassignment.views.adapter.GalleryItemAdapter.GalleryItemViewHolder;
import java.util.List;

/**
 * Created by Hamza on 2018-03-15.
 * Contact Hamza on hamza.qureshi@teabot.com
 */

public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemViewHolder> {

  private LayoutInflater mLayoutInflator;

  private SortedList<Content> sortedList = new SortedList<>(Content.class,
      new Callback<Content>() {
        @Override
        public int compare(Content o1, Content o2) {
          return o1.getContent_id().compareTo(o2.getContent_id());
        }

        @Override
        public void onChanged(int position, int count) {
          notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Content oldItem, Content newItem) {
          return oldItem.getContent_id().equals(newItem.getContent_id());
        }

        @Override
        public boolean areItemsTheSame(Content item1, Content item2) {
          return item1.getContent_id().equals(item2.getContent_id());
        }

        @Override
        public void onInserted(int position, int count) {
          notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
          notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
          notifyItemMoved(fromPosition, toPosition);
        }
      });

  public GalleryItemAdapter(Context context) {
    mLayoutInflator = LayoutInflater.from(context);
  }

  public void setSortedList(
      SortedList<Content> sortedList) {
    this.sortedList = sortedList;
  }

  public void add(Content content) {
    sortedList.add(content);
  }

  public void remove(Content content) {
    sortedList.remove(content);
  }

  public void add(List<Content> contentList) {
    sortedList.addAll(contentList);
  }

  public void removeAll(){
    sortedList.clear();
  }
  public void remove(List<Content> contentList) {
    sortedList.beginBatchedUpdates();
    for (Content model : contentList) {
      sortedList.remove(model);
    }
    sortedList.endBatchedUpdates();
  }

  public void replaceAll(List<Content> contentList) {
    sortedList.beginBatchedUpdates();
    for (int i = sortedList.size() - 1; i >= 0; i--) {
      final Content content = sortedList.get(i);
      if (!contentList.contains(content)) {
        sortedList.remove(content);
      }
    }
    sortedList.addAll(contentList);
    sortedList.endBatchedUpdates();
  }

  @Override
  public GalleryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    ItemGalleryBinding itemGalleryBinding = DataBindingUtil
        .inflate(mLayoutInflator, R.layout.item_gallery, parent, false);
    return new GalleryItemViewHolder(itemGalleryBinding);
  }

  @Override
  public void onBindViewHolder(GalleryItemViewHolder holder, int position) {
    ItemGalleryBinding itemGalleryBinding = holder.viewDataBinding;
    itemGalleryBinding.setModel(new GalleryItemViewModel(sortedList.get(position)));
  }

  @Override
  public int getItemCount() {
    return sortedList.size();
  }

  public class GalleryItemViewHolder extends ViewHolder {

    ItemGalleryBinding viewDataBinding;

    public GalleryItemViewHolder(ItemGalleryBinding viewDataBinding) {
      super(viewDataBinding.container);
      this.viewDataBinding = viewDataBinding;
    }
  }
}
