package enrich.and.com.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.ContactInfo;
import enrich.and.com.models.Product;
import enrich.and.com.ui.fragments.BaseFragment;
import enrich.and.com.ui.fragments.ScannedCardsFragment;
import enrich.and.com.utils.RoundedCornersTransform;

public class MainActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.main_content)
    FrameLayout mainContentLayout;

    @BindView(R.id.slidingmenulayout)
    LinearLayout slidingMenuLayout;

    @BindView(R.id.txtAffliateLink)
    TextView txtAffliateLink;

    @BindView(R.id.imgUserPhoto)
    ImageView imgUserPhoto;

    @BindView(R.id.txtCardRemaining)
    TextView txtCardRemaining;

    @BindView(R.id.txtMembershipLevel)
    TextView txtMembershipLevel;

    @BindView(R.id.txtEmailAddress)
    TextView txtEmailAddress;

    private boolean mSlideState = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        drawerLayout.closeDrawer(slidingMenuLayout);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mSlideState = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mSlideState = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        String strAffliateLink = AppConstants.AFFLIATE_LINK+ String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId());
        txtAffliateLink.setText(
                Html.fromHtml(
                        "<a href=\""+strAffliateLink+"\">"+strAffliateLink+"</a> "));
        txtAffliateLink.setMovementMethod(LinkMovementMethod.getInstance());

        m_Application.getFragmentChanger().showHomeFragment();

        int imgViewCornerRadius = (int) getResources().getDimensionPixelSize(R.dimen.user_photo_imageview_corner_radius);

        String userPhoto = MainApplication.getInstance().getCurrentSettingInfo().getProfilePhoto();
        if(userPhoto == null || userPhoto.equalsIgnoreCase(""))
        {
            //imgUserPhoto.setImageResource(R.drawable.normal_user_icon);
            Picasso.with(MainActivity.this).load(R.drawable.normal_user_icon).transform(new RoundedCornersTransform(imgViewCornerRadius,0)).placeholder(R.drawable.normal_user_icon).into(imgUserPhoto);
        }
        else
        {
            Picasso.with(MainActivity.this).load(userPhoto).transform(new RoundedCornersTransform(imgViewCornerRadius,0)).placeholder(R.drawable.normal_user_icon).into(imgUserPhoto);
        }

        txtEmailAddress.setText(MainApplication.getInstance().getCurrentUserInfo().getEmail());
        txtCardRemaining.setText(MainApplication.getInstance().getCurrentUserInfo().getCreditsAvailable()+ " Cards\nRemaining");

        List<Product> productList = MainApplication.getInstance().getProductsList();
        int product_id = 74;
        for(int i =0 ;i<productList.size(); i++)
        {
            if(productList.get(i).getProductId() > product_id)//get max product id
                product_id = productList.get(i).getProductId();
        }
        String strMembership = "";
        switch (product_id)
        {
            case 74:
                strMembership = "FREELOADER";
                break;
            case 75:
                strMembership = "BASIC";
                break;
            case 76:
                strMembership = "PREMIUM";
                break;
            case 77:
                strMembership = "ULTIMATE";
                break;
            case 79:
                strMembership = "ENRICHVIP";
                break;
            default:
                strMembership = "FREELOADER";
                break;
        }
        txtMembershipLevel.setText(getResources().getString(R.string.str_membership_level_freeloader)+"\n"+strMembership);

    }

    @Override
    public void onSlidingMenuClicked() {
        super.onSlidingMenuClicked();
        if(mSlideState == false)
            drawerLayout.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
        //else
        //    drawerLayout.closeDrawer(drawerLayout);
    }

    @OnClick(R.id.btnGetMoreCards)
    public void onBtnGetMoreCard()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConstants.GET_MORE_CARDS));
        startActivity(i);
    }

    @OnClick(R.id.btnUpgrade)
    public void onBtnUpgrade()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConstants.UPGARDE));
        startActivity(i);
    }

    @OnClick(R.id.txtSupport)
    public void onClickTextSupport()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConstants.SUPPORT));
        startActivity(i);
    }

    public void startCameraFragment(boolean isFrontCard)
    {
        MainApplication.getInstance().getFragmentChanger().showCameraFragment(isFrontCard);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
