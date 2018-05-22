package enrich.and.com.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import enrich.and.com.app.AWSConstants;
import enrich.and.com.app.MainApplication;

public class ContactInfo implements Serializable {

    public ContactModel contact;

    public CardImage frontCardImage = null;
    public CardImage backCardImage = null;

    public String frontCapturedDate = "";
    public String backCapturedDate = "";

    private boolean isSelected = false;

    public ContactInfo()
    {
        this.isSelected = false;
        contact = new ContactModel();
        this.frontCardImage = new CardImage(true);
        this.backCardImage = new CardImage(false);
        this.frontCapturedDate = "";
        this.backCapturedDate = "";
    }

    public ContactInfo(ContactInfo contactInfo)
    {
        this.frontCardImage = new CardImage(contactInfo.frontCardImage);
        this.backCardImage = new CardImage(contactInfo.backCardImage);
        this.frontCapturedDate = contactInfo.frontCapturedDate;
        this.backCapturedDate = contactInfo.backCapturedDate;
        this.contact = new ContactModel(contactInfo.contact);
    }

    public ContactInfo(ContactModel contactModel){
        this.contact = contactModel;
        this.frontCapturedDate = "";
        this.backCapturedDate = "";
        this.frontCardImage = new CardImage(true);
        this.backCardImage = new CardImage(false);
        for(int i=0;i< contact.getImages().size(); i++)
        {
            CardImage image = contact.getImages().get(i);
            if(image.getIsEdited() != 0)//edited image
                return;
            //only get original image
            if(image.getSide() == 0)//front card image
            {
                this.frontCardImage = image;
                long capturedTime = Long.valueOf(image.getModifiedAt());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(capturedTime*1000L);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd'th'-HH:mm yyyy");
                frontCapturedDate = dateFormat.format(calendar.getTime());
            }
            else
            {
                this.backCardImage = image;
                long capturedTime = Long.valueOf(image.getModifiedAt());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(capturedTime*1000L);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd'th'-HH:mm yyyy");
                backCapturedDate = dateFormat.format(calendar.getTime());
            }
        }
        this.isSelected = false;
    }

    public String getFullName()
    {
        String fullName = contact.getFname();
        if(!contact.getMname().equalsIgnoreCase(""))
            fullName = fullName + " " + contact.getMname();
        if(!contact.getLname().equalsIgnoreCase(""))
            fullName = fullName + " " + contact.getLname();
        return fullName;
    }

    public void setFrontCardImagePath(String path){
        if(frontCardImage == null)
        {
            frontCardImage = new CardImage(true);
            frontCardImage.setBucketName(AWSConstants.SCANNER_BUCKET_NAME);
            frontCardImage.setIsEdited(0);
            frontCardImage.setSide(0);
            frontCardImage.setId(0);
            frontCardImage.setHtJobId(contact.getId());
            frontCardImage.setImageType("jpg");

        }

        frontCardImage.setImageUrl(path);
        if(contact != null)
            frontCardImage.setHtJobId(contact.getId());
    }

    public String generateImageFileName(CardImage image)
    {
        String fileName = "img";
        if(image.getHtJobId()!=0)
            fileName = fileName + String.valueOf(image.getHtJobId());
        if(image.getSide() == 0)
            fileName = fileName+"a";
        else
            fileName = fileName+"b";
        fileName = fileName + "_id"+ String.valueOf(MainApplication.getInstance().getCurrentUserInfo().getId())+"_";
        fileName = fileName + "p" + String.valueOf(MainApplication.getInstance().getCurrentProfileIndex()+1)+"_";

        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        fileName = fileName+date.format(Calendar.getInstance().getTime())+".jpg";

        return fileName;
    }

    public String getFrontCardImagePath(){return this.frontCardImage == null?"":frontCardImage.getImageUrl();}

    public void setFrontCardImageTime(String datetime){this.frontCapturedDate = datetime;}
    public void setBackCardImageTime(String datetime){this.backCapturedDate = datetime;}

    public void setBackCardImagePath(String path){
        if(backCardImage == null)
        {
            backCardImage = new CardImage(false);
            backCardImage.setBucketName(AWSConstants.SCANNER_BUCKET_NAME);
            backCardImage.setIsEdited(0);
            backCardImage.setSide(1);
            backCardImage.setId(0);
            backCardImage.setHtJobId(contact.getId());
            backCardImage.setImageType("jpg");
        }

        backCardImage.setImageUrl(path);
        if(contact != null)
            backCardImage.setHtJobId(contact.getId());
    }
    public String getBackCardImagePath(){return this.backCardImage == null?"":backCardImage.getImageUrl();}

    public void setSelected(boolean selected){this.isSelected = selected;}
    public boolean isSelected(){return this.isSelected;}


}