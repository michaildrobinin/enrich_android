package enrich.and.com.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager<T> {
    public Converter.Factory createGsonConverter(Class<T> clazz) {
        //Type clsType = new TypeToken<APIResposne<UserInfo>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(clazz , new APIResponseDeserializer<T>(clazz));
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }




}
