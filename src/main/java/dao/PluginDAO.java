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
import model.Plugin;

public class PluginDAO implements Serializable {
	
	private static final long serialVersionUID = -5098749675099318542L;

	public List<Plugin> getPlugins() throws DBConsultException, DBConnectException {
		List<Plugin> plugins = new ArrayList<Plugin>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM PLUGIN");
			if(!result.isBeforeFirst()) {
				throw new DBConsultException("Nenhum dado encontrado.");
			}
			while(result.next()) {
				plugins.add(new Plugin(
					result.getInt("id"),
					result.getString("nome"),
					result.getString("descricao"),
					result.getDate("dataCriacao")
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
		return plugins;
	}
}
