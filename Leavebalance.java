package employeeManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Leavebalance {
	private static final String userName = "CISADM";
	private static final String password = "CISADM";
	private static final String url ="jdbc:oracle:thin:@//localhost:1521/orcl";
	
	private int emp_id, leave_balance,leave_used;
	private Date startDate,endDate;
	private String status;

	public int getEmpId() {
		return emp_id;
	}
	public int getLeaveBalance() {
		return leave_balance;
	}
	public int getLeaveUsed() {
		return leave_used;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date endDate() {
		return endDate;
	}
	public String getStatus() {
		return status;
	}
	private void setStatus(String string) {
		this.status=string;
		
	}
	
	//methods 
	public void approveRejectLeave(int emp_id, int daysRequested) throws SQLException {
	    Connection connection = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    try {
	        connection = DriverManager.getConnection(url, userName, password);
	        String query = "SELECT leave_balance, leave_used,status FROM leave_balance WHERE emp_id = ?";
	        ps = connection.prepareStatement(query);
	        ps.setInt(1, emp_id);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            int balance = rs.getInt("leave_balance");
	            int used = rs.getInt("leave_used");
	            //String status = rs.getString("status");
	            if (balance >= daysRequested && daysRequested <= 2) {
	                query = "UPDATE leave_balance SET leave_balance = ?, leave_used = ?, status=? WHERE emp_id = ?";
	                ps = connection.prepareStatement(query);
	                ps.setInt(1, balance - daysRequested);
	                ps.setInt(2, used + daysRequested);
	                ps.setString(3, "Approved");
	                ps.setInt(4, emp_id);
	                ps.executeUpdate();
	                setStatus("Approved");
	                System.out.println("Your Leave Request is Approaved");
	            } else {
	            	query = "UPDATE leave_balance SET status=? WHERE emp_id=?";
	            	ps=connection.prepareStatement(query);
	            	ps.setString(1,"Rejected");
	            	ps.setInt(2, emp_id);
	            	ps.executeUpdate();
	                setStatus("Insufficient balance or leave duration is not approved");
	                System.out.println("Insufficient balance or leave duration is not approved");
	            }
	        } else {
	            setStatus("Employee ID not found");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error updating leave balance: " + e.getMessage());
	    } finally {
	        if (rs != null) {
	            rs.close();
	        }
	        if (ps != null) {
	            ps.close();
	        }
	        if (connection != null) {
	            connection.close();
	        }
	    }
	}
	
}
