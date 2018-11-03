package com.pay.yc.common.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * ClassName: JsonUtils
 * 
 * @description
 * @author framework
 * @Date 2013-4-1
 * 
 */
public class JsonUtils {

    private static Map<Class<?>, Object> SERIALIZER;
    private static Map<Class<?>, Object> DESERIALIZER;

    private static class LogExclStrat implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredClass() == org.apache.commons.logging.Log.class;
        }

    }

    static {
        DESERIALIZER = new HashMap<Class<?>, Object>();
         

        SERIALIZER = new HashMap<Class<?>, Object>();
        
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return fromJson(json, classOfT, DESERIALIZER);
    }

    public static <T> T fromJson(String json, Type type) {
        return fromJson(json, type, DESERIALIZER);
    }

    public static <T> T fromJson(String json, Type type, Map<Class<?>, Object> adapters) {
    	if(json == null || json.equals("")) {
    		return null;
    	}
        Gson gson = getGson(adapters);
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Class<T> classOfT, Map<Class<?>, Object> adapters) {
        Gson gson = getGson(adapters);
        return gson.fromJson(json, classOfT);
    }

    public static String toJson(Object jsonElement) {
        return toJson(jsonElement, SERIALIZER);
    }

    public static String toJson(Object jsonElement, Type type) {
        Gson gson = getGson(SERIALIZER);
        return gson.toJson(jsonElement, type);
    }

    public static String toJson(Object jsonElement, Map<Class<?>, Object> adapters) {
        Gson gson = getGson(adapters);
        return gson.toJson(jsonElement);
    }

    public static Gson getGson(Map<Class<?>, Object> adapters) {
        Gson gson = null;
        if (adapters != null) {
            GsonBuilder gsonBuilder = new GsonBuilder(); // If want to show null
                                                         // field add the
                                                         // following:
                                                         // .serializeNulls();
            for (Map.Entry<Class<?>, Object> entry : adapters.entrySet()) {
                gsonBuilder.registerTypeAdapter(entry.getKey(), entry.getValue());
            }
            gson = gsonBuilder.setExclusionStrategies(new LogExclStrat()).create();
        } else {
            gson = new GsonBuilder().setExclusionStrategies(new LogExclStrat()).create(); // .serializeNulls()
        }
        return gson;
    }

    public static void main(String[] args) {
    	 // new TypeToken<List<Student>>() {}.getType()
         
        Map<String, Object> testvo2 = new HashMap<String, Object>();
        testvo2.put("name", "Tom");
        testvo2.put("age", 10);
        
        System.out.println(JsonUtils.toJson(testvo2));
        
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        
        testvo2 = JsonUtils.fromJson(JsonUtils.toJson(testvo2), type);
        System.out.println(JsonUtils.toJson(testvo2));

    }
}
