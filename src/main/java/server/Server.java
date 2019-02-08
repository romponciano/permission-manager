package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import common.ServerInterface;
import dao.FunctionalityDAO;
import dao.PluginDAO;
import dao.UserDAO;
import exception.DBConnectException;
import exception.DBConsultException;
import model.Functionality;
import model.Plugin;
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
		UserDAO userDAO = new UserDAO();
		return userDAO.getUsers();
	}

	@Override
	public List<Plugin> getPlugins() throws RemoteException, DBConnectException, DBConsultException {
		PluginDAO pluginDAO = new PluginDAO(); 
		return pluginDAO.getPlugins();
	}

	@Override
	public List<Functionality> getFunctionalities() throws RemoteException, DBConsultException, DBConnectException {
		FunctionalityDAO functionalityDAO = new FunctionalityDAO();
		functionalityDAO.getFunctionalities();
		return null;
	}
}
