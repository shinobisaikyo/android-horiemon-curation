package schin.horiemoncuration;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // リストビューに表示するためのデータを設定
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1);
        // リストビューにデータを設定
        listView = (ListView)findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        adapter.add("Test");
        adapter.add("Test2");

        // アイテムクリック時ののイベントを追加
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {

                // 選択アイテムを取得
                ListView listView = (ListView)parent;
                String item = (String)listView.getItemAtPosition(pos);

                Uri uri = Uri.parse(item);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
//        new MyTask().execute();
    }

    class MyTask extends AsyncTask<Void, String, String> {
        ArrayAdapter<String> adapter;

        @Override
        protected void onPreExecute() {
            adapter = (ArrayAdapter<String>) listView.getAdapter();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "";
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder().url(url).build();

            ArrayList arrayList = new ArrayList();
            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();

                //レスポンスの文字列をJSONオブジェクトに変換
                JSONArray link_list = new JSONArray(json);
                for (int i = 0; i < link_list.length(); i++) {
                    //JSONオブジェクトをパースして、レコードのname属性をログ出力
                    JSONArray link = link_list.getJSONArray(i);
                    arrayList.add(link.get(1));
                }
            } catch (Exception e) {
                Log.e("aaa", e.toString());
            }

            for (int i=0; i<arrayList.size(); i++) {
                publishProgress(arrayList.get(i).toString());
            }

//            String[] names = {"a", "b"};
//            for (String name : names) {
//                publishProgress(name);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            return "success";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
        }
    }

}
