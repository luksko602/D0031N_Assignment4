/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.List;

/**
 *
 * @author Lukas
 */
public class Reservation {
       private String id;
    private String startdate;
    private String starttime;
    private String enddate;
    private String endtime;
    private List<String> columns;

    public String toString(){
        String res = "ID: " + id + " \nStartdatum:" + startdate + "\nStarttid:" + starttime 
                + "\nSlutdatum:" + enddate + "\nSluttid:" + endtime + columns;
        int count = 0;
        for (String s: columns){        
            res += "\n" +count +": "+ s;
            count++;
        }
        return res;
    }
    
    public String getEventInfo(){
         String res = "ID: " + id + "\nStartdatum:" + startdate + "\nStarttid:" + starttime 
                + "\nSlutdatum:" + enddate + "\nSluttid:" + endtime;
        return res;   
    }
    
       public String getEventSpec(){
        String res = "";
        for (String s: columns){        
            res += s;
        }
        return res;   
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }
    
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

    public String getStartdate() {
        return startdate;
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
    
    public String getLokal(){
        return columns.get(1);
    }
    
    public String getLarare(){
        return columns.get(2);
    }
    
    public String getAktivitet(){
        return columns.get(3);
    }
    
    public String getKurs(){
        return columns.get(1);
    }
    
    public String getCampus(){
        return columns.get(1);
    }
    
    public String getService(){
        return columns.get(1);
    }
    
    public String getText1(){
        return columns.get(1);
    }
}
