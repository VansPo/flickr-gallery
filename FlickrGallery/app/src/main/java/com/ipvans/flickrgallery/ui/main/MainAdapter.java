package com.ipvans.flickrgallery.ui.main;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ipvans.flickrgallery.R;
import com.ipvans.flickrgallery.data.model.FeedItem;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<FeedItem> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), position == items.size() - 1);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView title;
        private TextView author;
        private TextView tags;
        private View divider;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            tags = itemView.findViewById(R.id.tags);
            divider = itemView.findViewById(R.id.divider);
        }

        void bind(FeedItem item, boolean isLast) {
            if (item.getMedia() != null && !TextUtils.isEmpty(item.getMedia().getUrl())) {
                Glide.with(itemView.getContext())
                        .load(item.getMedia().getUrl())
                        .centerCrop()
                        .into(image);
            }
            title.setText(item.getTitle());
            author.setText(itemView.getContext().getString(R.string.author, item.getAuthor()));
            tags.setText(item.getTags());
            divider.setVisibility(isLast ? View.GONE : View.VISIBLE);
        }
    }

    void replace(List<FeedItem> data) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new FeedItemDiffUtil(items, data));
        items.clear();
        items.addAll(data);
        result.dispatchUpdatesTo(this);
    }

    boolean isEmpty() {
        return items.isEmpty();
    }

    public class FeedItemDiffUtil extends DiffUtil.Callback {

        private final List<FeedItem> oldList;
        private final List<FeedItem> newList;

        FeedItemDiffUtil(List<FeedItem> oldList, List<FeedItem> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }

}
