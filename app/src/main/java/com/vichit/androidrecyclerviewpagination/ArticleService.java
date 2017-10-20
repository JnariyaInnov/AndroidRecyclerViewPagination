package com.vichit.androidrecyclerviewpagination;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ArticleService {
    @GET("api/article")
    Call<ArticleResponse> findAllByPagination(@Query("page") int page);


}
