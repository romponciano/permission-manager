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
import model.Functionality;

public class FunctionalityDAO implements Serializable {
	
	private static final long serialVersionUID = 2933870645847680620L;

	public List<Functionality> getFunctionalities() throws DBConsultException, DBConnectException, DBDataNotFoundException {
		List<Functionality> funcs = new ArrayList<Functionality>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT " + 
					"	f.id, " + 
					"	f.nome, " + 
					"	f.descricao, " +
					"   f.dataCriacao, " +
					"	f.pluginid, " + 
					"	p.nome AS \"nomeplugin\" " + 
					"FROM FUNCIONALIDADE f " + 
					"INNER JOIN PLUGIN p ON f.PLUGINID = p.ID");
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Functionality func = new Functionality();
				func.setId(result.getInt("id"));
				func.setNome(result.getString("nome"));
				func.setDescricao(result.getString("descricao"));
				func.setDataCriacaoFromDate(result.getDate("dataCriacao"));
				func.getPlugin().setId(result.getInt("pluginid"));
				func.getPlugin().setNome(result.getString("nomeplugin"));
				funcs.add(func);
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
		return funcs;
	}
	
	public void createFunctionality(Functionality func) throws DBConnectException, DBCreateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("INSERT INTO FUNCIONALIDADE (nome, descricao, pluginId, dataCriacao) VALUES (?,  ?, ?, TO_DATE(?,'YYYY-MM-DD'))");
			statment.setString(1, func.getNome());
			statment.setString(2, func.getDescricao());
			statment.setInt(3, func.getPlugin().getId());
			statment.setString(4, func.getDataCriacaoToString());
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
	
	public void updateFunctionality(Functionality func) throws DBConnectException, DBUpdateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("UPDATE FUNCIONALIDADE SET nome = ?, descricao = ?, pluginId = ? WHERE ID = ?");
			statment.setString(1, func.getNome());
			statment.setString(2,  func.getDescricao());
			statment.setInt(3,  func.getPlugin().getId());
			statment.setInt(4, func.getId());
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
	
	public void deleteFunctionality(int funcId) throws DBConnectException, DBDeleteException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("DELETE FROM FUNCIONALIDADE WHERE id = ?");
			statment.setInt(1, funcId);
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
	
	public List<Functionality> searchFunctionalities(String atributo, String termo) throws DBConnectException, DBConsultException, DBDataNotFoundException {
		List<Functionality> funcs = new ArrayList<Functionality>();
		DatabaseConnection db = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().createStatement();
			String query = "SELECT " + 
			"	f.id, " + 
			"	f.nome, " + 
			"	f.descricao, " +
			"   f.dataCriacao, " +
			"	f.pluginid, " + 
			"	p.nome AS \"nomeplugin\" " + 
			"FROM FUNCIONALIDADE f " + 
			"INNER JOIN PLUGIN p ON f.PLUGINID = p.ID " + 
			"WHERE " + atributo + " LIKE '%" + termo + "%'";
			result = statement.executeQuery(query);
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Functionality func = new Functionality();
				func.setId(result.getInt("id"));
				func.setNome(result.getString("nome"));
				func.setDescricao(result.getString("descricao"));
				func.setDataCriacaoFromDate(result.getDate("dataCriacao"));
				func.getPlugin().setId(result.getInt("pluginId"));
				func.getPlugin().setNome(result.getString("nomeplugin"));
				funcs.add(func);
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    try { db.closeConnection(); } catch (Exception e) { /* ignorar */ } 
		}
		return funcs;
	}
}
