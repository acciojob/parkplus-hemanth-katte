package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot newParkingLot = new ParkingLot(name,address);

        parkingLotRepository1.save(newParkingLot);

        return newParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {

        Spot newSpot = new Spot(pricePerHour);

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        if(numberOfWheels<=2)
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        else if(numberOfWheels>2 && numberOfWheels<=4)
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        else newSpot.setSpotType(SpotType.OTHERS);

        newSpot.setPricePerHour(pricePerHour);
        newSpot.setOccupied(false);
        newSpot.setParkingLot(parkingLot);
        newSpot.setReservationList(new ArrayList<>());

        List<Spot> spotList = parkingLot.getSpotList();
        if(spotList == null)
            spotList = new ArrayList<>();
        spotList.add(newSpot);

        parkingLotRepository1.save(parkingLot);

        return newSpot;

    }

    @Override
    public void deleteSpot(int spotId) {

//        Spot spot = spotRepository1.findById(spotId).get();
//
//        ParkingLot parkingLot = spot.getParkingLot();
//
//        parkingLot.getSpotList().remove(spot);
//
//        parkingLotRepository1.save(parkingLot);

        spotRepository1.deleteById(spotId);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

//        Spot spotToBeUpdated = spotRepository1.findById(spotId).get();
//
//        int oldParkingLotId = spotToBeUpdated.getParkingLot().getId();
//
//        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
//
//        spotToBeUpdated.setPricePerHour(pricePerHour);
//
//        if(oldParkingLotId != parkingLotId){
//            ParkingLot oldParkingLot = parkingLotRepository1.findById(oldParkingLotId).get();
//            oldParkingLot.getSpotList().remove(spotToBeUpdated);
//            parkingLotRepository1.save(oldParkingLot);
//            parkingLot.getSpotList().add(spotToBeUpdated);
//            spotToBeUpdated.setParkingLot(parkingLot);
//        }
//
//        parkingLotRepository1.save(parkingLot);
//
//        return spotToBeUpdated;

        ParkingLot parkingLot= parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList= parkingLot.getSpotList();
        if(spotList==null){
            spotList=new ArrayList<>();
        }

        Spot spotToBeUpdated = new Spot();

        for(Spot spot : spotList){
            if(spot.getId()==spotId){
                spot.setPricePerHour(pricePerHour);
                spotToBeUpdated = spot;
            }
        }

        spotRepository1.save(spotToBeUpdated);
        return spotToBeUpdated;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
