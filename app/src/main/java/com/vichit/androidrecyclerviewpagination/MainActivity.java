package com.vichit.androidrecyclerviewpagination;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoadMoreItem {
    RecyclerView recyclerView;
    AdapterArticle adapter;
    List<ArticleResponse.Articlelist> articleList;
    ArticleService articleService;
    private static final int ITEMS_PER_PAGE = 15;
    private int totalPage;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleService = ServiceGenerator.createService(ArticleService.class);
        setRecyclerView();
        adapter.onLoadMoreEvent(this);

        loadArticleByPagination(page);

    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRecyclerArticleLoadMore(ArticleLoadMoreEvent event) {
//        Log.e("ppppp ArticleLoadMore", "onRecyclerArticleLoadMore");
//        Toast.makeText(MainActivity.this, "onRecyclerArticleLoadMore", Toast.LENGTH_LONG).show();
//    }


    @Override
    public void onRecyclerArticleLoadMore() {
        if (page == totalPage) {
            return;
        }

        loadArticleByPagination(page++);
        Log.e("ppppp page count", page + "");
        Toast.makeText(MainActivity.this, "onRecyclerArticleLoadMore", Toast.LENGTH_LONG).show();
    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_article);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterArticle(recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void loadArticleByPagination(int page) {
        Call<ArticleResponse> call = articleService.findAllByPagination(page);
        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                totalPage = response.body().getData().getLastPage();
                articleList = response.body().getData().getArticlelist();

                adapter.addMoreItems(articleList);
                adapter.onLoaded();


            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }


//
//        if (page == totalPage) {
//            return;
//        }
//
//        adapter.addProgressBar();
//        loadArticleByPagination(++page);


//    private void loadArticleByPagination(final int page) {
//        Call<ArticleResponse> call = articleService.findAllByPagination(page);
//        call.enqueue(new Callback<ArticleResponse>() {
//            @Override
//            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
////                articleList = response.body().getData().getArticlelist();
////                adapter = new AdapterArticle(recyclerView);
////                recyclerView.setAdapter(adapter);
//
//                if (page == 1) {
//                    adapter.clearList();
//                }
//                totalPage = articleResponse.getData().getLastPage();
//
//
//                if (adapter.isLoading() && adapter.size() > ITEMS_PER_PAGE) {
//                    adapter.removeProgressBar();
//                }
//
//                adapter.addMoreItems(articleResponse.getData().getArticlelist());
//                adapter.onLoaded();
//                Log.e("ppppp", articleResponse.getData().getArticlelist() + "");
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ArticleResponse> call, Throwable t) {
//                t.printStackTrace();
//                Log.e("ppppp", "onFailure");
//            }
//        });
//    }


}
