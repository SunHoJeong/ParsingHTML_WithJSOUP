package com.example.suno.kakao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.spinkit)
    SpinKitView spinKitView;

    private RecyclerViewAdapter adapter = null;
    private List<HashMap<String, String>> imageList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        new HttpAsyncTask().execute();

    }

    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, imageList);
        recyclerView.setAdapter(adapter);
    }

    public class HttpAsyncTask extends AsyncTask<Void, Void, List<HashMap<String, String>>> {
        private final String url = "http://www.gettyimagesgallery.com/collections/archive/slim-aarons.aspx";

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... params) {
            Document rawData = null;
            List<HashMap<String, String>> imgUrlList = new ArrayList<>();

            try {
                rawData = Jsoup.connect(url)
                        .timeout(10000)
                        .get();

                for (Element element : rawData.select("div.gallery-item-group")) {
                    HashMap<String, String> map = new HashMap<>();

                    Elements imgElements = element.select("img.picture");
                    Elements titleElements = element.select("div.gallery-item-caption p");

                    map.put("title", titleElements.first().text());
                    map.put("url", imgElements.first().attr("src"));

                    imgUrlList.add(map);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return imgUrlList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            super.onPostExecute(list);
            spinKitView.setVisibility(View.INVISIBLE);

            imageList = list;

            initRecyclerView();
            for (HashMap<String, String> map : imageList) {
                Log.d("TITLE, URL", map.get("title") +", "+ map.get("url"));
            }

        }

    }
}
