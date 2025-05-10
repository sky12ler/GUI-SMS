package store;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;

/* ============================ STOCK MANAGEMENT DATABASE HANDLER ============================ */
public class DatabaseHandler {

 /* ======================== DATA FIELDS and GETTER ======================= */
 // Declaration of ArrayLists
 public static ArrayList<Product> products = new ArrayList<Product>();
 public static ArrayList<TV> tvs = new ArrayList<TV>();
 public static ArrayList<Refrigerator> refrigerators = new ArrayList<Refrigerator>();
 public static ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
 public static ArrayList<StockManagement> stockManagements = new ArrayList<StockManagement>();

 // Database path
 private static String databasePath = "jdbc:ucanaccess://" +
         System.getProperty("user.dir").replace("\\", "/") + 
         "/src/resources/StockManagement.accdb";

 // Status flags
 private static boolean connect = false;
 private static boolean storeData = false;
 
 public static boolean isConnect() {
	    return connect;
	    }
 
 public static boolean isStore() {
	    return storeData;
	    }

 /* ================ Confirm if user wants to store data ================ */
 public static boolean storePermission() {
     int store = JOptionPane.showConfirmDialog(null,
             "Welcome to Stock Management System!\n\n"
             + "Would you like to store your data in the database?\n\n"
             + "*Note: You can continue without database storage.",
             "Data Storage Permission", JOptionPane.YES_NO_OPTION);

     if (store == JOptionPane.YES_OPTION) {
         storeData = true;
     }
     return storeData;
 }

 /* ================ Check Database File ================ */
 private static boolean checkFile() {
     boolean fileExist = false;
     String filePath = System.getProperty("user.dir").replace("\\", "/") + "/src/resources/StockManagement.accdb";
     File accessFile = new File(filePath);

     if (accessFile.exists() && !accessFile.isDirectory()) {
         fileExist = true;
     } else {
         fileExist = false;
         JOptionPane.showMessageDialog(null, "StockManagement.accdb file cannot be found!", "Warning", JOptionPane.WARNING_MESSAGE);

         int createFile = JOptionPane.showConfirmDialog(null, 
                 "Would you like to create a new StockManagement.accdb file?", 
                 "File Creation", JOptionPane.YES_NO_OPTION);

         if (createFile == JOptionPane.YES_OPTION) {
             try {
                 Database db = new DatabaseBuilder(new File(filePath))
                         .setFileFormat(Database.FileFormat.V2000)
                         .create();
                 db.close();
                 JOptionPane.showMessageDialog(null, "Successfully created StockManagement.accdb file!", "File Created", JOptionPane.INFORMATION_MESSAGE);
                 fileExist = true;
                 storeData = true;
             } catch (IOException e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(null, "Failed to create StockManagement.accdb file!", "Error", JOptionPane.ERROR_MESSAGE);
             }
         }
     }
     return fileExist;
 }

 /* ================ Check Connection to Database ================ */
 public static boolean getConnection() throws SQLException, ClassNotFoundException {
     if (checkFile()) {
         Connection connection = DriverManager.getConnection(databasePath);
         if (connection != null) {
             connect = true;
             JOptionPane.showMessageDialog(null, "Successfully connected to Inventory Database!", "Connected", JOptionPane.INFORMATION_MESSAGE);
         } else {
             connect = false;
             JOptionPane.showMessageDialog(null, "Failed to connect to Inventory Database!", "Warning", JOptionPane.WARNING_MESSAGE);
         }
         connection.close();
     }
     return connect;
 }

