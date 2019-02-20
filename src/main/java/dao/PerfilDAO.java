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
import exception.DBUpdateException;
import model.Perfil;

public class PerfilDAO extends GenericDAO {

	private static final long serialVersionUID = -3165469845679009567L;
	
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
	
	public List<Perfil> getPerfis() throws DBConsultException, DBConnectException {
		List<Object> objs = runSelectSQL("SELECT * FROM PERFIL");
		return objectListToPerfilList(objs);
	}
	
	public List<Perfil> searchPerfis(String atributo, String termo) throws DBConnectException, DBConsultException {
		String sql = "SELECT * FROM PERFIL WHERE ?1 LIKE '%?2%'";
		sql = sql.replace("?1", atributo);
		sql = sql.replace("?2", termo);
		List<Object> objs = runSelectSQL(sql);
		return objectListToPerfilList(objs);
	}
	
	public Perfil createPerfil(Perfil perfil) throws DBConnectException, DBCreateException {
		// é preciso que seja PS/SQL e OracleCallableStatement para retornar o ID do perfil criado
		String sql = "BEGIN INSERT INTO PERFIL (NOME, DESCRICAO, DATACRIACAO) VALUES ('?1','?2',TO_DATE('?3','YYYY-MM-DD')) RETURNING ID INTO :x; END;";
		sql = sql.replace("?1", perfil.getName());
		sql = sql.replace("?2", perfil.getDescription());
		sql = sql.replace("?3", perfil.getDataCriacaoToString());
		try { perfil.setId(runOracleCallableStatement(sql)); } 
		catch(SQLException e) { throw new DBCreateException(e.getMessage(), e.getCause()); }
		return perfil;
	}
	
	public void updatePerfil(Perfil perfil) throws DBConnectException, DBUpdateException {
		String sql = "UPDATE PERFIL SET nome = '?1', descricao = '?2' WHERE id = ?3";
		sql = sql.replace("?1", perfil.getName());
		sql = sql.replace("?2", perfil.getDescription());
		sql = sql.replace("?3", perfil.getId().toString());
		try { runCreateUpdateDeleteSQL(sql); } 
		catch(SQLException e) { throw new DBUpdateException(e.getMessage(), e.getCause()); }
	}
	
	public void deletePerfil(Long perfilId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM PERFIL WHERE id = ?1";
		sql = sql.replace("?1", perfilId.toString());
		try { runCreateUpdateDeleteSQL(sql); } 
		catch(SQLException e) { throw new DBDeleteException(e.getMessage(), e.getCause()); }
	}
	
	private List<Perfil> objectListToPerfilList(List<Object> objs) {
		List<Perfil> perfis = new ArrayList<Perfil>();
		for(Object obj : objs) {
			perfis.add((Perfil)obj);
		}
		return perfis;
	}
}
