package Model;

import java.util.List;

/**
 *Klass som motsvarar ett kalenderevent
 * @author Lukas Skog Andersen, luksok-8
 */
public class Reservation {

    private String id;
    private String startdate;
    private String starttime;
    private String enddate;
    private String endtime;
    private List<String> columns;

    /**
     * Returnerar all information om eventet
     *
     * @return All information om eventet
     */
    public String toString() {
        String res = "ID: " + id + " \nStartdatum:" + startdate + "\nStarttid:" + starttime
                + "\nSlutdatum:" + enddate + "\nSluttid:" + endtime + columns;
        int count = 0;
        for (String s : columns) {
            res += "\n" + count + ": " + s;
            count++;
        }
        return res;
    }

    /**
     * Returnerar endast den huvudsakliga informationen
     *
     * @return Huvudsaklig information
     */
    public String getEventInfo() {
        String res = "ID: " + id + "\nStartdatum:" + startdate + "\nStarttid:" + starttime
                + "\nSlutdatum:" + enddate + "\nSluttid:" + endtime;
        return res;
    }

    /**
     * Returnerar endast specifikationerna om eventet
     *
     * @return specifik information
     */
    public String getEventSpec() {
        String res = "";
        for (String s : columns) {
            res += s;
        }
        return res;
    }

    //--------------Getters och Setters
    
    public void setId(String id) {
        this.id = id;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getId() {
        return id;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public String getStartdate() {
        return startdate;
    }
    //----------------
    public String getAktivitet(){
        return columns.get(3);
    }
    
    public String getLokal(){
        return columns.get(1);
    }
        
    public String getCampus(){
        return columns.get(5);
    }
     
    public String getText1(){
        return columns.get(6);
    }
    
    public String getText2(){
        return columns.get(7);
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getEndtime() {
        return endtime;
    }
}
