package com.gbreed.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    String[][] celebs = new String[100][2];

    int[] spot;
    int randomSpot;

    ImageView imageViewCelebs;

    Button button0;
    Button button1;
    Button button2;
    Button button3;

    protected class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {
        protected Bitmap doInBackground(String... urls)
        {
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();
                Bitmap aBitmap = BitmapFactory.decodeStream(in);

                return aBitmap;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    protected class DownloadCelebs extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "Failed";
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadCelebs downloadCelebs = new DownloadCelebs();
        String results = null;

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        imageViewCelebs = findViewById(R.id.imageViewCelebs);



        try {
            results = downloadCelebs.execute("http://www.posh24.se/kandisar").get();
            Log.i("Success", "Downloaded succesfully");

            Pattern link = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = link.matcher(results);

            Pattern t = Pattern.compile("alt=\"(.*?)\"");
            Matcher u = t.matcher(results);

            int num = 0;

            while(m.find())
            {
                celebs[num][1] = m.group(1);

                u.find();
                celebs[num][0] = u.group(1);

                num++;
            }

            Log.i("Downloading Pictures","Put all celebs in memory");
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.i("Failed", "failed");
        }

        getRandomCelebs();
    }

    public void getRandomCelebs()
    {
        Log.i("Downloading Pictures","0");

        spot = new int[4];

        Random ram = new Random();
        int currentCeleb = ram.nextInt(86);
        Bitmap aImage;
        ImageDownloader task = new ImageDownloader();

        try{
            Log.i("Downloading Pictures","2");
            aImage = task.execute(celebs[currentCeleb][1]).get();
            imageViewCelebs.setImageBitmap(aImage);

            Log.i("Downloading Pictures","3");

            int currentCeleb2;

            randomSpot = ram.nextInt(4);

            spot[randomSpot] = currentCeleb;

            for(int i = 0; i < 4; i++)
            {
                if(i != randomSpot) {
                    currentCeleb2 = ram.nextInt(86);

                    while (currentCeleb == currentCeleb2) {
                        currentCeleb2 = ram.nextInt(86);
                    }

                    spot[i] = currentCeleb2;
                }
            }

            button0.setText(celebs[spot[0]][0]);
            button1.setText(celebs[spot[1]][0]);
            button2.setText(celebs[spot[2]][0]);
            button3.setText(celebs[spot[3]][0]);

        }catch (Exception e)
        {
            e.printStackTrace();
            Log.i("Failed","Failed to get picture");
        }
    }

    public void buttonCelebs(View view)
    {
        if(Integer.parseInt(view.getTag().toString()) == randomSpot)
        {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Wrong! it's " + celebs[spot[randomSpot]][0], Toast.LENGTH_SHORT).show();
        }

        getRandomCelebs();
    }
}
