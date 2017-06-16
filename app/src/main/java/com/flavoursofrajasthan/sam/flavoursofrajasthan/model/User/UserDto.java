package com.flavoursofrajasthan.sam.flavoursofrajasthan.model.User;

import java.io.Serializable;

/**
 * Created by SAM on 6/6/2017.
 */

public class UserDto implements Serializable
{
    public long Userid ;
    public String UserName ;
    public String UserPwd ;
    public String PrimaryAddress ;
    public String UserPhoneNumber ;
    public String UserEmailAddress ;
    public Boolean IsAdmin ;
}