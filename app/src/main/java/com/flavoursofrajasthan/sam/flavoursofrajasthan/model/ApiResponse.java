package com.flavoursofrajasthan.sam.flavoursofrajasthan.model;

/**
 * Created by SAM on 6/5/2017.
 */

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {
    public T Obj;
    public ArrayList<T> ObjList;

    public String OrderNo;
    public String ErrMsg;
    public Boolean Status;

}
