package enrich.and.com.ui.customviews;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;

public class AspectRatioCameraPreview extends ViewGroup implements SurfaceHolder.Callback{
    private final String TAG = "Preview";

    SurfaceView mSurfaceView;
    CameraOverlayView mOverlayView;
    SurfaceHolder mHolder;
    Size mPreviewSize;
    Size mPictureSize;
    List<Size> mSupportedPreviewSizes;
    List<Size> mSupportedPictureSizes;

    Camera mCamera;
    boolean previewing = false;

    private Context mContext ;
    private boolean isFlashAvailable = false;

    AspectRatioCameraPreview(Context context) {
        this(context , null);

    }



    public AspectRatioCameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        isFlashAvailable = context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        RelativeLayout parentLayout = new RelativeLayout(context);
        parentLayout.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        //addView(parentLayout);

        mSurfaceView = new SurfaceView(context);
        mOverlayView = new CameraOverlayView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT);
        //mSurfaceView.setLayoutParams(params);
        //mOverlayView.setLayoutParams(params);

        addView(mSurfaceView);
        addView(mOverlayView);


        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera) {
        mCamera = camera;
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPictureSizes = mCamera.getParameters().getSupportedPictureSizes();

            mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        Log.d("down", "focusing now");

                        mCamera.autoFocus(null);
                    }

                    return true;
                }
            });

            requestLayout();
        }
    }

    public void switchCamera(Camera camera) {
        setCamera(camera);
        try {
            camera.setPreviewDisplay(mHolder);
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();

        camera.setParameters(parameters);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            Size optimalPicSize = getOptimalPictureSize(mSupportedPictureSizes , width , height);//portrait mode
            mPreviewSize = getOptimalPreviewSize(optimalPicSize , mSupportedPreviewSizes);
            mPictureSize = optimalPicSize;

            //mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }

        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                //Make or work out measurements for children here (MeasureSpec.make...)
                measureChild (child, width, height);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && getChildCount() > 0) {

            final int width = r - l;
            final int height = b - t;

            int previewWidth = width;
            int previewHeight = height;
            if (mPreviewSize != null) {
                if(mPreviewSize.width>mPreviewSize.height)//camera preview size is landscape , so swap width and height
                {
                    previewWidth = mPreviewSize.height;
                    previewHeight = mPreviewSize.width;
                }
                else
                {
                    previewWidth = mPreviewSize.width;
                    previewHeight = mPreviewSize.height;
                }
            }

            double previewRatioX = (double)previewWidth/previewHeight;
            double previewRatioY = (double)previewHeight/previewWidth;

            if(width * previewRatioY < height)
            {
                final int scaledChildHeight = (int)(width * previewRatioY);
                int childCount = getChildCount();
                for(int i= 0;i<childCount;i++)
                {
                    View child = getChildAt(i);
                    child.layout(0 , (height-scaledChildHeight)/2 , width , (height + scaledChildHeight)/2);
                    child.requestLayout();
                    child.invalidate();
                }
            }
            else
            {
                final int scaledChildWidth = (int)(height * previewRatioX);
                int childCount = getChildCount();
                for(int i= 0;i<childCount;i++)
                {
                    View child = getChildAt(i);
                    child.layout((width-scaledChildWidth)/2 , 0 , (width + scaledChildWidth)/2 , height);
                    child.requestLayout();
                    child.invalidate();
                }
            }

        }
    }

    public Rect getFinderViewRect(Rect origin)
    {
        return mOverlayView.getCropRectArea(origin);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }


    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }


    /*private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }*/

    public Size getOptimalPictureSize(List<Camera.Size> sizes , int displayWidth ,int displayHeight) {

        Size opt = mCamera.new Size(0 , 0);
        float objf = Float.MAX_VALUE;
        float v;
        for(Camera.Size s : sizes){
            if(opt.width == 0 && opt.height == 0)//set the first supported picture size as default
            {
                opt.width = s.width;
                opt.height = s.height;
            }
            if(s.height<displayWidth  || s.width<displayHeight)
                continue;

            v = (s.height-displayWidth)*s.width + (s.width-displayHeight)*displayWidth;

            if(v<objf){
                opt.width=s.width;
                opt.height=s.height;
                objf=v;
            }
        }

        return opt;

    }

    public Size getOptimalPreviewSize(Size picSize,List<android.hardware.Camera.Size> sizes) {
        Size opt = mCamera.new Size(picSize.width , picSize.height);
        double objf = Double.MAX_VALUE;
        double aspratio = (double)picSize.width/picSize.height;//picSize.getAspectRatio();
        double v;

        for(Camera.Size s : sizes){
            if(opt.width == 0 && opt.height == 0)//set the first supported preview size as default
            {
                opt.width = s.width;
                opt.height = s.height;
            }
            v = Math.abs( ((double)s.width)/((double)s.height) - aspratio )/(Math.max(((double)s.width)/((double)s.height), aspratio));

            if(v<objf){
                objf=v;
                opt.width=s.width;
                opt.height=s.height;
            }
        }

        return opt;
    }



    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        if (previewing)
        {
            mCamera.stopPreview();
        }

        Camera.Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            //params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        else
        {
            if(parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

            }
        }

        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height );
        parameters.setPictureSize(mPictureSize.width , mPictureSize.height);

        if(display.getRotation() == Surface.ROTATION_0)
        {
            //parameters.setPreviewSize(mPreviewSize.height , mPreviewSize.width);

            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height );
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            //parameters.setPreviewSize(height, width);
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height );

        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            //parameters.setPreviewSize(width, height);
            //parameters.setPreviewSize(mPreviewSize.height , mPreviewSize.width);
            mCamera.setDisplayOrientation(180);
        }


        if(isFlashAvailable)
        {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }

        requestLayout();
        invalidate();

        try {
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        previewing = true;
    }
}
