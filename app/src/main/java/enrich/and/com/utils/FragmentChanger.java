package enrich.and.com.utils;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import enrich.and.com.ui.fragments.BaseFragment;

import enrich.and.com.R;
import enrich.and.com.ui.fragments.CardDetailsFragment;
import enrich.and.com.ui.fragments.CameraScreenFragment;
import enrich.and.com.ui.fragments.GalleryImageCropFragment;
import enrich.and.com.ui.fragments.HomeFragment;
import enrich.and.com.ui.fragments.MyContactsFragment;
import enrich.and.com.ui.fragments.ScannedCardsFragment;
import enrich.and.com.ui.fragments.SettingsFragment;
import enrich.and.com.ui.fragments.ViewFullCardImageFragment;

public class FragmentChanger {
    private static FragmentChanger m_instance = new FragmentChanger();
    private FragmentActivity m_currentActivity;
    private BaseFragment m_currentFragment;

    private FragmentChanger() {

    }

    public static FragmentChanger getInstance() {
        return m_instance;
    }

    public void setCurrentActivity(FragmentActivity activity) {
        this.m_currentActivity = activity;
    }

    private void setFragment(BaseFragment newFragment, int fragmentContainer, boolean addToBackStack) {

        if (m_currentFragment != newFragment) {
            FragmentTransaction transaction = m_currentActivity.getSupportFragmentManager().beginTransaction();
            String tag = newFragment.getClass().getName();
            transaction.replace(fragmentContainer, newFragment, tag);
            if (addToBackStack) {
                if (m_currentFragment != null)
                    transaction.addToBackStack(m_currentFragment.getClass().getName());
            }

            transaction.commit();
            m_currentFragment = newFragment;
        }
    }

    private void cleanStack() {
        int count = m_currentActivity.getSupportFragmentManager().getBackStackEntryCount();

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                m_currentActivity.getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }

    public void setFragment(BaseFragment newFragment, boolean addToBackStack) {
        setFragment(newFragment, R.id.main_content, addToBackStack);
    }

    public void showSettingsFragment() {

        SettingsFragment fragment = (SettingsFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(SettingsFragment.class.getName());
        if (fragment == null)
            fragment = new SettingsFragment();

        setFragment(fragment, true);
    }

    public void showMyContactsFragment() {

        MyContactsFragment fragment = (MyContactsFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(MyContactsFragment.class.getName());
        if (fragment == null)
            fragment = new MyContactsFragment();

        setFragment(fragment, true);
    }

    public void showCameraFragment(boolean isFrontCard) {

        CameraScreenFragment fragment = (CameraScreenFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(CameraScreenFragment.class.getName());
        if (fragment == null)
            fragment = new CameraScreenFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isFrontCard" , isFrontCard);
        fragment.setMyArguments(bundle);

        setFragment(fragment, true);
    }

    public void showViewFullImageCardFragment(boolean isFrontCard , String photoPath) {

        ViewFullCardImageFragment fragment = (ViewFullCardImageFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(ViewFullCardImageFragment.class.getName());
        if (fragment == null)
            fragment = new ViewFullCardImageFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isFrontCard" , isFrontCard);
        bundle.putString("photoPath" , photoPath);
        fragment.setMyArguments(bundle);

        setFragment(fragment, true);
    }

    public void showCardDetailsFragment() {

        CardDetailsFragment fragment = (CardDetailsFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(CardDetailsFragment.class.getName());
        if (fragment == null)
            fragment = new CardDetailsFragment();

        setFragment(fragment, true);
    }

    public void showScannedCardsFragment() {

        ScannedCardsFragment fragment = (ScannedCardsFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(ScannedCardsFragment.class.getName());
        if (fragment == null)
            fragment = new ScannedCardsFragment();



        setFragment(fragment, true);
    }

    public void showGalleryImageCropFragment(String imgPath , boolean isFrontCard) {

        GalleryImageCropFragment fragment = (GalleryImageCropFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(GalleryImageCropFragment.class.getName());
        if (fragment == null)
            fragment = new GalleryImageCropFragment();

        Bundle bundle = new Bundle();
        bundle.putString("imagepath" , imgPath);
        bundle.putBoolean("isFrontCard" , isFrontCard);

        fragment.setArguments(bundle);

        setFragment(fragment, true);
    }
/*
    public void showVideoFragment() {
        VideoFragment fragment = (VideoFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(VideoFragment.class.getName());

        if (fragment == null)
            fragment = new VideoFragment();

        setFragment(fragment, true);
    }*/

    public void showHomeFragment() {

        if (m_currentFragment != null &&
                !m_currentFragment.getClass().getSimpleName().equalsIgnoreCase(HomeFragment.class.getName())) {
            cleanStack();
        }

        HomeFragment fragment = (HomeFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());

        if (fragment == null)
            fragment = new HomeFragment();

        setFragment(fragment, false);
    }




    public void updateCurrentFragmentFromBackStack() {
        if (m_currentActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            int lastCountIndex = m_currentActivity.getSupportFragmentManager().getBackStackEntryCount() - 1;
            String fragmentTag = m_currentActivity.getSupportFragmentManager().getBackStackEntryAt(lastCountIndex).getName();
            m_currentFragment = (BaseFragment) m_currentActivity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
        }
    }

    public BaseFragment getCurrentFragment() {
        return m_currentFragment;
    }


}
