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
import dao.PerfilDAO;
import dao.PermissionDAO;
import dao.PluginDAO;
import dao.StatusDAO;
import dao.UserDAO;
import exception.DBConnectException;
import exception.DBConsultException;
import exception.DBCreateException;
import exception.DBDataNotFoundException;
import exception.DBDeleteException;
import exception.DBUpdateException;
import exception.ServerServiceException;
import model.Functionality;
import model.Log;
import model.Log.TIPOS_LOG;
import model.Perfil;
import model.Permission;
import model.Plugin;
import model.Status;
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
	
	// ----------------------- search
	@Override
	public List<User> searchUsers(String atributo, String termo) throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		List<User> usrs = new ArrayList<User>();
		try {
			usrs = userDAO.searchUser(atributo, termo);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} 
		return usrs;
	}
	
	@Override
	public List<Plugin> searchPlugins(String atributo, String termo) throws RemoteException, ServerServiceException {
		PluginDAO pluginDAO = new PluginDAO();
		List<Plugin> plgs = new ArrayList<Plugin>();
		try {
			plgs = pluginDAO.searchPlugins(atributo, termo);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} 
		return plgs;
	}
	
	@Override
	public List<Functionality> searchFunctionalities(String atributo, String termo) throws RemoteException, ServerServiceException {
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		List<Functionality> funcs = new ArrayList<Functionality>();
		try {
			funcs = funcDAO.searchFunctionalities(atributo, termo);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} 
		return funcs;
	}
	
	@Override
	public List<Perfil> searchPerfils(String atributo, String termo) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		List<Perfil> perfils = new ArrayList<Perfil>();
		try {
			perfils = perfilDAO.searchPerfils(atributo, termo);
		} catch (DBDataNotFoundException e) {
			logarException(TIPOS_LOG.INFO, e);
			throw new ServerServiceException(Const.INFO_DATA_NOT_FOUND);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		} 
		return perfils;
	}
	
	@Override
	public List<Permission> searchPermissionsByPerfilId(int perfilId) throws RemoteException, ServerServiceException {
		PermissionDAO permissionDAO = new PermissionDAO();
		List<Permission> perms = new ArrayList<Permission>();
		try {
			perms = permissionDAO.searchPermissionsByPerfilId(perfilId);
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
		return perms;
	}
	
	// ----------------------- deletes
	@Override
	public void deleteUser(int userId) throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		try {
			userDAO.deleteUser(userId);
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?", "usuário"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void deletePlugin(int pluginId) throws RemoteException, ServerServiceException {
		PluginDAO pluginDAO = new PluginDAO();
		try {
			pluginDAO.deletePlugin(pluginId);
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?", "plugin"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void deleteFunctionality(int funcId) throws RemoteException, ServerServiceException {
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		try {
			funcDAO.deleteFunctionality(funcId);
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?", "funcionalidade"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void deletePerfil(int perfilId) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			perfilDAO.deletePerfil(perfilId);
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?", "perfil"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void deletePermissionsByPerfilId(int perfilId) throws RemoteException, ServerServiceException {
		PermissionDAO permissionDAO = new PermissionDAO();
		try {
			permissionDAO.deletePermissionByPerfilId(perfilId);
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?", "permissão"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	// ----------------------- updates
	@Override
	public void updateUser(User user) throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		try {
			userDAO.updateUser(user);
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?", "usuário"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void updatePlugin(Plugin plugin) throws RemoteException, ServerServiceException {
		PluginDAO pluginDAO = new PluginDAO();
		try {
			pluginDAO.updatePlugin(plugin);
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?", "plugin"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void updateFunctionality(Functionality func) throws RemoteException, ServerServiceException {
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		try {
			funcDAO.updateFunctionality(func);
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?", "funcionalidade"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void updatePerfil(Perfil perfil) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		PermissionDAO permissionDAO = new PermissionDAO();
		try {
			perfilDAO.updatePerfil(perfil);
			for(Permission p : perfil.getPermissoes()) {
				permissionDAO.updatePermission(p);
			}
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?", "perfil"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}		
	}
	
	// ----------------------- creates
	@Override
	public void createUser(User user) throws RemoteException, ServerServiceException {
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
	public void createPlugin(Plugin plg) throws RemoteException, ServerServiceException {
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
	public void createFunctionality(Functionality func) throws RemoteException, ServerServiceException {
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		try {
			funcDAO.createFunctionality(func);
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public Perfil createPerfil(Perfil perfil) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			return perfilDAO.createPerfil(perfil);
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	@Override
	public void createPermission(Permission permission) throws RemoteException, ServerServiceException {
		PermissionDAO permissionDAO = new PermissionDAO();
		try {
			permissionDAO.createPermission(permission);
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE);
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}
	
	// ----------------------- getAll
	@Override
	public List<User> getUsers() throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		List<User> users = new ArrayList<User>();
		try {
			users = userDAO.getUsers();
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
			throw new ServerServiceException(Const.ERROR_DB_CONSULT);
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
	
	@Override
	public List<Status> getStatus() throws RemoteException, ServerServiceException {
		StatusDAO statusDAO = new StatusDAO();
		List<Status> status = new ArrayList<Status>();
		try {
			status = statusDAO.getStatus();
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
		return status;
	}
	
	@Override
	public List<Perfil> getPerfils() throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		List<Perfil> perfils = new ArrayList<Perfil>();
		try {
			perfils = perfilDAO.getPerfils();
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
		return perfils;
	}
	
	@Override
	public List<Permission> getPermissions() throws RemoteException, ServerServiceException {
		PermissionDAO permissionDAO = new PermissionDAO();
		List<Permission> perms = new ArrayList<Permission>();
		try {
			perms = permissionDAO.getPermissions();
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
		return perms;
	}
	
	private void logarException(TIPOS_LOG tipo, Exception e) {
		LogDAO logDAO = new LogDAO();
		Log log = new Log(tipo, e.getMessage());
		if(e.getCause() != null) log.setCausa(e.getCause().toString());
		logDAO.createLog(log);
	}	
}
