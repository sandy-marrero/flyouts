package com.flyoutsgroup.flyouts;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Item")
public class Item extends ParseObject {

    public static final String KEY_PRICE = "price";
    public static final String KEY_NAME = "ItemName";
    public static final String KEY_ITEM_DESCRIPTION = "itemDescription";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CONTACT = "contactInfo";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CREATED = "createdAt";

    //Item Name
    public String getItemName() {
        return getString(KEY_NAME);
    }

    public void setItemName(String Name) {
        put(KEY_NAME, Name);
    }

    //Item Image
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    //Seller
    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setUser(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    //Item description
    public String getItemDescription() {
        return getString(KEY_ITEM_DESCRIPTION);
    }

    public void setItemDescription(String ItemDescription) {
        put(KEY_ITEM_DESCRIPTION, ItemDescription);
    }

    //Seller's Contact information
    public String getContact() {
        return getString(KEY_CONTACT);
    }

    public void setContact(String ContactInfo) {
        put(KEY_CONTACT, ContactInfo);
    }

    //Item Price
    public double getPrice() {
        return getDouble(KEY_PRICE);
    }

    public void setPrice(double Price) {
        put(KEY_PRICE, Price);
    }

    //Item condition (New, Used, etc.)
    public String getCondition() {
        return getString(KEY_CONDITION);
    }

    public void setCondition(String Condition) {
        put(KEY_CONDITION, Condition);
    }
}
