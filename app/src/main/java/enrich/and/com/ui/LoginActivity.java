package enrich.and.com.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.api.APIManager;
import enrich.and.com.api.EnrichAPIService;
import enrich.and.com.api.SettingResponse;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.api.LoginResponse;
import enrich.and.com.database.MySharedPreferenceManager;
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


public class LoginActivity extends BaseActivity{

    @BindView(R.id.btnEnter)
    Button btnEnter;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    EnrichAPIService apiService;

    private String strSavedEmail = "";
    private String strSavedPassword = "";
    private long savedTime = 0;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login_screen);

        ButterKnife.bind(this);

          edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // If triggered by an enter key, this is the event; otherwise, this is null.
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Do whatever you want here
                    login();
                    return true;
                }
                return false;
            }
        });

        MySharedPreferenceManager sf = new MySharedPreferenceManager(this);
        savedTime = sf.getValueLong("signedTime" , 0);
        if(System.currentTimeMillis() - savedTime > 1000*60*60*24*7)//if expired a week
        {
            strSavedEmail = "";
            strSavedPassword = "";
            sf.setValueString("email" , "");
            sf.setValueString("password" , "");
        }
        else
        {
            strSavedEmail = sf.getValueString("email" , "");
            strSavedPassword = sf.getValueString("password" , "");
        }

        edtEmail.setText(strSavedEmail);
        edtPassword.setText(strSavedPassword);
    }

    @OnClick(R.id.btnEnter)
    public void onButtonEnter()
    {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();

        if(validateInputs())
            login();

    }

    private void login()
    {
        final String strEmail = edtEmail.getText().toString().trim();
        final String strPassword = edtPassword.getText().toString().trim();

        //final String strEmail = "enrich.robots@gmail.com";
        //String strPassword = "RichieRobot";

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<LoginResponse>().createGsonConverter(LoginResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("" , false , null);
        Observable<LoginResponse> call = apiService.login(strEmail , strPassword);
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginResponse>() {
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

                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(LoginResponse response) {

                        if(response.getStatusCode() != 200)
                        {
                            hideProgressDialog();
                            Intent resetPasswordIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                            resetPasswordIntent.putExtra("email" , strEmail);
                            resetPasswordIntent.putExtra("login_error_msg" , response.getMsg());
                            startActivity(resetPasswordIntent);
                            return;
                        }

                        //if new email/password login , then save account info
                        if(!strEmail.equalsIgnoreCase(strSavedEmail) || !strPassword.equalsIgnoreCase(strSavedPassword))
                        {
                            MySharedPreferenceManager sf = new MySharedPreferenceManager(LoginActivity.this);
                            sf.setValueString("email" , strEmail);
                            sf.setValueString("password" , strPassword);
                            sf.setValueLong("signedTime" , System.currentTimeMillis());
                        }
                        MainApplication.getInstance().setUserIdHash(response.getUseridHash());
                        MainApplication.getInstance().setProductsList(response.getProducts());
                        MainApplication.getInstance().setCurrentUserInfo(response.getData());
                        getSettingInfo();

                    }
                });
    }

    private void getSettingInfo()
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<SettingResponse>().createGsonConverter(SettingResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        String uid = String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId());

        Observable<SettingResponse> call = apiService.getSettingInfo(uid);
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SettingResponse>() {
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

                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(SettingResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            hideProgressDialog();
                            LoginActivity.this.showErrorMessage("Failed to Login" , response.getMsg());

                            return;
                        }
                        MainApplication.getInstance().setCurrentSettingInfo(response.getData());
                        Intent homeIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        hideProgressDialog();
                        finish();
                    }
                });
    }


    private boolean validateInputs()
    {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        if(strEmail.equalsIgnoreCase(""))
        {
            showToast(getResources().getString(R.string.str_alert_msg_enter_email) , Toast.LENGTH_LONG);
            return false;
        }
        if(strPassword.equalsIgnoreCase(""))
        {
            showToast(getResources().getString(R.string.str_alert_msg_enter_password) , Toast.LENGTH_LONG);
            return false;
        }

        if(!util.isEmailValid(strEmail))
        {
            showToast(getResources().getString(R.string.str_alert_msg_invalid_email) , Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

}
