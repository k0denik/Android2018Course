package com.afl.przedszkolelabapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link XMLFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class XMLFragment extends android.support.v4.app.Fragment {
    private ChildViewModel childViewModel;
    private List<Child> mChildren;

    public XMLFragment() {
        // Required empty public constructor
    }

    public static XMLFragment newInstance() {
        return new XMLFragment();
    }

    private static final int EXPORT_REQUEST_CODE = 20;
    private static final int IMPORT_REQUEST_CODE = 22;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childViewModel = ViewModelProviders.of(this).get(ChildViewModel.class);
        childViewModel.getallChildren().observe(this, new Observer<List<Child>>() {
            @Override
            public void onChanged(@Nullable List<Child> children) {
                mChildren = children;
            }
        });

    }

    private void writeXml(OutputStream fileStream) {
        XmlSerializer serializer = Xml.newSerializer();
        try {
            OutputStreamWriter writer = new OutputStreamWriter(fileStream);
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "database");
            if (mChildren != null) {
                for (Child child : mChildren) {
                    serializer.startTag("", "child");
                    serializer.attribute("", "name", child.getName());
                    serializer.attribute("", "surname", child.getSurname());
                    serializer.attribute("", "descriptionBin", getBase64Data(child.getTextFilePath()));
                    serializer.attribute("", "descriptionUri", child.getTextFilePath());
                    serializer.attribute("", "imageBin", getBase64Data(child.getImagePath()));
                    serializer.attribute("", "imageUri", child.getImagePath());
                    serializer.endTag("", "child");
                }
            }
            serializer.endTag("", "database");
            serializer.endDocument();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBase64Data(String filePath) {
        File inputFile = new File(getContext().getFilesDir() + "/" + filePath);
        byte[] data = null;
        try {
            InputStream inputStream = new FileInputStream(inputFile);
            byte[] b = new byte[1024];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int c;
            while ((c = inputStream.read(b)) != -1) {
                os.write(b, 0, c);
            }
            data = os.toByteArray();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xml, container, false);


        view.findViewById(R.id.ExportChildrenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExternal = ((Switch) getActivity().findViewById(R.id.ExportImportDestinationSwitch)).isChecked();
                String filename = "DBBackup.xml";
                File file;
                if (isExternal) {
                    createXMLFile(filename);
                } else {
                    file = new File(getContext().getFilesDir() + "/" + "DBBackup.xml");
                    try {
                        OutputStream outputStream = new FileOutputStream(file);
                        writeXml(outputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        view.findViewById((R.id.ImportChildrenButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExternal = ((Switch) getActivity().findViewById(R.id.ExportImportDestinationSwitch)).isChecked();
                if (isExternal) {
                    selectXMLFile();
                } else {
                    File file = new File(getContext().getFilesDir() + "/" + "DBBackup.xml");
                    try {
                        ParseXml(new FileInputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

    private void selectXMLFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");
        Intent chooser = Intent.createChooser(intent, "Wybierz plik XML");
        startActivityForResult(chooser, IMPORT_REQUEST_CODE);
    }

    private void createXMLFile(String filename) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/xml");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        startActivityForResult(intent, EXPORT_REQUEST_CODE);
    }

    private void ParseXml(InputStream xmlStream) {
        File directory = getContext().getFilesDir();
        String[] filenames = directory.list();
        for (String file : filenames) {
            if (file.contains("Backup"))
                break;
            new File(directory, file).delete();
        }
        childViewModel.cleanDatabase();

        List<Child> children = new SaxChildrenParser(xmlStream, getContext()).parse();




        for (Child c : children) {
            childViewModel.insertChild(c);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXPORT_REQUEST_CODE) {
            OutputStream outStream = null;
            try {
                outStream = getContext().getContentResolver().openOutputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writeXml(outStream);
        } else if (requestCode == IMPORT_REQUEST_CODE){
            InputStream inStream = null;
            try {
                inStream = getContext().getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ParseXml(inStream);
        }

    }
}
