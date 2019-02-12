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
import exception.DBCreateException;
import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBDataNotFoundException;
import exception.ServerServiceException;
import model.Functionality;
import model.Log;
import model.Log.TIPOS_LOG;
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
	public void createUser(User user) throws ServerServiceException {
		UserDAO userDAO = new UserDAO();
		try {
			userDAO.createUser(user);
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void createPlugin(Plugin plg) throws ServerServiceException {
		PluginDAO pluginDAO = new PluginDAO();
		try {
			pluginDAO.createPlugin(plg);
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public List<User> getUsers() throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		List<User> users = new ArrayList<User>();
		try {
			users = userDAO.getUsers();
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			//throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
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
			logarException(TIPOS_LOG.ERRO, e);
			//throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
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
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
		}
		return funcionalidades;
	}
	
	private void logarException(TIPOS_LOG tipo, Exception e) {
		LogDAO logDAO = new LogDAO();
		Log log = new Log(tipo, e.getMessage());
		if(e.getCause() != null) log.setCausa(e.getCause().toString());
		logDAO.createLog(log);
	}
}
