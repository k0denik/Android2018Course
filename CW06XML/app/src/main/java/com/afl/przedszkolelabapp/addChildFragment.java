package com.afl.przedszkolelabapp;

import android.Manifest;
import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addChildFragment extends android.support.v4.app.Fragment {

    private int ACTION_REQUEST_GALLERY = 1;

    public addChildFragment() {
        // Required empty public constructor
    }

    Uri imageUri = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment add_child_fragment.
     */
    public static addChildFragment newInstance() {
        addChildFragment fragment = new addChildFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_child_fragment, container, false);
        final addChildFragment temp = this;
        //Button for selecting image
        rootView.findViewById(R.id.buttonPickImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getImageIntent.setType("image/*");
                Intent chooser = Intent.createChooser(getImageIntent, "Wybierz zdjęcie:");
                startActivityForResult(chooser, ACTION_REQUEST_GALLERY);
            }
        });


        //Button for adding children
        rootView.findViewById(R.id.addChildButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((TextView) rootView.findViewById(R.id.nameText)).getText().toString();
                String surname = ((TextView) rootView.findViewById(R.id.surnameText)).getText().toString();

                //create .txt file and save
                String description = ((TextView)rootView.findViewById(R.id.descriptionText)).getText().toString();
                String filenamePrefix = ""+(name + surname).hashCode();
                String pathDescription = filenamePrefix + "description.txt";
                File file = new File(getContext().getFilesDir()+"/" + pathDescription);
                try {
                    file.createNewFile();
                    OutputStream outStream = new FileOutputStream(file);
                    OutputStreamWriter writer = new OutputStreamWriter(outStream);
                    writer.write(description);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ChildViewModel childViewModel = ViewModelProviders.of(temp).get(ChildViewModel.class);
                childViewModel.insertChild(new Child(name, surname, pathDescription, copyToInternal(filenamePrefix)));
            }
        });
        return rootView;
    }

    private String copyToInternal(String filenamePrefix){
        String externalFilename = getFileName(imageUri);
        String internalPath = null;
        try {
            internalPath = filenamePrefix+"image_"+externalFilename;
            InputStream inStream = getContext().getContentResolver().openInputStream(imageUri);

            OutputStream outStream = new FileOutputStream(getContext().getFilesDir()+"/"+internalPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer))>0){
                outStream.write(buffer,0,length);
            }
            inStream.close();
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalPath;
    }

    public String getFileName(Uri uri) {
        Cursor returnCursor = getContext().getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == ACTION_REQUEST_GALLERY) {
                if (!isWriteStoragePermissionGranted()) {
                    isWriteStoragePermissionGranted();
                }

                ((ImageView)getActivity().findViewById(R.id.imageView)).setImageURI(data.getData());
                imageUri = data.getData();
            }
    }




    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "External storage2");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

}
