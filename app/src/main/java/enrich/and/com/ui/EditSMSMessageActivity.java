package enrich.and.com.ui;

import android.content.ClipData;
import android.os.Bundle;
import android.text.Html;
import android.text.method.KeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.api.APIManager;
import enrich.and.com.api.EnrichAPIService;
import enrich.and.com.api.SettingResponse;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.SettingInfo;
import enrich.and.com.models.SmsMsgTestNowRequestBody;
import enrich.and.com.models.UserProfile;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.ui.customviews.SmsMessageHeaderTagTextView;
import enrich.and.com.utils.ViewIdGenerator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditSMSMessageActivity extends BaseActivity{

    @BindView(R.id.txtEditSmsMessageDescription)
    TextView txtEditSmsMessageDescription;

    @BindView(R.id.txtTestNowDescription)
    TextView txtTestNowDescription;

    @BindView(R.id.edtSmsMessage)
    EditText edtSmsMessage;

    @BindView(R.id.currentProfileBarView)
    CurrentProfileBarView currentProfileBarView;

    @BindView(R.id.btnEdit)
    Button btnEdit;

    private boolean isEditable = false;
    private int nSelectedProfileIndex = 0;
    private UserProfile profile;

    private KeyListener originalKeyListener;

    private EnrichAPIService apiService;
    /*
    @BindView(R.id.messageHeaderTagsLayout)
    LinearLayout messageHeaderTagsLayout;

    @BindView(R.id.txtFirstNameTag)
    SmsMessageHeaderTagTextView txtFirstNameTag;

    @BindView(R.id.txtLastNameTag)
    SmsMessageHeaderTagTextView txtLastNameTag;

    @BindView(R.id.txtCompanyTag)
    SmsMessageHeaderTagTextView txtCompanyTag;

    @BindView(R.id.linearDragDropTagLayout)
    LinearLayout linearDragDropTagLayout;

    @BindView(R.id.dropLayout1)
    LinearLayout dropLayout1;

    @BindView(R.id.dropLayout2)
    LinearLayout dropLayout2;

    @BindView(R.id.dropLayout3)
    LinearLayout dropLayout3;

    private SmsMessageHeaderTagTextView droppedTextView1 ,droppedTextView2 , droppedTextView3;
    private int droppedTextViewId1 , droppedTextViewId2 , droppedTextViewId3;

    private ArrayList<String> smsContentHeaderTagsArray = new ArrayList<String>();
*/


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_sms_message);
        ButterKnife.bind(this);

        //String htmlString = "<font color='#ee0000'>Variables</font> - These tokens are replaced at the time<br> of sending with the contacts data.";
        //txtEditSmsMessageDescription.setText(Html.fromHtml(htmlString));

        String htmlString2 = "Variable data for test: <font color='#ee0000'>First Name: Richie Last Name: Robot Company: enRICH</font>";

        txtTestNowDescription.setText(Html.fromHtml(htmlString2));

        originalKeyListener = edtSmsMessage.getKeyListener();

        if(!isEditable) {
            edtSmsMessage.setEnabled(isEditable);
            //set this null for the edit scrolling while its disabled
            edtSmsMessage.setKeyListener(null);
        }
        else {
            edtSmsMessage.setEnabled(isEditable);
            edtSmsMessage.setKeyListener(originalKeyListener);
        }

        nSelectedProfileIndex = MainApplication.getInstance().getCurrentProfileIndex();
        profile = MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex);

        currentProfileBarView.setProfile(profile);

        String base64EncodedSmsMessage = profile.getProfileSmsMessage();
        byte[] data = Base64.decode(base64EncodedSmsMessage, Base64.DEFAULT);
        String decodedText = "";
        try {
            decodedText = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        edtSmsMessage.setText(decodedText);

        edtSmsMessage.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        /*smsContentHeaderTagsArray.add(getResources().getString(R.string.str_firstname));
        smsContentHeaderTagsArray.add(getResources().getString(R.string.str_company));

        droppedTextViewId1 = ViewIdGenerator.generateViewId();
        droppedTextViewId2 = ViewIdGenerator.generateViewId();
        droppedTextViewId3 = ViewIdGenerator.generateViewId();

        droppedTextView1 = new SmsMessageHeaderTagTextView(EditSMSMessageActivity.this);
        droppedTextView1.setId(droppedTextViewId1);
        droppedTextView1.setOnTouchListener(myOnTouchListener);

        droppedTextView2 = new SmsMessageHeaderTagTextView(EditSMSMessageActivity.this);
        droppedTextView2.setId(droppedTextViewId2);
        droppedTextView2.setOnTouchListener(myOnTouchListener);


        droppedTextView3 = new SmsMessageHeaderTagTextView(EditSMSMessageActivity.this);
        droppedTextView3.setId(droppedTextViewId3);
        droppedTextView3.setOnTouchListener(myOnTouchListener);

        showAddedTagViewsInMessageContent();

        messageHeaderTagsLayout.setOnDragListener(myOnDragListener);
        dropLayout1.setOnDragListener(myOnDragListener);
        dropLayout2.setOnDragListener(myOnDragListener);
        dropLayout3.setOnDragListener(myOnDragListener);

        txtFirstNameTag.setOnTouchListener(myOnTouchListener);
        txtLastNameTag.setOnTouchListener(myOnTouchListener);
        txtCompanyTag.setOnTouchListener(myOnTouchListener);*/

    }

    @OnClick(R.id.btnSave)
    public void onClickButtonSave()
    {
        SettingInfo newSetting = new SettingInfo(MainApplication.getInstance().getCurrentSettingInfo());
        String smsMessage = edtSmsMessage.getText().toString();
        byte[] data = new byte[0];
        String base64EncodedString = "";
        try {
            data = smsMessage.getBytes("UTF-8");
            base64EncodedString = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        switch(nSelectedProfileIndex)
        {
            case 0:
                newSetting.setSmsmessagep1(base64EncodedString);
                break;
            case 1:
                newSetting.setSmsmessagep2(base64EncodedString);
                break;
            case 2:
                newSetting.setSmsmessagep3(base64EncodedString);
                break;
        }

        saveUserSettings(newSetting);

    }

    @OnClick(R.id.btnTestNow)
    public void onClickButtonTestNow()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.TESTNOW_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();

        if(apiService == null)
            apiService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("" , false , null);

        SmsMsgTestNowRequestBody reqBody = new SmsMsgTestNowRequestBody();
        reqBody.setUserid(String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId()));
        reqBody.setProfile(String.valueOf(profile.getProfileIndex()));


        Call<ResponseBody> result = apiService.sendSmsMsgTestNow(reqBody);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("------------"+response.body().string());
                    showErrorMessage("Success" , "You should received the test message within 5 minutes.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }

        });


    }

    @OnClick(R.id.btnEdit)
    public void onClickButtonEdit()
    {
        isEditable = !isEditable;
        if(isEditable) {
            edtSmsMessage.setEnabled(isEditable);
            edtSmsMessage.setKeyListener(originalKeyListener);
            btnEdit.setVisibility(View.GONE);
        }
        else {
            edtSmsMessage.setEnabled(isEditable);
            edtSmsMessage.setKeyListener(null);
            btnEdit.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnCancel)
    public void onClickButtonCancel()
    {
        if(isEditable)
        {
            isEditable = false;
            edtSmsMessage.setEnabled(isEditable);
            btnEdit.setVisibility(View.VISIBLE);
        }
        else {
            EditSMSMessageActivity.this.finish();
        }
    }

    /*private void showAddedTagViewsInMessageContent()
    {
        //first remove all tags from linearlayouts
        dropLayout1.removeAllViews();
        dropLayout2.removeAllViews();
        dropLayout3.removeAllViews();

        if(smsContentHeaderTagsArray.size()>0) {
            droppedTextView1.setHeaderTag(smsContentHeaderTagsArray.get(0));
            dropLayout1.addView(droppedTextView1);
        }
        if(smsContentHeaderTagsArray.size()>1) {
            droppedTextView2.setHeaderTag(smsContentHeaderTagsArray.get(1));
            dropLayout2.addView(droppedTextView2);
        }
        if(smsContentHeaderTagsArray.size()>2) {
            droppedTextView3.setHeaderTag(smsContentHeaderTagsArray.get(2));
            dropLayout3.addView(droppedTextView3);
        }
    }


    View.OnTouchListener myOnTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            {
                //Manually handle the event.
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //Remember the time and press position
                    Log.e("test","Action down");
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    //Check if user is actually longpressing, not slow-moving
                    // if current position differs much then press positon then discard whole thing
                    // If position change is minimal then after 0.5s that is a longpress. You can now process your other gestures
                    Log.e("test","Action move");
                }
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    //Get the time and position and check what that was :)
                    Log.e("test","Action down");
                }

            }
            return true;
        }
    };

    View.OnDragListener myOnDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {

            String area;
//            if(v == messageHeaderTagsLayout){
//                area = "area1";
//            }else if(v == smsMsgContentLayout){
//                area = "area2";
//            }else{
//                area = "unknown";
//            }
*/
            /*switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //System.out.println("ACTION_DRAG_STARTED: " + area);

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //System.out.println("ACTION_DRAG_ENTERED: " + area);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //System.out.println("ACTION_DRAG_EXITED: " + area);
                    break;
                case DragEvent.ACTION_DROP:
                    //System.out.println("ACTION_DROP: " + area);
                    View view = (View)event.getLocalState();

                    LinearLayout oldParent = (LinearLayout)view.getParent();
                    //drop out of message content to remove tag
                    if((v.getId() == R.id.messageHeaderTagsLayout )
                         && (oldParent.getId() == R.id.dropLayout1 || oldParent.getId() == R.id.dropLayout2 || oldParent.getId() == R.id.dropLayout3))
                    {
                        dropLayout1.removeAllViews();
                        dropLayout2.removeAllViews();
                        dropLayout3.removeAllViews();

                        SmsMessageHeaderTagTextView tagView = (SmsMessageHeaderTagTextView)view;
                        String strHeaderTag = tagView.getHeaderTag();
                        for (int i = 0; i < smsContentHeaderTagsArray.size(); i++) {
                            if(smsContentHeaderTagsArray.get(i).equalsIgnoreCase(strHeaderTag))
                            {
                                smsContentHeaderTagsArray.remove(i);
                                break;
                            }
                        }

                        showAddedTagViewsInMessageContent();
                    }
                    //add first tag
                    else if((v.getId() == R.id.dropLayout1))// && (oldParent.getId() == R.id.messageHeaderTagsLayout))
                    {
                        SmsMessageHeaderTagTextView tagView = (SmsMessageHeaderTagTextView)view;
                        if(smsContentHeaderTagsArray.size()<3)
                        {
                            smsContentHeaderTagsArray.add(tagView.getHeaderTag());
                        }
                        showAddedTagViewsInMessageContent();
                    }
                    else if((v.getId() == R.id.dropLayout2))// && (oldParent.getId() == R.id.messageHeaderTagsLayout))
                    {
                        SmsMessageHeaderTagTextView tagView = (SmsMessageHeaderTagTextView)view;
                        if(smsContentHeaderTagsArray.size()<3)
                        {
                            smsContentHeaderTagsArray.add(tagView.getHeaderTag());
                        }
                        showAddedTagViewsInMessageContent();
                    }
                    else if((v.getId() == R.id.dropLayout3))// && (oldParent.getId() == R.id.messageHeaderTagsLayout))
                    {
                        SmsMessageHeaderTagTextView tagView = (SmsMessageHeaderTagTextView)view;
                        if(smsContentHeaderTagsArray.size()<3)
                        {
                            smsContentHeaderTagsArray.add(tagView.getHeaderTag());
                        }
                        showAddedTagViewsInMessageContent();
                    }
                    //oldParent.removeView(view);
                    //LinearLayout newParent = (LinearLayout)v;
                    //linearDragDropTagLayout.addView(view);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //System.out.println("ACTION_DRAG_ENDED: " + area );
                default:
                    break;
            }
            return true;
        }

    };*/

    private void saveUserSettings(SettingInfo settingInfo)
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<SettingResponse>().createGsonConverter(SettingResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("" , false , null);

        Observable<SettingResponse> call = apiService.updateSettingInfo(settingInfo);
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
                            showErrorMessage("Error" , errorMsg , null);
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(SettingResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            hideProgressDialog();
                            showErrorMessage("Error" , "Failed to save settings." , null);
                            return;
                        }
                        SettingInfo setting = response.getData();
                        MainApplication.getInstance().setCurrentSettingInfo(setting);
                        hideProgressDialog();
                        EditSMSMessageActivity.this.finish();
                    }
                });
    }
}
