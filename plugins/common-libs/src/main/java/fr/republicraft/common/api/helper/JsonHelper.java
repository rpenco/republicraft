package fr.republicraft.common.api.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {
    private static final Gson gson = new GsonBuilder().create();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String jsonStr, Class<T> objClass) {
        return gson.fromJson(jsonStr, objClass);
    }
}
