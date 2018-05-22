package enrich.and.com.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.utils.util;

public class SmsImageCropActivity extends BaseActivity {

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    /**
     * the options that were set for the crop image
     */
    private CropImageOptions mOptions;

    private String originalPhotoFilePath = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.crop_img_layout);
        ButterKnife.bind(this);

        originalPhotoFilePath = getIntent().getStringExtra("imagepath");

        Uri imgUri = Uri.fromFile(new File(originalPhotoFilePath));

        cropImageView.setImageUriAsync(imgUri);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAspectRatio(9,16);
    }

    @OnClick(R.id.imgRotate)
    public void onClickImageRotate()
    {
        cropImageView.rotateImage(90);
    }

    @OnClick(R.id.imgCrop)
    public void onClickImageCrop()
    {
        Bitmap croppedBitmap = cropImageView.getCroppedImage();

        showProgressDialog("Saving..." , false , null);
        saveImageTask.execute(croppedBitmap);
    }

    @OnClick(R.id.imgClose)
    public void onClickButtonClose()
    {
        finish();
    }


    AsyncTask<Bitmap , Void ,String> saveImageTask = new AsyncTask<Bitmap, Void, String>() {
        @Override
        protected String doInBackground(Bitmap... bitmapParams) {
            Bitmap croppedBitmap = bitmapParams[0];
            if(croppedBitmap == null)
                return "";

            SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
            String strTime = format.format(Calendar.getInstance().getTime());

            String fileName = strTime.replace(' ', '_').replace(":", "_") + ".jpg";
            // 6. Save Crop bitmap to file
            return util.saveImageToExternalStorage(croppedBitmap, fileName);
        }


        @Override
        protected void onPostExecute(String savedFilePath) {
            hideProgressDialog();
            if(savedFilePath.equalsIgnoreCase(""))
            {
                SmsImageCropActivity.this.setResult(RESULT_CANCELED);
                SmsImageCropActivity.this.finish();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("imagepath" , savedFilePath);
            SmsImageCropActivity.this.setResult(RESULT_OK , intent);
            SmsImageCropActivity.this.finish();
        }
    };

}
