package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText city;
    TextView result;

    //  http://api.openweathermap.org/data/2.5/weather?q=tokyo&appid=320ee761f33f67fc0dbf602b859fd4f1
    String baseURL = "http://api.openweathermap.org/data/2.5/weather?q=";
    String API = "&appid=320ee761f33f67fc0dbf602b859fd4f1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button =  (Button) findViewById(R.id.button);
        city =  (EditText) findViewById(R.id.getcity);
        result = (TextView) findViewById(R.id.result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myURL = baseURL + city.getText().toString() + API;
                Log.i("URL","URL: "+ myURL);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                String weath = "";
                                int myLng = 0,myLat = 0, kelvin = 0;

                                Log.i("JSON", "JSON: " + jsonObject);
                                try {
                                    String info = jsonObject.getString("weather");
                                    JSONArray array = new JSONArray(info);
                                    for (int i = 0 ; i < array.length() ; i++) {
                                        JSONObject parObj = array.getJSONObject(i);

                                        String myWeather = parObj.getString("main");
                                        weath = myWeather;
                                        Log.e("Weather: ", myWeather + "\n");
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String lngLat = jsonObject.getString("coord");
                                    JSONObject object = new JSONObject(lngLat);
                                    myLng = object.getInt("lon");
                                    myLat = object.getInt("lat");
                                    Log.e("Long: ", String.valueOf(myLng));
                                    Log.e("Lat: ", String.valueOf(myLat));
//                                    result.setText("Longitude: " + myLng + "\n" + "Latitude: " + myLat + "\n" + "Weather: " + weath);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    String temperature = jsonObject.getString("main");
                                    JSONObject tempObj = new JSONObject(temperature);
                                    kelvin = tempObj.getInt("temp");
                                    kelvin = kelvin - 271;
                                    int humidity = tempObj.getInt("humidity");
                                    result.setText("Longitude: " + myLng
                                            + "\n" + "Latitude: " + myLat
                                            + "\n" + "Weather: " + weath
                                            + "\n" + "Temperature: " + kelvin
                                            + "\n" + "Humidity: " + humidity
                                    );
                                } catch (JSONException ey) {
                                    ey.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,"OOpppss!",Toast.LENGTH_SHORT).show();
                            }
                        });
                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
            }
        });

    }
}
