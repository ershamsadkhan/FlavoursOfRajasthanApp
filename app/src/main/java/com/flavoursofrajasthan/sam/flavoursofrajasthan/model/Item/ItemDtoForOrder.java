package com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by SAM on 6/11/2017.
 */

public class ItemDtoForOrder implements Serializable {
    public static Bitmap image;
    public long Itemid;
    public long Categoryid;
    public String ItemHeader;
    public String ItemDescription;
    public long QuaterPrice;
    public long HalfPrice;
    public long FullPrice;
    public String ImageUrl;

    public static Bitmap getImage() {
        return image;
    }
}
