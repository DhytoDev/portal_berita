package com.lawba.portalberita;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lawba.portalberita.adapter.NewsAdapter;
import com.lawba.portalberita.config.Config;
import com.lawba.portalberita.helper.JSONParser;
import com.lawba.portalberita.model.News;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private JSONParser jsonParser ;
    private Config config ;
    private JSONArray jsonArray = null ;
    private ArrayList<News> newses ;
    private NewsAdapter adapter ;
    private ProgressDialog progress ;
    private ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonParser = new JSONParser() ;
        config = new Config() ;
        newses = new ArrayList<>();

        new TampilkanBerita().execute();
    }

    private class TampilkanBerita extends AsyncTask<String, String, String> {

        private String id, judul, gambar = "" ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(MainActivity.this);
            progress.setTitle("Informasi");
            progress.setMessage("Sedang mengambil data, coy !!");
            progress.setCancelable(false);
            progress.setIndeterminate(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> param =new ArrayList<>();

            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.URL_GET_BERITA, "GET", param) ;

            try {
                jsonArray = jsonObject.getJSONArray("berita");

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    id = obj.getString("id");
                    judul = obj.getString("judul");
                    gambar = config.BASE_IMG + obj.getString("gambar");

                    News news = new News() ;
                    news.setId(id);
                    news.setJudul(judul);
                    news.setGambar(gambar);

                    newses.add(news);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progress.dismiss();

            listView = (ListView) findViewById(R.id.listView);

            adapter = new NewsAdapter(MainActivity.this, newses);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    News news = newses.get(position);

                    Intent i = new Intent(MainActivity.this, DetailActivity.class);
                    i.putExtra("id", news.getId());
                    i.putExtra("judul", news.getJudul());
                    i.putExtra("gambar", news.getGambar());
                    startActivity(i);
                }
            });
        }
    }

}
