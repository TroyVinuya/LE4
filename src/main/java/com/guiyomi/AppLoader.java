package com.guiyomi;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppLoader {
    public static List<App> loadApps(String jsonFileName) throws IOException {
        Gson gson = new Gson();
        Type appListType = new TypeToken<List<App>>(){}.getType();

        // Load resource
        InputStream inputStream = AppLoader.class.getResourceAsStream("/" + jsonFileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + jsonFileName);
        }
        
        InputStreamReader reader = new InputStreamReader(inputStream);
        List<App> apps = gson.fromJson(reader, appListType);
        reader.close();
        return apps;
    }
}

