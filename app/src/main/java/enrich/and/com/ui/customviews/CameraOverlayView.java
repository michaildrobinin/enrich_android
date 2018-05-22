package enrich.and.com.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.theartofdev.edmodo.cropper.CropImageView;

public class CameraOverlayView extends View {

    Paint mBorderPaint , mCornerPaint;
    Paint mBackgroundPaint , mCropAreaPaint;

    private int desiredWidth = 480;
    private int desiredHeight = 800;

    private int mViewWidth = 0 , mViewHeight = 0;
    private Rect mBackgroundRect , mCropRect;

    private float aspectRatioX = 4;
    private float aspectRatioY = 7;

    private int cornerSize = 100;
    private int cornerStrokeSize = 14;
    private int halfCornerStrokeSize = 1;

    public CameraOverlayView(Context context) {
        this(context , null);

    }

    public CameraOverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init()
    {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setARGB(170 , 0, 0, 0);

        mCropAreaPaint = new Paint();
        mCropAreaPaint.setAntiAlias(true);
        mCropAreaPaint.setStyle(Paint.Style.FILL);
        mCropAreaPaint.setColor(getResources().getColor(android.R.color.transparent));

        mCornerPaint = new Paint();

        mCornerPaint.setColor( Color.WHITE);
        mCornerPaint.setStrokeWidth( cornerStrokeSize );

        halfCornerStrokeSize = (int)(cornerStrokeSize / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = widthSize;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = heightSize;
        }

        /* this.mViewWidth = width;
        this.mViewHeight = height;

        mBackgroundRect = new Rect(0 , 0 , width , height);

        Rect desiredCropRect = new Rect(0,0,width,height);

        int desiredCropRectWidth = desiredCropRect.width();
        int desiredCropRectHeight = desiredCropRect.height();

        float xRatio = aspectRatioY / aspectRatioX;
        float yRatio = aspectRatioX / aspectRatioY;

        if(desiredCropRectWidth * xRatio > desiredCropRectHeight)
        {
            int cropWidth = (int)(desiredCropRectHeight * yRatio);
            int offset = (int)((desiredCropRectWidth-cropWidth)/2);
            mCropRect = new Rect(desiredCropRect.left+offset,
                    desiredCropRect.top,
                    desiredCropRect.right-offset ,
                    desiredCropRect.bottom);

        }
        else
        {
            int cropHeight = (int)(desiredCropRectWidth * xRatio);
            int offset = (int)((desiredCropRectHeight-cropHeight)/2);
            mCropRect = new Rect(desiredCropRect.left,
                    desiredCropRect.top + offset,
                    desiredCropRect.right ,
                    desiredCropRect.bottom-offset);
        }

        cornerSize = (int)(mCropRect.width()*0.17);*/

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = right - left;
        int height = bottom - top;

        this.mViewWidth = width;
        this.mViewHeight = height;


        mBackgroundRect = new Rect(0 , 0 , width , height);

        /*Rect desiredCropRect = new Rect(
                (int)(0.125*width),
                (int)(0.086*height),
                (int)(0.875*width),
                (int)(0.816*height)
        );*/
        Rect desiredCropRect = new Rect(0,0,width,height);


        int desiredCropRectWidth = desiredCropRect.width();
        int desiredCropRectHeight = desiredCropRect.height();

        float xRatio = aspectRatioY / aspectRatioX;
        float yRatio = aspectRatioX / aspectRatioY;

        if(desiredCropRectWidth * xRatio > desiredCropRectHeight)
        {
            int cropWidth = (int)(desiredCropRectHeight * yRatio);
            int offset = (int)((desiredCropRectWidth-cropWidth)/2);
            mCropRect = new Rect(desiredCropRect.left+offset,
                    desiredCropRect.top,
                    desiredCropRect.right-offset ,
                    desiredCropRect.bottom);

        }
        else
        {
            int cropHeight = (int)(desiredCropRectWidth * xRatio);
            int offset = (int)((desiredCropRectHeight-cropHeight)/2);
            mCropRect = new Rect(desiredCropRect.left,
                    desiredCropRect.top + offset,
                    desiredCropRect.right ,
                    desiredCropRect.bottom-offset);
        }

        cornerSize = (int)(mCropRect.width()*0.17);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mViewWidth == 0 || mViewHeight == 0)
            return;

