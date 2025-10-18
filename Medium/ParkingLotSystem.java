import java.util.*;
enum VehcileType{
    TwoWheeler, FourWheeler, LargeVechile;
}
enum FuelType{
    Electric, Petrol, Diesel, CNG;
}
enum SpotType{
    Regular, Electric, HandiCapped;
}
abstract class Vehicle{
    protected VehcileType vehcileType;
    private FuelType fuelType;
    private  String licenseNo;

    public Vehicle(String licenseNo, FuelType fuelType){
        this.licenseNo = licenseNo;
        this.fuelType = fuelType;
    }

    public FuelType getFuelType(){return this.fuelType;}
    public String getLicenseNo(){return this.licenseNo;}
    public VehcileType getVehcileType(){return this.vehcileType;}
}
class Car extends Vehicle{
    public Car(String licenseNo, FuelType fuelType){
        super(licenseNo,fuelType);
        this.vehcileType = VehcileType.FourWheeler;
    }
}
class MotorCycle extends Vehicle{
    public MotorCycle(String licenseNo, FuelType fuelType){
        super(licenseNo,fuelType);
        this.vehcileType = VehcileType.TwoWheeler;
    }
}
class Truck extends Vehicle{
    public Truck(String licenseNo, FuelType fuelType){
        super(licenseNo,fuelType);
        this.vehcileType = VehcileType.LargeVechile;
    }
}
class ParkingSpot{
    private int spotNo;
    private SpotType spotType;
    private VehcileType vehcileType;
    private boolean isOccupied;
    private Vehicle vehicle;
    private int price;
    public ParkingSpot(int spotNo, SpotType spotType,int price,VehcileType vehcileType){
        this.spotNo = spotNo;
        this.isOccupied = false;
        this.spotType = spotType;
        this.price = price;
        this.vehcileType = vehcileType;
    }
    public void park(Vehicle vehicle){
        this.vehicle = vehicle;
        this.isOccupied = true;
    }
    public void unpark(){
        this.vehicle = null;
        this.isOccupied = false;
    }
    public int getSpotNo(){return spotNo;}
    public SpotType getSpotType(){return this.spotType;}
    public boolean is_available(){return !isOccupied;}
    public VehcileType getVehcileType(){return this.vehcileType;}
    public int getPrice(){return this.price;}
}
class ParkingFloor{
    private List<ParkingSpot> parkingSpots;
    private int floorNo;
    public ParkingFloor(int floorNo,List<ParkingSpot> parkingSpots){
        this.floorNo = floorNo;
        this.parkingSpots = parkingSpots;
    }
    public int getFloorNo(){return floorNo;}
    public ParkingSpot findAvailableSpot(Vehicle vehicle,SpotType spotType){
        for(ParkingSpot spot : parkingSpots){
            if(spot.is_available() && spot.getSpotType() == spotType && spot.getVehcileType() == vehicle.getVehcileType()){
                return spot;
            }
        }
        return null;   
    }
}
class Ticket{
    private ParkingSpot spot;
    private double start;
    private boolean isValid;
    public Ticket(ParkingSpot spot){
        this.spot = spot;
        start = System.nanoTime();
        isValid = true;
    }
    public void unpark(){
        if(!isValid){
            System.out.println("Not a valid ticket");
            return;
        }
        isValid = false;
        double end = System.nanoTime();
        double total_mints = (end-start)/1e9;
        double price = (total_mints * spot.getPrice());
        price = Double.parseDouble(String.format("%.1f",price));
        System.out.println("Total cost for the parking is "+price);
        spot.unpark();
    }

}
class ParkingLot{
    private static ParkingLot instance = null;
    private List<ParkingFloor> floors;
    private ParkingLot(List<ParkingFloor> floors){
        this.floors = floors;
    }
    public static ParkingLot getInstance(List<ParkingFloor> floors){
        if(instance == null){
            instance = new ParkingLot(floors);
        }
        return instance;
    }
    public Ticket park(Vehicle vehicle, SpotType spotType){
        for(ParkingFloor floor : floors){
            ParkingSpot spot = floor.findAvailableSpot(vehicle, spotType);
            if(spot != null){
                spot.park(vehicle);
                System.out.println("Vehicle parked");
                Ticket ticket = new Ticket(spot);
                return ticket;
            }
        }
        System.out.println("Space is not available in parking lot");
        return null;
    }
    public void unParkVehicle(Ticket ticket){
        if(ticket == null){
            System.out.println("Invalid ticket");
            return;
        }
        ticket.unpark();
    }
}
public class ParkingLotSystem {
    public static void main(String[] args) {
        // Create parking spots
        List<ParkingSpot> spots = new ArrayList<>();
        spots.add(new ParkingSpot(1, SpotType.Electric, 20,VehcileType.TwoWheeler));
        spots.add(new ParkingSpot(2, SpotType.Regular, 15,VehcileType.FourWheeler));
        spots.add(new ParkingSpot(3, SpotType.HandiCapped, 10,VehcileType.LargeVechile));

        // Create a parking floor
        ParkingFloor floor = new ParkingFloor(1, spots);

        // Create parking lot (singleton)
        List<ParkingFloor> floors = new ArrayList<>();
        floors.add(floor);
        ParkingLot parkingLot = ParkingLot.getInstance(floors);

        // Create vehicles
        Vehicle car1 = new Car("MH-12-AB-1234", FuelType.Petrol);
        Vehicle bike1 = new MotorCycle("MH-14-CD-5678", FuelType.Electric);

        // Park car
        Ticket ticket1 = parkingLot.park(car1, SpotType.Regular);

        // Park bike (will fail since no 2-wheeler spot)
        Ticket ticket2 = parkingLot.park(bike1, SpotType.Electric);

        // Wait for some simulated time (just for testing price calculation)
        try {
            Thread.sleep(3000); // 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Unpark car
        parkingLot.unParkVehicle(ticket1);
        parkingLot.unParkVehicle(ticket2);

        // Try to unpark again (should show invalid)
        parkingLot.unParkVehicle(ticket1);
    }
}

