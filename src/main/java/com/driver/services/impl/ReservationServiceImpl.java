package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.*;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try {
            User user = userRepository3.findById(userId).get();
            if (user == null) {
                throw new Exception("Cannot make reservation");
                //return null;
            }
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            if (parkingLot == null) {
                throw new Exception("Cannot make reservation");
               // return null;
            }
            List<Spot> spotList = parkingLot.getSpotList();

            Spot requiredSpot = null;
            SpotType requiredSpotType;
            if (numberOfWheels <= 2)
                requiredSpotType = SpotType.TWO_WHEELER;
            else if (numberOfWheels > 2 && numberOfWheels <= 4)
                requiredSpotType = SpotType.FOUR_WHEELER;
            else requiredSpotType = SpotType.OTHERS;

            int price = Integer.MAX_VALUE;

            for (Spot spot : spotList) {
                if (requiredSpotType == SpotType.TWO_WHEELER) {
                    if (spot.getSpotType() == requiredSpotType) {
                        if (spot.getOccupied() == false && spot.getPricePerHour() < price) {
                            requiredSpot = spot;
                            price = spot.getPricePerHour();
                            continue;
                        } else
                            continue;
                    } else if (spot.getSpotType() == SpotType.FOUR_WHEELER && spot.getOccupied() == false && spot.getPricePerHour() < price) {
                        requiredSpot = spot;
                        price = spot.getPricePerHour();
                        continue;
                    } else if (spot.getOccupied() == false && spot.getPricePerHour() < price) {
                        requiredSpot = spot;
                        price = spot.getPricePerHour();
                        continue;
                    }
                } else if (requiredSpotType == SpotType.FOUR_WHEELER) {
                    if (spot.getSpotType() == requiredSpotType && spot.getOccupied() == false && spot.getPricePerHour() < price) {
                        requiredSpot = spot;
                        price = spot.getPricePerHour();
                        continue;
                    } else if (spot.getSpotType() == SpotType.OTHERS && spot.getOccupied() == false && spot.getPricePerHour() < price) {
                        requiredSpot = spot;
                        price = spot.getPricePerHour();
                        continue;
                    }
                } else {
                    if (spot.getSpotType() == SpotType.OTHERS && spot.getOccupied() == false && spot.getPricePerHour() < price) {
                        requiredSpot = spot;
                        price = spot.getPricePerHour();
                        continue;
                    }

                }
            }

            if (requiredSpot == null) {
                throw new Exception("Cannot make reservation");
            }

            Reservation newReservation = new Reservation();
            newReservation.setNumberOfHours(timeInHours);
            newReservation.setSpot(requiredSpot);
            newReservation.setUser(user);
            user.getReservationList().add(newReservation);
            requiredSpot.getReservationList().add(newReservation);
            spotRepository3.save(requiredSpot);
            userRepository3.save(user);
            return newReservation;


        } catch (Exception e) {
            return null;
        }

    }
}
