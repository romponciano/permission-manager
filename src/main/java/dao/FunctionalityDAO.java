package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DatabaseConnection;
import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBDataNotFoundException;
import model.Functionality;

public class FunctionalityDAO implements Serializable {
	
	private static final long serialVersionUID = 2933870645847680620L;

	public List<Functionality> getFunctionalities() throws DBConsultException, DBConnectException, DBDataNotFoundException {
		List<Functionality> plugins = new ArrayList<Functionality>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM Functionality");
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				plugins.add(new Functionality(
					result.getInt("id"),
					result.getString("nome"),
					result.getString("descricao"),
					result.getDate("dataCriacao")
				));
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
		return plugins;
	}
}
