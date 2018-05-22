package enrich.and.com.api;

import android.media.MediaRouter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enrich.and.com.models.Product;
import enrich.and.com.models.UserInfo;

public class LoginResponse extends APIResposne<UserInfo>{

    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<Product>();
    @SerializedName("userid_hash")
    @Expose
    private String useridHash;

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginResponse() {
    }

    /**
     *
     * @param useridHash
     * @param products
     */
    public LoginResponse(List<Product> products, String useridHash) {
        super();
        this.products = products;
        this.useridHash = useridHash;
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getUseridHash() {
        return useridHash;
    }

    public void setUseridHash(String useridHash) {
        this.useridHash = useridHash;
    }

}
