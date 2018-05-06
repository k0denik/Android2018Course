package com.afl.przedszkolelabapp;

import android.content.Context;
import android.util.Base64;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Jakub Pamuła on 06/05/2018.
 */
class SaxChildrenParser {
    private InputStream inStream;
    private Context thisContext;

    public SaxChildrenParser(InputStream inStream, Context context) {
        this.inStream = inStream;
        thisContext = context;
    }

    public List<Child> parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            ChildrenParseHandler handler = new ChildrenParseHandler();
            parser.parse(inStream, handler);
            return handler.getChildren();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ChildrenParseHandler extends DefaultHandler {
        private List<Child> children;

        public List<Child> getChildren() {
            return children;
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (localName.equalsIgnoreCase("child")) {
                String name = attributes.getValue("name");
                String surname = attributes.getValue("surname");
                String imagePath = attributes.getValue("imageUri");
                recreateFile(attributes.getValue("imageBin"), imagePath);
                String descriptionUri = attributes.getValue("descriptionUri");
                recreateFile(attributes.getValue("descriptionBin"), descriptionUri);
                children.add(new Child(name, surname, descriptionUri, imagePath));
            }
        }

        private void recreateFile(String base64Data, String filepath) {

            File f = new File(thisContext.getFilesDir()+"/"+filepath);
            try {
                OutputStream outStream = new FileOutputStream(f);
                byte[] data = Base64.decode(base64Data,Base64.DEFAULT);
                outStream.write(data);
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            children = new ArrayList<>();
        }
    }
}
