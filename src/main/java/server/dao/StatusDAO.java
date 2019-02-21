package server.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.model.Status;
import server.exceptions.DBConnectException;
import server.exceptions.DBConsultException;

public class StatusDAO extends GenericDAO {
	
	private static final long serialVersionUID = -1503189464001292373L;

	@Override
	public List<Object> populateListOfSelectSQL(ResultSet result) throws SQLException {
		List<Status> stats = new ArrayList<Status>();
		while(result.next()) {
			Long id = result.getLong("id");
			String name = result.getString("nome");
			stats.add(new Status(id, name));
		};
		return new ArrayList<Object>(stats);
	}
	
	public List<Status> getStatus() throws DBConsultException, DBConnectException {
		List<Object> objs = runSelectSQL("SELECT * FROM STATUS");
		return objectListToStatusList(objs);
	}
	
	private List<Status> objectListToStatusList(List<Object> objs) {
		List<Status> stats = new ArrayList<Status>();
		for(Object obj : objs) {
			stats.add((Status)obj);
		}
		return stats;
	}
}
    