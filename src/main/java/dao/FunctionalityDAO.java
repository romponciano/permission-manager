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
import exception.DBUpdateException;
import model.Functionality;
import model.Plugin;

public class FunctionalityDAO extends GenericDAO {

	private static final long serialVersionUID = 2933870645847680620L;

	@Override
	public List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException {
		List<Functionality> funcs = new ArrayList<Functionality>();
		while (result.next()) {
			Long id = result.getLong("id");
			String name = result.getString("nome");
			String desc = result.getString("descricao");
			Date dataCriacao = result.getDate("dataCriacao");
			Long pluginId = result.getLong("pluginid");
			String pluginNome = result.getString("nomeplugin");
			Functionality func = new Functionality(id, name, desc, dataCriacao);
			func.setPlugin(new Plugin(pluginId, pluginNome, null, null));
			funcs.add(func);
		}
		;
		return new ArrayList<Object>(funcs);
	}

	public List<Functionality> getFunctionalities() throws DBConsultException, DBConnectException {
		List<Object> objs = runSelectSQL(
				"SELECT " + "f.id, f.nome, f.descricao, f.dataCriacao, f.pluginid, p.nome AS \"nomeplugin\" "
						+ "FROM FUNCIONALIDADE f INNER JOIN PLUGIN p ON f.PLUGINID = p.ID");
		return objectListToFunctionalityList(objs);
	}

	public List<Functionality> searchFunctionalities(String atributo, String termo)
			throws DBConnectException, DBConsultException {
		String sql = "SELECT " + "	f.id, " + "	f.nome, " + "	f.descricao, " + "   f.dataCriacao, "
				+ "	f.pluginid, " + "	p.nome AS \"nomeplugin\" "
				+ "FROM FUNCIONALIDADE f INNER JOIN PLUGIN p ON f.PLUGINID = p.ID WHERE " + atributo + " LIKE ?";
		List<Object> objs = runSelectSQL(sql, "%"+termo+"%");
		return objectListToFunctionalityList(objs);
	}

	public List<Functionality> searchFunctionalitiesByPluginId(Long pluginId)
			throws DBConnectException, DBConsultException {
		String sql = "SELECT " + "	f.id, " + "	f.nome, " + "	f.descricao, " + "   f.dataCriacao, "
				+ "	f.pluginid, " + "	p.nome AS \"nomeplugin\" "
				+ "FROM FUNCIONALIDADE f INNER JOIN PLUGIN p ON f.PLUGINID = p.ID WHERE f.pluginId = ?";
		List<Object> objs = runSelectSQL(sql, pluginId);
		return objectListToFunctionalityList(objs);
	}

	public void createFunctionality(Functionality func) throws DBConnectException, DBCreateException {
		String sql = "INSERT INTO FUNCIONALIDADE (nome, descricao, pluginId, dataCriacao) VALUES (?, ?, ?, ?)";
		try {
			runCreateUpdateDeleteSQL(sql, func.getName(), func.getDescription(), func.getPlugin().getId(),
					func.getDataCriacao());
		} catch (SQLException e) {
			throw new DBCreateException(e.getMessage(), e.getCause());
		}
	}

	public void updateFunctionality(Functionality func) throws DBConnectException, DBUpdateException {
		String sql = "UPDATE FUNCIONALIDADE SET nome = ?, descricao = ?, pluginId = ? WHERE ID = ?";
		try {
			runCreateUpdateDeleteSQL(sql, func.getName(), func.getDescription(), func.getPlugin().getId(),
					func.getId());
		} catch (SQLException e) {
			throw new DBUpdateException(e.getMessage(), e.getCause());
		}
	}

	public void deleteFunctionality(Long funcId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM FUNCIONALIDADE WHERE id = ?";
		try {
			runCreateUpdateDeleteSQL(sql, funcId);
		} catch (SQLException e) {
			throw new DBDeleteException(e.getMessage(), e.getCause());
		}
	}

	private List<Functionality> objectListToFunctionalityList(List<Object> objs) {
		List<Functionality> plugins = new ArrayList<Functionality>();
		for (Object obj : objs) {
			plugins.add((Functionality) obj);
		}
		return plugins;
	}
}
