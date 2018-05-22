package enrich.and.com.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.api.APIManager;
import enrich.and.com.api.AddUpdateContactResponse;
import enrich.and.com.api.EnrichAPIService;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.ContactInfo;
import enrich.and.com.models.ContactModel;
import enrich.and.com.ui.customviews.ContactInfoView;
import enrich.and.com.utils.util;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditCardDetailsActivity extends BaseActivity {

    @BindView(R.id.contactInfoView)
    ContactInfoView contactInfoView;

    @BindView(R.id.imgViewHeaderCard)
    ImageView imgViewHeaderCard;

    @BindView(R.id.txtContactName)
    TextView txtContactName;

    @BindView(R.id.txtHeaderTitle)
    TextView txtHeaderTitle;

    @BindView(R.id.txtHeaderCompany)
    TextView txtHeaderCompany;


    private ContactInfo contactInfo;
    private EnrichAPIService apiService;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_editcard_details_screen);
        ButterKnife.bind(this);


        contactInfo = MainApplication.getInstance().getCurrentContactInfo();
        if(contactInfo == null)
            contactInfo = new ContactInfo();

        contactInfoView.setViewValuesFromContactInfo(contactInfo);


        String strCardPath = this.contactInfo.getFrontCardImagePath();
        if(strCardPath.equalsIgnoreCase(""))
            strCardPath = this.contactInfo.getBackCardImagePath();

        util.showCardImageViewWithPicasso(this , strCardPath , imgViewHeaderCard);


        txtContactName.setText(contactInfo.getFullName());
        txtHeaderTitle.setText(contactInfo.contact.getTitle());
        txtHeaderCompany.setText(contactInfo.contact.getCompany());
    }

    @OnClick(R.id.btnCancel)
    public void onCancelClicked()
    {
        finish();
    }

    @OnClick(R.id.btnSave)
    public void onSaveClicked()
    {
        contactInfoView.getContactInfoFromViewValues();

        addUpdateContact();

        //MainApplication.getInstance().setCurrentContactInfo(contactInfo);
        //finish();
    }
    private void addUpdateContact()
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
                        showErrorMessage("Error", "Failed to update contact" , null);
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
                        contactInfo.contact = contact;
                        MainApplication.getInstance().setCurrentContactInfo(contactInfo);
                        finish();
                    }
                });
    }
}
