package kreishravi.cp470.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "WeatherForecastActivity";

    ProgressBar loading_bar = null;
    TextView current_temp_view = null;
    TextView min_temp_view = null;
    TextView max_temp_view = null;
    ImageView weather_image_view = null;

    List<String> city_list = null;
    TextView city_name_view = null;
    Spinner city_spinner = null;

    Drawable draw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        setTitle("Weather Network Information");

        loading_bar = findViewById(R.id.loading_bar);
        current_temp_view = findViewById(R.id.current_temp);
        min_temp_view = findViewById(R.id.min_temp);
        max_temp_view = findViewById(R.id.max_temp);
        weather_image_view = findViewById(R.id.weather_image_view);
        city_name_view = findViewById(R.id.city_name);
        city_spinner = findViewById(R.id.city_spinner);
        draw = getResources().getDrawable(R.drawable.custom_progress_bar);
        loading_bar.setProgressDrawable(draw);
        chooseCity();
    }

    public void chooseCity() {
        city_list = Arrays.asList(getResources().getStringArray(R.array.cities));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        city_spinner.setAdapter(adapter);
        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    loading_bar.setVisibility(View.VISIBLE);
                    city_name_view.setVisibility(View.VISIBLE);
                    current_temp_view.setVisibility(View.INVISIBLE);
                    min_temp_view.setVisibility(View.INVISIBLE);
                    max_temp_view.setVisibility(View.INVISIBLE);
                    weather_image_view.setVisibility(View.INVISIBLE);
                    new ForecastQuery(city_list.get(position)).execute();
                    city_name_view.setText("Weather in " + city_list.get(position));
                } else {
                    loading_bar.setVisibility(View.INVISIBLE);
                    current_temp_view.setVisibility(View.INVISIBLE);
                    min_temp_view.setVisibility(View.INVISIBLE);
                    max_temp_view.setVisibility(View.INVISIBLE);
                    weather_image_view.setVisibility(View.INVISIBLE);
                    city_name_view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        private String currentTemp;
        private String minTemp;
        private  String maxTemp;
        private Bitmap weather_image;
        protected String city;

        ForecastQuery(String city) {
            this.city = city;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.openweathermap.org/"
                        + "data/2.5/weather?q=" + this.city
                        + ","
                        + "ca&APPID=79cecf493cb6e52d25bb7b7050ff723c&"
                        + "mode=xml&units=metric");

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                Log.i(ACTIVITY_NAME, "Querying: " + url);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                InputStream in = conn.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);

                    int type;
                    while((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("temperature")) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                Thread.sleep(200);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                Thread.sleep(200);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                                Thread.sleep(200);
                            } else if (parser.getName().equals("weather")) {
                                String icon_name = parser.getAttributeValue(null, "icon");
                                publishProgress(100);
                                Thread.sleep(200);
                                String file_name = icon_name + ".png";
                                Log.i(ACTIVITY_NAME, "Looking for file: " + file_name);
                                if (fileExistence(file_name)) {
                                    FileInputStream file_input = null;
                                    try {
                                        file_input = openFileInput(file_name);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME, "File was found locally!");
                                    weather_image = BitmapFactory.decodeStream(file_input);
                                } else {
                                    String icon_url = "https://openweathermap.org/img/w/" + file_name;
                                    weather_image = getImage(new URL(icon_url));
                                    FileOutputStream file_output = openFileOutput(file_name, Context.MODE_PRIVATE);
                                    weather_image.compress(Bitmap.CompressFormat.PNG, 80, file_output);
                                    Log.i(ACTIVITY_NAME, "Downloaded weather image from the Internet!");
                                    file_output.flush();
                                    file_output.close();
                                }
                            }
                        }
                        parser.next();
                    }
                } finally {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        public boolean fileExistence(String file_name) {
            File file = getBaseContext().getFileStreamPath(file_name);
            Log.i(ACTIVITY_NAME, file_name + " is at " + file.getAbsolutePath());
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpsURLConnection connection = null;

            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            loading_bar.setVisibility(View.VISIBLE);
            loading_bar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            weather_image_view.setImageBitmap(weather_image);

            String current_temp_string = "<font color='#000000'>" + "<b> Current Temperature: </b>" + "</font>" + "<font color='#EE0000'>" + currentTemp + "C\u00b0" + "</font>";
            current_temp_view.setText(Html.fromHtml(current_temp_string));

            String min_temp_string = "<font color='#000000'>" + "<b> Minimum Temperature: </b>" + "</font>" + "<font color='#EE0000'>" + minTemp + "C\u00b0" + "</font>";
            min_temp_view.setText(Html.fromHtml(min_temp_string));

            String max_temp_string = "<font color='#000000'>" +"<b> Maximum Temperature: </b>" + "</font>" + "<font color='#EE0000'>" + maxTemp + "C\u00b0" + "</font>";
            max_temp_view.setText(Html.fromHtml(max_temp_string));

            loading_bar.setVisibility(View.INVISIBLE);
            current_temp_view.setVisibility(View.VISIBLE);
            min_temp_view.setVisibility(View.VISIBLE);
            max_temp_view.setVisibility(View.VISIBLE);
            weather_image_view.setVisibility(View.VISIBLE);
        }
    }
}