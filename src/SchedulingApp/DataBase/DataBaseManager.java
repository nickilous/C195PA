package SchedulingApp.DataBase;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.Models.Customer;
import SchedulingApp.Models.User;

import javax.xml.transform.Result;
import java.sql.*;

public class DataBaseManager {
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
                    "FROM " + searchKey +
                    "ORDER BY " + searchId);
            ResultSet rs = pst.executeQuery();
            if (rs.last()){
                nextAvailableId =  rs.getInt("countryId");
            }
            rs.close();
        } catch (SQLException ex){
            //TODO: add print statement here
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
        int countryId = 0;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM " + table +
                    "WHERE " + table + " = '" + searchKey + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                countryId = rs.getInt(table + "Id");
            } else {
                countryId = getNextId(table);
            }
            rs.close();
            return countryId;
        } catch (SQLException ex){
            //TODO: add print statement here
        }
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
}
