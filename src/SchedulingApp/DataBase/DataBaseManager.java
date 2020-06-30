package SchedulingApp.DataBase;

import SchedulingApp.AppState.State;
import SchedulingApp.Exceptions.UserNotValidException;
import SchedulingApp.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;


public class DataBaseManager {
    private static String SELECT = "SELECT ";
    private static String ALL = "* ";
    private static String INSERT = "INSERT INTO ";
    private static String UPDATE = "UPDATE ";
    private static String DELETE = "DELETE ";
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
                    "WHERE userName=? AND password=?;");
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
    public static ResultSet getAllUsers(){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM user;");
            rs = pst.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return rs;
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
     * @param pst the state to find the next id with
     * @return
     */
    public static int getId(PreparedStatement pst){
        int id = 0;
        try{
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        } catch (SQLException ex){

        }
        return id;
    }

    public static ResultSet getAllActiveCustomerData(){
        ResultSet rs = null;
        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM customer " +
                    "JOIN address ON customer.addressId=address.addressId " +
                    "JOIN city ON address.cityId=city.cityId " +
                    "JOIN country ON city.countryId=country.countryId " +
                    "WHERE active=1;"
            );
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }
    public static ResultSet getAllInActiveCustomerData(){
        ResultSet rs = null;
        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM customer " +
                    "JOIN address ON customer.addressId=address.addressId " +
                    "JOIN city ON address.cityId=city.cityId " +
                    "JOIN country ON city.countryId=country.countryId " +
                    "WHERE active=0;");
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }

    public static ResultSet getAllAppointmentsByUser(User user){
        ResultSet rs = null;
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("SELECT * " +
                    "FROM appointment " +
                    "WHERE userId=?;");
            pst.setInt(1, user.getUserID());
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
                    "FROM appointment;");
            rs = pst.executeQuery();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return rs;
    }
    public static ResultSet getUpcomingAppt(User user) {
        String getUpcomingApptSQL = "SELECT * FROM appointment "
                + "WHERE (start BETWEEN ? AND ADDTIME(NOW(), '00:15:00') AND userId=?)";
        ResultSet rs = null;
        try {
            PreparedStatement stmt = DBConnection.getConnection().prepareStatement(getUpcomingApptSQL);
            ZonedDateTime localZT = ZonedDateTime.now(State.getzId());
            ZonedDateTime zdtUTC = localZT.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime localUTC = zdtUTC.toLocalDateTime();
            stmt.setTimestamp(1, Timestamp.valueOf(localUTC));
            stmt.setInt(2, State.getUser().getUserID());
            rs = stmt.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public static ObservableList<Appointment> getOverlappingAppts(LocalDateTime start, LocalDateTime end) {
        ObservableList<Appointment> getOverlappedAppts = FXCollections.observableArrayList();
        String getOverlappingApptsSQL = "SELECT * FROM appointment "
                + "WHERE (start >= ? AND end <= ?) "
                + "OR (start <= ? AND end >= ?) "
                + "OR (start BETWEEN ? AND ? OR end BETWEEN ? AND ?)";

        try {
            LocalDateTime startLDT = start.atZone(State.getzId()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime endLDT = end.atZone(State.getzId()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(getOverlappingApptsSQL);
            pst.setTimestamp(1, Timestamp.valueOf(startLDT));
            pst.setTimestamp(2, Timestamp.valueOf(endLDT));
            pst.setTimestamp(3, Timestamp.valueOf(startLDT));
            pst.setTimestamp(4, Timestamp.valueOf(endLDT));
            pst.setTimestamp(5, Timestamp.valueOf(startLDT));
            pst.setTimestamp(6, Timestamp.valueOf(endLDT));
            pst.setTimestamp(7, Timestamp.valueOf(startLDT));
            pst.setTimestamp(8, Timestamp.valueOf(endLDT));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Appointment overlappedAppt = new Appointment();
                overlappedAppt.setAppointmentId(rs.getInt("appointmentId"));
                overlappedAppt.setTitle(rs.getString("title"));
                overlappedAppt.setDescription(rs.getString("description"));
                overlappedAppt.setLocation(rs.getString("location"));
                overlappedAppt.setContact(rs.getString("contact"));
                overlappedAppt.setType(rs.getString("type"));
                overlappedAppt.setUrl(rs.getString("url"));
                LocalDateTime startUTC = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endUTC = rs.getTimestamp("end").toLocalDateTime();
                ZonedDateTime startLocal = ZonedDateTime.ofInstant(startUTC.toInstant(ZoneOffset.UTC), State.getzId());
                ZonedDateTime endLocal = ZonedDateTime.ofInstant(endUTC.toInstant(ZoneOffset.UTC), State.getzId());
                overlappedAppt.setStart(startLocal);
                overlappedAppt.setEnd(endLocal);
                getOverlappedAppts.add(overlappedAppt);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return getOverlappedAppts;
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
    public static int saveAddress(Address address){
        int nextId = 0;
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
            nextId = getId(pst);
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return nextId;
    }
    public static int saveCity(City city) {
        int nextId = 0;
        String addCitySQL = String.join(" ",
                "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)",
                "VALUES (?, ?, NOW(), ?, NOW(), ?)");
        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(addCitySQL);
            pst.setString(1, city.getCity());
            pst.setInt(2, city.getCountryId());
            pst.setString(3, State.getUser().getUserName());
            pst.setString(4, State.getUser().getUserName());
            pst.executeUpdate();
            nextId = getId(pst);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

    public static void updateCustomer(Customer customer){
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(UPDATE + CUSTOMER +
                    " SET " +
                            "customerName=?," +
                            "addressId=?," +
                            "lastUpdate=?," +
                            "lastUpdateBy=? " +
                    "WHERE " +
                            "customerId=?;"

            );
            pst.setString(1, customer.getCustomerName());
            pst.setInt(2, customer.getAddressId());
            pst.setTimestamp(3, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            pst.setString(4, State.getUser().getUserName());
            pst.setInt(5, customer.getCustomerId());
            pst.executeUpdate();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public static int saveCustomer(Customer customer){
        int nextId = 0;
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
            nextId = getId(pst);
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return nextId;
    }
    public static void deleteCustomer(Customer customer){
        try{
            PreparedStatement pst = DBConnection.getConnection().prepareStatement("UPDATE customer " +
                    "SET active=0"+
                    " WHERE " +
                        "customerId=?;");
            pst.setInt(1, customer.getCustomerId());
            pst.executeUpdate();
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static int saveAppointment(Appointment appt){
        int nextId = 0;
        String addAppointmentSQL = String.join(" ",
                "INSERT INTO appointment (customerId, userId, title, "
                        + "description, location, contact, type, url, start, end, "
                        + "createDate, createdBy, lastUpdate, lastUpdateBy) ",
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");

        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(addAppointmentSQL);
            pst.setObject(1, appt.getCustomerId());
            pst.setObject(2, State.getUser().getUserID());
            pst.setObject(3, appt.getTitle());
            pst.setObject(4, appt.getDescription());
            pst.setObject(5, appt.getLocation());
            pst.setObject(6, appt.getContact());
            pst.setObject(7, appt.getType());
            pst.setObject(8, appt.getUrl());

            ZonedDateTime startZDT = appt.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appt.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            pst.setTimestamp(9, Timestamp.valueOf(startZDT.toLocalDateTime()));
            pst.setTimestamp(10, Timestamp.valueOf(endZDT.toLocalDateTime()));

            pst.setString(11, State.getUser().getUserName());
            pst.setString(12, State.getUser().getUserName());
            pst.executeUpdate();

            nextId = getId(pst);
        }
        catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        return nextId;
    }
    public static void updateAppointment(Appointment appointment) {
        String updateApptSQL = String.join(" ",
                "UPDATE appointment",
                "SET customerId=?, userId=?, title=?, description=?, location=?," +
                        "contact=?, type=?, url=?, start=?, end=?, lastUpdate=NOW(), lastUpdateBy=?",
                "WHERE appointmentId=?");

        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(updateApptSQL);
            pst.setObject(1, appointment.getCustomerId());
            pst.setObject(2, appointment.getUserId());
            pst.setObject(3, appointment.getTitle());
            pst.setObject(4, appointment.getDescription());
            pst.setObject(5, appointment.getLocation());
            pst.setObject(6, appointment.getContact());
            pst.setObject(7, appointment.getType());
            pst.setObject(8, appointment.getUrl());

            ZonedDateTime startZDT = appointment.getStart().withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endZDT = appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC"));
            pst.setTimestamp(9, Timestamp.valueOf(startZDT.toLocalDateTime()));
            pst.setTimestamp(10, Timestamp.valueOf(endZDT.toLocalDateTime()));

            pst.setString(11, State.getUser().getUserName());
            pst.setObject(12, appointment.getAppointmentId());
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void deleteAppointment(Appointment appointment) {
        String deleteAppointmentSQL = "DELETE FROM appointment WHERE appointmentId = ?";

        try {
            PreparedStatement pst = DBConnection.getConnection().prepareStatement(deleteAppointmentSQL);
            pst.setObject(1, appointment.getAppointmentId());
            pst.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
