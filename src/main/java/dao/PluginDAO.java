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
import exception.DBCreateException;
import exception.DBDataNotFoundException;
import exception.DBDeleteException;
import exception.DBUpdateException;
import model.Plugin;

public class PluginDAO implements Serializable {
	
	private static final long serialVersionUID = -5098749675099318542L;

	public List<Plugin> getPlugins() throws DBConsultException, DBConnectException, DBDataNotFoundException {
		List<Plugin> plugins = new ArrayList<Plugin>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM PLUGIN");
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Plugin plg = new Plugin();
				plg.setId(result.getInt("id"));
				plg.setNome(result.getString("nome"));
				plg.setDescricao(result.getString("descricao"));
				plg.setDataCriacaoFromDate(result.getDate("dataCriacao"));
				plugins.add(plg);
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
	
	public void createPlugin(Plugin plugin) throws DBConnectException, DBCreateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("INSERT INTO PLUGIN (nome, descricao, dataCriacao) VALUES (?, ?, TO_DATE(?,'YYYY-MM-DD'))");
			statment.setString(1, plugin.getNome());
			statment.setString(2, plugin.getDescricao());
			statment.setString(3, plugin.getDataCriacaoToString());
			statment.executeUpdate();
		} catch(SQLException e) {
			throw new DBCreateException(e.getMessage(), e.getCause());
		} catch(DBConnectException e) {
			throw e;
		} finally {
			try { statment.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
	}
	
	public void updatePlugin(Plugin plugin) throws DBConnectException, DBUpdateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("UPDATE PLUGIN SET nome = ?, descricao = ? WHERE id = ?");
			statment.setString(1, plugin.getNome());
			statment.setString(2,  plugin.getDescricao());
			statment.setInt(3,  plugin.getId());
			statment.executeUpdate();
		} catch(SQLException e) {
			throw new DBUpdateException(e.getMessage(), e.getCause());
		} catch(DBConnectException e) {
			throw e;
		} finally {
			try { statment.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
	}
	
	public void deletePlugin(int pluginId) throws DBConnectException, DBDeleteException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("DELETE FROM FUNCIONALIDADE WHERE pluginId = ?");
			statment.setInt(1, pluginId);
			statment.executeUpdate();
			statment = db.getConnection().prepareStatement("DELETE FROM PLUGIN WHERE id = ?");
			statment.setInt(1, pluginId);
			statment.executeUpdate();
		} catch(SQLException e) {
			throw new DBDeleteException(e.getMessage(), e.getCause());
		} catch(DBConnectException e) {
			throw e;
		} finally {
			try { statment.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
	}
}
