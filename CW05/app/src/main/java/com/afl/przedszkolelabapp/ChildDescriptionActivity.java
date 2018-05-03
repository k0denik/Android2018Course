package com.afl.przedszkolelabapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ChildDescriptionActivity extends Activity {
    String descriptionData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        descriptionData = getIntent().getExtras().getString("description");
        setContentView(R.layout.activity_child_description);

        ((TextView)findViewById(R.id.DescriptionPreviewTextBox)).setText(descriptionData);
    }
}
