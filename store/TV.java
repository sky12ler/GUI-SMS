package store;

public class TV extends Product {
	private String screenType;
	private String resolution;
	private double displaySize;
	
    public TV(String item_number, String name, int stock_quantity, double price, String screenType,String resolution, double displaySize) {
        super(item_number, name, stock_quantity, price);
        this.screenType=screenType;
        this.resolution=resolution;
        this.displaySize=displaySize;
    }
    
    //getter
    public String getScreenType() {
    	return screenType;
    }
    
    public String getResolution() {
    	return resolution;
    }
    
    public double getDisplaySize() {
    	return displaySize;
    }
    
    //setter
    public void setScreenType(String screenType) {
    	this.screenType = screenType;
    }
    
    public void setResolution(String resolution) {
    	this.resolution = resolution;
    }
    
    public void setdisplaySize(Double displaySize) {
    	this.displaySize = displaySize;
    }
    
    //if use "super", output arrangement cannot same as guideline!
    @Override
    public String toString() {
        return super.toString() +
        		"\nScreen Type: " + screenType +
        		"\nResolution: " + resolution +
        		"\nDisplay Size: " + displaySize;
    }
}