package nyc.c4q.androidtest_unit4final;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    RecyclerView recyclerView;
    Rainbow rainbow;

    // TODO: adding all the colors and their values would be tedious, instead fetch it from the url below

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);
        makeRequestWithOkHttp(url);
        colorClickListener = setColorClickListener();
        setUpRecyclerView();
    }

    //sets up RecyclerView so onCreate isn't so messy
    public void setUpRecyclerView() {
        recyclerView = findViewById(R.id.rv);
        adapter = new ColorAdapter(colorsList, colorDict, colorClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    // TODO display a long toast with the text "{color_name} has a HEX value of {color_hex}  // for example: "blue has a HEX value of #0000ff"
    //sets clickListener for recyclerview itemview
    public View.OnClickListener setColorClickListener() {
        colorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thisColor = v.getTag().toString();
                String toastMessage = thisColor;
                StringBuilder sb = new StringBuilder(toastMessage);
                String colorHex = colorDict.get(thisColor);
                sb.append(" has a Hex value of ");
                sb.append(colorHex);
                toastMessage = sb.toString();
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        };
        return colorClickListener;
    }

    //inflate info menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_options_menu, menu);
        return true;
    }

    //hides top fragment when info is clicked.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if (fragmentContainer.getVisibility() == View.VISIBLE) {
            fragmentContainer.setVisibility(View.GONE);
        } else {
            fragmentContainer.setVisibility(View.VISIBLE);
        }
        return true;
    }

    //gets the JSON string from url. parses json, updates list and map, and notifies the adapter.
    // with more time I would put these things in their own classes.
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
                //parseJson(jsonString);
                rainbow = new Rainbow(jsonString);

                // colorsList.addAll(jsonArrayNames);
                //colorDict.putAll(colorMap);
                colorsList.addAll(rainbow.getRainbow());
                colorDict.putAll(rainbow.getDoubleRainbow());


                /*//test parsing
                for(int i = 0; i<jsonArrayNames.size(); i++){
                    Log.d("names array keys", jsonArrayNames.get(i));
                }
                Log.d("colorMap size", String.valueOf(colorMap.size()));*/
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    // TODO: Add options menu with the item "Info" which is always visible
    // TODO: When "Info" menu item is clicked, display the fragment InfoFragment
    // TODO: If InfoFragment is already visible and I click "Info", remove InfoFragment from the view.
    // Link to creating options menu: https://github.com/C4Q/AC-Android/tree/v2/Android/Lecture-9-Menus-and-Navigation#anatomy-of-menu-xml
}
