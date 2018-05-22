package enrich.and.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("access_start_date")
    @Expose
    private String accessStartDate;
    @SerializedName("access_end_date")
    @Expose
    private String accessEndDate;
    @SerializedName("transaction_id")
    @Expose
    private int transactionId;
    @SerializedName("coupon_id")
    @Expose
    private int couponId;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = -4454430871931711674L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Product() {
    }

    /**
     *
     * @param accessEndDate
     * @param accessStartDate
     * @param transactionId
     * @param status
     * @param userId
     * @param couponId
     * @param productId
     */
    public Product(int userId, int productId, String accessStartDate, String accessEndDate, int transactionId, int couponId, String status) {
        super();
        this.userId = userId;
        this.productId = productId;
        this.accessStartDate = accessStartDate;
        this.accessEndDate = accessEndDate;
        this.transactionId = transactionId;
        this.couponId = couponId;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getAccessStartDate() {
        return accessStartDate==null?"":accessStartDate;
    }

    public void setAccessStartDate(String accessStartDate) {
        this.accessStartDate = accessStartDate;
    }

    public String getAccessEndDate() {
        return accessEndDate==null?"":accessEndDate;
    }

    public void setAccessEndDate(String accessEndDate) {
        this.accessEndDate = accessEndDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getStatus() {
        return status==null?"":status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}