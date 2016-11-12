import java.util.*;
public class Order {
	ArrayList<Item> items;
	Order() {
		items = new ArrayList<Item>();
	}
	public void addItem(Item newItem) {
		items.add(newItem);
	}
	public void removeItem(int index) {
		items.remove(index);
	}
	public void clearOrder() {
		items.clear();
	}
}
