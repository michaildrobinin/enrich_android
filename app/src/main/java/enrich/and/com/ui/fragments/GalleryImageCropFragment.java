package enrich.and.com.ui.fragments;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageOptions;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.MainApplication;
import enrich.and.com.utils.util;

public class GalleryImageCropFragment extends BaseFragment implements CropImageView.OnCropImageCompleteListener , CropImageView.OnSetImageUriCompleteListener{

    protected View m_rootView = null;

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;

    private boolean isFrontCard = false;

    /**
     * the options that were set for the crop image
     */
    private CropImageOptions mOptions;

    public GalleryImageCropFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.crop_img_layout, container, false);
            ButterKnife.bind(this, m_rootView);

        }

        String photoPath = getArguments().getString("imagepath" , "");
        this.isFrontCard = getArguments().getBoolean("isFrontCard" , true);
        if(photoPath.equalsIgnoreCase("")) {
            return m_rootView;
        }

        Uri imgUri = Uri.fromFile(new File(photoPath));
        cropImageView.setOnSetImageUriCompleteListener(this);
        cropImageView.setOnCropImageCompleteListener(this);

        cropImageView.setImageUriAsync(imgUri);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setAspectRatio(7,4);


        return m_rootView;
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

        getContainer().showProgressDialog("" , false , null);
        saveImageTask.execute(croppedBitmap);
    }

    @OnClick(R.id.imgClose)
    public void onClickButtonClose()
    {
        onBackPressed();
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

    }


    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {

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
            getContainer().hideProgressDialog();
            MainApplication.getInstance().setCurrentPhotoPath(savedFilePath);
            if (isFrontCard) {
                MainApplication.getInstance().getCurrentContactInfo().setFrontCardImagePath(savedFilePath);
            } else {
                MainApplication.getInstance().getCurrentContactInfo().setBackCardImagePath(savedFilePath);
            }

            if(MainApplication.getInstance().getCurrentContactInfo().contact.getId()>0)
            {
                onBackPressed();
            }
            else {

                onBackPressed();
                MainApplication.getInstance().getFragmentChanger().showScannedCardsFragment();
            }
        }
    };

    protected void cropImage() {

    }

}
