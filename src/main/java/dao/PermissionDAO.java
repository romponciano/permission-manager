package dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBCreateException;
import exception.DBDeleteException;
import model.Functionality;
import model.Plugin;

public class PermissionDAO extends GenericDAO {

	private static final long serialVersionUID = -2349876772332391843L;

	@Override
	public List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException {
		List<Functionality> funcs = new ArrayList<Functionality>();
		while(result.next()) {
			Long id = result.getLong("id");
			String name = result.getString("nome");
			String desc = result.getString("descricao");
			Date dataCriacao = result.getDate("dataCriacao");
			Long pluginId = result.getLong("pluginid");
			String pluginNome = result.getString("nomeplugin");
			Functionality func = new Functionality(id, name, desc, dataCriacao);
			func.setPlugin(new Plugin(pluginId, pluginNome, null, null));
			funcs.add(func);
		};
		return new ArrayList<Object>(funcs);
	}
	
	public void deletePermissionsByPerfilId(Long perfilId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM PERMISSAO WHERE perfilId = ?1";
		sql = sql.replace("?1", perfilId.toString());
		try { runCreateUpdateDeleteSQL(sql); }
		catch(SQLException e) { throw new DBDeleteException(e.getMessage(), e.getCause()); }
	}
	
	public void deletePermissionsByFunctionalityId(Long funcId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM PERMISSAO WHERE FUNCID = ?1";
		sql = sql.replace("?1", funcId.toString());
		try { runCreateUpdateDeleteSQL(sql); }
		catch(SQLException e) { throw new DBDeleteException(e.getMessage(), e.getCause()); }
	}
	
	public void createPermission(Long perfilId, Long funcId) throws DBCreateException, DBConnectException {
		String sql = "INSERT INTO PERMISSAO (PERFILID, FUNCID) VALUES (?1, ?2)";
		sql = sql.replace("?1", perfilId.toString());
		sql = sql.replace("?2", funcId.toString());
		try { runCreateUpdateDeleteSQL(sql); }
		catch(SQLException e) { throw new DBCreateException(e.getMessage(), e.getCause()); }
	}
	
	public List<Functionality> getPermissionsByPerfilId(Long perfilId) throws DBConsultException, DBConnectException {
		String sql = "SELECT " 
				+ "f.id, f.nome, f.descricao, f.dataCriacao, f.pluginid, p.nome AS \"nomeplugin\" " 
				+ "FROM FUNCIONALIDADE f "
				+ "INNER JOIN PLUGIN p ON f.PLUGINID = p.ID " 
				+ "INNER JOIN PERMISSAO per ON f.ID = per.FUNCID WHERE per.PERFILID = ?1";
		sql = sql.replace("?1", perfilId.toString());
		List<Object> objs = runSelectSQL(sql);
		return objectListToFunctionalityList(objs);
	}

	private List<Functionality> objectListToFunctionalityList(List<Object> objs) {
		List<Functionality> plugins = new ArrayList<Functionality>();
		for(Object obj : objs) {
			plugins.add((Functionality)obj);
		}
		return plugins;
	}
}
