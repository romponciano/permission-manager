package dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DatabaseConnection;
import exception.DBConnectException;
import exception.DBConsultException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public abstract class GenericDAO implements Serializable {
	
	private static final long serialVersionUID = 8957384532133456801L;
	
	private ResultSet result;
	
	public GenericDAO() { }
	
	public List<Object> runSelectSQL(String query) throws DBConnectException, DBConsultException {
		DatabaseConnection db = null;
		Statement statement = null;
		this.result = null;
		List<Object> out = new ArrayList<Object>();
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().createStatement();
			this.result = statement.executeQuery(query);
			if(this.result.isBeforeFirst()) {
				out = populateListOfSelectSQL(this.result);
			}
		}
		catch (DBConnectException e) { throw e; } 
		catch (SQLException e) { throw new DBConsultException(e.getMessage(), e.getCause()); }
		finally {
			try { result.close(); } catch (Exception e) { /* ignorar */ }
			try { statement.close(); } catch (Exception e) { /* ignorar */ }
			db.closeConnection();
		}
		return out;
	}
	
	public abstract List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException;
	
	public void runCreateUpdateDeleteSQL(String sql) throws DBConnectException, SQLException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement(sql);
			statment.executeUpdate();
		} finally {
			try { statment.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
	}
	
	public Long runOracleCallableStatement(String query) throws SQLException, DBConnectException {
		DatabaseConnection db = null;
		OracleCallableStatement cs = null;
		try {
			db = new DatabaseConnection();
			cs = (OracleCallableStatement) db.getConnection().prepareCall(query);
			cs.registerOutParameter(1, OracleTypes.NUMBER );
			cs.execute();
			return cs.getLong(1);
		} finally {
			try { cs.close(); } catch (Exception e) { }
		    db.closeConnection();
		}
	}
}
