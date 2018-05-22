package enrich.and.com.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
import enrich.and.com.app.AppConstants;


public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.edtRegisteredEmail)
    EditText edtRegisteredEmail;

    String strEmail = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_reset_password_screen);
        ButterKnife.bind(this);


        strEmail = this.getIntent().getStringExtra("email");
        String strErrorMsg = this.getIntent().getStringExtra("login_error_msg");
        edtRegisteredEmail.setEnabled(false);
        edtRegisteredEmail.setText(strEmail);

        showErrorMessage("Failed to Login" , strErrorMsg);

    }

    @OnClick(R.id.btnResetPassword)
    public void onButtonResetPassword()
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(AppConstants.API_RESET_PASSWORD+strEmail));
        startActivity(i);
    }

    @OnClick(R.id.txtLoginButton)
    public void onButtonLogin()
    {
        Intent intent = new Intent(ResetPasswordActivity.this , LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
