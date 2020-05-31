package SchedulingApp.DataBase;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.Models.Address;
import SchedulingApp.Models.City;
import SchedulingApp.Models.Customer;
import SchedulingApp.Models.User;

import javax.print.DocFlavor;
import javax.xml.transform.Result;
import java.sql.*;

public class DataBaseManager {
    private static String SELECT = "SELECT ";
    private static String ALL = "* ";
    private static String INSERT = "INSERT INTO ";
    private static String UPDATE = "UPDATE ";
    private static String FROM = "FROM ";
    private static String JOIN = "JOIN ";
    private static String ON = " ON ";

    private static String CUSTOMER = "customer";
    private static String ADDRESS = "address";
    private static String CITY = "city";
    private static String COUNTRY = "country";

    public DataBaseManager(){

    }
    /**
     * Searches for matching username and password in database
     * @param user
     * @return user if match found
     */
    public static User validateLogin(User user) throws UserNotValidException {
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM user " +
                    "WHERE userName=? AND password=?");
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getPassword());
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userId"));
            } else {
                throw new UserNotValidException("User not found in DB.");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    public static ResultSet getAllAddresses(){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(SELECT +
                    ALL + FROM + " " + ADDRESS + " " +
                    JOIN + CITY + ON + "address.cityId=city.cityId " +
                    JOIN + COUNTRY + ON + "city.countryId=country.countryId;" );
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public static ResultSet getAllCities(){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM city;");
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public static ResultSet getSelectedCities(int countryId){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(SELECT + ALL + FROM
            + CITY + " WHERE countryId=?;"
            );
            pst.setInt(1, countryId);
            rs = pst.executeQuery();
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public static ResultSet getAllCountries(){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM country;");
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }


    /**
     *
     * @param searchKey search key is one of either city, country, address table name
     * @return next available id in either of the city, country, address table
     */
    public static int getNextId(String searchKey){
        int nextAvailableId = 0;
        String searchId = searchKey + "Id";
        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM " + searchKey + " " +
                    "ORDER BY " + searchId + ";");
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                nextAvailableId =  rs.getInt(searchId) + 1;
            }
            rs.close();
        } catch (SQLException ex){
            System.out.println("Failed get Next Id");
        }
        return nextAvailableId;
    }

    /**
     *
     * @param table name of the table to search through
     * @param searchKey name of the thing to search for must be the name
     * @return the next available id in the table with the specific city, country
     */
    public static int getId(String table, String searchKey){
        int id = 0;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM " + table +
                    "WHERE " + table + " = '" + searchKey + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt(table + "Id");
            } else {
                id = getNextId(table);
            }
            rs.close();
        } catch (SQLException ex){
            //TODO: add print statement here
        }
        return id;
    }

    public static ResultSet getAllCustomerData(){
        ResultSet rs = null;
        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM customer " +
                    "JOIN address ON customer.addressId=address.addressId " +
                    "JOIN city ON address.cityId=city.cityId " +
                    "JOIN country ON city.countryId=country.countryId");
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public static ResultSet getAllAppointments(){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM appointment ");
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public static void updateAddress(Address address){
        /**
         * Update address
         */
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("UPDATE address " +
                    "SET address.address=?," +
                    "address.address2=?," +
                    "address.phone=?,"+
                    "address.postalCode=?," +
                    "address.cityId=?," +
                    "WHERE addressId=?;");
            pst.setString(1,address.getAddress());
            pst.setString(2, address.getAddress2());
            pst.setString(3, address.getPhone());
            pst.setString(4, address.getPostalCode());
            pst.setInt(5, address.getCityId());
            pst.setInt(6, address.getAddressId());
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void saveAddress(Address address){
        String columnsToSave = "(address, address2, cityId, createDate, createdBy, lastUpdate, lastUpdateBy, phone, postalCode)";
        String dataToSave = "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement( INSERT + ADDRESS +
                    columnsToSave +
                    "VAlUES" +
                    dataToSave);
            pst.setString(1, address.getAddress());
            pst.setString(2, address.getAddress2());
            pst.setInt(3, address.getCityId());
            pst.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setString(5, State.getUser().getUserName());
            pst.setTimestamp(6, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            pst.setString(7, State.getUser().getUserName());
            pst.setString(8, address.getPhone());
            pst.setString(9, address.getPostalCode());
            pst.executeUpdate();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void updateCustomer(Customer customer){
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(UPDATE + CUSTOMER +
                    "SET " +
                            "customerName=?," +
                            "addressId=?," +
                            "lastUpdate=?," +
                            "lastUpdateBy=?;"

            );
            pst.setString(1, customer.getCustomerName());
            pst.setInt(2, customer.getAddressId());
            pst.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            pst.setString(4, State.getUser().getUserName());
            pst.executeUpdate();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static void saveCustomer(Customer customer){
        String columnsToSave = "(active, customerName, addressId, createDate, createdBy, lastUpdate, lastUpdateBy)";
        String dataToSave = "(1, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(INSERT + CUSTOMER +
                    columnsToSave +
                    "VALUES" +
                    dataToSave);
            pst.setString(1, customer.getCustomerName());
            pst.setInt(2, customer.getAddressId());
            pst.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pst.setString(4, State.getUser().getUserName());
            pst.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            pst.setString(6, State.getUser().getUserName());
            pst.executeUpdate();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
}
