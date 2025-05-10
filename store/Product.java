package store;

public abstract class Product {
	
	//data field
	private String name;
	private double price;
	private int stock_quantity;
	private String item_number;
	private boolean status;
	
	//constructor
	public Product() {
		this.status = true;
	}
	
	public Product(String item_number,String name,int stock_quantity,double price) {
		this.item_number = item_number;
		this.name = name;
		this.stock_quantity = stock_quantity;
		this.price = price;
		this.status = true;
		
	}
	
	//getter
	public String getItemNumber() {
		return item_number;
	}
	
	
	public String getName() {
		return name;
	}
	
	public int getStockQuantity() {
		return stock_quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	//setter
	
	public void setItemNumber(String item_number) {
		this.item_number = item_number;
	}
	
	
	public void setName(String name) {
		this.name =name;
	}
	
	
	public void setStockQuantity(int stock_quantity) {
		this.stock_quantity = stock_quantity;
	}
	
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	//instance method
	
	public double getTotalInventory() {
		return  (price * stock_quantity);
	}
	
	// Method to add stock quantity
    public void addStock(int quantityToAdd) {
        if (status) { // Only allow adding stock if product is active
            this.stock_quantity += quantityToAdd;
        } else {
            System.out.println("Cannot add stock to a discontinued product.");
        }
    }

    // Method to deduct stock quantity
    public void deductStock(int quantityToDeduct) {
        if (!status) {
            System.out.println("Cannot deduct stock from a discontinued product.");
            return;
        }
        if (quantityToDeduct > 0 && quantityToDeduct <= this.stock_quantity) {
            this.stock_quantity -= quantityToDeduct;
        } else {
            System.out.println("Invalid quantity to deduct.");
        }
    }

    
    // discontinue method
    public void discontinue(boolean status) {
    	this.status = false;
    	if (!status) {
            System.out.println("Product " + this.name + " is now discontinued.");
        }
    }

    // Overriding toString() method
    public String toString() {
        return "\nItem Number: " + item_number +
               "\nProduct Name: " + name +
               "\nQuantity available: " + stock_quantity +
               "\nPrice (RM): " + price +
               "\nInventory value (RM): " + getTotalInventory() +
               "\nProduct status: " + (status ? "Active" : "Discontinued");
               
    }
	
	
}
