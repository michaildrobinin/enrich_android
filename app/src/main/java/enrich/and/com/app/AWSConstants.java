package enrich.and.com.app;

public class AWSConstants {
    /*
     * You should replace these values with your own. See the README for details
     * on what to fill in.
     */
    //public static final String COGNITO_POOL_ID = "us-east-1:3d12c754-087a-4304-bac0-f94d343587ce";
    public static final String COGNITO_POOL_ID = "us-east-1:29faba3e-8c58-43c0-a6b7-d160356a1f83";

    /*
     * Region of your Cognito identity pool ID.
     */
    public static final String COGNITO_POOL_REGION = "US East (N. Virginia)";

    /*
     * Note, you must first create a bucket using the S3 console before running
     * the sample (https://console.aws.amazon.com/s3/). After creating a bucket,
     * put it's name in the field below.
     */
    public static final String BUCKET_NAME = "enrich";
    public static final String SCANNER_BUCKET_NAME = "enrich-scanner";

    /*
     * Region of your bucket.
     */
    public static final String BUCKET_REGION = "US East (N. Virginia)";
}
