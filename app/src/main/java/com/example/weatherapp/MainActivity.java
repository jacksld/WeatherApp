package com.example.weatherapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;




public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button button;
    TextView show;
    String url;

    class getWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));

                String line="";
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("main");
                weatherInfo =  weatherInfo.replace("temp", "Temperature");
                weatherInfo =  weatherInfo.replace("humidity", "Humidity");
                weatherInfo =  weatherInfo.replace("pressure", "Pressure");
                weatherInfo = weatherInfo.replace("temp_min", "Lowest temperature");
                weatherInfo = weatherInfo.replace("temp_max", "Highest temperature");
                weatherInfo = weatherInfo.replace("feels_like", "Feels like");
                weatherInfo = weatherInfo.replace("sea_level", "Sea level");
                weatherInfo = weatherInfo.replace("grnd_level", "Ground level");
                weatherInfo = weatherInfo.replace("{", "");
                weatherInfo = weatherInfo.replace("}", "");
                weatherInfo = weatherInfo.replace(",", "\n");
                weatherInfo = weatherInfo.replace(":", " : ");
                show.setText(weatherInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); */
        cityName = findViewById(R.id.cityName);
        button = findViewById(R.id.button);
        show = findViewById(R.id.weatherInfo);

        final String[] temp={""};

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Button Clicked! ", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                try{
                    if(city!=null){
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=5c6d80e979d05fd4cd517d6753af8e45";
                    }else{
                        Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                    }
                    getWeather task= new getWeather();
                    temp[0] = task.execute(url).get();
                }catch(ExecutionException e){
                    e.printStackTrace();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(temp[0] == null){
                    show.setText("Cannot able to find Weather");
                }

            }
        });

    }
}