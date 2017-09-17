package com.example.suno.kakao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    private RecyclerViewAdapter adapter = null;

    private List<HashMap<String, String>> imageList = null;
    //private List<Bitmap> bitmapList = new ArrayList<>();

    //private String baseUrl = "http://www.gettyimagesgallery.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        new HttpAsyncTask().execute();

        Log.d("MAIN", "-----------------------");

    }

    public void initRecyclerView() {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, imageList);
        recyclerView.setAdapter(adapter);
    }

//    private boolean downloadImage(ResponseBody body) {
//
//        try {
//            Log.d("DownloadImage", "Reading and writing file");
//            InputStream in = null;
//            FileOutputStream out = null;
//
//            try {
//                in = body.byteStream();
//                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
//                int c;
//
//                while ((c = in.read()) != -1) {
//                    out.write(c);
//                }
//            }
//            catch (IOException e) {
//                Log.d("DownloadImage",e.toString());
//                return false;
//            }
//            finally {
//                if (in != null) {
//                    in.close();
//                }
//                if (out != null) {
//                    out.close();
//                }
//            }
//
//            int width, height;
//            Bitmap bMap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
//            width = 2*bMap.getWidth();
//            height = 6*bMap.getHeight();
//            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
//
//            return true;
//
//        } catch (IOException e) {
//            Log.d("DownloadImage",e.toString());
//            return false;
//        }
//    }

    public class HttpAsyncTask extends AsyncTask<Void, Void, List<HashMap<String, String>>> {
        private final String url = "http://www.gettyimagesgallery.com/collections/archive/slim-aarons.aspx";

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... params) {
            Document rawData = null;
            List<HashMap<String, String>> imgUrlList = new ArrayList<>();

            try {
                rawData = Jsoup.connect(url)
                        .timeout(5000)
                        .get();

                for (Element element : rawData.select("div.gallery-item-group")) {
                    HashMap<String, String> map = new HashMap<>();

                    Elements imgElements = element.select("img.picture");
                    Elements titleElements = element.select("div.gallery-item-caption p");

                    map.put("title", titleElements.first().text());
                    map.put("url", imgElements.first().attr("src"));

                    imgUrlList.add(map);
                }

//                for (HashMap<String, String> map : imgUrlList) {
//                    Log.d("TITLE, URL", map.get("title") +", "+ map.get("url"));
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imgUrlList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            super.onPostExecute(list);

            imageList = list;

            for (HashMap<String, String> map : imageList) {
                Log.d("TITLE, URL", map.get("title") +", "+ map.get("url"));
            }

            initRecyclerView();

//            for (HashMap<String,String> map : imageList) {
//                Log.d("callCnt", count + "");
//                count++;
//
//                Call<ResponseBody> call = service.getBitmapFromUrl(map.get("url"));
//
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            if (response.body() != null) {
//                                // display the image data in a ImageView or save it
//                                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
//                                bitmapList.add(bitmap);
//                                adapter.notifyItemInserted(addCnt);
//
//                                Log.d("onResponseCnt", addCnt + "");
//                                addCnt++;
//                            } else {
//                                // TODO
//                            }
//                        } else {
//                            // TODO
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                });
//            }
        }

    }
}
