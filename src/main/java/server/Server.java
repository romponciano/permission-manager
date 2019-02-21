package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import common.Const;
import common.ServerInterface;
import common.exceptions.ServerServiceException;
import common.model.Functionality;
import common.model.Perfil;
import common.model.Plugin;
import common.model.Status;
import common.model.User;
import server.dao.FunctionalityDAO;
import server.dao.LogDAO;
import server.dao.PerfilDAO;
import server.dao.PermissionDAO;
import server.dao.PluginDAO;
import server.dao.StatusDAO;
import server.dao.UserDAO;
import server.dao.UserProfileDAO;
import server.exceptions.DBConnectException;
import server.exceptions.DBConsultException;
import server.exceptions.DBCreateException;
import server.exceptions.DBDeleteException;
import server.exceptions.DBUpdateException;
import server.utils.Log;
import server.utils.Log.TIPOS_LOG;

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
			usrs = userDAO.searchUsers(atributo, termo);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Usuários"));
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
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Plugins"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return plgs;
	}

	@Override
	public List<Functionality> searchFunctionalities(String atributo, String termo)
			throws RemoteException, ServerServiceException {
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		List<Functionality> funcs = new ArrayList<Functionality>();
		try {
			funcs = funcDAO.searchFunctionalities(atributo, termo);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Funcionalidades"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return funcs;
	}

	@Override
	public List<Perfil> searchPerfis(String atributo, String termo) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		List<Perfil> perfils = new ArrayList<Perfil>();
		try {
			perfils = perfilDAO.searchPerfis(atributo, termo);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Perfis"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return perfils;
	}

	@Override
	public List<Functionality> searchPermissionsByPerfilId(Long perfilId)
			throws RemoteException, ServerServiceException {
		List<Functionality> funcs = new ArrayList<Functionality>();
		PermissionDAO permissionDAO = new PermissionDAO();
		try {
			funcs = permissionDAO.getPermissionsByPerfilId(perfilId);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Permissões"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return funcs;
	}

	@Override
	public List<Perfil> searchUserProfilesByUserId(Long userId) throws RemoteException, ServerServiceException {
		List<Perfil> perfis = new ArrayList<Perfil>();
		UserProfileDAO userProfileDAO = new UserProfileDAO();
		try {
			perfis = userProfileDAO.getUserProfilesByUserId(userId);
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Permissões"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return perfis;
	}

	// ----------------------- deletes
	@Override
	public void deleteUser(Long userId) throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		UserProfileDAO userProfileDAO = new UserProfileDAO();
		try {
			userProfileDAO.deleteUserProfilesByUserId(userId);
			userDAO.deleteUser(userId);
			logarSucesso(TIPOS_LOG.DELETE, userId.toString());
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?1", "Usuário"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}

	@Override
	public void deletePlugin(Long pluginId) throws RemoteException, ServerServiceException {
		PluginDAO pluginDAO = new PluginDAO();
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		try {
			List<Functionality> funcs = funcDAO.searchFunctionalitiesByPluginId(pluginId);
			for (Functionality f : funcs) {
				this.deleteFunctionality(f.getId());
			}
			pluginDAO.deletePlugin(pluginId);
			logarSucesso(TIPOS_LOG.DELETE, pluginId.toString());
		} catch (DBConsultException | DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?1", "Plugin"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}

	@Override
	public void deleteFunctionality(Long funcId) throws RemoteException, ServerServiceException {
		FunctionalityDAO funcDAO = new FunctionalityDAO();
		PermissionDAO permissionDAO = new PermissionDAO();
		try {
			permissionDAO.deletePermissionsByFunctionalityId(funcId);
			funcDAO.deleteFunctionality(funcId);
			logarSucesso(TIPOS_LOG.DELETE, funcId.toString());
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?1", "Funcionalidade"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}

	@Override
	public void deletePerfil(Long perfilId) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		UserProfileDAO userProfileDAO = new UserProfileDAO();
		PermissionDAO permissionDAO = new PermissionDAO();
		try {
			userProfileDAO.deleteUserProfilesByProfileId(perfilId);
			permissionDAO.deletePermissionsByPerfilId(perfilId);
			perfilDAO.deletePerfil(perfilId);
			logarSucesso(TIPOS_LOG.DELETE, perfilId.toString());
		} catch (DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_DELETE.replace("?1", "Perfil"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}

	// ----------------------- updates
	@Override
	public void updateUser(User user) throws RemoteException, ServerServiceException {
		UserDAO userDAO = new UserDAO();
		UserProfileDAO userProfileDAO = new UserProfileDAO();
		try {
			userDAO.updateUser(user);
			/*
			 * essa abordagem de apagar todas os User_Profile e depois criar os
			 * User_Profiles novamente foi adotada pois é isso que acontece na prática.
			 * Atualizar user_profiles é remover e/ou add, então basta remover todas os
			 * user_profile que o perfil tinha e criar todas que estão na lista no momento.
			 */
			userProfileDAO.deleteUserProfilesByUserId(user.getId());
			createUserProfiles(user.getPerfis(), user.getId());
			logarSucesso(TIPOS_LOG.UPDATE, user.toString());
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?1", "usuário"));
		} catch (DBCreateException | DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?", "user_profile"));
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
			logarSucesso(TIPOS_LOG.UPDATE, plugin.toString());
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?1", "plugin"));
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
			logarSucesso(TIPOS_LOG.UPDATE, func.toString());
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?1", "funcionalidade"));
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
			/*
			 * essa abordagem de apagar todas as permissões do perfil e depois criar as
			 * permissões concedidas foi adotada pois é isso que acontece na prática.
			 * Atualizar permissões é remover e/ou add, então basta remover todas as
			 * permissões que o perfil tinha e criar todas que estão marcadas no momento.
			 */
			permissionDAO.deletePermissionsByPerfilId(perfil.getId());
			createPermissions(perfil.getPermissoes(), perfil.getId());
			logarSucesso(TIPOS_LOG.UPDATE, perfil.toString());
		} catch (DBUpdateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?1", "perfil"));
		} catch (DBCreateException | DBDeleteException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_UPDATE.replace("?", "permissão"));
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
			user = userDAO.createUser(user);
			createUserProfiles(user.getPerfis(), user.getId());
			logarSucesso(TIPOS_LOG.CREATE, user.toString());
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE.replace("?1", "Usuário"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
	}

	@Override
	public void createPerfil(Perfil perfil) throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			perfil = perfilDAO.createPerfil(perfil);
			createPermissions(perfil.getPermissoes(), perfil.getId());
			logarSucesso(TIPOS_LOG.CREATE, perfil.toString());
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE.replace("?1", "Perfil"));
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
			logarSucesso(TIPOS_LOG.CREATE, plg.toString());
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE.replace("?1", "Plugin"));
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
			logarSucesso(TIPOS_LOG.CREATE, func.toString());
		} catch (DBCreateException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CREATE.replace("?1", "Funcionalidade"));
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
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Usuário"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
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
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Plugin"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
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
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Funcionalidade"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
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
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Status"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return status;
	}

	@Override
	public List<Perfil> getPerfis() throws RemoteException, ServerServiceException {
		PerfilDAO perfilDAO = new PerfilDAO();
		List<Perfil> perfils = new ArrayList<Perfil>();
		try {
			perfils = perfilDAO.getPerfis();
		} catch (DBConsultException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONSULT.replace("?1", "Perfil"));
		} catch (DBConnectException e) {
			logarException(TIPOS_LOG.ERRO, e);
			throw new ServerServiceException(Const.ERROR_DB_CONNECT);
		}
		return perfils;
	}

	private void logarException(TIPOS_LOG tipo, Exception e) {
		LogDAO logDAO = new LogDAO();
		Log log = new Log(tipo, e.getMessage());
		if (e.getCause() != null)
			log.setCausa(e.getCause().toString());
		logDAO.createLog(log);
	}

	private void logarSucesso(TIPOS_LOG tipo, String msg) {
		LogDAO logDAO = new LogDAO();
		Log log = new Log(tipo, msg);
		logDAO.createLog(log);
	}

	private void createPermissions(List<Functionality> funcs, Long perfilId)
			throws DBCreateException, DBConnectException {
		PermissionDAO permissionDAO = new PermissionDAO();
		for (Functionality func : funcs)
			permissionDAO.createPermission(perfilId, func.getId());
	}

	private void createUserProfiles(List<Perfil> perfis, Long userId) throws DBCreateException, DBConnectException {
		UserProfileDAO userProfileDAO = new UserProfileDAO();
		for (Perfil perf : perfis)
			userProfileDAO.createUserProfile(userId, perf.getId());
	}
}
