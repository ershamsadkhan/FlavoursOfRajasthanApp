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
    public Date OrderDate ;
    public long GrandTotal ;
    public long UserId ;
    public String UserName ;
    public String DeliveryAddress ;
    public int CityCode ;

    public ArrayList<OrderLineItemDto> OrderLineItemList ;
}
