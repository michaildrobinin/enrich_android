package enrich.and.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import enrich.and.com.R;
import enrich.and.com.app.MainApplication;
import enrich.and.com.models.ContactInfo;
import enrich.and.com.ui.customviews.recyclersectionview.SectionedRecyclerViewAdapter;
import enrich.and.com.utils.util;

public class RecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<ContactInfo> transcribingContacts;
    //private ArrayList<ContactInfo> addedContacts;

    public boolean isDeletingContacts = false;

    private String strSearch = "";

    public void setDeleteMode(boolean isDeletingContacts)
    {
        this.isDeletingContacts = isDeletingContacts;
        if(!isDeletingContacts)
        {
            for(int i=0;i<transcribingContacts.size();i++)
            {
                transcribingContacts.get(i).setSelected(false);
            }
            /*for(int i=0;i<addedContacts.size();i++)
            {
                addedContacts.get(i).setSelected(false);
            }*/
        }
        notifyDataSetChanged();
    }
    public boolean getDeleteMode(){return this.isDeletingContacts;}

    public void refreshChecked(int checkingPosition)
    {
        for(int i=0;i<transcribingContacts.size();i++) {
            if (i != checkingPosition)
                transcribingContacts.get(i).setSelected(false);
            else
                transcribingContacts.get(i).setSelected(!transcribingContacts.get(i).isSelected());

        }

    }

    public ArrayList<ContactInfo> getCheckedItems()
    {
        ArrayList<ContactInfo> selectedContacts = new ArrayList<ContactInfo>();
        for(int i=0;i<transcribingContacts.size();i++) {
            if (transcribingContacts.get(i).isSelected())
                selectedContacts.add(transcribingContacts.get(i));
        }
        /*for(int i=0;i<addedContacts.size();i++) {
            if (addedContacts.get(i).isSelected())
                selectedContacts.add(addedContacts.get(i));
        }*/
        return selectedContacts;
    }


    public RecyclerViewSectionAdapter(Context context)
    {
        this.mContext = context;
        transcribingContacts = new ArrayList<>();
        //addedContacts = new ArrayList<>();
    }

    public RecyclerViewSectionAdapter(Context context , ArrayList<ContactInfo> transcribingContacts
                                      )
    {
        this.mContext = context;
        this.transcribingContacts = transcribingContacts;
        //this.addedContacts = addedContacts;
    }

    public void setContactsArray(ArrayList<ContactInfo> contactsArray)
    {
        transcribingContacts.clear();
        //addedContacts.clear();

        for(int i=0;i<contactsArray.size(); i++)
        {
            /*if(contactsArray.get(i).getFrontCardImagePath().equalsIgnoreCase("") && contactsArray.get(i).getBackCardImagePath().equalsIgnoreCase(""))
            {
                addedContacts.add(contactsArray.get(i));
            }
            else*/
            {
                transcribingContacts.add(contactsArray.get(i));
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getSectionCount() {
        return 1;
    }

    @Override
    public int getItemCount(int section) {
        //if(section == 0) return transcribingContacts.size();
        return transcribingContacts.size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        String sectionName = "";
        if(section == 0)
        {
            sectionName = "TRANSCRIBING ("+String.valueOf(transcribingContacts.size())+")";
        }
        /*else
        {
            sectionName = "ADDED ("+String.valueOf(addedContacts.size())+")";
        }*/
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.sectionTitle.setText(sectionName);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, final int relativePosition, int absolutePosition) {
        ContactInfo contactInfo = null;
        if(section == 0)
            contactInfo = transcribingContacts.get(relativePosition);
        //else
        //    contactInfo = addedContacts.get(relativePosition);

        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.setContactItem(contactInfo);

        itemViewHolder.txtCompanyName.setText(contactInfo.contact.getCompany());
        itemViewHolder.txtContactName.setText(contactInfo.getFullName());
        util.showCardImageViewWithPicasso(mContext , contactInfo.getFrontCardImagePath() , itemViewHolder.imgCardView);

        if(isDeletingContacts) {
            itemViewHolder.checkContactItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshChecked(relativePosition);
                    notifyDataSetChanged();
                }
            });
            itemViewHolder.checkContactItem.setVisibility(View.VISIBLE);
        }
        else
            itemViewHolder.checkContactItem.setVisibility(View.GONE);

        if(itemViewHolder.contactItem.isSelected())
            itemViewHolder.checkContactItem.setChecked(true);
        else
            itemViewHolder.checkContactItem.setChecked(false);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        View v = null;
        if (header)
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mycontacts_listview_section_item, parent, false);
            return new SectionViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mycontacts_listview_contact_item, parent, false);
            return new ItemViewHolder(this , v);
        }
    }

    // SectionViewHolder Class for Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        final TextView sectionTitle;

        public SectionViewHolder(View itemView) {
            super(itemView);

            sectionTitle = (TextView) itemView.findViewById(R.id.txtSectionTitle);

        }
    }

    // ItemViewHolder Class for Items in each Section
    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        final TextView txtContactName , txtCompanyName;

        final ImageView imgCardView;

        final CheckBox checkContactItem;

        private ContactInfo contactItem;

        public void setContactItem(ContactInfo item){this.contactItem = item;}
        public ContactInfo getContactItem(){return this.contactItem;}

        RecyclerViewSectionAdapter parentAdapter;

        public ItemViewHolder(RecyclerViewSectionAdapter adapter , View itemView) {
            super(itemView);
            txtContactName = (TextView) itemView.findViewById(R.id.txtContactName);
            txtCompanyName = (TextView) itemView.findViewById(R.id.txtCompanyName);
            checkContactItem = (CheckBox)itemView.findViewById(R.id.checkContactItem);

            this.parentAdapter = adapter;

            /*checkContactItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    contactItem.setSelected(isChecked);
                }
            });*/

            imgCardView = (ImageView) itemView.findViewById(R.id.imgViewCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!parentAdapter.isDeletingContacts) {
                        MainApplication.getInstance().setCurrentContactInfo(contactItem);
                        MainApplication.getInstance().getFragmentChanger().showCardDetailsFragment();
                    }
                }
            });
        }
    }
}
