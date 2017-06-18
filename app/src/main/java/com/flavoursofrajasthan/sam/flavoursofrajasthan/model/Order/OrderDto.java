package com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SAM on 6/11/2017.
 */

public class OrderDto implements Serializable
{
    public long OrderNo ;
    public String OrderDate ;
    public long GrandTotal ;
    public long UserId ;
    public String UserName ;
    public String DeliveryAddress ;
    public int CityCode ;
    public String OrderStatus;

    public ArrayList<OrderLineItemDto> OrderLineItemList ;
    public long getGrandTotal(){
        GrandTotal=0;
        for (OrderLineItemDto item:OrderLineItemList) {
            GrandTotal=GrandTotal+item.Price*item.Quantity;
        }
        return GrandTotal;
    }
}
