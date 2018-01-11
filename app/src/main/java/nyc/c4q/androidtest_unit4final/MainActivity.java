package nyc.c4q.androidtest_unit4final;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ColorAdapter adapter;
    protected HashMap<String, String> colorDict;
    protected List<String> colorsList;
    View.OnClickListener colorClickListener;
    private static String TAG = "OnClick";
    LinearLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);

        colorDict = new HashMap<>();
        colorDict.put("indigo", "#4b0082");
        colorDict.put("green", "#00ff00");
        colorDict.put("blue", "#0000ff");
        colorDict.put("red", "#ff0000");
        // TODO: adding all the colors and their values would be tedious, instead fetch it from the url below
        // https://raw.githubusercontent.com/operable/cog/master/priv/css-color-names.json
        colorClickListener = setColorClickListener();

        colorsList = new ArrayList<>();
        String[] names = new String[] {"blue", "red", "purple", "indigo", "orange", "brown", "black", "green"};
        for(String n: names) colorsList.add(n);

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

    // TODO: Add options menu with the item "Info" which is always visible
    // TODO: When "Info" menu item is clicked, display the fragment InfoFragment
    // TODO: If InfoFragment is already visible and I click "Info", remove InfoFragment from the view.
    // Link to creating options menu: https://github.com/C4Q/AC-Android/tree/v2/Android/Lecture-9-Menus-and-Navigation#anatomy-of-menu-xml
}
