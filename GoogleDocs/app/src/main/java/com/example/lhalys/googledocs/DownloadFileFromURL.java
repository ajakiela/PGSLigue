package com.example.lhalys.googledocs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lhalys on 2014-08-05.
 */
public class DownloadFileFromURL extends AsyncTask<String, String, String> {


    //AasyncTask asyncTask =new AasyncTask();
    //IAsyncResponse outputDelegate;

    Context context ;
    CharSequence text = "File Downloaded";
    int duration = Toast.LENGTH_SHORT;
    URL MyURL;
//    public AsyncResponse delegate=null;

    private AsyncResponse mCallback;


    DownloadFileFromURL(Context context, String url, String YourMessage)
    {
        this.context = context;
        this.mCallback = (AsyncResponse) context;
        text = YourMessage;
        try {
            MyURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        execute();

    }


    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //   showDialog(progress_bar_type);

    }
    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
           // URL url = new URL(f_url[0]);
            URL url = MyURL;
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath().toString() + "/liga.csv");

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress(""+(int)((total*100)/lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     * */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        //    pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        // dismissDialog(progress_bar_type);

        // Displaying downloaded image into image view
        // Reading image path from sdcard
        String imagePath = Environment.getExternalStorageDirectory().toString() + "/liga.csv";
        // setting downloaded into image view
      // ImageView my_image = (ImageView) findViewById(R.id.my_image);
      //  my_image.setImageDrawable(Drawable.createFromPath(imagePath));


        mCallback.processFinish(imagePath);


        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


    }


}