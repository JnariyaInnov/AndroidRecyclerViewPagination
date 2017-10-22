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














