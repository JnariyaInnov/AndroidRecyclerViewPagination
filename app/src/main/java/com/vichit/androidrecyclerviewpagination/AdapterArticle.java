package com.vichit.androidrecyclerviewpagination;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AdapterArticle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ArticleResponse.Articlelist> articleList;
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;

    private int visibleThreshold = 3;
    private boolean loading = false;
    LoadMoreItem loadMoreItem;

    public AdapterArticle(RecyclerView recyclerView) {
        articleList = new ArrayList<>();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //get position items in LayoutManager
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do Something
                        loading = true;
                        //send event bus to other subscriber
//                        EventBus.getDefault().post(new ArticleLoadMoreEvent());
                        loadMoreItem.onRecyclerArticleLoadMore();
                    }
                }
            });
        }
    }

    public void onLoaded() {
        loading = false;
    }

    public boolean isLoading() {
        return loading;
    }

    public void onLoadMoreEvent(LoadMoreItem loadMoreItem) {
        this.loadMoreItem = loadMoreItem;
    }

    @Override
    public int getItemViewType(int position) {
        return (articleList.get(position) != null) ? VIEW_ITEM : VIEW_PROG;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_article_layout, parent, false);
            vh = new MyViewHolder(view);
        } else if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_progressbar, parent, false);
            vh = new ProgressBarViewHolder(view);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder articleViewHolder = (MyViewHolder) holder;
            articleViewHolder.title.setText(articleList.get(position).getTitle());
            articleViewHolder.description.setText(articleList.get(position).getContent());

        } else if (holder instanceof ProgressBarViewHolder) {
            ProgressBarViewHolder progressBarViewHolder = (ProgressBarViewHolder) holder;
            progressBarViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void addMoreItems(List<ArticleResponse.Articlelist> articleList) {
        this.articleList.addAll(articleList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    /*
        when recyclerview stay at down, arrayList will add null to last index of arrayList.
        when arrayList add null to last index of arrayList, ViewType is check: if the last index of
        arrayList null, progressBar will show but if the last index of arrayList had values, it will
        show item simple.
     */
    public void addProgressBar() {
        articleList.add(null);
        notifyItemInserted(articleList.size() - 1);
    }

    //after progressBar show, it will remove progressBar
    public void removeProgressBar() {
        articleList.remove(articleList.size() - 1);
        notifyItemRemoved(articleList.size() - 1);
    }

    //    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_article_layout, parent, false);
//        return new MyViewHolder(view);
//
//    }

//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.title.setText(articleList.get(position).getTitle());
//        holder.description.setText(articleList.get(position).getContent());
//
//    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            description = itemView.findViewById(R.id.text_desc);

        }
    }

    class ProgressBarViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressBarViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

    }

}

