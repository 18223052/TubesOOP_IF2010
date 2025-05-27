// In a new file: object/InventorySlot.java
package object;

public class InventorySlot {
    private IItem item;
    private int quantity;
    private final int maxStackSize = 10; // Define max stack size here

    public InventorySlot(IItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public IItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        if (quantity < maxStackSize) {
            quantity++;
        }
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    public boolean isFull() {
        return quantity >= maxStackSize;
    }

    public boolean isEmpty() {
        return quantity <= 0;
    }

    // You might want to add an equals method if comparing slots directly
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventorySlot that = (InventorySlot) o;
        return item.getName().equals(that.item.getName()); // Compare by item name
    }

    @Override
    public int hashCode() {
        return item.getName().hashCode();
    }
}