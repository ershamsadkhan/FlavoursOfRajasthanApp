package com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item.ItemDto;

import java.io.Serializable;

/**
 * Created by SAM on 6/11/2017.
 */

public class OrderLineItemDto implements Serializable{

    public long OrderNo ;
    public long OrderLineItemId ;
    public String ItemHeader ;
    public int Quantity ;
    public int Price ;
    public short PriceType ;
    public long ItemId ;
    public String ImageUrl ;

    public ItemDto item ;
}