        drawBackground(canvas);

        //drawBorders(canvas);

        drawCorners(canvas);
    }

    public float getPreviewScreenRatio()
    {
        float res = 1.0f;
        if(mViewHeight != 0 && mViewWidth != 0)
        {
            res = (float)mViewHeight/mViewWidth;
        }
        return res;
    }

    protected void drawBackground(Canvas canvas)
    {
        canvas.drawRect(0 , 0, mBackgroundRect.right , mCropRect.top , mBackgroundPaint);
        canvas.drawRect(0 , mCropRect.bottom, mBackgroundRect.right , mBackgroundRect.bottom , mBackgroundPaint);
        canvas.drawRect(0 , mCropRect.top, mCropRect.left , mCropRect.bottom , mBackgroundPaint);
        canvas.drawRect(mCropRect.right , mCropRect.top, mBackgroundRect.right , mCropRect.bottom , mBackgroundPaint);

        canvas.drawRect(mCropRect , mCropAreaPaint);
    }

    protected void drawBorders(Canvas canvas)
    {

    }

    protected void drawCorners(Canvas canvas)
    {
        //top-left corner
        canvas.drawLine(mCropRect.left,mCropRect.top+halfCornerStrokeSize , mCropRect.left+cornerSize , mCropRect.top+halfCornerStrokeSize , mCornerPaint);
        canvas.drawLine(mCropRect.left+halfCornerStrokeSize,mCropRect.top , mCropRect.left+halfCornerStrokeSize , mCropRect.top+cornerSize , mCornerPaint);

        //top-right corner
        canvas.drawLine(mCropRect.right,mCropRect.top+halfCornerStrokeSize , mCropRect.right-cornerSize , mCropRect.top+halfCornerStrokeSize , mCornerPaint);
        canvas.drawLine(mCropRect.right-halfCornerStrokeSize,mCropRect.top , mCropRect.right-halfCornerStrokeSize , mCropRect.top+cornerSize , mCornerPaint);

        //bottom-left corner
        canvas.drawLine(mCropRect.left+halfCornerStrokeSize,mCropRect.bottom , mCropRect.left+halfCornerStrokeSize, mCropRect.bottom-cornerSize , mCornerPaint);
        canvas.drawLine(mCropRect.left,mCropRect.bottom-halfCornerStrokeSize , mCropRect.left+cornerSize , mCropRect.bottom-halfCornerStrokeSize , mCornerPaint);

        //bottom-right corner
        canvas.drawLine(mCropRect.right,mCropRect.bottom-halfCornerStrokeSize , mCropRect.right-cornerSize, mCropRect.bottom-halfCornerStrokeSize , mCornerPaint);
        canvas.drawLine(mCropRect.right-halfCornerStrokeSize,mCropRect.bottom , mCropRect.right-halfCornerStrokeSize , mCropRect.bottom-cornerSize , mCornerPaint);

    }

    public Rect getCropRectArea(Rect originRect)
    {
        Rect croppedRect = new Rect();
        float leftRate = (float)mCropRect.left/mViewWidth;
        float rightRate = (float)mCropRect.right/mViewWidth;
        float topRate = (float)mCropRect.top / mViewHeight;
        float bottomRate = (float)mCropRect.bottom / mViewHeight;

        int cropLeft = (int)(leftRate*originRect.width());
        int cropTop = (int)(topRate*originRect.height());
        int cropRight = (int)(rightRate*originRect.width());
        int cropBottom = (int)(bottomRate*originRect.height());

        return new Rect(cropLeft , cropTop , cropRight, cropBottom);
    }
}
