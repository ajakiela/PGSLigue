package com.example.lhalys.googledocs;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AsyncResponse{
//
    TableLayout t1;
    View last;
    TextView tx;
    TableLayout table;
    TableRow rowHeader;
    TextView rank, percentage, score;
    String filepath;
    ImageView my_image;
    DownloadFileFromURL dtest;
    private MenuItem _menuRefresh;

    // button to show progress dialog
    Button btnShowProgress;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url1 = "https://www.google.pl/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&docid=_yuqkUXfp362GM&tbnid=vQJ4joyl7FykdM:&ved=0CAUQjRw&url=http%3A%2F%2Foakhill.newton.k12.ma.us%2Fcontent%2Fgoogle-docs&ei=srvgU8THBcSUOIPrgegJ&bvm=bv.72197243,d.ZGU&psig=AFQjCNGT0L_cgtg92rnkfV-X-sOl5u1w9w&ust=1407323413524209";
     private static String file_url = "https://docs.google.com/spreadsheets/d/1lVY7cHxBAoRXxsB9RK6-f62OpDLz2ylQ3mmYQS8S39E/export?format=csv";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                _menuRefresh.collapseActionView();
                _menuRefresh.setActionView(null);

                break;

            case R.id.action_refresh:
                _menuRefresh = item;
                _menuRefresh.setActionView(R.layout.progressbar);
                _menuRefresh.expandActionView();
                dtest =  new DownloadFileFromURL(MainActivity.this ,file_url,"file downloaded" );

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(String output) {

//        TextView tv = (TextView) findViewById(R.id.textPath);
//        tv.setText(output);

        filepath = output;
        _menuRefresh.collapseActionView();
        _menuRefresh.setActionView(null);
        createTable();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public void createTable(){
        CSVParser parser = new CSVParser(filepath);
        List<String[]> list = parser.parse();

        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);


        TableRow rowHeader = new TableRow(this);
        //  rowHeader.setBackgroundResource(R.drawable.row_border);
        for (int k=0; k<list.get(0).length; k++){

            TextView label = new TextView(this);
            label.setBackgroundResource(R.drawable.row_border);
            label.setPadding(10,0,10,0);
            label.setId(k);
            label.setTextSize(25);
            label.setText(list.get(0)[k]);
            rowHeader.addView(label);


        }
        table.addView(rowHeader);


        for (int i=1; i<list.size(); i++){
            TableRow tr = new TableRow(this);
            tr.setClickable(true);
            tr.setId(i*100+1);
            //tr.setBackgroundResource(R.drawable.selected_row_border);
            tr.setOnClickListener(tablerowOnClickListener);
            for (int k=0; k<list.get(0).length; k++){
                TextView label = new TextView(this);
                label.setId(i*100+k+2);
                //label.setBackgroundResource(R.drawable.row_border);
                label.setTextSize(18);
                label.setGravity(Gravity.CENTER);
                label.setText(list.get(i)[k]);
                tr.addView(label);
                label.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
            }
            table.addView(tr);
        }
    }
    private View.OnClickListener tablerowOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            v.setBackgroundResource(R.drawable.row_border);
            Toast.makeText(getApplicationContext(),
                    "Row is clicked" + v, Toast.LENGTH_SHORT).show();

        }
    };


}
