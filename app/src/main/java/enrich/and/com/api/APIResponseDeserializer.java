package enrich.and.com.api;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class APIResponseDeserializer<T> implements JsonDeserializer<T> {

    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";

    Class<T> returnType;


    public APIResponseDeserializer(Class<T> clazz)
    {
        returnType = clazz;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        // Read simple String values.
        final int statusCode = jsonObject.get(KEY_STATUS).getAsInt();
        JsonElement dataElement = jsonObject.get(KEY_DATA);

        // Read the dynamic parameters object.
        //final Map<String, String> parameters = readParametersMap(jsonObject);
        if(LoginResponse.class.isAssignableFrom(returnType)) {
            LoginResponse result = new LoginResponse();
            result.setStatusCode(statusCode);

            if (statusCode != 200) {
                JsonObject dataObj = dataElement.getAsJsonObject();
                boolean hasMsg = false;
                for (Map.Entry<String, JsonElement> entry : dataObj.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();
                    if (key.equalsIgnoreCase("msg")) {
                        result.setMsg(value);
                        hasMsg = true;
                    }
                }
                if (!hasMsg)
                    result.setMsg("Error");

                result.setData(null);

                return (T)result;
            }
            //Type type = new TypeToken<LoginResponse>() {}.getType();
            //context.deserialize(json, type);

            Gson gson = new Gson();
            LoginResponse m = gson.fromJson(json, LoginResponse.class);
            return  (T)m;
        }
        else if(SettingResponse.class.isAssignableFrom(returnType)) {
            SettingResponse result = new SettingResponse();
            result.setStatusCode(statusCode);

            if (statusCode != 200) {
                JsonObject dataObj = dataElement.getAsJsonObject();
                boolean hasMsg = false;
                for (Map.Entry<String, JsonElement> entry : dataObj.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();
                    if (key.equalsIgnoreCase("msg")) {
                        result.setMsg(value);
                        hasMsg = true;
                    }
                }
                if (!hasMsg)
                    result.setMsg("Error");

                result.setData(null);

                return (T)result;
            }
            //Type type = new TypeToken<LoginResponse>() {}.getType();
            //context.deserialize(json, type);

            Gson gson = new Gson();
            SettingResponse m = gson.fromJson(json, SettingResponse.class);
            return  (T)m;
        }
        else if(GetMyContactsResponse.class.isAssignableFrom(returnType)) {
            GetMyContactsResponse result = new GetMyContactsResponse();
            result.setStatusCode(statusCode);

            if (statusCode != 200) {
                JsonObject dataObj = dataElement.getAsJsonObject();
                boolean hasMsg = false;
                for (Map.Entry<String, JsonElement> entry : dataObj.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();
                    if (key.equalsIgnoreCase("msg")) {
                        result.setMsg(value);
                        hasMsg = true;
                    }
                }
                if (!hasMsg)
                    result.setMsg("Error");

                result.setData(null);

                return (T)result;
            }
            //Type type = new TypeToken<LoginResponse>() {}.getType();
            //context.deserialize(json, type);

            Gson gson = new Gson();
            GetMyContactsResponse m = gson.fromJson(json, GetMyContactsResponse.class);
            return  (T)m;
        }
        else if(CardImageResponse.class.isAssignableFrom(returnType)) {
            CardImageResponse result = new CardImageResponse();
            result.setStatusCode(statusCode);

            if (statusCode != 200) {
                JsonObject dataObj = dataElement.getAsJsonObject();
                boolean hasMsg = false;
                for (Map.Entry<String, JsonElement> entry : dataObj.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();
                    if (key.equalsIgnoreCase("msg")) {
                        result.setMsg(value);
                        hasMsg = true;
                    }
                }
                if (!hasMsg)
                    result.setMsg("Error");

                result.setData(null);

                return (T)result;
            }
            //Type type = new TypeToken<LoginResponse>() {}.getType();
            //context.deserialize(json, type);

            Gson gson = new Gson();
            CardImageResponse m = gson.fromJson(json, CardImageResponse.class);
            return  (T)m;
        }
        else if(AddUpdateContactResponse.class.isAssignableFrom(returnType)) {
            AddUpdateContactResponse result = new AddUpdateContactResponse();
            result.setStatusCode(statusCode);

            if (statusCode != 200) {
                JsonObject dataObj = dataElement.getAsJsonObject();
                boolean hasMsg = false;
                for (Map.Entry<String, JsonElement> entry : dataObj.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();
                    if (key.equalsIgnoreCase("msg")) {
                        result.setMsg(value);
                        hasMsg = true;
                    }
                }
                if (!hasMsg)
                    result.setMsg("Error");

                result.setData(null);

                return (T)result;
            }
            //Type type = new TypeToken<LoginResponse>() {}.getType();
            //context.deserialize(json, type);

            Gson gson = new Gson();
            AddUpdateContactResponse m = gson.fromJson(json, AddUpdateContactResponse.class);
            return  (T)m;
        }
        else if(DeleteContactResponse.class.isAssignableFrom(returnType)) {
            DeleteContactResponse result = new DeleteContactResponse();
            result.setStatusCode(statusCode);

            if (statusCode != 200) {
                JsonObject dataObj = dataElement.getAsJsonObject();
                boolean hasMsg = false;
                for (Map.Entry<String, JsonElement> entry : dataObj.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().getAsString();
                    if (key.equalsIgnoreCase("msg")) {
                        result.setMsg(value);
                        hasMsg = true;
                    }
                }
                if (!hasMsg)
                    result.setMsg("Error");

                result.setData(null);

                return (T)result;
            }
            //Type type = new TypeToken<LoginResponse>() {}.getType();
            //context.deserialize(json, type);

            Gson gson = new Gson();
            DeleteContactResponse m = gson.fromJson(json, DeleteContactResponse.class);
            return  (T)m;
        }
        return null;
    }

    /*@Nullable
    private Map<String, String> readParametersMap(@NonNull final JsonObject jsonObject) {
        final JsonElement paramsElement = jsonObject.get(KEY_PARAMETERS);
        if (paramsElement == null) {
            // value not present at all, just return null
            return null;
        }

        final JsonObject parametersObject = paramsElement.getAsJsonObject();
        final Map<String, String> parameters = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : parametersObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            parameters.put(key, value);
        }
        return parameters;
    }*/
}