 /* ================ Initialize database and ArrayLists ================ */
 public static void initialiseDatabase() throws SQLException, ClassNotFoundException {
     Connection connection = DriverManager.getConnection(databasePath);
     Statement statement = connection.createStatement();
     DatabaseMetaData metaData = connection.getMetaData();

     /* ============================ PRODUCT ============================ */
     ResultSet productTableCheck = metaData.getTables(null, null, "Product", null);
     if (!productTableCheck.next()) {
         statement.executeUpdate(
                 "create table Product(item_number varchar(20), name varchar(100), stock_quantity int, price double, status boolean)");
     }

     ResultSet productCount = statement.executeQuery("SELECT COUNT(*) FROM Product");
     if (productCount.next()) {
         int recordCount = productCount.getInt(1);
         if (recordCount == 0) {
             // No default products to add
         }
     }
     
     ResultSet productResultSet = statement.executeQuery("select * from Product");
     while (productResultSet.next()) {
         Product product = new GeneralProduct( 
                 productResultSet.getString(1),
                 productResultSet.getString(2),
                 productResultSet.getInt(3),
                 productResultSet.getDouble(4));
         product.setStatus(productResultSet.getBoolean(5));
         products.add(product);
     }

     /* ============================ TV ============================ */
     ResultSet tvTableCheck = metaData.getTables(null, null, "TV", null);
     if (!tvTableCheck.next()) {
         statement.executeUpdate(
                 "create table TV(item_number varchar(20), screenType varchar(50), resolution varchar(50), displaySize double)");
     }

     ResultSet tvResultSet = statement.executeQuery("select * from TV");
     while (tvResultSet.next()) {
         for (Product p : products) {
             if (p.getItemNumber().equals(tvResultSet.getString(1))) {
                 TV tv = new TV(
                         p.getItemNumber(),
                         p.getName(),
                         p.getStockQuantity(),
                         p.getPrice(),
                         tvResultSet.getString(2),
                         tvResultSet.getString(3),
                         tvResultSet.getDouble(4));
                 tv.setStatus(p.getStatus());
                 tvs.add(tv);
             }
         }
     }

     /* ============================ REFRIGERATOR ============================ */
     ResultSet refrigeratorTableCheck = metaData.getTables(null, null, "Refrigerator", null);
     if (!refrigeratorTableCheck.next()) {
         statement.executeUpdate(
                 "create table Refrigerator(item_number varchar(20), doorDesign varchar(50), color varchar(50), capacity int)");
     }

     ResultSet refrigeratorResultSet = statement.executeQuery("select * from Refrigerator");
     while (refrigeratorResultSet.next()) {
         for (Product p : products) {
             if (p.getItemNumber().equals(refrigeratorResultSet.getString(1))) {
                 Refrigerator refrigerator = new Refrigerator(
                         p.getItemNumber(),
                         p.getName(),
                         p.getStockQuantity(),
                         p.getPrice(),
                         refrigeratorResultSet.getString(2),
                         refrigeratorResultSet.getString(3),
                         refrigeratorResultSet.getInt(4));
                 refrigerator.setStatus(p.getStatus());
                 refrigerators.add(refrigerator);
             }
         }
     }

     /* ============================ USERINFO ============================ */
     ResultSet userInfoTableCheck = metaData.getTables(null, null, "UserInfo", null);
     if (!userInfoTableCheck.next()) {
    	 statement.executeUpdate(
    			    "CREATE TABLE UserInfo (" +
    			    "username VARCHAR(50) PRIMARY KEY, " +
    			    "passwordHash VARCHAR(64), " +
    			    "user_role VARCHAR(20)" +
    			    ")"
    			);
     }

     ResultSet userInfoResultSet = statement.executeQuery("select * from UserInfo");
     while (userInfoResultSet.next()) {
         UserInfo userInfo = new UserInfo( 
                 userInfoResultSet.getString(1), //username
                 userInfoResultSet.getString(2), //passwordHash
                 userInfoResultSet.getString(3));
         userInfos.add(userInfo);
     }

     /* ============================ STOCKMANAGEMENT ============================ */
     ResultSet stockManagementTableCheck = metaData.getTables(null, null, "StockManagement", null);
     if (!stockManagementTableCheck.next()) {
         statement.executeUpdate(
                 "create table StockManagement(item_number varchar(20), product_name varchar(100), action varchar(50), quantity int, date date)");
     }

     ResultSet stockManagementResultSet = statement.executeQuery("select * from StockManagement");
     while (stockManagementResultSet.next()) {
         StockManagement stockManagement = new StockManagement( 
                 stockManagementResultSet.getString(1),
                 stockManagementResultSet.getString(2),
                 stockManagementResultSet.getString(3),
                 stockManagementResultSet.getInt(4),
                 stockManagementResultSet.getDate(5));
         stockManagements.add(stockManagement);
     }

     connection.close();
 }

 /* ===================== ADD ITEM to Database ===================== */
 public static void addDatabase(String command) {
     if (connect && storeData) {
         try {
             Connection connection = DriverManager.getConnection(databasePath);
             connection.createStatement().executeUpdate(command);
             connection.close();
         } catch (SQLException e1) {
             e1.printStackTrace();
         }
     }
 }

 /* ===================== REMOVE ITEM from Database ===================== */
 public static void removeDatabase(String command) {
     if (connect && storeData) {
         try {
             Connection connection = DriverManager.getConnection(databasePath);
             connection.createStatement().executeUpdate(command);
             connection.close();
         } catch (SQLException e1) {
             e1.printStackTrace();
         }
     }
 }
}

