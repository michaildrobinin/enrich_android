package enrich.and.com.ui;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.api.APIManager;
import enrich.and.com.api.EnrichAPIService;
import enrich.and.com.api.FileUploadService;
import enrich.and.com.api.LoginResponse;
import enrich.and.com.api.SettingResponse;
import enrich.and.com.app.AWSConstants;
import enrich.and.com.app.AWSUtil;
import enrich.and.com.app.AppConstants;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.SettingInfo;
import enrich.and.com.models.UserProfile;
import enrich.and.com.ui.customviews.CurrentProfileBarView;
import enrich.and.com.utils.WavAudioRecorder;
import enrich.and.com.utils.util;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Part;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditMagicVoicemailActivity extends BaseActivity {

    @BindView(R.id.currentProfileBarView)
    CurrentProfileBarView currentProfileBarView;

    @BindView(R.id.playSeekBar)
    SeekBar playSeekbar;

    @BindView(R.id.recordSeekBar)
    SeekBar recordSeekbar;

    @BindView(R.id.btnPlayCurrentRecord)
    ImageView btnPlayCurrentRecord;

    @BindView(R.id.btnPauseRecord)
    ImageView btnPauseRecord;

    @BindView(R.id.btnPlayNewRecord)
    ImageView btnPlayNewRecord;

    @BindView(R.id.txtStartStopRecord)
    TextView txtStartStopRecord;


    private boolean isRecording = false;
    private String strRecordingFilePath = "";
    private String strRecordingFileName = "";

    private int nSelectedProfileIndex = 0;
    private UserProfile profile;

    private String strVoiceMailPath = "";
    private String strVoiceMailFileName = "";
    private boolean isPlaying = false;

    private WavAudioRecorder wavRecorder;

    private Handler seekHandler = new Handler();
    private Handler mRecordHandler = new Handler();

    private long startRecordingTime = 0;

    private MediaPlayer currentPlayer , newRecordPlayer;

    private final String TAG = "Audio File";

    private final int MY_PERMISSION_REQUEST_RECORD_AUDIO = 10;

    private String uploadedVoiceFileUrl = "";

    //----------- Amazon S3 Upload--------------------------------------//
    // The TransferUtility is the primary class for managing transfer to S3
    private TransferUtility transferUtility;

    // A List of all transfers
    private List<TransferObserver> observers;

    private EnrichAPIService apiService;

    /**
     * This map is used to provide data to the SimpleAdapter above. See the
     * fillMap() function for how it relates observers to rows in the displayed
     * activity.
     */
    //private ArrayList<HashMap<String, Object>> transferRecordMaps;
    //-------------------------------------------------------------------//

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_magic_voicemail);
        ButterKnife.bind(this);

        // Initializes TransferUtility, always do this before using it.
        transferUtility = AWSUtil.getTransferUtility(this);
        //transferRecordMaps = new ArrayList<HashMap<String, Object>>();

        nSelectedProfileIndex = MainApplication.getInstance().getCurrentProfileIndex();
        profile = MainApplication.getInstance().getProfileByIndex(nSelectedProfileIndex);

        currentProfileBarView.setProfile(profile);

        strVoiceMailFileName = profile.getProfileVoiceMailPath();
        if(strVoiceMailFileName.startsWith("http"))
            strVoiceMailFileName = "";
        strVoiceMailPath = AppConstants.UPLOAD_API_BASE_URL+"/"+AppConstants.StratisticsNetworkAccountId+"/"+strVoiceMailFileName;

        //check the wav file is downloaded or not
        if(!strVoiceMailFileName.equalsIgnoreCase(""))
        {
            if(!util.isAudioFileExist(strVoiceMailFileName))
            {
                downloadAudioFile();
            }
            else
            {
                String fullFilePath = util.getAppUsingFilePath(util.AUDIO_FOLDER_NAME , strVoiceMailFileName);
                if(!fullFilePath.equalsIgnoreCase(""))
                    currentPlayer = loadAudioFile(currentPlayer ,fullFilePath);
            }
        }

        btnPlayNewRecord.setVisibility(View.GONE);

        wavRecorder = WavAudioRecorder.getInstanse();
        recordSeekbar.setProgress(0);
        recordSeekbar.setMax(23*1000);

        playSeekbar.setProgress(0);

        playSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(currentPlayer != null)
                {
                    {
                        currentPlayer.seekTo(seekBar.getProgress());
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (observers != null && !observers.isEmpty()) {
            for (TransferObserver observer : observers) {
                observer.cleanTransferListener();
            }
        }
    }
    TransferListener transferListener = new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {

            if(state.toString().compareTo("COMPLETED") == 0)
            {
                System.out.println("--------"+state.toString());
                uploadedVoiceFileUrl = AppConstants.API_AUDIO_FILE_BASE_URL+strRecordingFileName;
                System.out.println("--------"+uploadedVoiceFileUrl);
                hideProgressDialog();
            }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            System.out.println("--------"+bytesCurrent + "/"+bytesTotal);

        }

        @Override
        public void onError(int id, Exception ex) {
            System.out.println("--------Error ="+ex.toString());
            hideProgressDialog();
        }
    };
    private void initData() {
        //transferRecordMaps.clear();
        // Use TransferUtility to get all upload transfers.
        /*observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);

        for (TransferObserver observer : observers) {

            // For each transfer we will will create an entry in
            // transferRecordMaps which will display
            // as a single row in the UI
            HashMap<String, Object> map = new HashMap<String, Object>();
            AWSUtil.fillMap(map, observer, false);
            transferRecordMaps.add(map);

            // Sets listeners to in progress transfers
            if (TransferState.WAITING.equals(observer.getState())
                    || TransferState.WAITING_FOR_NETWORK.equals(observer.getState())
                    || TransferState.IN_PROGRESS.equals(observer.getState())) {
                observer.setTransferListener(transferListener);
            }
        }*/
    }

    private MediaPlayer loadAudioFile(MediaPlayer player, String filePath)
    {
        if(player == null) {
            player = MediaPlayer.create(this, Uri.parse(filePath));
        }
        else
        {
            try
            {
                if(player.isPlaying())
                    player.stop();
                player.release();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                player = MediaPlayer.create(this, Uri.parse(filePath));;
            }
        }

        int max = player.getDuration();
        playSeekbar.setMax(max);
        playSeekbar.setProgress(0);

        return player;
    }

    Runnable playAudioThread = new Runnable() {
        @Override
        public void run() {
            seekPlayUpdation();
        }
    };

    public void seekPlayUpdation() {
        if(currentPlayer == null) return;
        playSeekbar.setProgress(currentPlayer.getCurrentPosition());
        if(!currentPlayer.isPlaying())
            return;
        seekHandler.postDelayed(playAudioThread, 1000);
    }

    Runnable playNewAudioThread = new Runnable() {
        @Override
        public void run() {
            seekNewRecordPlayUpdation();
        }
    };

    public void seekNewRecordPlayUpdation() {
        if(newRecordPlayer == null) return;
        recordSeekbar.setProgress(newRecordPlayer.getCurrentPosition());
        if(!newRecordPlayer.isPlaying())
            return;
        mRecordHandler.postDelayed(playNewAudioThread, 1000);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(currentPlayer!=null && currentPlayer.isPlaying())
        {
            try
            {
                currentPlayer.stop();
                currentPlayer.release();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                currentPlayer = null;
            }
        }

        if(newRecordPlayer!=null && newRecordPlayer.isPlaying())
        {
            try
            {
                newRecordPlayer.stop();
                newRecordPlayer.release();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                newRecordPlayer = null;
            }
        }

        if (null != wavRecorder) {
            wavRecorder.release();
        }
    }

    @OnClick(R.id.btnPlayNewRecord)
    public void onClickButtonPlayNewRecord()
    {
        if(strRecordingFilePath.equalsIgnoreCase(""))
            return;
        File recordedFile = new File(strRecordingFilePath);
        if(!recordedFile.exists())
        {
            return;
        }
        newRecordPlayer = loadAudioFile(newRecordPlayer , strRecordingFilePath);
        if(newRecordPlayer != null)
        {
            newRecordPlayer.start();
            seekNewRecordPlayUpdation();
        }
    }

    @OnClick(R.id.btnPlayCurrentRecord)
    public void onClickButtonPlayCurrentRecord()
    {
        if(currentPlayer != null)
        {
            currentPlayer.start();
            seekPlayUpdation();
        }
    }

    @OnClick(R.id.btnPauseRecord)
    public void onClickPause()
    {
        if(currentPlayer != null && currentPlayer.isPlaying())
        {
            currentPlayer.pause();
        }
    }

    Runnable recordUpdateThread = new Runnable() {
        @Override
        public void run() {
            recordSeekBarUpdate();
        }
    };

    public synchronized void recordSeekBarUpdate() {
        if(wavRecorder == null) return;
        long elapsedTime = System.currentTimeMillis() - startRecordingTime;
        //if recorded time is over the limited time, then stop recording
        if(elapsedTime>recordSeekbar.getMax() && wavRecorder.getState() == WavAudioRecorder.State.RECORDING) {
            //force stop recording
            wavRecorder.stop();
            wavRecorder.reset();
            txtStartStopRecord.setText(getResources().getString(R.string.str_start_recording));
            recordSeekbar.setProgress(recordSeekbar.getMax());
            btnPlayNewRecord.setVisibility(View.VISIBLE);
            System.out.println("------Record Time is Over--------");
            return;
        }
        else if (WavAudioRecorder.State.ERROR == wavRecorder.getState()) {
            wavRecorder.release();
            wavRecorder = WavAudioRecorder.getInstanse();
            wavRecorder.setOutputFile(strRecordingFilePath);
            txtStartStopRecord.setText(getResources().getString(R.string.str_start_recording));
            recordSeekbar.setProgress(0);
            System.out.println("------Record Error , initiate--------");
            return;
        }

        if( wavRecorder.getState() == WavAudioRecorder.State.RECORDING) {
            recordSeekbar.setProgress((int) elapsedTime);
            System.out.println("------Record Progress = " + elapsedTime + "--------");
            mRecordHandler.postDelayed(recordUpdateThread, 1000);
        }
    }

    @OnClick(R.id.txtStartStopRecord)
    public void onClickStartStopButton()
    {
        int recordPermissionCheck = ContextCompat.checkSelfPermission(EditMagicVoicemailActivity.this,
                Manifest.permission.RECORD_AUDIO);
        if(recordPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("SPLASH_SCREEN" , "Record Audio Permission denied");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditMagicVoicemailActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(EditMagicVoicemailActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSION_REQUEST_RECORD_AUDIO);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(EditMagicVoicemailActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSION_REQUEST_RECORD_AUDIO);

            }
            return;
        }


        if (WavAudioRecorder.State.INITIALIZING == wavRecorder.getState()) {
            stopPlayingNewRecord();
            btnPlayNewRecord.setVisibility(View.GONE);

            SimpleDateFormat fmtOut = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
            String date = fmtOut.format(Calendar.getInstance().getTime());
            strRecordingFileName = "enRICH"+date+"_id"+MainApplication.getInstance().getCurrentUserInfo().getId()+"_p"+profile.getProfileIndex()+".wav";
            strRecordingFilePath = util.getVoiceRecordAudioFilePath(strRecordingFileName);

            wavRecorder.setOutputFile(strRecordingFilePath);
            wavRecorder.prepare();
            wavRecorder.start();
            startRecordingTime = System.currentTimeMillis();
            recordSeekbar.setProgress(0);
            txtStartStopRecord.setText(getResources().getString(R.string.str_stop_recording));
            System.out.println("------Record Started--------");
            recordSeekBarUpdate();

        } else if (WavAudioRecorder.State.ERROR == wavRecorder.getState()) {
            stopPlayingNewRecord();
            btnPlayNewRecord.setVisibility(View.GONE);

            wavRecorder.release();
            wavRecorder = new WavAudioRecorder(MediaRecorder.AudioSource.MIC,
                    8000,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);;
            wavRecorder.setOutputFile(strRecordingFilePath);
            txtStartStopRecord.setText(getResources().getString(R.string.str_start_recording));
            System.out.println("------Can't start record because of error state , initiating...--------");


        } else {
            wavRecorder.stop();
            wavRecorder.reset();
            txtStartStopRecord.setText(getResources().getString(R.string.str_start_recording));
            System.out.println("------Record stopped--------");
            btnPlayNewRecord.setVisibility(View.VISIBLE);
        }

    }

    private void stopPlayingNewRecord()
    {
        if(newRecordPlayer != null)
        {
            try
            {
                if(newRecordPlayer.isPlaying())
                {
                    newRecordPlayer.stop();
                }
                newRecordPlayer.release();
            }catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                newRecordPlayer = null;
            }
        }
    }

    @OnClick(R.id.btnUploadNewRecording)
    public void onClickUploadNewRecording()
    {
        if(wavRecorder != null && wavRecorder.getState() == WavAudioRecorder.State.RECORDING)
        {
            return;
        }
        if(strRecordingFilePath.equalsIgnoreCase("")) {
            showToast("No Recording file to upload." , Toast.LENGTH_LONG);
            return;
        }
        File recordingFile = new File(strRecordingFilePath);
        if(!recordingFile.exists())
        {
            showToast("No Recording file to upload." , Toast.LENGTH_LONG);
            return;
        }
        uploadAudioFileToStraticsNetwork(strRecordingFilePath);
    }

    private void uploadAudioFileToS3(String filePath)
    {
        uploadedVoiceFileUrl = "";
        File file = new File(filePath);
        showProgressDialog("Uploading..." , false , null);
        TransferObserver observer = transferUtility.upload(AWSConstants.BUCKET_NAME, "app-audio/"+file.getName(),
                file, CannedAccessControlList.PublicRead);
        observer.setTransferListener(transferListener);
    }

    private void uploadAudioFileToStraticsNetwork(final String filePath)
    {
        uploadedVoiceFileUrl = "";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.UPLOAD_API_BASE_URL)//.baseUrl(" https://requestb.in")//
                .build();

        // create upload service client
        FileUploadService service = retrofit.create(FileUploadService.class);

        final File file = new File(filePath);
        if(!file.exists())
            return;

        showProgressDialog("Uploading..." , false , null);

               // MultipartBody.Part is used to send also the actual file name
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("audioFile", file.getName(), requestFile);

        RequestBody accountid = RequestBody.create(MediaType.parse("text/plain"), AppConstants.StratisticsNetworkAccountId);
        RequestBody authhash = RequestBody.create(MediaType.parse("text/plain"), AppConstants.StratisticsNetworkAuthHash);
        RequestBody audioFile = RequestBody.create(MediaType.parse("text/plain"), "@"+file.getName());

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("account_id", accountid);
        map.put("authhash", authhash);
        map.put("audioFile", audioFile);

        // finally, execute the request
        Call<ResponseBody> call = service.uploadAudioFile(body , map);

        //Call<ResponseBody> call = service.uploadAudioFile(body , AppConstants.StratisticsNetworkAccountId ,
        //        AppConstants.StratisticsNetworkAuthHash, "@"+file.getName());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                hideProgressDialog();

                if(response != null) {
                    try {
                        String strResponse = response.body().toString();
                        System.out.println(strResponse);
                        uploadedVoiceFileUrl = file.getName();
                        System.out.println("------Uploaded Voice File Name = " + file.getName() + "------");
                        strVoiceMailPath = filePath;
                        currentPlayer = loadAudioFile(currentPlayer, filePath);

                        sendActivateMVMCampaign(strVoiceMailFileName);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                hideProgressDialog();
                showErrorMessage("Error" , "Failed to Upload", null);
            }
        });
    }

    private void sendActivateMVMCampaign(String strAudioFileName)
    {
        showProgressDialog("Activating MVM" , false , null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_ENRICH_BASE_URL)
                .build();

        EnrichAPIService service = retrofit.create(EnrichAPIService.class);
        Call<ResponseBody> call = service.activateMVMCampaign(String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId()),
                strAudioFileName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                hideProgressDialog();
                if(response != null) {
                    try {
                        String strResponse = response.body().toString();
                        System.out.println(strResponse);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Activate MVM error:", t.getMessage());
                hideProgressDialog();
                showErrorMessage("Error" , "Failed to Activate voice mail", null);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {
                    showErrorMessage(getResources().getString(R.string.str_alert_record_permission),
                            getResources().getString(R.string.str_alert_require_record_permission),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(EditMagicVoicemailActivity.this,
                                            new String[]{Manifest.permission.RECORD_AUDIO},
                                            MY_PERMISSION_REQUEST_RECORD_AUDIO);
                                }
                            });
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            break;
        }

    }

    @OnClick(R.id.btnCancel)
    public void onBtnCancelClicked()
    {
        EditMagicVoicemailActivity.this.finish();
    }

    @OnClick(R.id.btnSave)
    public void onBtnSaveClicked()
    {
        if(uploadedVoiceFileUrl.equalsIgnoreCase(""))
        {
            EditMagicVoicemailActivity.this.finish();
            return;
        }

        SettingInfo newSetting = new SettingInfo(MainApplication.getInstance().getCurrentSettingInfo());

        switch(nSelectedProfileIndex) {
            case 0:
                newSetting.setMvm1p1(uploadedVoiceFileUrl);
                break;
            case 1:
                newSetting.setMvm1p2(uploadedVoiceFileUrl);
                break;
            case 2:
                newSetting.setMvm1p3(uploadedVoiceFileUrl);
                break;
        }

        saveUserSettings(newSetting);
    }

    private void downloadAudioFile()
    {
        if(strVoiceMailFileName.equalsIgnoreCase(""))
            return;

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://manage.ivr-platform.com:8083")
                .addCallAdapterFactory(rxAdapter)
                .build();
        EnrichAPIService downloadService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("Getting voice mail..." , false , null);

        Call<ResponseBody> call = downloadService.downloadAudioFileUrlAsync(AppConstants.StratisticsNetworkAccountId, strVoiceMailFileName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    boolean writtenToDisk = util.writeResponseBodyToDisk(response, util.AUDIO_FOLDER_NAME , strVoiceMailFileName);
                    String fullFilePath = util.getAppUsingFilePath(util.AUDIO_FOLDER_NAME ,strVoiceMailFileName);

                    if(writtenToDisk)
                    {
                        currentPlayer = loadAudioFile(currentPlayer, fullFilePath);
                    }
                    else
                    {
                        File downloadedFile = new File(fullFilePath);
                        if(downloadedFile.exists())
                            downloadedFile.delete();
                        hideProgressDialog();
                        downloadAudioFile();
                        return;
                    }


                   System.out.println("------file download was a success? " + writtenToDisk);
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                hideProgressDialog();
            }

        });

    }
    private void saveUserSettings(SettingInfo settingInfo)
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<SettingResponse>().createGsonConverter(SettingResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        showProgressDialog("Saving..." , false , null);
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
                        EditMagicVoicemailActivity.this.finish();
                    }
                });
    }
}
