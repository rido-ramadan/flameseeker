package com.edgardrake.flameseeker.lib.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Edgar Drake on 13-Jun-17.
 */

public class Serializer {

    private static Gson gson;

    public static Gson GSON() {
        if (gson == null) {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        }
        return gson;
    }
}
