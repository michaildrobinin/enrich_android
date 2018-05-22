package enrich.and.com.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.ContactInfo;
import enrich.and.com.ui.AddMyContactActivity;
import enrich.and.com.ui.MainActivity;

public class HomeFragment extends BaseFragment {



    public HomeFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_home_screen, container, false);
            ButterKnife.bind(this, m_rootView);

            //check here for the last load settings

            updateUi();
        }

        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUi()
    {

    }


    @OnClick(R.id.bottomHelpLayout)
    public void onClickNeedHelp()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConstants.HELP_URL));
        startActivity(i);
    }

    @OnClick(R.id.btnMenu)
    public void onClickMenu()
    {
        getContainer().onSlidingMenuClicked();
    }

    @OnClick(R.id.btnSetting)
    public void onSettingsClick() {
        getApplication().getFragmentChanger().showSettingsFragment();
    }

    @OnClick(R.id.btnAddContact)
    public void onAddContactClick() {
        //getApplication().getFragmentChanger().showSettingsFragment();
        Intent intent = new Intent(getContainer() , AddMyContactActivity.class);
        getContainer().startActivity(intent);
    }

    @OnClick(R.id.btnMyContacts)
    public void onMyContactsClick() {
        getApplication().getFragmentChanger().showMyContactsFragment();
    }

    @OnClick(R.id.btnCardScanner)
    public void onCardScannerClick() {
        //getApplication().getFragmentChanger().showSettingsFragment();
        MainApplication.getInstance().setCurrentContactInfo(new ContactInfo());
        ((MainActivity)getContainer()).startCameraFragment(true);
    }

}
