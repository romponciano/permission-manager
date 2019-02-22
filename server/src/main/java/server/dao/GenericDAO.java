package server.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.DatabaseConnection;
import common.exceptions.DBConnectException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import server.exceptions.DBConsultException;

public abstract class GenericDAO implements Serializable {

	private static final long serialVersionUID = 8957384532133456801L;

	private ResultSet result;

	public GenericDAO() {
	}

	public List<Object> runSelectSQL(String query, Object... args) throws DBConnectException, DBConsultException {
		DatabaseConnection db = null;
		PreparedStatement statement = null;
		this.result = null;
		List<Object> out = new ArrayList<Object>();
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().prepareStatement(query);
			fillPreparedStatementArguments(statement, args);
			this.result = statement.executeQuery();
			if (this.result.isBeforeFirst()) {
				out = populateListOfSelectSQL(this.result);
			}
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
			try {
				result.close();
			} catch (Exception e) {
				/* ignorar */ }
			try {
				statement.close();
			} catch (Exception e) {
				/* ignorar */ }
			db.closeConnection();
		}
		return out;
	}

	public abstract List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException;

	public void runCreateUpdateDeleteSQL(String sql, Object... args) throws DBConnectException, SQLException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement(sql);
			fillPreparedStatementArguments(statment, args);
			statment.executeUpdate();
		} finally {
			try {
				statment.close();
			} catch (Exception e) {
				/* ignorar */ }
			db.closeConnection();
		}
	}

	public Long runOracleCallableStatement(String query, Object... args) throws SQLException, DBConnectException {
		DatabaseConnection db = null;
		OracleCallableStatement cs = null;
		try {
			db = new DatabaseConnection();
			cs = (OracleCallableStatement) db.getConnection().prepareCall(query);
			int posFinal = fillOCSArguments(cs, args);
			cs.registerOutParameter(posFinal, OracleTypes.NUMBER);
			cs.execute();
			return cs.getLong(posFinal);
		} finally {
			try {
				cs.close();
			} catch (Exception e) {
			}
			db.closeConnection();
		}
	}

	private int fillOCSArguments(OracleCallableStatement cs, Object[] args) throws SQLException {
		int pos = 1;
		for (Object argument : args) {
			if (argument instanceof Long) {
				cs.setLong(pos++, (Long) argument);
			} else if (argument instanceof String) {
				cs.setString(pos++, (String) argument);
			} else if (argument instanceof Date) {
				java.sql.Date sqlDate = new java.sql.Date(((Date) argument).getTime());
				cs.setDate(pos++, sqlDate);
			}
		}
		return pos;
	}
	
	private int fillPreparedStatementArguments(PreparedStatement state, Object[] args) throws SQLException {
		int pos = 1;
		for (Object argument : args) {
			if (argument instanceof Long) {
				state.setLong(pos++, (Long) argument);
			} else if (argument instanceof String) {
				state.setString(pos++, (String) argument);
			} else if (argument instanceof Date) {
				java.sql.Date sqlDate = new java.sql.Date(((Date) argument).getTime());
				state.setDate(pos++, sqlDate);
			}
		}
		return pos;
	}

}
