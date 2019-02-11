package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import common.Const;
import common.ServerInterface;
import dao.FunctionalityDAO;
import dao.LogDAO;
import dao.PluginDAO;
import dao.UserDAO;
import exception.DBConnectException;
import exception.DBConsultException;
import exception.ServerServiceException;
import model.Functionality;
import model.Log;
import model.Log.TIPOS_LOG;
import model.Plugin;
import model.User;

public class Server implements ServerInterface {
	
	LogDAO logDAO = new LogDAO();
	
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
	public List<User> getUsers() throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		List<User> users = new ArrayList<User>();
		try {
			users = userDAO.getUsers();
		} catch (DBConsultException e) {
			Log log = new Log(TIPOS_LOG.ERRO, e.getMessage(), e.getCause().toString());
			logDAO.createLog(log);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			Log log = new Log(TIPOS_LOG.ERRO, e.getMessage(), e.getCause().toString());
			logDAO.createLog(log);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return users;
	}

	@Override
	public List<Plugin> getPlugins() throws RemoteException, ServerServiceException {
		PluginDAO pluginDAO = new PluginDAO();
		List<Plugin> plugins = new ArrayList<Plugin>();
		try {
			plugins = pluginDAO.getPlugins();
		} catch (DBConsultException e) {
			Log log = new Log(TIPOS_LOG.ERRO, e.getMessage(), e.getCause().toString());
			logDAO.createLog(log);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			Log log = new Log(TIPOS_LOG.ERRO, e.getMessage(), e.getCause().toString());
			logDAO.createLog(log);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return plugins;
	}

	@Override
	public List<Functionality> getFunctionalities() throws RemoteException, ServerServiceException {
		FunctionalityDAO functionalityDAO = new FunctionalityDAO();
		List<Functionality> funcionalidades = new ArrayList<Functionality>();
		try {
			funcionalidades = functionalityDAO.getFunctionalities();
		} catch (DBConsultException e) {
			Log log = new Log(TIPOS_LOG.ERRO, e.getMessage(), e.getCause().toString());
			logDAO.createLog(log);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			Log log = new Log(TIPOS_LOG.ERRO, e.getMessage(), e.getCause().toString());
			logDAO.createLog(log);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return funcionalidades;
	}
}
