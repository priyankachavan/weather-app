package com.example.priyanka.testproject;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FetchWeatherTask weatherTask= new FetchWeatherTask();
            weatherTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList weatherItems=new ArrayList();
        weatherItems.add("Today-Sunny-88/63");
        weatherItems.add("Tomorrow-Foggy-88/63");
        weatherItems.add("Weds-Cloudy-88/63");
        weatherItems.add("Thus-Rainy-88/63");
        weatherItems.add("Friday-Foggy-88/63");
        weatherItems.add("Sat-Sunny-88/63");
        weatherItems.add("Sun-Sunny-88/63");

        ArrayAdapter<String> weatherListAdapter=new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weatherItems);

        ListView listView=(ListView)rootView.findViewById(R.id.ListViewForecast);
        listView.setAdapter(weatherListAdapter);
        FetchWeatherTask weatherTask= new FetchWeatherTask();
        weatherTask.execute();

        return rootView;
    }
    public class FetchWeatherTask extends AsyncTask<Void,Void,Void>{
        private final  String LOG_CAT=FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;

            String forecastJsonStr=null;
            try{

                String baseUrl="http://api.openweathermap.org/data/2.5/forecast/daily?id=1259229&mode=json&units=metric&cnt=7";
                String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                URL url=new URL(baseUrl.concat(apiKey));

                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer stringBuffer=new StringBuffer();

                if(inputStream==null){
                    return  null;
                }

                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line=reader.readLine())!=null){
                    stringBuffer.append(line+"\n");
                }

                if(stringBuffer.length()==0){
                    return null;
                }

                forecastJsonStr=stringBuffer.toString();

                Log.v(LOG_CAT,"Forecast :"+forecastJsonStr);
            }catch (IOException e){
                Log.e("PlaceHolderFragment","ERROR",e);
                return  null;
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    }catch (final IOException e){
                        Log.e("PlaceHolderFragment","ERROR Closing Stream",e);
                    }
                }
            }
            return null;
        }


    }
}
