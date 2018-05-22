package enrich.and.com.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import enrich.and.com.R;

public class SmsMessageHeaderTagTextView extends TextView{

    private String strTagName = "";

    public SmsMessageHeaderTagTextView(Context context) {
        super(context);
        this.strTagName = "";
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.setting_sms_message_header_tag_textview_textsize));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setTextColor(getResources().getColor(R.color.colorLightYellow , null));
        }
        else
        {
            setTextColor(getResources().getColor(R.color.colorLightYellow));
        }
        setBackgroundResource(R.drawable.sms_message_header_tag_textview_bg);

        LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(iv_params);

        init();
    }

    public SmsMessageHeaderTagTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        readAttr(context , attrs);
        init();
    }

    public SmsMessageHeaderTagTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttr(context , attrs);
        init();
    }

    private void init()
    {
        setGravity(Gravity.CENTER);
        String htmlString = "<font color='#000000'>[[</font>" + strTagName + "<font color='#000000'>]]</font>";
        setText(Html.fromHtml(htmlString));
    }

    public void setHeaderTag(String strTagName)
    {
        this.strTagName = strTagName;
        init();
    }
    public String getHeaderTag(){
        return this.strTagName;
    }

    private void readAttr(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmsMessageHeaderTagTextViewAttr);

            // Read the title and set it if any
            int stringResId = a.getResourceId(R.styleable.SmsMessageHeaderTagTextViewAttr_customText , -1);
            if(stringResId>0) {
                String customText = context.getResources().getString(stringResId);
                if (customText != null) {
                    // We have a attribute value and set it to proper value as you want
                    strTagName = customText;
                }
            }
            a.recycle();
        }
    }

}
