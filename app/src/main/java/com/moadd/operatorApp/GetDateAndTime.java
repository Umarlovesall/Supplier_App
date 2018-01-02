package com.moadd.operatorApp;

/**
 * Created by moadd on 19-Dec-17.
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetDateAndTime {

public static String timeStamp()
{
    Calendar calander;
    SimpleDateFormat simpledateformat;
    String Date;
    calander = Calendar.getInstance();
    simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Date = simpledateformat.format(calander.getTime());
    return Date.replace(" ","*");
}
}
