package enrich.and.com.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import enrich.and.com.app.MainApplication;
import enrich.and.com.mywidget.ProgressHUD;


public abstract class BaseActivity extends AppCompatActivity {

    protected MainApplication m_Application;

    public final int REQUEST_TAKE_PICTURE_BY_CAMERA = 1;
    public final int REQUEST_GET_PICTURE_FROM_GALLERY = 10;

    private ProgressHUD progressHUD;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        m_Application = (MainApplication)getApplication();
        m_Application.getFragmentChanger().setCurrentActivity(this);

        init();
    }

    public void init() {
    }

    public void showProgressDialog(CharSequence message , boolean cancelable , DialogInterface.OnCancelListener cancelListener)
    {
        if(progressHUD == null)
            progressHUD = ProgressHUD.show(this , message , true , cancelable , cancelListener);
        else {
            progressHUD.setMessage(message);
            progressHUD.show();
        }
    }

    public void hideProgressDialog()
    {
        if(progressHUD != null)
            progressHUD.dismiss();
    }


    public void onResume() {
        super.onResume();
        m_Application.getFragmentChanger().setCurrentActivity(this);

    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void showToast(String message , int toast_length)
    {
        Toast.makeText(this, message , toast_length).show();
    }

    public void onSlidingMenuClicked(){}

    public void showErrorMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setOnCancelListener(null)
                .setPositiveButton( "Done" , null)
                .show();
    }
    public void showErrorMessage(String title, String message , DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setOnCancelListener(null)
                .setPositiveButton( "Done" , listener)
                .show();
    }
}
