package com.vouchr.michelle.vouchrtwitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.vouchr.michelle.vouchrtwitter.models.SearchResponse;
import com.vouchr.michelle.vouchrtwitter.models.Status;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Michelle Luo on 2018-05-11
 * */

public class MainActivity extends AppCompatActivity {

    public Retrofit retrofit;
    public static String bearerToken;
    @BindView(R.id.button)
    protected Button button;
    @BindView(R.id.text)
    protected EditText text;
    protected LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    @BindView(R.id.recycleView)
    protected RecyclerView recyclerView;
    protected Adapter adapter;
    public List<Status> statuses;
    protected List<ListTweet> listTweets = new ArrayList<>();
    public String inputSearch;
    public Long max_id = 0L;
    public Long since_id = 10123456465465L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        button.setText(R.string.Search);

//      RecyclerView initialization
        adapter = new Adapter(listTweets);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

//      set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//      add your other interceptors …

//      add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.twitter.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        authenticate();

        adapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                TwitterAPI service = retrofit.create(TwitterAPI.class);

                service.page("Bearer " + bearerToken, inputSearch, max_id, since_id).enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse res = response.body();
                        statuses = res.getStatuses();
                        for(Status status: statuses){
                            ListTweet newTweet = new ListTweet(
                                    status.getUser().getScreenName(),
                                    status.getUser().getName(),
                                    status.getText(),
                                    status.getUser().getProfileImageUrl()
                            );
                            if (status.getId() > max_id){
                                max_id = status.getId();
                            }
                            if (status.getId() < since_id){
                                since_id = status.getId();
                            }
                            listTweets.add(newTweet);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    /**
     * Assigns text in the EditText and calls the Search API
     * */
    @OnClick(R.id.button)
    protected void buttonClicked() {
        inputSearch = text.getText().toString();
        search();
    }

    private void search(){
        TwitterAPI service = retrofit.create(TwitterAPI.class);
        listTweets.clear();

        service.search("Bearer " + bearerToken, inputSearch).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse res = response.body();
                statuses = res.getStatuses();
                for(Status status: statuses){
                    ListTweet newTweet = new ListTweet(
                            status.getUser().getScreenName(),
                            status.getUser().getName(),
                            status.getText(),
                            status.getUser().getProfileImageUrl()
                    );
                    if (status.getId() > max_id){
                        max_id = status.getId();
                    }
                    if (status.getId() < since_id){
                        since_id = status.getId();
                    }
                    listTweets.add(newTweet);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.v("Fail", t.toString());
            }
        });
    }

    /**
     * Authenticates
     * Uses retrofit to obtain the twitter bearer token for application-only authentication
     * */
    private void authenticate(){
        String authorization = Credentials.basic(getString(R.string.consumerKey),
                getString(R.string.consumerSecret));

        TwitterAPI service = retrofit.create(TwitterAPI.class);

        service.authorize(authorization,"application/x-www-form-urlencoded;charset=UTF-8").enqueue(new Callback<OAuthToken>() {
            @Override
            public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                bearerToken = response.body().getAccess_token();
            }
            @Override
            public void onFailure(Call<OAuthToken> call, Throwable t) {
                Log.v("failure",t.toString());
            }
        });
    }
}