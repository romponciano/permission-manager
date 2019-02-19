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
import model.Permission;

public class PermissionDAO implements Serializable {

	private static final long serialVersionUID = 9219401712537769994L;
	
	public List<Permission> getPermissions() throws DBConsultException, DBConnectException, DBDataNotFoundException {
		List<Permission> permissaos = new ArrayList<Permission>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM PERMISSAO");
			if(result.isBeforeFirst()) {
				while(result.next()) {
					Permission permissao = new Permission();
					permissao.setId(result.getInt("id"));
					permissao.getPerfil().setId(result.getInt("perfilId"));
					permissao.getFunc().setId(result.getInt("funcId"));
					permissaos.add(permissao);
				};
			}			
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
		return permissaos;
	}
	
	public void createPermission(Permission permissao) throws DBConnectException, DBCreateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("INSERT INTO PERMISSAO (perfilId, funcId) VALUES (?, ?)");
			statment.setInt(1, permissao.getPerfil().getId());
			statment.setInt(2, permissao.getFunc().getId());
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
	
	public void updatePermission(Permission permissao) throws DBConnectException, DBUpdateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("UPDATE PERMISSAO SET nome = ?, descricao = ? WHERE id = ?");
			statment.setInt(1, permissao.getPerfil().getId());
			statment.setInt(2, permissao.getFunc().getId());
			statment.setInt(3, permissao.getId());
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
	
	public void deletePermission(int permissaoId) throws DBConnectException, DBDeleteException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("DELETE FROM PERMISSAO WHERE id = ?");
			statment.setInt(1, permissaoId);
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
	
	public void deletePermissionByPerfilId(int perfilId) throws DBConnectException, DBDeleteException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("DELETE FROM PERMISSAO WHERE perfilId = ?");
			statment.setInt(1, perfilId);
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
	
	public List<Permission> searchPermissions(String atributo, String termo) throws DBConnectException, DBConsultException, DBDataNotFoundException {
		List<Permission> perfs = new ArrayList<Permission>();
		DatabaseConnection db = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().createStatement();
			String query = "SELECT * FROM PERMISSAO WHERE " + atributo + " LIKE '%" + termo + "%'";
			result = statement.executeQuery(query);
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Permission permissao = new Permission();
				permissao.setId(result.getInt("id"));
				permissao.getPerfil().setId(result.getInt("perfilId"));
				permissao.getFunc().setId(result.getInt("funcId"));
				perfs.add(permissao);
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    try { db.closeConnection(); } catch (Exception e) { /* ignorar */ } 
		}
		return perfs;
	}

	public List<Permission> searchPermissionsByPerfilId(int perfilId) throws DBConnectException, DBConsultException, DBDataNotFoundException {
		List<Permission> perfs = new ArrayList<Permission>();
		DatabaseConnection db = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().createStatement();
			String query = "SELECT * FROM PERMISSAO WHERE perfilId = " + perfilId;
			result = statement.executeQuery(query);
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Permission permissao = new Permission();
				permissao.setId(result.getInt("id"));
				permissao.getPerfil().setId(result.getInt("perfilId"));
				permissao.getFunc().setId(result.getInt("funcId"));
				perfs.add(permissao);
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    try { db.closeConnection(); } catch (Exception e) { /* ignorar */ } 
		}
		return perfs;
	}
}
