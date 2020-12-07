/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Reservation;
import java.util.List;

/**
 *
 * @author Lukas
 */
public class ReservationContainer {
        private List<Reservation> reservations;

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    
    public Reservation getReservationByID(String id){
        for(Reservation res: reservations){
         if(res.getId().equals(id)){
             return res;
            }
        }
        return null;
    }
    public Reservation getReservationByIndex(int index){
        return reservations.get(index);
    }
}
