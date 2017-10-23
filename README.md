# How to build android Pagination with RecyclerView
## Pagination loading without progress
## Retrofit Library
1. Gradle
```java
compile 'com.squareup.retrofit2:retrofit:2.3.0'
compile 'com.squareup.retrofit2:converter-gson:2.3.0'
compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
```
2. Service Generator
```java
public class ServiceGenerator {
     private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
 
     private static Retrofit.Builder builder = new Retrofit.Builder()
             .baseUrl("http://10.0.3.2:8000/")
             .addConverterFactory(GsonConverterFactory.create());
 
     public static <S> S createService(Class<S> serviceClass) {
         httpClient.addInterceptor(new Interceptor() {
             @Override
             public Response intercept(Chain chain) throws IOException {
                 okhttp3.Request original = chain.request();
                 Request.Builder requestBuilder = original.newBuilder()
                         .header("Authorization", "base64:6DF9uxsDRUsXiBmXukfoX2Uo7dtTq841J1kJipRNS1A=")
                         .header("Accept", "application/json");
                 Request request = requestBuilder.build();
                 return chain.proceed(request);
             }
         });
          OkHttpClient client = httpClient.build();
          Retrofit retrofit = builder.client(client).build();
          return retrofit.create(serviceClass);
      }
 } 
```
3. Interface Service 
```java
public interface ArticleService {
     @GET("api/article")
     Call<ArticleResponse> findAllByPagination(@Query("page") int page);
}
```
@Query("page") int page : when want to use pagination

### Interface Event
```java
public interface LoadMoreItem {
    void onRecyclerArticleLoadMore();
}
```
### Adapter
1. Constructor

```java
private List<ArticleResponse.Articlelist> articleList;
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
                         //send interface to MainActivity                      
                         loadMoreItem.onRecyclerArticleLoadMore();
                     }
                 }
             });
         }
    }
```
2. Method
```java
public void onLoaded() {
     loading = false;
}
public void addMoreItems(List<ArticleResponse.Articlelist> articleList) {
     this.articleList.addAll(articleList);
     notifyDataSetChanged();
}  
```
#### Method for reference interface
```java
public void onLoadMoreEvent(LoadMoreItem loadMoreItem) {
    this.loadMoreItem = loadMoreItem;
}
```
3. onCreateViewHolder
```java
@Override
public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_article_layout, parent, false);
     return new MyViewHolder(view);
}
```
4. onBindViewHolder
```java
@Override
public void onBindViewHolder(MyViewHolder holder, int position) {
    holder.title.setText(articleList.get(position).getTitle());
    holder.description.setText(articleList.get(position).getContent());
 }
```

5. getItemCount
```java
@Override
public int getItemCount() {
    return articleList.size();
}
```
6. MyViewHolder Sub class of Adapter
```java
class MyViewHolder extends RecyclerView.ViewHolder {
         private TextView title, description;
         public MyViewHolder(View itemView) {
             super(itemView);
             title = itemView.findViewById(R.id.text_title);
             description = itemView.findViewById(R.id.text_desc);
 
         }
}         
```
### MainActivity
Before set up pagination. You must know How many page that you have in API. So If you don't know total page, You cannot handle your scroll page. It's mean that when you scroll to down of item in RecyclerView, It's will load more item. 
```java
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
```
For get Total page 
```java
totalPage = response.body().getData().getLastPage();
```

#### Implement Interface to get reference from Adapter
When you srcoll RecyclerView to down of item list, this method will invoke from adapter.
```java
@Override
public void onRecyclerArticleLoadMore() {
     if (page == totalPage) {
          return;
     }
     loadArticleByPagination(page++);
     Log.e("ppppp page count", page + "");
     Toast.makeText(MainActivity.this, "onRecyclerArticleLoadMore", Toast.LENGTH_LONG).show();
}
```
#### SetUp RecyclerView
```java
private void setRecyclerView() {
     recyclerView = (RecyclerView) findViewById(R.id.recycler_article);
     recyclerView.setLayoutManager(new LinearLayoutManager(this));
     adapter = new AdapterArticle(recyclerView);
     recyclerView.setAdapter(adapter);
}
```

#### Full Code 
```Java
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
 }

```











































