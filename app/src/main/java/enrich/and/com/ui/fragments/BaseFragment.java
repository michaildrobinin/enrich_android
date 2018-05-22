package enrich.and.com.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import enrich.and.com.app.MainApplication;
import enrich.and.com.ui.BaseActivity;

public abstract class BaseFragment extends Fragment {
    private MainApplication m_Application;
    protected View m_rootView = null;


    private BaseActivity m_Activity;

    protected BaseActivity getContainer() {
        return m_Activity;
    }

    private Bundle argument;

    public void setMyArguments(Bundle arg)
    {
        this.argument = arg;
    }
    public Bundle getMyArguments()
    {
        return this.argument;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            m_Activity = (BaseActivity) context;
        }
    }

    protected MainApplication getApplication() {
        if (m_Application == null) {
            initApplication();
        }
        return m_Application;
    }

    @Override
    public void onViewCreated(View v, Bundle bundle) {
        initViews();
    }

    private void initViews() {

    }

    private void initApplication() {
        if (m_Activity != null)
            m_Application = (MainApplication) m_Activity.getApplication();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public boolean onBackPressed() {

        if (getActivity() != null && getActivity().getSupportFragmentManager() != null && getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
            getApplication().getFragmentChanger().updateCurrentFragmentFromBackStack();
            return true;
        } else {
            return false;
        }
    }
}
