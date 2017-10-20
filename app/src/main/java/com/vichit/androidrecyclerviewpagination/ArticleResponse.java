package com.vichit.androidrecyclerviewpagination;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by VichitDeveloper on 10/20/17.
 */

public class ArticleResponse {


    @SerializedName("STATUS")
    private boolean status;
    @SerializedName("DATA")
    private Data data;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Articlelist {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public String toString() {
            return "Articlelist{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", updatedAt='" + updatedAt + '\'' +
                    '}';
        }
    }

    public static class Data {
        @SerializedName("current_page")
        private int currentPage;
        @SerializedName("data")
        private List<Articlelist> articlelist;
        @SerializedName("from")
        private int from;
        @SerializedName("last_page")
        private int lastPage;
        @SerializedName("next_page_url")
        private String nextPageUrl;
        @SerializedName("path")
        private String path;
        @SerializedName("per_page")
        private int perPage;
        @SerializedName("prev_page_url")
        private String prevPageUrl;
        @SerializedName("to")
        private int to;
        @SerializedName("total")
        private int total;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public List<Articlelist> getArticlelist() {
            return articlelist;
        }

        public void setArticlelist(List<Articlelist> articlelist) {
            this.articlelist = articlelist;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public String getNextPageUrl() {
            return nextPageUrl;
        }

        public void setNextPageUrl(String nextPageUrl) {
            this.nextPageUrl = nextPageUrl;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }

        public String getPrevPageUrl() {
            return prevPageUrl;
        }

        public void setPrevPageUrl(String prevPageUrl) {
            this.prevPageUrl = prevPageUrl;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "currentPage=" + currentPage +
                    ", articlelist=" + articlelist +
                    ", from=" + from +
                    ", lastPage=" + lastPage +
                    ", nextPageUrl='" + nextPageUrl + '\'' +
                    ", path='" + path + '\'' +
                    ", perPage=" + perPage +
                    ", prevPageUrl='" + prevPageUrl + '\'' +
                    ", to=" + to +
                    ", total=" + total +
                    '}';
        }
    }
}
