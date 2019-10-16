/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classactivity2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author 1894192
 */
@Path("mad")
public class GenericResource {

    @Context
    private UriInfo context;

    public GenericResource() {
    }

    static Connection con = null;
    static PreparedStatement stm = null;
    static ResultSet rs = null;

    static JSONObject conErr = new JSONObject();
    static JSONObject stmErr = new JSONObject();
    static JSONObject rsErr = new JSONObject();

    public Connection createConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "hr", "inf5180");

            if (con != null) {
                System.out.println("con success");
            } else {
                System.out.println("error");
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    @GET
    @Path("getDataMethod")
    @Produces(MediaType.TEXT_PLAIN)
    public void getDataList() {

        int id;
        String street_add, postal_code, city, state_pro, con_id;

        JSONArray mainarrList = new JSONArray();
        JSONObject getDatalist = new JSONObject();

        con = createConnection();
        try {
            String sql = "SELECT LOCATION_ID, STREET_ADDRESS,POSTAL_CODE,CITY,STATE_PROVINCE,COUNTRY_ID FROM LOCATIONS";
            stm = (PreparedStatement) con.createStatement();
            int i = stm.executeUpdate(sql);

            rs = stm.executeQuery(sql);

            getDatalist.accumulate("Status", "OK");
            getDatalist.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

            while (rs.next()) {

                id = rs.getInt("LOCATION_ID");

                street_add = rs.getString("STREET_ADDRESS");
                postal_code = rs.getString("POSTAL_CODE");
                city = rs.getString("CITY");
                state_pro = rs.getString("STATE_PROVINCE");
                con_id = rs.getString("COUNTRY_ID");

                getDatalist.accumulate("id", id);
                getDatalist.accumulate("Street_address", street_add);
                getDatalist.accumulate("Postal_Code", postal_code);
                getDatalist.accumulate("City", city);
                getDatalist.accumulate("State_Province", state_pro);
                getDatalist.accumulate("Country_Id", con_id);

                mainarrList.add(getDatalist);
                getDatalist.clear();
            }
            System.out.println(mainarrList);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @GET
    @Path("getSingleMethod&{location_id}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getSinglelist(@PathParam("location_id") int loc_id) {

        int id;
        String street_add, postal_code, city, state_pro, con_id;

        JSONArray mainarrList = new JSONArray();
        JSONObject getSinglelist = new JSONObject();

        con = createConnection();
        try {
            String sql = "select * from locations where location_id='" + loc_id + "'";

            stm = (PreparedStatement) con.createStatement();
            int i = stm.executeUpdate(sql);

            rs = stm.executeQuery(sql);

            getSinglelist.accumulate("Status", "OK");
            getSinglelist.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

            while (rs.next()) {

                id = rs.getInt("LOCATION_ID");

                street_add = rs.getString("STREET_ADDRESS");
                postal_code = rs.getString("POSTAL_CODE");
                city = rs.getString("CITY");
                state_pro = rs.getString("STATE_PROVINCE");
                con_id = rs.getString("COUNTRY_ID");

                getSinglelist.accumulate("id", id);
                getSinglelist.accumulate("Street_address", street_add);
                getSinglelist.accumulate("Postal_Code", postal_code);
                getSinglelist.accumulate("City", city);
                getSinglelist.accumulate("State_Province", state_pro);
                getSinglelist.accumulate("Country_Id", con_id);

                mainarrList.add(getSinglelist);
                getSinglelist.clear();
            }
            System.out.println(mainarrList);
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
    }

    @GET
    @Path("insertMethod&{LOCATION_ID}&{STREET_ADDRESS}&{POSTAL_CODE}&{CITY}&{STATE_PROVINCE}&{COUNTRY_ID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String location_insert(@PathParam("LOCATION_ID") int loc_Id, @PathParam("STREET_ADDRESS") String street_Add, @PathParam("POSTAL_CODE") String postal_Code, @PathParam("CITY") String city, @PathParam("STATE_PROVINCE") String state_Province, @PathParam("COUNTRY_ID") int con_Id) {

        JSONObject locInsert = new JSONObject();
        con = createConnection();

        try {
            String sql = "insert into LOCATION values('" + loc_Id + "','" + street_Add + "','" + postal_Code + "','" + city + "','" + state_Province + "','" + con_Id + "')";

            stm = (PreparedStatement) con.createStatement();

            int i = stm.executeUpdate(sql);
            if (i > 0) {
                locInsert.accumulate("Status", "OK");
                locInsert.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
                locInsert.accumulate("Message", "employee_location inserted");
            } else {
                locInsert.accumulate("Status", "Error");
                locInsert.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

                locInsert.accumulate("Message", "error in insert");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return locInsert.toString();

    }

    @GET
    @Path("updateMethod&{LOCATION_ID}&{CITY}")
    @Produces(MediaType.TEXT_PLAIN)
    public String location_update(@PathParam("LOCATION_ID") int loc_Id, @PathParam("CITY") String city) {

        JSONObject locUpdate = new JSONObject();
        con = createConnection();

        try {
            String sql = "update LOCATION SET CITY='" + city + "' WHERE LOCATION_ID='" + loc_Id + "')";

            stm = (PreparedStatement) con.createStatement();

            int i = stm.executeUpdate(sql);
            if (i > 0) {
                locUpdate.accumulate("Status", "OK");
                locUpdate.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
                locUpdate.accumulate("Message", "employee_location updated");
            } else {
                locUpdate.accumulate("Status", "Error");
                locUpdate.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

                locUpdate.accumulate("Message", "error in update");
            }

        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return locUpdate.toString();

    }

    @GET
    @Path("deleteMethod&{LOCATION_ID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String location_delete(@PathParam("LOCATION_ID") int loc_Id) {

        JSONObject locDelete = new JSONObject();
        con = createConnection();

        try {
            String sql = "delete FROM LOCATION WHERE LOCATION_ID='" + loc_Id + "'";

            stm = (PreparedStatement) con.createStatement();

            int i = stm.executeUpdate(sql);

            if (i > 0) {
                locDelete.accumulate("Status", "OK");
                locDelete.accumulate("TimeStamp", System.currentTimeMillis() / 1000);
                locDelete.accumulate("Message", "employee_location updated");
            } else {
                locDelete.accumulate("Status", "Error");
                locDelete.accumulate("TimeStamp", System.currentTimeMillis() / 1000);

                locDelete.accumulate("Message", "error in update");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return locDelete.toString();

    }

    public static void closeConnection() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                rsErr.accumulate("message", "Resultset Error");
                System.out.println("Resultset Error");
            }
        }
        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                stmErr.accumulate("message", "Statemnet Error");
                System.out.println("Statement Error");
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                conErr.accumulate("message", "Connection Error");
                System.out.println("Connection Error");
            }
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getText() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /* PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.*/
    @PUT
    @Consumes("text/plain")
    public void putText(String content) {
    }
}
