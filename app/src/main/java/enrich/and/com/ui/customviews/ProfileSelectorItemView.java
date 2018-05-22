package enrich.and.com.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import enrich.and.com.R;
import enrich.and.com.interfaces.OnProfileItemSelectedListener;
import enrich.and.com.models.UserProfile;
import enrich.and.com.utils.RoundedCornersTransform;


public class ProfileSelectorItemView extends LinearLayout {

    private boolean isShowCollapseButton = false;
    private OnProfileItemSelectedListener profileSelectedListener;

    private CheckBox checkBox;
    private TextView txtProfileNumber , txtProfileNumberDescription , txtProfileName;
    private ImageView imgProfilePhoto;

    private UserProfile profile;
    private Context mContext;

    private int viewIndex = 0;



    public ProfileSelectorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_profile_selector_itemview, this, true);

        txtProfileNumber = (TextView)findViewById(R.id.txtProfileNumber);
        txtProfileNumberDescription = (TextView)findViewById(R.id.txtProfileNumberDescription);
        txtProfileName = (TextView)findViewById(R.id.txtProfileName);
        imgProfilePhoto = (ImageView)findViewById(R.id.imgProfilePhoto);


        checkBox = (CheckBox)findViewById(R.id.profileCheckbox);
        checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileSelectedListener != null)
                {
                    profileSelectedListener.onSelected(viewIndex);
                }
            }
        });

     }

    public ProfileSelectorItemView(Context context) {
        this(context, null);
    }

    public ProfileSelectorItemView(Context context, int viewIndex , UserProfile profile, OnProfileItemSelectedListener listener) {

        this(context, null);
        mContext = context;
        this.profile = profile;
        this.viewIndex = viewIndex;
        this.profileSelectedListener = listener;
    }

    public void showViewValues()
    {
        txtProfileNumber.setText(String.valueOf(profile.getProfileIndex()));
        txtProfileNumberDescription.setText("Profile "  + String.valueOf(profile.getProfileIndex()));
        txtProfileName.setText(profile.getProfileName());
        int imgViewCornerRadius = (int) getResources().getDimensionPixelSize(R.dimen.profile_photo_imageview_corner_radius);

        if(!profile.getProfileSmsImagePath().equalsIgnoreCase(""))
            Picasso.with(mContext).load(profile.getProfileSmsImagePath()).transform(new RoundedCornersTransform(imgViewCornerRadius,0)).placeholder(R.drawable.normal_user_icon).into(imgProfilePhoto);
    }
    public void updateCheckBox(boolean isChecked)
    {
        checkBox.setChecked(isChecked);
    }
}
