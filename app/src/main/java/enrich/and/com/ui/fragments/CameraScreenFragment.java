package enrich.and.com.ui.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.MainApplication;
import enrich.and.com.ui.customviews.AspectRatioCameraPreview;
import enrich.and.com.ui.customviews.CameraOverlayView;
import enrich.and.com.utils.RealPathUtil;
import enrich.and.com.utils.util;

public class CameraScreenFragment extends BaseFragment{

    @BindView(R.id.camerapreview)
    AspectRatioCameraPreview mPreview;

    @BindView(R.id.btnGallery)
    ImageButton btnGallery;

    @BindView(R.id.progressBarLayout)
    RelativeLayout progressBarLayout;

    @BindView(R.id.flashSwitchLayout)
    LinearLayout flashSwitchLayout;

    @BindView(R.id.imgFlashIcon)
    ImageView imgFlashIcon;

    @BindView(R.id.txtFlashStatus)
    TextView txtFlashStatus;

    Camera camera;
    LayoutInflater controlInflater = null;

    boolean isCameraMode = true; //gallery or camera mode

    private MediaPlayer mp;

    String tmpImageFilePath = "";

    private boolean isFrontCard = true;

    private boolean isTakingPicture = false;

    private boolean isFlashOn = false;

    private final String[] flashModes = {Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_ON, Camera.Parameters.FLASH_MODE_OFF};


