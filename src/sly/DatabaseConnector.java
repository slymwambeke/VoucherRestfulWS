package sly;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mwambeke
 */
public class DatabaseConnector {
    public Connection conn;
	
    public DatabaseConnector(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String sysURL = "jdbc:mysql://localhost/safaricom_hackathon";
            conn = DriverManager.getConnection(sysURL,"root","");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
           
        }catch(SQLException ex){
        	Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } 
        catch(Exception ex){
        	Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } 
   }
    
    /***
    * 
    * @return Connection conn
    */
   public Connection getConn(){
        return conn;
   }
   
   public void closeConnect() {
        try {
            System.out.println("Closing database connections");
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.WARNING, null, ex);
            ex.printStackTrace();
        }
    }
}

