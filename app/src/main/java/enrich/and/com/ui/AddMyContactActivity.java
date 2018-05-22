package enrich.and.com.ui;


import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.api.APIManager;
import enrich.and.com.api.AddUpdateContactResponse;
import enrich.and.com.api.CardImageResponse;
import enrich.and.com.api.EnrichAPIService;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.interfaces.OnCurrentProfileBarCollapseListener;
import enrich.and.com.interfaces.OnProfileItemSelectedListener;
import enrich.and.com.models.CardImage;
import enrich.and.com.models.ContactInfo;
import enrich.and.com.models.ContactModel;
import enrich.and.com.ui.customviews.ContactInfoView;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.ui.customviews.ProfileSelectorItemView;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddMyContactActivity extends BaseActivity implements OnProfileItemSelectedListener ,
        OnCurrentProfileBarCollapseListener {

    @BindView(R.id.currentProfileBarView)
    CurrentProfileBarView currentProfileBarView;

    @BindView(R.id.profileListLayout)
    LinearLayout profileListLayout;

    @BindView(R.id.profileSelectorDivider)
    View profileSelectorDivider;

    @BindView(R.id.contactInfoView)
    ContactInfoView contactInfoView;

    ArrayList<ProfileSelectorItemView> profileItemsList = new ArrayList<>();

    private int nSelectedProfileIndex = 0;

    private EnrichAPIService apiService = null;

    private ContactInfo contactInfo;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_addcontact_screen);
        ButterKnife.bind(this);

        contactInfo = new ContactInfo();

        currentProfileBarView.setOnCurrentProfileBarCollapseListener(this);

        nSelectedProfileIndex = MainApplication.getInstance().getCurrentProfileIndex();
        currentProfileBarView.setProfile(MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex));

        profileItemsList.add(new ProfileSelectorItemView(this , 0 , MainApplication.getInstance().getProfileByIndex(0), this));
        profileItemsList.add(new ProfileSelectorItemView(this , 1, MainApplication.getInstance().getProfileByIndex(1) , this));
        profileItemsList.add(new ProfileSelectorItemView(this , 2, MainApplication.getInstance().getProfileByIndex(2) , this));

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
        if(currentProfileBarView.isCollapsed()) {
            profileListLayout.setVisibility(View.GONE);
            profileSelectorDivider.setVisibility(View.GONE);
        }
        else {
            profileListLayout.setVisibility(View.VISIBLE);
            profileSelectorDivider.setVisibility(View.VISIBLE);
        }

        contactInfoView.setViewValuesFromContactInfo(contactInfo);
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

    @Override
    public void onSelected(int index) {
        nSelectedProfileIndex = index;
        updateCheckboxes();
        currentProfileBarView.setCollapsed(true);
        onProfileListCollapsed(true);
        currentProfileBarView.setProfile(MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex));
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

    @OnClick(R.id.btnCancel)
    public void onBtnCancelClicked()
    {
        AddMyContactActivity.this.finish();
    }

    @OnClick(R.id.btnSubmit)
    public void onBtnSubmitClicked()
    {
        this.contactInfo = contactInfoView.getContactInfoFromViewValues();
        contactInfo.contact.setProfileId(String.valueOf(nSelectedProfileIndex+1));

        addUpdateContact(contactInfo);
    }

    private void addUpdateContact(ContactInfo contactInfo)
    {

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<AddUpdateContactResponse>().createGsonConverter(AddUpdateContactResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("" , false , null);
        Observable<AddUpdateContactResponse> call = apiService.addUpdateContact(contactInfo.contact);
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<AddUpdateContactResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // cast to retrofit.HttpException to get the response code
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException)e;
                            Response response = exception.response();
                            String errorMsg = response.message();
                            showErrorMessage("Error" , errorMsg , null);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(AddUpdateContactResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            hideProgressDialog();

                            return;
                        }

                        ContactModel contact = (ContactModel)response.getData();
                        hideProgressDialog();
                        AddMyContactActivity.this.finish();
                    }
                });
    }

}

