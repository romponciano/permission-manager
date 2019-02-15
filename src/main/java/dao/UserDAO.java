package dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.DatabaseConnection;
import exception.DBCreateException;
import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBDataNotFoundException;
import exception.DBDeleteException;
import exception.DBUpdateException;
import model.User;

public class UserDAO implements Serializable {
	
	private static final long serialVersionUID = -1503189464001292373L;

	public List<User> getUsers() throws DBConsultException, DBConnectException, DBDataNotFoundException {
		List<User> usrs = new ArrayList<User>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM USUARIO");
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				User usr = new User();
				usr.setId(result.getInt("id"));
				usr.setNome(result.getString("nomeCompleto"));
				usr.setLogin(result.getString("login"));
				usr.setStatus(result.getInt("status"));
				usr.setGerenciaAtual(result.getString("gerenciaAtual"));
				usrs.add(usr);
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    try { db.closeConnection(); } catch (Exception e) { /* ignorar */ } 
		}
		return usrs;
	}
	
	public void createUser(User user) throws DBConnectException, DBCreateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("INSERT INTO USUARIO (login, nomecompleto, status, gerenciaatual) VALUES (?, ?, ?, ?)");
			statment.setString(1, user.getLogin());
			statment.setString(2,  user.getNome());
			statment.setInt(3,  user.getStatus());
			statment.setString(4, user.getGerenciaAtual());
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
	
	public void updateUser(User user) throws DBConnectException, DBUpdateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("UPDATE USUARIO SET login = ?, nomecompleto = ?, status = ?, gerenciaatual = ? WHERE ID = ?");
			statment.setString(1, user.getLogin());
			statment.setString(2,  user.getNome());
			statment.setInt(3,  user.getStatus());
			statment.setString(4, user.getGerenciaAtual());
			statment.setInt(5, user.getId());
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
	
	public void deleteUser(int userId) throws DBConnectException, DBDeleteException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("DELETE FROM USUARIO WHERE id = ?");
			statment.setInt(1, userId);
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
	
	public List<User> searchUser(String atributo, String termo) throws DBConnectException, DBConsultException, DBDataNotFoundException {
		List<User> usrs = new ArrayList<User>();
		DatabaseConnection db = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().createStatement();
			String query = "SELECT * FROM USUARIO WHERE " + atributo + " LIKE '%" + termo + "%'";
			result = statement.executeQuery(query);
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				User usr = new User();
				usr.setId(result.getInt("id"));
				usr.setNome(result.getString("nomeCompleto"));
				usr.setLogin(result.getString("login"));
				usr.setStatus(result.getInt("status"));
				usr.setGerenciaAtual(result.getString("gerenciaAtual"));
				usrs.add(usr);
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(e.getMessage(), e.getCause());
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    try { db.closeConnection(); } catch (Exception e) { /* ignorar */ } 
		}
		return usrs;
	}
}
    