package dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.Const;
import common.DatabaseConnection;
import exception.DBConnectException;
import exception.DBConsultException;
import model.User;

public class UserDAO implements Serializable {
	
	private static final long serialVersionUID = -1503189464001292373L;

	public List<User> getUsers() throws DBConsultException, DBConnectException {
		List<User> usrs = new ArrayList<User>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM USUARIO");
			if(!result.isBeforeFirst()) {
				throw new DBConsultException("Nenhum dado encontrado.");
			}
			while(result.next()) {
				usrs.add(new User(
					result.getInt("id"),
					result.getString("nomeCompleto"),
					result.getString("login"),
					result.getInt("status"),
					result.getString("gerenciaAtual")
				));
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(Const.ERROR_DB_CONSULT);
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
		return usrs;
	}
}
