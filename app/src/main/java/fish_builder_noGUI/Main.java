//coba coba
public class Main {
    public static void main(String[] args) {
        FishBuilder builder = new FishConcreteBuilder();
        FishDirector director = new FishDirector();

        director.constructHalibut(builder);
        Fish halibut = builder.getResult();

        halibut.describe();

        
    }
}
