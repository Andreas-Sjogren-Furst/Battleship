public class Ship {
    String frontOfShip;
    String endOfShip;
    int shipLength;
    boolean isHit;

    Ship(String frontOfShip, String endOfShip, int lengthOfShip){
        this.frontOfShip = frontOfShip;
        this.endOfShip = endOfShip;
        this.shipLength = lengthOfShip;
        this.isHit = false;
    }
}
