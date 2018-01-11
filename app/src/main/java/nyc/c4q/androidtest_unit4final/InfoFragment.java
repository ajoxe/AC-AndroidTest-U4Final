package nyc.c4q.androidtest_unit4final;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by justiceo on 1/9/18.
 */

public class InfoFragment extends Fragment {

    TextView moreText;
    Button moreButton;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView  = inflater.inflate(R.layout.info_fragment, container, false);
        moreButton = (Button) rootView.findViewById(R.id.more_button);
        moreText = (TextView) rootView.findViewById(R.id.more_textView);
        moreText.setVisibility(View.GONE);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    moreButton.setVisibility(View.GONE);
                    moreText.setVisibility(View.VISIBLE);

            }
        });
        return rootView;
    }
}
