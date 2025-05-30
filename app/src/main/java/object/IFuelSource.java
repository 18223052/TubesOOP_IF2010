package object;

public interface IFuelSource {
    int getCurrentFuelValue();
    void deductFuel(int amount);
    boolean isFuelEmpty();
    String getName();
}
