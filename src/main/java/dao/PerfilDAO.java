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
import model.Perfil;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class PerfilDAO implements Serializable {

	private static final long serialVersionUID = -3165469845679009567L;
	
	public List<Perfil> getPerfils() throws DBConsultException, DBConnectException, DBDataNotFoundException {
		List<Perfil> perfils = new ArrayList<Perfil>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM PERFIL");
			if(result.isBeforeFirst()) {
				while(result.next()) {
					Perfil perfil = new Perfil();
					perfil.setId(result.getInt("id"));
					perfil.setNome(result.getString("nome"));
					perfil.setDescricao(result.getString("descricao"));
					perfil.setDataCriacaoFromDate(result.getDate("dataCriacao"));
					perfils.add(perfil);
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
		return perfils;
	}
	
	public Perfil createPerfil(Perfil perfil) throws DBConnectException, DBCreateException {
		DatabaseConnection db = null;
		OracleCallableStatement cs = null;
		try {
			db = new DatabaseConnection();
			// é preciso que seja PS/SQL e OracleCallableStatement para retornar o ID do usuário criado
			String query = "BEGIN INSERT INTO PERFIL "
					+ "(NOME, DESCRICAO, DATACRIACAO) VALUES ('"+perfil.getNome()+"','"+perfil.getDescricao()+"',TO_DATE('"+perfil.getDataCriacaoToString()+"','YYYY-MM-DD')) "
					+ "RETURNING ID INTO :x; END;";
			cs = (OracleCallableStatement) db.getConnection().prepareCall(query);
			cs.registerOutParameter(1, OracleTypes.NUMBER );
			cs.execute();
			perfil.setId(cs.getInt(1));
			return perfil;
		} catch(SQLException e) {
			throw new DBCreateException(e.getMessage(), e.getCause());
		} catch(DBConnectException e) {
			throw e;
		} finally {
			try { cs.close(); } catch (Exception e) { }
		    db.closeConnection();
		}
	}
	
	public void updatePerfil(Perfil perfil) throws DBConnectException, DBUpdateException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("UPDATE PERFIL SET nome = ?, descricao = ? WHERE id = ?");
			statment.setString(1, perfil.getNome());
			statment.setString(2,  perfil.getDescricao());
			statment.setInt(3,  perfil.getId());
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
	
	public void deletePerfil(int perfilId) throws DBConnectException, DBDeleteException {
		DatabaseConnection db = null;
		PreparedStatement statment = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().prepareStatement("DELETE FROM PERFIL WHERE id = ?");
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
	
	public List<Perfil> searchPerfils(String atributo, String termo) throws DBConnectException, DBConsultException, DBDataNotFoundException {
		List<Perfil> perfs = new ArrayList<Perfil>();
		DatabaseConnection db = null;
		Statement statement = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statement = db.getConnection().createStatement();
			String query = "SELECT * FROM PERFIL WHERE " + atributo + " LIKE '%" + termo + "%'";
			result = statement.executeQuery(query);
			if(!result.isBeforeFirst()) {
				throw new DBDataNotFoundException();
			}
			while(result.next()) {
				Perfil perfil = new Perfil();
				perfil.setId(result.getInt("id"));
				perfil.setNome(result.getString("nome"));
				perfil.setDescricao(result.getString("descricao"));
				perfil.setDataCriacaoFromDate(result.getDate("dataCriacao"));
				perfs.add(perfil);
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
