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
			result = statment.executeQuery("SELECT * FROM FUNCIONALIDADE");
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Functionality func = new Functionality();
				func.setId(result.getInt("id"));
				func.setPluginId(result.getInt("pluginId"));
				func.setNome(result.getString("nome"));
				func.setDescricao(result.getString("descricao"));
				func.setDataCriacaoFromDate(result.getDate("dataCriacao"));
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
			statment.setInt(3, func.getPluginId());
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
			statment.setInt(3,  func.getPluginId());
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
}
