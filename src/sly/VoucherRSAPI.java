package sly;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 
@Path("/VoucherRSAPI")
public class VoucherRSAPI {
 
	@Path("{amount}")
	@POST
	@Produces("application/json")
	public Response createVoucher(@PathParam("amount") int amount) throws JSONException {
		
		String voucherNumber = formatDigits(generateUniqueVoucherCode(16, 4), 4);
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection conn = databaseConnector.getConn();
		
		String insertSQL_voucher  = "INSERT INTO vouchers(voucher_num,expiry_date, amount) "
				+ "values(?,NOW() + INTERVAL 5 MINUTE,?)";
		PreparedStatement preparedStatement_voucher  ;
		try {
			preparedStatement_voucher   = conn.prepareStatement(insertSQL_voucher);
			preparedStatement_voucher.setString(1, voucherNumber);	
			preparedStatement_voucher.setInt(2, amount);
									
			preparedStatement_voucher.executeUpdate();		
			
			try {
        		Functions.writeToFile("access.log", "Executed. voucherNumber added to the database");
	        }
	        catch(IOException ex) {
	        	ex.printStackTrace();
	        }
			catch(NullPointerException e){
				e.printStackTrace();
			}
			           
            
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
        		Functions.writeToFile("access.log","SQL Exception: "+e.getMessage().toString());
	        }
			catch(IOException ex) {
	        	ex.printStackTrace();
	        }
			catch(NullPointerException ex){
				ex.printStackTrace();
			}
		}
 
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("created", true);
		jsonObject.put("voucherNumber", voucherNumber);
 
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}
	
	@Path("{duration}")
	@GET
	@Produces("application/json")
	public Response voucherNumLastMinutes(@PathParam("duration") int duration) throws JSONException {
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection conn = databaseConnector.getConn();
		
		 String selectVoucher = "SELECT "
					+ "voucher_num "
					+ "FROM vouchers "
					+ "WHERE created_at > NOW()- INTERVAL ? MINUTE ";
		 
		 System.out.println(selectVoucher);
		 
		 JSONArray jsonArray = new JSONArray();
			
		 
		    PreparedStatement pstmtVoucher = null;
	        ResultSet rsVoucher = null;
	        String voucherNumber = null;
			
			try {
				pstmtVoucher = conn.prepareStatement(selectVoucher);
				pstmtVoucher.setInt(1, duration);
				rsVoucher = pstmtVoucher.executeQuery();	
		       
		        while (rsVoucher.next()) {
		        	JSONObject jsonObject = new JSONObject();
		        	voucherNumber = rsVoucher.getString("voucher_num");
		        	jsonObject.put("voucherNumber", voucherNumber);
		        	jsonArray.put(jsonObject);
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}	
 
		
		
 
		String result = jsonArray.toString();
		return Response.status(200).entity(result).build();
	}
	
 
	@Path("{s}/{sNo}")
	@GET
	@Produces("application/json")
	public Response voucherNoBySerialNo(@PathParam("sNo") String s, @PathParam("sNo") String sNo) throws JSONException {
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection conn = databaseConnector.getConn();
		
		 String selectVoucher = "SELECT "
					+ "voucher_num "
					+ "FROM vouchers "
					+ "WHERE serial_num = ? ";
		 
		 System.out.println(selectVoucher +", sNo: "+sNo);
		 
		    PreparedStatement pstmtVoucher = null;
	        ResultSet rsVoucher = null;
	        String voucherNumber = null;
	        
	        JSONObject jsonObject = new JSONObject();
			
			
			try {
				pstmtVoucher = conn.prepareStatement(selectVoucher);
				pstmtVoucher.setString(1, sNo);
				rsVoucher = pstmtVoucher.executeQuery();	
		       
		        while (rsVoucher.next()) {
		        	voucherNumber = rsVoucher.getString("voucher_num");
		        	jsonObject.put("voucherNumber", voucherNumber);
					jsonObject.put("sNo", sNo);
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}			
 
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}
	
	@PUT
	@Produces("application/json")		
	public Response updateActiveVouchertoInactive() throws JSONException {
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection conn = databaseConnector.getConn();
		
		 String updateVoucher = "UPDATE "
					+ "vouchers "
					+ "SET status = ? "
					+ "WHERE status = ? "
					+ "AND created_at > NOW()- INTERVAL 5 MINUTE";
		 
		    PreparedStatement pstmtVoucher = null;
			
	        JSONObject jsonObject = new JSONObject();
			
			
			try {
				pstmtVoucher = conn.prepareStatement(updateVoucher);
				pstmtVoucher.setInt(1, 0);
				pstmtVoucher.setInt(2, 1);
				pstmtVoucher.executeUpdate();
				jsonObject.put("updated", true);
		       
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jsonObject.put("updated", false);
			}	
 
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	
	@DELETE
	@Produces("application/json")
	public Response deleteAllInactiveVouchers() throws JSONException {
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection conn = databaseConnector.getConn();
		
		 /*String deleteVoucher = "DELETE "
					+ "FROM vouchers "
					+ "WHERE is_active = ?"; */
		 
		 String deleteVoucher = "UPDATE "
					+ "vouchers "
					+ "SET is_deleted = 1 "
					+ "WHERE is_active = ?";
		 
		    PreparedStatement pstmtVoucher = null;
	        String voucherNumber = null;
			
	        JSONObject jsonObject = new JSONObject();
			
			
			try {
				pstmtVoucher = conn.prepareStatement(deleteVoucher);
				pstmtVoucher.setInt(1, 0);
				pstmtVoucher.executeUpdate();
				jsonObject.put("deleted", true);
		       
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jsonObject.put("deleted", false);
			}	
 
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}
	

	@Path("{vNo}")
	@PUT
	@Produces("application/json")		
	public Response updateVoucher(@PathParam("vNo") String vNo) throws JSONException {
		
		DatabaseConnector databaseConnector = new DatabaseConnector();
		Connection conn = databaseConnector.getConn();
		
		 String updateVoucher = "UPDATE "
					+ "vouchers "
					+ "SET status = ? "
					+ "WHERE voucher_num = ? ";
		 
		    PreparedStatement pstmtVoucher = null;
			
	        JSONObject jsonObject = new JSONObject();
			
			
			try {
				pstmtVoucher = conn.prepareStatement(updateVoucher);
				pstmtVoucher.setInt(1, 2);
				pstmtVoucher.setString(2, vNo);
				pstmtVoucher.executeUpdate();
				jsonObject.put("vNo", vNo);
				jsonObject.put("update", true);
		       
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jsonObject.put("vNo", vNo);
				jsonObject.put("updated", false);
			}	
 
		String result = jsonObject.toString();
		return Response.status(200).entity(result).build();
	}

	private  String generateUniqueVoucherCode(int digit, int charCnt) {
	    long nano = System.nanoTime();
	    Random rand = new Random();
	    StringBuilder util = new StringBuilder(String.valueOf(nano));
	    System.out.println("String.valueOf(nano): "+String.valueOf(nano));
	    System.out.println("Hapa: "+util.toString());
	    util = util.reverse();
	   
	    System.out.println("Hapa: "+util.toString());
	    return rand.nextInt(10)+util.toString()+rand.nextInt(10);
	}
	
	private  String formatDigits(String original, int num){
	    System.out.println("Before formatting " + original);
	    return original.substring(0, num) + "-" + original.substring(num, 2*num)
	            + "-" + original.substring(2*num, 3*num) + "-" + original.substring(3*num, 4*num);
	}
}
