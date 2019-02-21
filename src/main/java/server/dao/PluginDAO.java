package server.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.model.Plugin;
import server.exceptions.DBConnectException;
import server.exceptions.DBConsultException;
import server.exceptions.DBCreateException;
import server.exceptions.DBDeleteException;
import server.exceptions.DBUpdateException;

public class PluginDAO extends GenericDAO {

	private static final long serialVersionUID = -5098749675099318542L;

	@Override
	public List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException {
		List<Plugin> plugins = new ArrayList<Plugin>();
		while (result.next()) {
			Long id = result.getLong("id");
			String name = result.getString("nome");
			String desc = result.getString("descricao");
			Date dataCriacao = result.getDate("dataCriacao");
			plugins.add(new Plugin(id, name, desc, dataCriacao));
		}
		;
		return new ArrayList<Object>(plugins);
	}

	public List<Plugin> getPlugins() throws DBConsultException, DBConnectException {
		List<Object> objs = runSelectSQL("SELECT * FROM PLUGIN");
		return objectListToPluginList(objs);
	}

	public List<Plugin> searchPlugins(String atributo, String termo) throws DBConnectException, DBConsultException {
		String sql = "SELECT * FROM PLUGIN WHERE " + atributo + " LIKE ?";
		List<Object> objs = runSelectSQL(sql, atributo, "%"+termo+"%");
		return objectListToPluginList(objs);
	}

	public void createPlugin(Plugin plugin) throws DBConnectException, DBCreateException {
		String sql = "INSERT INTO PLUGIN (nome, descricao, dataCriacao) VALUES (?, ?, ?)";
		try {
			runCreateUpdateDeleteSQL(sql, plugin.getName(), plugin.getDescription(), plugin.getDataCriacao());
		} catch (SQLException e) {
			throw new DBCreateException(e.getMessage(), e.getCause());
		}
	}

	public void updatePlugin(Plugin plugin) throws DBConnectException, DBUpdateException {
		String sql = "UPDATE PLUGIN SET nome = ?, descricao = ? WHERE id = ?";
		try {
			runCreateUpdateDeleteSQL(sql, plugin.getName(), plugin.getDescription(), plugin.getId());
		} catch (SQLException e) {
			throw new DBUpdateException(e.getMessage(), e.getCause());
		}
	}

	public void deletePlugin(Long pluginId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM PLUGIN WHERE id = ?";
		try {
			runCreateUpdateDeleteSQL(sql, pluginId);
		} catch (SQLException e) {
			throw new DBDeleteException(e.getMessage(), e.getCause());
		}
	}

	private List<Plugin> objectListToPluginList(List<Object> objs) {
		List<Plugin> plugins = new ArrayList<Plugin>();
		for (Object obj : objs) {
			plugins.add((Plugin) obj);
		}
		return plugins;
	}
}
