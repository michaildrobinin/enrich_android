package enrich.and.com.ui.fragments;

 import android.Manifest;
 import android.content.ContentProviderOperation;
 import android.content.Intent;
 import android.content.pm.PackageManager;
 import android.os.AsyncTask;
 import android.os.Bundle;
 import android.provider.ContactsContract;
 import android.support.v4.app.ActivityCompat;
 import android.support.v4.content.ContextCompat;
 import android.support.v7.widget.GridLayoutManager;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.RecyclerView;
 import android.text.Editable;
 import android.text.TextWatcher;
 import android.util.Log;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.EditText;

 import java.util.ArrayList;
 import java.util.List;

 import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import enrich.and.com.R;
 import enrich.and.com.adapter.RecyclerViewSectionAdapter;
 import enrich.and.com.api.APIManager;
 import enrich.and.com.api.DeleteContactResponse;
 import enrich.and.com.api.EnrichAPIService;
 import enrich.and.com.api.GetMyContactsResponse;
 import enrich.and.com.api.LoginResponse;
 import enrich.and.com.app.AppConstants;
 import enrich.and.com.app.MainApplication;
 import enrich.and.com.models.ContactInfo;
 import enrich.and.com.models.ContactModel;
 import enrich.and.com.ui.LoginActivity;
 import enrich.and.com.ui.MainActivity;
 import enrich.and.com.ui.ResetPasswordActivity;
 import retrofit2.HttpException;
 import retrofit2.Response;
 import retrofit2.Retrofit;
 import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
 import rx.Observable;
 import rx.Subscriber;
 import rx.Subscription;
 import rx.android.schedulers.AndroidSchedulers;
 import rx.schedulers.Schedulers;

public class MyContactsFragment extends BaseFragment{

    private RecyclerViewSectionAdapter contactsAdapter;

    @BindView(R.id.contactsRecyclerView)
    RecyclerView myRecyclerView;

    @BindView(R.id.edtSearchCards)
    EditText edtSearchCards;

    private ArrayList<ContactInfo> contactsInfolist = new ArrayList<ContactInfo>();

    EnrichAPIService apiService;

    private final int MY_PERMISSION_REQUEST_WRITE_CONTACTS = 10;

    public MyContactsFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (m_rootView == null) {
            m_rootView = inflater.inflate(R.layout.fragment_mycontacts_screen, container, false);
            ButterKnife.bind(this, m_rootView);

        }

        contactsInfolist = new ArrayList<>();
        getContainer().showProgressDialog("Loading Contacts...", false , null);


        contactsAdapter = new RecyclerViewSectionAdapter(getContainer());
        contactsAdapter.setContactsArray(contactsInfolist);

        loadMyContacts();

        //myRecyclerView.addItemDecoration(
        //        new DividerItemDecoration(getContainer() , null));

        GridLayoutManager manager = new GridLayoutManager(getContainer(), 1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        myRecyclerView.setHasFixedSize(true);
        //contactsAdapter.setLayoutManager(manager);
        myRecyclerView.setAdapter(contactsAdapter);
        myRecyclerView.setLayoutManager(linearLayoutManager);

        edtSearchCards.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setSearchString(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUi()
    {
    }

    @OnClick(R.id.btnMenu)
    public void onClickMenu()
    {
        getContainer().onSlidingMenuClicked();
    }

    @OnClick(R.id.btnUpload)
    public void onUploadButtonClicked()
    {
        if(contactsAdapter.getDeleteMode())
        {
            //delete items
            if(contactsAdapter.getCheckedItems().size()>0)
            {
                String indexes = "";
                ArrayList<ContactInfo> contacts = contactsAdapter.getCheckedItems();
                ContactModel contact = null;
                for(int i =0 ; i<contacts.size(); i++)
                {
                    contact = contacts.get(i).contact;
                    break;
                }

                //---------Export contact to the local phone contacts
                //check add contacts permission
                int recordPermissionCheck = ContextCompat.checkSelfPermission(getContainer(),
                        Manifest.permission.WRITE_CONTACTS);
                if(recordPermissionCheck != PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("SPLASH_SCREEN" , "Record Audio Permission denied");
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getContainer(),
                            Manifest.permission.RECORD_AUDIO)) {
                        ActivityCompat.requestPermissions(getContainer(),
                                new String[]{Manifest.permission.WRITE_CONTACTS},
                                MY_PERMISSION_REQUEST_WRITE_CONTACTS);
                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(getContainer(),
                                new String[]{Manifest.permission.WRITE_CONTACTS},
                                MY_PERMISSION_REQUEST_WRITE_CONTACTS);

                    }
                    return;
                }
                if(contact != null)
                    new ExportContactToLocalPhoneTask().execute(contact);
            }
            else
            {
                contactsAdapter.setDeleteMode(false);
            }
        }
        else
        {
            contactsAdapter.setDeleteMode(true);
        }
    }

