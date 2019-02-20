package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBCreateException;
import exception.DBDeleteException;
import model.Perfil;

public class UserProfileDAO extends GenericDAO {

	private static final long serialVersionUID = -4624205244267762954L;
	
	public void deleteUserProfilesByUserId(Long userId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM USER_PROFILE WHERE USUARIOID = ?1";
		sql = sql.replace("?1", userId.toString());
		try { runCreateUpdateDeleteSQL(sql); }
		catch(SQLException e) { throw new DBDeleteException(e.getMessage(), e.getCause()); }
	}
	
	public void deleteUserProfilesByProfileId(Long perfilId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM USER_PROFILE WHERE PERFIL = ?1";
		sql = sql.replace("?1", perfilId.toString());
		try { runCreateUpdateDeleteSQL(sql); }
		catch(SQLException e) { throw new DBDeleteException(e.getMessage(), e.getCause()); }
	}
	
	public void createUserProfile(Long userId, Long perfilId) throws DBCreateException, DBConnectException {
		String sql = "INSERT INTO USER_PROFILE(USUARIOID, PERFILID) VALUES (?1, ?2)";
		sql = sql.replace("?1", userId.toString());
		sql = sql.replace("?2", perfilId.toString());
		try { runCreateUpdateDeleteSQL(sql); }
		catch(SQLException e) { throw new DBCreateException(e.getMessage(), e.getCause()); }
	}
	
	public List<Perfil> getUserProfilesByUserId(Long userId) throws DBConsultException, DBConnectException {
		String sql = "SELECT * FROM PERFIL p INNER JOIN USER_PROFILE up ON p.ID = up.PERFILID WHERE up.USUARIOID = ?1";
		sql = sql.replace("?1", userId.toString());
		List<Object> objs = runSelectSQL(sql);
		return objectListToPerfilList(objs);
	}
	

	@Override
	public List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException {
		List<Perfil> perfis = new ArrayList<Perfil>();
		while(result.next()) {
			Long id = result.getLong("id");
			String name = result.getString("nome");
			String desc = result.getString("descricao");
			Date dataCriacao = result.getDate("dataCriacao");
			perfis.add(new Perfil(id, name, desc, dataCriacao));
		};
		return new ArrayList<Object>(perfis);
	}
	
	private List<Perfil> objectListToPerfilList(List<Object> objs) {
		List<Perfil> perfis = new ArrayList<Perfil>();
		for(Object obj : objs) {
			perfis.add((Perfil)obj);
		}
		return perfis;
	}
}
