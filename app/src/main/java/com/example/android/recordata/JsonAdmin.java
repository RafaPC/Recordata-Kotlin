package com.example.android.recordata;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import com.example.android.recordata.classModel.Carrete;
import com.example.android.recordata.classModel.Datos;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import com.google.gson.*;

public class JsonAdmin {

    private static class ZonedDateTimeSerializer implements JsonSerializer<ZonedDateTime> {
        public JsonElement serialize(ZonedDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class LocationSerializer implements JsonSerializer<Location> {
        public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class ZonedDateTimeDeserializer implements JsonDeserializer<ZonedDateTime> {
        public ZonedDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString());
        }
    }

    private static class LocationDeserializer implements JsonDeserializer<Location> {
        public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Location location = new Location("GPS_PROVIDER");
            //Coger del string los datos que quiera y meterlos al objeto location
            return location;
        }
    }

    public static ArrayList<Carrete> getCarretes(Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //Le digo que utilice el deserializador personalizado que hace el ZonedDateTime.parse(lo que venia en el json, que viene a ser el toString)
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeDeserializer());
        Gson gson = gsonBuilder.create();

        String json = PreferenceManager.
                getDefaultSharedPreferences(context).getString("datos", "");
        Log.d("CARRETES: ", json);

        if (json.length() == 0) {
            return new ArrayList<Carrete>();
        } else {
            Datos datos = gson.fromJson(json, Datos.class);
            ArrayList<Carrete> carretes = (ArrayList) datos.getCarretes();
            return carretes;
        }

    }

    public static void guardarCarretes(Context context, ArrayList<Carrete> carretes) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        //Le digo que utilice el deserializador personalizado que hace el ZonedDateTime.toString;
        gsonBuilder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer());
        Gson gson = gsonBuilder.create();

        Datos datos = new Datos(carretes);

        String json = gson.toJson(datos);
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString("datos", json).apply();
    }
}
