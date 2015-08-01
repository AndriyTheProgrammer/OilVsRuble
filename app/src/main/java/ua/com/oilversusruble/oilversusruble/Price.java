package ua.com.oilversusruble.oilversusruble;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User on 14.05.2015.
 */
public class Price implements Serializable {

    public static final int RUB = 1, OIL = 2, UAH = 3;

    private int category;
    private String price1, price2;
    private ArrayList<String> prices1;
    private ArrayList<String> prices2;
    private boolean isGrow1, isGrow2;



    public Price(int category, String price1, String price2, boolean isGrow1, boolean isGrow2) {
        this.category = category;
        this.price1 = price1;
        this.price2 = price2;
        this.isGrow1 = isGrow1;
        this.isGrow2 = isGrow2;
    }

    public int getCategory() {

        return category;
    }

    public String getPrice1() {
        return price1;
    }

    public String getPrice2() {
        return price2;
    }

    public ArrayList<String> getPrices1() {
        return prices1;
    }

    public ArrayList<String> getPrices2() {
        return prices2;
    }

    public void setPrices2(ArrayList<String> prices2) {
        this.prices2 = prices2;
    }

    public void setPrices1(ArrayList<String> prices1) {
        this.prices1 = prices1;
    }

    public boolean isGrow1() {
        return isGrow1;
    }

    public boolean isGrow2() {
        return isGrow2;
    }
}
