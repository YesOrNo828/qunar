package qunar;

/**
 * Created by yexianxun on 2017/2/28.
 */
public class QunarFlight {
    String depDate;
    String airco;
    String price;
    String tax;
    boolean direct;

    public String getDepDate() {
        return depDate;
    }

    public void setDepDate(String depDate) {
        this.depDate = depDate;
    }

    public String getAirco() {
        return airco;
    }

    public void setAirco(String airco) {
        this.airco = airco;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }
}
