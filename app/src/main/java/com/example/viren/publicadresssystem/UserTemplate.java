package com.example.viren.publicadresssystem;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by VIREN on 26-Mar-18.
 */

public class UserTemplate {

    public String id,pwd,name,cont,add;

    public UserTemplate(){

    }
    public  UserTemplate(String eid,String epwd,String ename,String econt,String eadd){
        id = eid;
        pwd = epwd;
        name = ename;
        cont = econt;
        add = eadd;
    }
}
