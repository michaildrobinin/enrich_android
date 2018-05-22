package enrich.and.com.ui.fragments;

 import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

 import java.security.MessageDigest;
 import java.security.NoSuchAlgorithmException;
 import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.AppConstants;
 import enrich.and.com.app.MainApplication;
 import enrich.and.com.interfaces.OnCurrentProfileBarCollapseListener;
import enrich.and.com.interfaces.OnProfileItemSelectedListener;
import enrich.and.com.ui.EditMagicVoicemailActivity;
import enrich.and.com.ui.EditSMSImageActivity;
import enrich.and.com.ui.EditSMSMessageActivity;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.ui.customviews.ProfileSelectorItemView;
 import enrich.and.com.utils.MD5Digest;

public class SettingsFragment extends BaseFragment implements OnCurrentProfileBarCollapseListener ,OnProfileItemSelectedListener {

    @BindView(R.id.txtEnrichWebsite)
    TextView txtGotoEnrichWebsite;

    @BindView(R.id.currentProfileBarView)
    CurrentProfileBarView currentProfileBarView;

    @BindView(R.id.profileListLayout)
    LinearLayout profileListLayout;

    @BindView(R.id.profileSelectorDivider)
    View profileSelectorDivider;

    ArrayList<ProfileSelectorItemView> profileItemsList = new ArrayList<>();

    private int nSelectedProfileIndex = 0;

    public SettingsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_settings_screen, container, false);
            ButterKnife.bind(this, m_rootView);

            currentProfileBarView.setOnCurrentProfileBarCollapseListener(this);

            profileItemsList = new ArrayList<ProfileSelectorItemView>();


        }



        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        nSelectedProfileIndex = MainApplication.getInstance().getCurrentProfileIndex();
        currentProfileBarView.setProfile(MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex));

        profileItemsList.clear();

        profileItemsList.add(new ProfileSelectorItemView(getContainer() , 0 , MainApplication.getInstance().getProfileByIndex(0), this));
        profileItemsList.add(new ProfileSelectorItemView(getContainer() , 1, MainApplication.getInstance().getProfileByIndex(1) , this));
        profileItemsList.add(new ProfileSelectorItemView(getContainer() , 2, MainApplication.getInstance().getProfileByIndex(2) , this));


        profileListLayout.removeAllViews();

        for(int i=0;i<profileItemsList.size(); i++)
        {
            profileItemsList.get(i).showViewValues();
            profileListLayout.addView(profileItemsList.get(i));
        }

        updateUi();

        updateCheckboxes();
    }

    private void updateUi()
    {
        String link = AppConstants.ENRICH_WEBSITE_LINk+String.valueOf(nSelectedProfileIndex+1)+".php?d="+
                MainApplication.getInstance().getUserIdHash();

        Spanned html = Html.fromHtml("Manage all your settings on the enRICH " +
                "<a href='"+link+"'>website</a>");

        txtGotoEnrichWebsite.setMovementMethod(LinkMovementMethod.getInstance());

        // Set TextView text from html
        txtGotoEnrichWebsite.setText(html);

        if(currentProfileBarView.isCollapsed()) {
            profileListLayout.setVisibility(View.GONE);
            profileSelectorDivider.setVisibility(View.GONE);
        }
        else {
            profileListLayout.setVisibility(View.VISIBLE);
            profileSelectorDivider.setVisibility(View.VISIBLE);
        }
    }

    private void updateCheckboxes()
    {
        for(int i = 0;i < profileItemsList.size(); i++)
        {
            if(nSelectedProfileIndex == i)
                profileItemsList.get(i).updateCheckBox(true);
            else
                profileItemsList.get(i).updateCheckBox(false);
        }
    }

    @OnClick(R.id.btnMenu)
    public void onClickMenu()
    {
        getContainer().onSlidingMenuClicked();
    }

    @OnClick(R.id.editSmsImageLayout)
    public void onEditSmsImageClick()    {
        Intent intent = new Intent(getContainer() , EditSMSImageActivity.class);
        getContainer().startActivity(intent);
    }

    @OnClick(R.id.editSmsMessageLayout)
    public void onEditSmsMessageClick(){
        Intent intent = new Intent(getContainer() , EditSMSMessageActivity.class);
        getContainer().startActivity(intent);
    }

    @OnClick(R.id.editMagicVoiceMail)
    public void onEditMagicVoicemailClick()    {
        Intent intent = new Intent(getContainer() , EditMagicVoicemailActivity.class);
        getContainer().startActivity(intent);
    }

    @Override
    public void onProfileListCollapsed(boolean isCollapsed) {
        if(isCollapsed) {
            profileListLayout.setVisibility(View.GONE);
            profileSelectorDivider.setVisibility(View.GONE);
        }
        else {
            profileListLayout.setVisibility(View.VISIBLE);
            profileSelectorDivider.setVisibility(View.VISIBLE);
        }
    }

    //Profile Selected
    @Override
    public void onSelected(int index) {
        nSelectedProfileIndex = index;
        MainApplication.getInstance().setCurrentProfileIndex(index);
        updateCheckboxes();
        currentProfileBarView.setCollapsed(true);
        onProfileListCollapsed(true);
        currentProfileBarView.setProfile(MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex));
    }
}
