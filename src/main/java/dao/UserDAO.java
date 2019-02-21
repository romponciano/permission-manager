package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBCreateException;
import exception.DBDeleteException;
import exception.DBUpdateException;
import model.Status;
import model.User;

public class UserDAO extends GenericDAO {

	private static final long serialVersionUID = -1503189464001292373L;

	@Override
	public List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException {
		List<User> usrs = new ArrayList<User>();
		while (result.next()) {
			Long id = result.getLong("id");
			String name = result.getString("nomeCompleto");
			User usr = new User(id, name);
			usr.setLogin(result.getString("login"));
			usr.setGerenciaAtual(result.getString("gerenciaAtual"));
			usr.setStatus(new Status(result.getLong("statusid"), result.getString("statusnome")));
			usrs.add(usr);
		}
		;
		return new ArrayList<Object>(usrs);
	}

	public List<User> getUsers() throws DBConsultException, DBConnectException {
		List<Object> objs = runSelectSQL("SELECT "
				+ "u.id, u.nomeCompleto, u.login, u.gerenciaAtual, s.id AS \"statusid\", s.nome AS \"statusnome\" "
				+ "FROM USUARIO u INNER JOIN STATUS s ON u.STATUS = s.ID");
		return objectListToUserList(objs);
	}

	public List<User> searchUsers(String atributo, String termo) throws DBConnectException, DBConsultException {
		String sql = "SELECT "
				+ "u.id, u.nomeCompleto, u.login, u.gerenciaAtual, s.id AS \"statusid\", s.nome AS \"statusnome\" "
				+ "FROM USUARIO u INNER JOIN STATUS s ON u.STATUS = s.ID WHERE " + atributo + " LIKE ?";
		List<Object> objs = runSelectSQL(sql, "%"+termo+"%");
		return objectListToUserList(objs);
	}

	public User createUser(User usr) throws DBConnectException, DBCreateException {
		String sql = "BEGIN INSERT INTO USUARIO (login, nomeCompleto, status, gerenciaatual) VALUES (?, ?, ?, ?) RETURNING ID INTO :x; END;";
		try {
			usr.setId(runOracleCallableStatement(sql, usr.getLogin(), usr.getName(), usr.getStatus().getId(),
					usr.getGerenciaAtual()));
		} catch (SQLException e) {
			throw new DBCreateException(e.getMessage(), e.getCause());
		}
		return usr;
	}

	public void updateUser(User usr) throws DBConnectException, DBUpdateException {
		String sql = "UPDATE USUARIO SET login = ?, nomecompleto = ?, status = ?, gerenciaatual = ? WHERE ID = ?";
		try {
			runCreateUpdateDeleteSQL(sql, usr.getLogin(), usr.getName(), usr.getStatus().getId(),
					usr.getGerenciaAtual(), usr.getId());
		} catch (SQLException e) {
			throw new DBUpdateException(e.getMessage(), e.getCause());
		}
	}

	public void deleteUser(Long usrId) throws DBConnectException, DBDeleteException {
		String sql = "DELETE FROM USUARIO WHERE id = ?";
		try {
			runCreateUpdateDeleteSQL(sql, usrId);
		} catch (SQLException e) {
			throw new DBDeleteException(e.getMessage(), e.getCause());
		}
	}

	private List<User> objectListToUserList(List<Object> objs) {
		List<User> usrs = new ArrayList<User>();
		for (Object obj : objs) {
			usrs.add((User) obj);
		}
		return usrs;
	}
}
