package enrich.and.com.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.interfaces.OnCurrentProfileBarCollapseListener;
import enrich.and.com.models.UserProfile;
import enrich.and.com.utils.RoundedCornersTransform;


public class CurrentProfileBarView extends LinearLayout {

    private boolean isShowCollapseButton = false;

    private boolean isCollapsed= false;

    private OnCurrentProfileBarCollapseListener collapseListener;

    private ImageView imgCollapse;
    private TextView txtEnrichProfileTitle;
    private TextView txtCurrentProfileNumber;
    private ImageView imgCurrentProfilePhoto;

    private UserProfile profile;
    private Context mContext;

    public CurrentProfileBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs , R.styleable.CurrentProfileBarAttr);
        isShowCollapseButton = a.getBoolean(R.styleable.CurrentProfileBarAttr_showCollapseButton,
                true);
        isCollapsed = a.getBoolean(R.styleable.CurrentProfileBarAttr_isCollapsed , true);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_current_profile_bar, this, true);

        txtEnrichProfileTitle = (TextView)findViewById(R.id.txtEnrichProfileTitle);
        txtCurrentProfileNumber = (TextView)findViewById(R.id.txtCurrentProfileNumber);
        imgCurrentProfilePhoto = (ImageView)findViewById(R.id.imgCurrentProfilePhoto);

        imgCollapse = (ImageView)findViewById(R.id.imgCollapse);
        if(isShowCollapseButton)
            imgCollapse.setVisibility(View.VISIBLE);
        else
            imgCollapse.setVisibility(View.GONE);

        imgCollapse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isCollapsed = !isCollapsed;
                updateCollapseArrow();

                if(collapseListener != null)
                    collapseListener.onProfileListCollapsed(isCollapsed);
            }
        });

        updateCollapseArrow();
    }

    public CurrentProfileBarView(Context context) {
        this(context, null);
    }

    private void updateCollapseArrow()
    {
        if(isCollapsed)
            imgCollapse.setImageResource(R.drawable.icon_down_arrow);
        else
            imgCollapse.setImageResource(R.drawable.icon_up_arrow);
    }

    public void setCollapsed(boolean collapsed)
    {
        this.isCollapsed = collapsed;
    }
    public boolean isCollapsed(){return this.isCollapsed;}

    public void setOnCurrentProfileBarCollapseListener(OnCurrentProfileBarCollapseListener listener)
    {
        this.collapseListener = listener;
    }

    public void setProfile(UserProfile profile)
    {
        this.profile = profile;
        updateUI();
    }

    public void updateUI()
    {
        updateCollapseArrow();
        if(this.profile != null) {
            txtEnrichProfileTitle.setText(profile.getProfileName());
            txtCurrentProfileNumber.setText(String.valueOf(profile.getProfileIndex()));

            try {
                imgCurrentProfilePhoto.setImageBitmap(null);
                imgCurrentProfilePhoto.invalidate();

                int imgViewCornerRadius = (int) getResources().getDimensionPixelSize(R.dimen.profile_photo_imageview_corner_radius);

                if(!profile.getProfileSmsImagePath().equalsIgnoreCase(""))
                    Picasso.with(mContext).load(profile.getProfileSmsImagePath()).transform(new RoundedCornersTransform(imgViewCornerRadius,0)).placeholder(R.drawable.normal_user_icon).into(imgCurrentProfilePhoto);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
