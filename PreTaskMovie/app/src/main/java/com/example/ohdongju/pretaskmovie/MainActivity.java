package com.example.ohdongju.pretaskmovie;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Naver API CLIENT_ID, CLIENNT_SECRET
    private String CLIENT_ID = "8pvaxqugcEf3uavvq_4D";
    private String CLIENT_SECRET = "eXcRlFZBID";

    private String url, myJSON, movie_name = null;
    private JSONArray movie = null;
    private Button search_btn;
    private EditText movie_input;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<Movie> movieList=null;

    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 영화 입력 창과 검색버튼 참조
        movie_input = (EditText)findViewById(R.id.movie_input);
        search_btn = (Button)findViewById(R.id.search_button);

        // RecyclerView 참조
        recyclerView = (RecyclerView) findViewById(R.id.movie_list) ;

        // 영화리스트(movieList)
        movieList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);

        // RecyclerView 세로 레이아웃
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 리사이클러뷰 구분선
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 검색 버튼 기능
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.search_button) {

                    // 키보드 숨기기 기능
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    // 입력받은 데이터 movie_name 변수에 추가
                    movie_name = movie_input.getText().toString();

                    // Set URL
                    url = "https://openapi.naver.com/v1/search/movie.json?query=" + movie_name + "&display=100";

                    // 입력 전달 정보 지우기
                    movieList.removeAll(movieList);

                    // API 통신
                    NaverMovie(url);
                }
            }
        });
    }

    // Naver Movie API 통신
    private void NaverMovie(String url) {
        class getDataJSON extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... urls) {
                String strUrl = urls[0];
                try {
                    URL url = new URL(strUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", CLIENT_ID);
                    con.setRequestProperty("X-Naver-Client-Secret", CLIENT_SECRET);

                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    br.close();
                    con.disconnect();

                    return sb.toString().trim();
                } catch (MalformedURLException e) {
                    return e.getMessage();
                } catch (ProtocolException e) {
                    return e.getMessage();
                } catch (IOException e) {
                    return e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                myJSON = result;

                parseJSON();

                if(movieList.size() != 0) {
                    MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movieList);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    // 검색 결과가 없으면 Toast로 알려줌.
                    Toast.makeText(getApplicationContext(),"\""+movie_input.getText().toString()+"\""+"결과물이 없습니다.",Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }

        getDataJSON g = new getDataJSON();
        g.execute(url);
    }

    private void parseJSON() {
        try {
            // JSON 객체 생성
            JSONObject jsonObj = new JSONObject(myJSON);
            movie = jsonObj.getJSONArray("items");

            for(int i=0;i<movie.length();i++) {
                JSONObject obj = movie.getJSONObject(i);

                String title = obj.optString("title");
                    // 문자열 치환 <b>,</b> 지우고 title에 값 입력
                    title = title.replace("<b>", "");
                    title = title.replace("</b>", "");
                String image = obj.optString("image");
                float rating = (float)obj.optDouble("userRating");
                String year = obj.optString("pubDate");
                String director = obj.optString("director");
                String actor = obj.optString("actor");
                String link = obj.optString("link");

                // movieList에 네이버 영화 정보 입력
                movieList.add(new Movie(title, image, rating, year, director, actor, link));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 한번에 바로 종료되지 않고 두번 눌러야 종료되는 기능
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"한번 더 눌러야 종료됩니다.",Toast.LENGTH_LONG).show();
        }

    }
}
