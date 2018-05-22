package enrich.and.com.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.AppConstants;

public class FirstScreen extends BaseActivity {

    @BindView(R.id.btnLogin)
    Button btnLogin;

    private final int MY_PERMISSION_REQUEST_CAMERA = 10;
    private final int MY_PERMISSION_REQUEST_WRITE_STORAGE = 12;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        ButterKnife.bind(this);


        int cameraPermissionCheck = ContextCompat.checkSelfPermission(FirstScreen.this,
                Manifest.permission.CAMERA);
        if(cameraPermissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("SPLASH_SCREEN" , "Camera Permission denied");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(FirstScreen.this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(FirstScreen.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_REQUEST_CAMERA);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(FirstScreen.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSION_REQUEST_CAMERA);
            }
        }
        else
        {
            int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(FirstScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(writeExternalStoragePermissionCheck != PackageManager.PERMISSION_GRANTED)
            {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(FirstScreen.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(FirstScreen.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_WRITE_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(FirstScreen.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_WRITE_STORAGE);
                }
            }
        }

    }


    @OnClick(R.id.btnLogin)
    public void onButtonLoginClicked()
    {
        Intent intent = new Intent(FirstScreen.this , LoginActivity.class);
        //Intent intent = new Intent(FirstScreen.this , ResetPasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnRegister)
    public void onClickButtonRegister()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConstants.REGISTER_URL));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    int writeExternalStoragePermissionCheck = ContextCompat.checkSelfPermission(FirstScreen.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(writeExternalStoragePermissionCheck != PackageManager.PERMISSION_GRANTED)
                    {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(FirstScreen.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(FirstScreen.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSION_REQUEST_WRITE_STORAGE);
                        } else {
                            ActivityCompat.requestPermissions(FirstScreen.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSION_REQUEST_WRITE_STORAGE);
                        }
                    }

                } else {
                    showErrorMessage(getResources().getString(R.string.str_alert_camera_permission),
                            getResources().getString(R.string.str_alert_require_camera_permission),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(FirstScreen.this,
                                            new String[]{Manifest.permission.CAMERA},
                                            MY_PERMISSION_REQUEST_CAMERA);
                                }
                            });
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            case MY_PERMISSION_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    int cameraPermissionCheck = ContextCompat.checkSelfPermission(FirstScreen.this,
                            Manifest.permission.CAMERA);
                    if(cameraPermissionCheck != PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d("SPLASH_SCREEN" , "Camera Permission denied");
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(FirstScreen.this,
                                Manifest.permission.CAMERA)) {
                            ActivityCompat.requestPermissions(FirstScreen.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSION_REQUEST_CAMERA);
                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(FirstScreen.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSION_REQUEST_CAMERA);
                        }
                    }
                } else {
                    showErrorMessage(getResources().getString(R.string.str_alert_write_permission),
                            getResources().getString(R.string.str_alert_require_write_permission),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(FirstScreen.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSION_REQUEST_WRITE_STORAGE);
                                }
                            });
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
