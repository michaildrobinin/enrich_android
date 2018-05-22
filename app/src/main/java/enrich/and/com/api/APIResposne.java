package enrich.and.com.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APIResposne<T> implements Serializable{
    @SerializedName("status")
    @Expose
    public int statusCode;

    @SerializedName("data")
    @Expose
    public T data;

    private String msg;


    public APIResposne()
    {
        msg = "";

    }

    public int getStatusCode(){return this.statusCode;}
    public void setStatusCode(int code){this.statusCode = code;}

    public T getData(){return data;}
    public void setData(T data){this.data = data;}

    public String getMsg(){return msg;}
    public void setMsg(String msg){this.msg = msg;}
}
