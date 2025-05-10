package store;

public class Refrigerator extends Product {
    private String doorDesign;
    private String color;
    private int capacity;
    
    public Refrigerator(String item_number, String name, int stock_quantity, double price,
            String doorDesign, String color, int capacity) {
    	super(item_number, name, stock_quantity, price);
        this.doorDesign = doorDesign;
        this.color = color;
        this.capacity = capacity;
    }

    // getters and setters
    public String getDoorDesign() {
    	return doorDesign;
    	}
    public void setDoorDesign(String doorDesign) { 
    	this.doorDesign = doorDesign; 
    	}
    
    public String getColor() { 
    	return color; 
    	}
    public void setColor(String color) {
    	this.color = color; 
    	}
    
    public int getCapacity() {
    	return capacity; 
    	}
    public void setCapacity(int capacity) { 
    	this.capacity = capacity; 
    	}

    //override
    public String toString() {
        return super.toString() +
               "\nDoor design        : " + doorDesign +
               "\nColor              : " + color +
               "\nCapacity (in Litres): " + capacity;
    }

}