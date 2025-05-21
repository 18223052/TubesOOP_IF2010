package entity;
public class Ingredient {
    public String name;
    public int amount;

    public Ingredient(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public boolean matches(object.BaseItem item){
        if (this.name.equalsIgnoreCase("Any Fish")){
            return item.getCategory().equals("fish");
        }
        return this.name.equalsIgnoreCase(item.getName());
    }
}