    private float previewScreenRatio = 1;
    boolean isFlashAvailable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_camera_screen, container, false);
            ButterKnife.bind(this, m_rootView);

            isFrontCard = getMyArguments().getBoolean("isFrontCard" , true);

            getContainer().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            getContainer().getWindow().setFormat(PixelFormat.UNKNOWN);


        }

        isFlashAvailable = getContainer().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(isFlashAvailable)
            flashSwitchLayout.setVisibility(View.VISIBLE);
        else
            flashSwitchLayout.setVisibility(View.INVISIBLE);

        mPreview.setFocusable(true);
        mPreview.setFocusableInTouchMode(true);

        isFlashOn = false;


        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        isFrontCard = getMyArguments().getBoolean("isFrontCard" , true);


        camera = Camera.open();
        mPreview.setCamera(camera);

        updateFlashStatusView();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (camera != null) {
            mPreview.setCamera(null);
            camera.release();
            camera = null;
        }
    }

    private void updateFlashStatusView()
    {
        if(isFlashOn)
        {
            imgFlashIcon.setImageResource(R.drawable.icon_flash_turnon);
            txtFlashStatus.setText(getResources().getString(R.string.str_flash_on));
        }
        else
        {

            imgFlashIcon.setImageResource(R.drawable.icon_flash_turnoff);
            txtFlashStatus.setText(getResources().getString(R.string.str_flash_off));
        }
    }



    @OnClick(R.id.btnGallery)
    public void onClickButtonGallery()
    {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    @OnClick(R.id.btnCameraPreview)
    public void onClickButtonCamera()
    {
        isCameraMode = true;
        btnGallery.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnShutter)
    public void onClickButtonShtter()
    {
        if(isCameraMode)
        {
            camera.autoFocus(new Camera.AutoFocusCallback() {
                public void onAutoFocus(boolean success, Camera camera) {
                    if(success && !isTakingPicture){
                        isTakingPicture = true;
                        //camera.takePicture(shutterCallback, rawCallback, jpegCallback);

                        camera.autoFocus(null);
                        camera.takePicture(null , null , myPictureCallback_JPG);
                        progressBarLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
        else
        {

        }
    }

    @OnClick(R.id.btnCloseCamera)
    public void onClickButtonCloseCamera()
    {
        MainApplication.getInstance().setCurrentPhotoPath("");
        onBackPressed();
    }

    @OnClick(R.id.flashSwitchLayout)
    public void onClickSwichFlashLedLight()
    {
        try {
            if (isFlashOn) {
                turnOnOffFlashLight(false);
            } else {
                turnOnOffFlashLight(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOnOffFlashLight(boolean isTurchOn) {
        if(isTurchOn)
        {
            /*try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, true);
                    playOnOffSound();
                    imgFlashIcon.setImageResource(R.drawable.icon_flash_turnon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            if( camera != null ){
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode( Camera.Parameters.FLASH_MODE_TORCH );
                camera.setParameters( params );
                isFlashOn = true;
            }
            playOnOffSound();
        }
        else
        {
            /*try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, false);
                    playOnOffSound();
                    imgFlashIcon.setImageResource(R.drawable.icon_flash_turnoff);
                }
                else
                {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            if( camera != null ){
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode( Camera.Parameters.FLASH_MODE_OFF );
                camera.setParameters( params );
                isFlashOn = false;
            }
            playOnOffSound();
        }

        updateFlashStatusView();

    }

    private void playOnOffSound(){

        mp = MediaPlayer.create(getContainer(), R.raw.flash_sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }


    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback(){

        @Override
        public void onPictureTaken(byte[] data, Camera arg1) {
            // TODO Auto-generated method stub
            Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);

            if(bitmap != null)
            {
                tmpImageFilePath = util.saveImageInExternalCacheDir(getContainer(), bitmap , "tmp_file");
            }

            isTakingPicture = false;

            new SaveImageTask().execute();

            //camera.startPreview();
        }};

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data!=null) {
            String realPath = "";
            Uri imageUri = data.getData();

            try {
                realPath = util.getPath(getContainer() , data.getData());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                realPath = "";
            }

            System.out.println("-------Real PhotoPath ="+realPath+"---------");

            onBackPressed();
            MainApplication.getInstance().getFragmentChanger().showGalleryImageCropFragment(realPath , isFrontCard);
        }

    }

    class SaveImageTask extends AsyncTask<Void , Void ,String>
    {

        public SaveImageTask()
        {

        }

        @Override
        protected String doInBackground(Void... params) {
            if(!tmpImageFilePath.equalsIgnoreCase(""))
            {
                Bitmap bitmap = util.decodeFile(new File(tmpImageFilePath) , 788, 450);
                if(bitmap == null) return "";


                int bitWidth = bitmap.getWidth();
                int bitHeight = bitmap.getHeight();

                // 4. Scale it.
                // Assume you draw Rect as "canvas.drawRect(60, 50, 210, 297, paint);" command

                Rect originBitmapRect = null;

                Rect cropRect =  null;

                if(bitWidth < bitHeight) {
                    originBitmapRect = new Rect(0, 0, bitWidth, bitHeight);
                    cropRect =  mPreview.getFinderViewRect(originBitmapRect);
                }
                else {
                    originBitmapRect = new Rect(0, 0, bitHeight , bitWidth);
                    Rect cropRect1 =  mPreview.getFinderViewRect(originBitmapRect);
                    cropRect = new Rect(cropRect1.top, cropRect1.left , cropRect1.height() + cropRect1.top , cropRect1.width()+cropRect1.left);
                }


                try {
                    // 5. Crop image
                    Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, cropRect.left, cropRect.top, cropRect.right-cropRect.left, cropRect.bottom-cropRect.top);
                    bitmap.recycle();

                    SimpleDateFormat format = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
                    String strTime = format.format(Calendar.getInstance().getTime());

                    String fileName = strTime.replace(' ', '_').replace(":", "_") + ".jpg";
                    // 6. Save Crop bitmap to file
                    return util.saveImageToExternalStorage(croppedBitmap, fileName);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String finalImagePath) {
            super.onPostExecute(finalImagePath);
            progressBarLayout.setVisibility(View.GONE);

            if(finalImagePath.equalsIgnoreCase(""))
            {
                MainApplication.getInstance().setCurrentPhotoPath("");
            }
            else {
                MainApplication.getInstance().setCurrentPhotoPath(finalImagePath);
                boolean isContactExist = false;
                if(isFrontCard)
                {
                    if(MainApplication.getInstance().getCurrentContactInfo().contact.getId()> 0)
                        isContactExist = true;

                    MainApplication.getInstance().getCurrentContactInfo().setFrontCardImagePath(finalImagePath);

                }
                else
                {
                    if(MainApplication.getInstance().getCurrentContactInfo().contact.getId() > 0)
                        isContactExist = true;

                    MainApplication.getInstance().getCurrentContactInfo().setBackCardImagePath(finalImagePath);

                }
                onBackPressed();
                if(!isContactExist)
                    MainApplication.getInstance().getFragmentChanger().showScannedCardsFragment();
                else
                    MainApplication.getInstance().getFragmentChanger().showViewFullImageCardFragment(isFrontCard , finalImagePath);
            }

        }
    }


    public Intent getPickImageChooserIntent() {
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager =  getContainer().getPackageManager();

        // collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main  intent
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
}
