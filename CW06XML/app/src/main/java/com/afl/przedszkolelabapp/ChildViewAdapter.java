package com.afl.przedszkolelabapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Jakub Pamuła on 01/05/2018.
 */

public class ChildViewAdapter extends RecyclerView.Adapter<ChildViewAdapter.ViewHolder> {
    private List<Child> children;
    private LayoutInflater inflater;
    private Context context;

    public ChildViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View childView = inflater.inflate(R.layout.single_child_line, parent, false);
        return new ViewHolder(childView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (children != null) {
            final Child currentChild = children.get(position);
            holder.name.setText(currentChild.name);
            holder.surname.setText(currentChild.surname);
            if (currentChild.imagePath != null)
                holder.pictureButton.setImageURI(Uri.fromFile(new File(context.getFilesDir()+"/"+currentChild.getImagePath())));
            //todo: add button for eating children?
            holder.descriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = context.getFilesDir()+"/" + currentChild.getTextFilePath();
                    StringBuilder description = new StringBuilder();
                    try {
                        BufferedReader inStream = new BufferedReader(new FileReader(path));
                        for (String line; (line = inStream.readLine()) != null; ) {
                            description.append(line).append("\n");
                        }
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intentDescriptionActivity = new Intent(context,ChildDescriptionActivity.class);
                    intentDescriptionActivity.putExtra("description", (CharSequence) description);
                    context.startActivity(intentDescriptionActivity);
                }
            });
        }
    }


    public void setChildren(List<Child> children) {
        this.children = children;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (children == null)
            return 0;
        else
            return children.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView surname;
        Button descriptionButton;
        ImageView pictureButton;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.singleChildNameText);
            surname = itemView.findViewById(R.id.singleChildSurnameText);
            descriptionButton = itemView.findViewById(R.id.singleChildDescriptionButton);
            pictureButton = itemView.findViewById(R.id.singleChildImageButton);
        }
    }
}