    @OnClick(R.id.btnTrash)
    public void onTrashButtonClicked()
    {
        if(contactsAdapter.getDeleteMode())
        {
            //delete items
            if(contactsAdapter.getCheckedItems().size()>0)
            {
                String indexes = "";
                ArrayList<ContactInfo> contacts = contactsAdapter.getCheckedItems();
                for(int i =0 ; i<contacts.size(); i++)
                {
                    if(i<contacts.size()-1)
                        indexes = indexes + String.valueOf(contacts.get(i).contact.getId())+"_";
                    else
                        indexes = indexes + String.valueOf(contacts.get(i).contact.getId());
                }
                deleteSelectedContacts(indexes);
            }
            else
            {
                contactsAdapter.setDeleteMode(false);
            }
        }
        else
        {
            contactsAdapter.setDeleteMode(true);
        }
    }


    private void deleteSelectedContacts(final String indexes)
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<DeleteContactResponse>().createGsonConverter(DeleteContactResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        getContainer().showProgressDialog("Removing Contacts" , false , null);
        Observable<DeleteContactResponse> call = apiService.deleteContacts(indexes);//String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId()));
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<DeleteContactResponse>() {
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
                            getContainer().showErrorMessage("Error" , errorMsg , null);
                        }
                        getContainer().hideProgressDialog();
                    }

                    @Override
                    public void onNext(DeleteContactResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            getContainer().hideProgressDialog();
                            getContainer().showErrorMessage("Error" , "Failed to delete contacts." , null);
                            return;
                        }

                        int index = 0;
                        while(index<contactsInfolist.size())
                        {
                            if(contactsInfolist.get(index).isSelected()) {
                                contactsInfolist.remove(index);
                                continue;
                            }
                            index++;
                        }

                        contactsAdapter.setContactsArray(contactsInfolist);
                        contactsAdapter.notifyDataSetChanged();
                        getContainer().hideProgressDialog();
                    }
                });
    }

    public void setSearchString(String strSearch)
    {
        if(strSearch.equalsIgnoreCase("")) {
            contactsAdapter.setContactsArray(contactsInfolist);
        }
        else
        {
            ArrayList<ContactInfo> contacts = new ArrayList<ContactInfo>();
            for (int i = 0; i < contactsInfolist.size(); i++) {
                if(contactsInfolist.get(i).getFullName().toLowerCase().contains(strSearch) ||
                        contactsInfolist.get(i).contact.getCompany().toLowerCase().contains(strSearch)) {
                    contacts.add(contactsInfolist.get(i));
                }
            }
            contactsAdapter.setContactsArray(contacts);
        }
    }


    private void loadMyContacts()
    {
        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_BASE_URL)
                .addConverterFactory(new APIManager<GetMyContactsResponse>().createGsonConverter(GetMyContactsResponse.class))
                .addCallAdapterFactory(rxAdapter)
                .build();

        apiService = retrofit.create(EnrichAPIService.class);

        getContainer().showProgressDialog("" , false , null);
        //Observable<GetMyContactsResponse> call = apiService.getMyContacts("2802");
        Observable<GetMyContactsResponse> call = apiService.getMyContacts(String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId()));
        Subscription subscription = call
                .subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<GetMyContactsResponse>() {
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
                        getContainer().hideProgressDialog();
                    }

                    @Override
                    public void onNext(GetMyContactsResponse response) {
                        //if(validateInputs())
                        if(response.getStatusCode() != 200)
                        {
                            getContainer().hideProgressDialog();

                            return;
                        }
                        List<ContactModel> contacts = response.getData();
                        contactsInfolist.clear();
                        for(ContactModel model : contacts)
                        {
                            contactsInfolist.add(new ContactInfo(model));
                        }
                        contactsAdapter.setContactsArray(contactsInfolist);
                        contactsAdapter.notifyDataSetChanged();
                        getContainer().hideProgressDialog();
                    }
                });
    }
    private boolean addContact(ContactModel contact)
    {
        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        );

        String displayName = contact.getFname()+" "+contact.getMname()+" "+contact.getLname();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        displayName).build()
        );

        //------------------------------------------------------ Mobile Number
        if(contact.getCellPhone()!=null)
        {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getCellPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build()
            );
        }

        //------------------------------------------------------ Home Numbers
        if(contact.getHomePhone() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getHomePhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if(contact.getWorkPhone() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getWorkPhone())
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if(contact.getEmail() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.getEmail())
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if(contact.getCompany() != null)
        {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.getCompany())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    //.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try
        {
            getContainer().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //  Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class ExportContactToLocalPhoneTask extends AsyncTask<ContactModel , Void , Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getContainer().showProgressDialog("Exporting..." ,false ,null);
        }

        @Override
        protected void onPostExecute(Boolean bResult) {
            super.onPostExecute(bResult);
            getContainer().hideProgressDialog();
            if(bResult)
                getContainer().showErrorMessage("Success!" , "Contact is exported to your local address book successfully.");
            else
                getContainer().showErrorMessage("Success!" , "Contact is exported to your local address book successfully.");
        }

        @Override
        protected Boolean doInBackground(ContactModel... contact) {
            boolean res =  addContact(contact[0]);
            return res;

        }
    }
}
