package Modules;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Duration {
    public String text;
    public int value;

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }
    public String getArrival(){
        return("Yes");
    }
}
