
package object;

public class InventorySlot {
    private IItem item;
    private int quantity;
    private final int maxStackSize = 15;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventorySlot that = (InventorySlot) o;
        return item.getName().equals(that.item.getName());
    }

    @Override
    public int hashCode() {
        return item.getName().hashCode();
    }
}