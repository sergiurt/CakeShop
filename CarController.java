package gui;


import Exceptions.RentalException;
import Repository.EntityRepository.MemoryRepository;
import Service.CarService;
import Validator.CarInputValidator;
import domain.Car;
import domain.Reservation;

import java.util.List;

public class CarController {
    private final CarService carService;
    private final CarInputValidator carInputValidator;

    public CarController(CarService _carService) {
        this.carService = _carService;
        this.carInputValidator = new CarInputValidator(_carService.getCarRepository());
    }

    public void addCar(String id, String make, String model, String year, String rate) {
        carInputValidator.checkDataTypes(id, make, model, year, rate);

        Car car = new Car(id, make, model, Integer.parseInt(year), Double.parseDouble(rate));
        carService.add(car.getId(), car);
    }

    public void updateCar(String id, String make, String model, String year, String rate) {
        try {
            carInputValidator.checkDataTypes(id, make, model, year, rate);
            Car car = null;
            car = carService.findById(id);

            int yearV = Integer.parseInt(year);
            int rateV = Integer.parseInt(rate);

            car.setMake(make);
            car.setModel(model);
            car.setYear(yearV);
            car.setDailyRate(rateV);

        } catch (NumberFormatException e) {
            System.out.println("Invalid car year or daily rate. Please try again.");
        } catch (RentalException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCar(String id) {
        carService.delete(id);
    }

    public List<Car> getAllCars() {
        return carService.getCars();
    }

}
