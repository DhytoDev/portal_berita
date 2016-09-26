package com.lawba.portalberita;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lawba.portalberita.config.Config;
import com.lawba.portalberita.helper.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private String id, gambar ;
    private JSONParser jsonParser ;
    private JSONArray jsonArray ;
    private ProgressDialog progress ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        jsonParser = new JSONParser();

        id = getIntent().getStringExtra("id");
        gambar = getIntent().getStringExtra("gambar");

        new TampilkanDetailBerita().execute();
    }

    private class TampilkanDetailBerita extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(DetailActivity.this);
            progress.setTitle("Information");
            progress.setMessage("Sedang Pengambilan Data");
            progress.setCancelable(false);
            progress.setIndeterminate(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("id_berita",id));

            JSONObject jsonObject = jsonParser.makeHttpRequest(Config.URL_GET_BERITA_DETAIL, "GET", param);

            try {
                jsonArray = jsonObject.getJSONArray("berita");
            } catch (Exception e) {

            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView img = (ImageView)findViewById(R.id.ivDetailImage);
                    TextView jdl = (TextView) findViewById(R.id.txtDetailJudul);
                    WebView isi = (WebView) findViewById(R.id.wvDetail);
                    String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";

                    try {
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String judul = obj.getString("judul");
                        String isi_berita = obj.getString("deskripsi");

                        jdl.setText(judul);
                        isi.loadData(String.format(htmlText, isi_berita),"text/html", "utf-8");

                        Glide
                                .with(DetailActivity.this)
                                .load(gambar)
                                .into(img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
        }
    }

}
