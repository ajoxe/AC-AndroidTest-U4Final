package nyc.c4q.androidtest_unit4final;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ColorAdapter adapter;
    protected HashMap<String, String> colorDict = new HashMap<>();
    protected List<String> colorsList = new ArrayList<>();
    View.OnClickListener colorClickListener;
    private static String TAG = "OnClick";
    LinearLayout fragmentContainer;
    String url = "https://raw.githubusercontent.com/operable/cog/master/priv/css-color-names.json";
    String jsonString;
    List<String> jsonArrayNames = new ArrayList<>();
    HashMap<String, String> colorMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);
        makeRequestWithOkHttp(url);


        //colorDict.put("indigo", "#4b0082");
        //colorDict.put("green", "#00ff00");
        //colorDict.put("blue", "#0000ff");
        //colorDict.put("red", "#ff0000");
        // TODO: adding all the colors and their values would be tedious, instead fetch it from the url below
        // https://raw.githubusercontent.com/operable/cog/master/priv/css-color-names.json
        colorClickListener = setColorClickListener();


        //String[] names = new String[] {"blue", "red", "purple", "indigo", "orange", "brown", "black", "green"};
        //for(String n: names) colorsList.add(n);




        RecyclerView recyclerView = findViewById(R.id.rv);
        adapter = new ColorAdapter(colorsList, colorDict, colorClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    //sets clickListener for recyclerview item
    public View.OnClickListener setColorClickListener(){
        colorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display a long toast with the text "{color_name} has a HEX value of {color_hex}
                // for example: "blue has a HEX value of #0000ff"
                String thisColor = v.getTag().toString();
                String toastMessage = thisColor;
                StringBuilder sb = new StringBuilder(toastMessage);
                String colorHex = colorDict.get(thisColor);
                sb.append(" has a Hex value of ");
                sb.append(colorHex);
                toastMessage = sb.toString();
                Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_SHORT).show();
            }
        };
        return colorClickListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_options_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (fragmentContainer.getVisibility() == View.VISIBLE){
            fragmentContainer.setVisibility(View.GONE);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private void makeRequestWithOkHttp(String url) {
        OkHttpClient client = new OkHttpClient();   // 1
        Request request = new Request.Builder().url(url).build();  // 2

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) { // 3
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result = response.body().string();// 4
                jsonString = result;
                parseJson(jsonString);
                //test parsing
                colorsList.addAll(jsonArrayNames);
                colorDict.putAll(colorMap);
                for(int i = 0; i<jsonArrayNames.size(); i++){
                    Log.d("names array keys", jsonArrayNames.get(i));
                }
                Log.d("colorMap size", String.valueOf(colorMap.size()));

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // perform some ui work with `result`  // 5
                            adapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private JSONObject setJSon(String jSonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jSonString);
        } catch (JSONException e) {

        }
        return jsonObject;
    }

    private JSONArray getJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
            jsonArray = jsonObject.names();
        return jsonArray;
    }

    public void parseJson(String json){
        //get json object
        JSONObject jsonObject = setJSon(json);
        //get keys from jsonobject, add to arraylist.
        JSONArray names = getJsonArray(jsonObject);
        for(int i =0; i< names.length(); i++){
            try {
                jsonArrayNames.add(names.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int i =0; i<jsonArrayNames.size(); i++){
            String colorKey = jsonArrayNames.get(i);
            String hexValue;
            try {
                hexValue  = jsonObject.getString(colorKey);
            } catch (JSONException e) {
               hexValue  = "n/a";
                e.printStackTrace();
            }
            colorMap.put(colorKey,hexValue);
            //test parsing.
            Log.d("hex values", hexValue);
        }

    }
    public class JsonColors{
        String [] colors;

        public String[] getColors() {
            return colors;
        }
    }

    // TODO: Add options menu with the item "Info" which is always visible
    // TODO: When "Info" menu item is clicked, display the fragment InfoFragment
    // TODO: If InfoFragment is already visible and I click "Info", remove InfoFragment from the view.
    // Link to creating options menu: https://github.com/C4Q/AC-Android/tree/v2/Android/Lecture-9-Menus-and-Navigation#anatomy-of-menu-xml
}
