package enrich.and.com.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import enrich.and.com.R;
import enrich.and.com.interfaces.OnCurrentProfileBarCollapseListener;
import enrich.and.com.models.ContactInfo;


public class ContactInfoView extends LinearLayout {

    private boolean isEditable = false;

    private ContactInfo contactInfo = new ContactInfo();

    public EditText edtFirstName;
    public EditText edtMiddelName;
    public EditText edtLastName;
    public EditText edtCompany;
    public EditText edtTitle;
    public EditText edtCompanyUrl;
    public EditText edtWorkEmail;
    public EditText edtWorkPhone;
    public EditText edtMobilePhone;
    public EditText edtHomePhone;
    public EditText edtOtherPhone;
    public EditText edtWorkFax;
    public EditText edtTwitter;
    public EditText edtSkype;
    public EditText edtAddress;
    public EditText edtEnrichTags;
    public EditText edtEnrichNotes;

    private LinearLayout firstnameLayout;
    private LinearLayout middlenameLayout;
    private LinearLayout lastnameLayout;
    private LinearLayout companyLayout;
    private LinearLayout titleLayout;



    public ContactInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs , R.styleable.ContactInfoViewAttr);
        isEditable = a.getBoolean(R.styleable.ContactInfoViewAttr_isEditable , true);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.contact_information_layout, this, true);

        edtFirstName = (EditText)findViewById(R.id.edtFirstName);edtFirstName.setHint("");
        edtMiddelName = (EditText)findViewById(R.id.edtMiddleName);edtMiddelName.setHint("");
        edtLastName = (EditText)findViewById(R.id.edtLastName);edtLastName.setHint("");
        edtCompany = (EditText)findViewById(R.id.edtCompany);edtCompany.setHint("");
        edtTitle = (EditText)findViewById(R.id.edtTitle);edtTitle.setHint("");
        edtCompanyUrl = (EditText)findViewById(R.id.edtCompanyUrl);edtCompanyUrl.setHint("");
        edtWorkEmail = (EditText)findViewById(R.id.edtWorkEmail);edtWorkEmail.setHint("");
        edtWorkPhone = (EditText)findViewById(R.id.edtWorkPhone);edtWorkPhone.setHint("");
        edtMobilePhone = (EditText)findViewById(R.id.edtMobilePhone);edtMobilePhone.setHint("");
        edtHomePhone = (EditText)findViewById(R.id.edtHomePhone);edtHomePhone.setHint("");
        edtOtherPhone = (EditText)findViewById(R.id.edtOtherPhone);edtOtherPhone.setHint("");
        edtWorkFax = (EditText)findViewById(R.id.edtWorkFax);edtWorkFax.setHint("");
        edtTwitter = (EditText)findViewById(R.id.edtTwitter);edtTwitter.setHint("");
        edtSkype = (EditText)findViewById(R.id.edtSkype);edtSkype.setHint("");
        edtAddress = (EditText)findViewById(R.id.edtAddress);edtAddress.setHint("");
        edtEnrichTags = (EditText)findViewById(R.id.edtEnrichTags);
        edtEnrichNotes = (EditText)findViewById(R.id.edtEnrichNotes);

        firstnameLayout = (LinearLayout)findViewById(R.id.firstnameLayout);
        middlenameLayout = (LinearLayout)findViewById(R.id.middlenameLayout);
        lastnameLayout = (LinearLayout)findViewById(R.id.lastnameLayout);
        companyLayout = (LinearLayout)findViewById(R.id.companyLayout);
        titleLayout = (LinearLayout)findViewById(R.id.titleLayout);


        setEditableViews(isEditable);
        //setViewValuesFromContactInfo(contactInfo);
    }

    public ContactInfoView(Context context) {
        this(context, null);
    }

    public void setViewValuesFromContactInfo(ContactInfo contact)
    {
        this.contactInfo = contact;
        edtFirstName.setText(contactInfo.contact.getFname());
        edtMiddelName.setText(contactInfo.contact.getMname());
        edtLastName.setText(contactInfo.contact.getLname());
        edtCompany.setText(contactInfo.contact.getCompany());
        edtTitle.setText(contactInfo.contact.getTitle());
        edtCompanyUrl.setText(contactInfo.contact.getWebsite());
        edtWorkEmail.setText(contactInfo.contact.getEmail());
        edtWorkPhone.setText(contactInfo.contact.getWorkPhone());
        edtMobilePhone.setText(contactInfo.contact.getCellPhone());
        edtHomePhone.setText(contactInfo.contact.getHomePhone());
        edtOtherPhone.setText(contactInfo.contact.getOtherPhone());
        edtWorkFax.setText(contactInfo.contact.getFax());
        edtTwitter.setText(contactInfo.contact.getTwitter());
        edtSkype.setText(contactInfo.contact.getSkype());
        edtAddress.setText(contactInfo.contact.getAddress());
        edtEnrichTags.setText(contactInfo.contact.getTags());
        edtEnrichNotes.setText(contactInfo.contact.getNotes());
    }

    public ContactInfo getContactInfo()
    {
        return this.contactInfo;
    }

    public ContactInfo getContactInfoFromViewValues()
    {
        if(contactInfo == null)
            this.contactInfo = new ContactInfo();
        contactInfo.contact.setFname(edtFirstName.getText().toString().trim());
        contactInfo.contact.setMname(edtMiddelName.getText().toString().trim());
        contactInfo.contact.setLname(edtLastName.getText().toString().trim());
        contactInfo.contact.setCompany(edtCompany.getText().toString().trim());
        contactInfo.contact.setTitle(edtTitle.getText().toString().trim());
        contactInfo.contact.setWebsite(edtCompanyUrl.getText().toString().trim());
        contactInfo.contact.setEmail(edtWorkEmail.getText().toString().trim());
        contactInfo.contact.setWorkPhone(edtWorkPhone.getText().toString().trim());
        contactInfo.contact.setCellPhone(edtMobilePhone.getText().toString().trim());
        contactInfo.contact.setHomePhone(edtHomePhone.getText().toString().trim());
        contactInfo.contact.setOtherPhone(edtOtherPhone.getText().toString().trim());
        contactInfo.contact.setFax(edtWorkFax.getText().toString().trim());
        contactInfo.contact.setTwitter(edtTwitter.getText().toString().trim());
        contactInfo.contact.setSkype(edtSkype.getText().toString().trim());
        contactInfo.contact.setAddress(edtAddress.getText().toString().trim());
        contactInfo.contact.setTags(edtEnrichTags.getText().toString().trim());
        contactInfo.contact.setNotes(edtEnrichNotes.getText().toString().trim());

        return this.contactInfo;
    }

    public void setEditableViews(boolean isEditable)
    {
        this.isEditable = isEditable;
        edtFirstName.setEnabled(isEditable);
        edtMiddelName.setEnabled(isEditable);
        edtLastName.setEnabled(isEditable);
        edtCompany.setEnabled(isEditable);
        edtTitle.setEnabled(isEditable);
        edtCompanyUrl.setEnabled(isEditable);
        edtWorkEmail.setEnabled(isEditable);
        edtWorkPhone.setEnabled(isEditable);
        edtMobilePhone.setEnabled(isEditable);
        edtHomePhone.setEnabled(isEditable);
        edtOtherPhone.setEnabled(isEditable);
        edtWorkFax.setEnabled(isEditable);
        edtTwitter.setEnabled(isEditable);
        edtSkype.setEnabled(isEditable);
        edtAddress.setEnabled(isEditable);
        edtEnrichTags.setEnabled(isEditable);
        edtEnrichNotes.setEnabled(isEditable);

        if(isEditable)
        {
            firstnameLayout.setVisibility(View.VISIBLE);
            middlenameLayout.setVisibility(View.VISIBLE);
            lastnameLayout.setVisibility(View.VISIBLE);
            companyLayout.setVisibility(View.VISIBLE);
            titleLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            firstnameLayout.setVisibility(View.GONE);
            middlenameLayout.setVisibility(View.GONE);
            lastnameLayout.setVisibility(View.GONE);
            companyLayout.setVisibility(View.GONE);
            titleLayout.setVisibility(View.GONE);
        }
    }

}
