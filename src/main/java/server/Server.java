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

import common.DatabaseConnection;
import common.FunctionalityInterface;
import common.PluginInterface;
import common.ServerInterface;
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
	public List<User> getUsers() throws RemoteException {
		List<User> usrs = new ArrayList<User>();
		DatabaseConnection db = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			db = new DatabaseConnection();
			st = db.getConnection().createStatement();
			rs = st.executeQuery("SELECT * FROM USUARIO");
			while(rs.next()) {
				usrs.add(new User(
					rs.getInt("id"),
					rs.getString("nomeCompleto"),
					rs.getString("login"),
					rs.getInt("status"),
					rs.getString("gerenciaAtual")
				));
			};
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try { rs.close(); } catch (Exception e) { /* ignored */ }
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
