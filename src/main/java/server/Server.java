package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import common.Const;
import common.DatabaseConnection;
import common.FunctionalityInterface;
import common.PluginInterface;
import common.ServerInterface;
import exception.DBConnectException;
import exception.DBConsultException;
import model.User;

public class Server implements ServerInterface {

	public static void main(String args[]) {
		try {
			ServerInterface server = new Server();
			server = (ServerInterface) UnicastRemoteObject.exportObject(server, ServerInterface.RMI_PORT);

			Registry registry = LocateRegistry.createRegistry(ServerInterface.RMI_PORT);
			registry.bind(ServerInterface.REFERENCE_NAME, server);

			System.out.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	public List<User> getUsers() throws RemoteException, DBConnectException, DBConsultException {
		List<User> usrs = new ArrayList<User>();
		DatabaseConnection db = null;
		Statement statment = null;
		ResultSet result = null;
		try {
			db = new DatabaseConnection();
			statment = db.getConnection().createStatement();
			result = statment.executeQuery("SELECT * FROM USUARIO");
			while(result.next()) {
				usrs.add(new User(
					result.getInt("id"),
					result.getString("nomeCompleto"),
					result.getString("login"),
					result.getInt("status"),
					result.getString("gerenciaAtual")
				));
			};
		} catch (DBConnectException e) {
			throw e;
		} catch (SQLException e) {
			throw new DBConsultException(Const.ERROR_DB_CONSULT);
		} finally {
		    try { result.close(); } catch (Exception e) { /* ignorar */ }
		    db.closeConnection();
		}
		return usrs;
	}

	@Override
	public List<PluginInterface> getPlugins() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FunctionalityInterface> getFunctionalities() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}
