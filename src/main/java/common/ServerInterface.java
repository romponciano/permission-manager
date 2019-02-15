package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import exception.ServerServiceException;
import model.Functionality;
import model.Plugin;
import model.Status;
import model.User;

/**
 * Define a interface remota do servidor
 */
public interface ServerInterface extends Remote {

  /** nome para referenciar remotamente o servidor */
  String REFERENCE_NAME = "ServerInterface";

  int RMI_PORT = 1099;

  /**
   * Insere novo usuário no sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void createUser(User user) throws RemoteException, ServerServiceException;
  
  /**
   * Insere novo plugin no sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void createPlugin(Plugin plg) throws RemoteException, ServerServiceException;
  
  /**
   * Insere nova funcionalidade no sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void createFunctionality(Functionality func) throws RemoteException, ServerServiceException;
  
  /**
   * Deleta usuário do sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void deleteUser(int userId) throws RemoteException, ServerServiceException;
  
  /**
   * Deleta plugin e suas funcionalidades do sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void deletePlugin(int pluginId) throws RemoteException, ServerServiceException;
  
  /**
   * Deleta funcionalidade do sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void deleteFunctionality(int funcId) throws RemoteException, ServerServiceException;
  
  /**
   * Atualiza usuário do sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void updateUser(User user) throws RemoteException, ServerServiceException;
  
  /**
   * Atualiza plugin do sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void updatePlugin(Plugin plugin) throws RemoteException, ServerServiceException;
  
  /**
   * Atualiza funcionalidade do sistema.
   * 
   * @throws ServerServiceException 
   * @throws RemoteException
   */
  void updateFunctionality(Functionality func) throws RemoteException, ServerServiceException;
  
  /**
   * Obtém todos os usuários do sistema.
   * 
   * @return lista com os usuários
   * @throws RemoteException .
   * @throws ServerServiceException
   */
  List<User> getUsers() throws RemoteException, ServerServiceException;
  
  /**
   * Obtém todos os status de usuários do sistema.
   * 
   * @return lista com os usuários
   * @throws RemoteException .
   * @throws ServerServiceException
   */
  List<Status> getStatus() throws RemoteException, ServerServiceException;
  
  /**
   * Obtém todos os plugins do sistema.
   * 
   * @return lista com os plugins
   * @throws RemoteException .
   * @throws ServerServiceException
   */
  List<Plugin> getPlugins() throws RemoteException, ServerServiceException;
  
  /**
   * Obtém todas as funcionalidades do sistema.
   * 
   * @return lista com as funcionalidades
   * @throws RemoteException .
   * @throws ServerServiceException
   */
  List<Functionality> getFunctionalities() throws RemoteException, ServerServiceException;

  /**
   * Consulta usuários no sistema através do 
   * atributo e termo passados.
   * 
   * @param atributo - atributo/coluna desejado para consulta
   * @param termo - termo de consulta
   * @return - todos os resultados encontrados na busca
   * @throws RemoteException
   * @throws ServerServiceException
   */
  List<User> searchUsers(String atributo, String termo) throws RemoteException, ServerServiceException;

  /**
   * Consulta plugins no sistema através do 
   * atributo e termo passados.
   * 
   * @param atributo - atributo/coluna desejado para consulta
   * @param termo - termo de consulta
   * @return - todos os resultados encontrados na busca
   * @throws RemoteException
   * @throws ServerServiceException
   */
  List<Plugin> searchPlugins(String atributo, String termo) throws RemoteException, ServerServiceException;

  /**
   * Consulta funcionalidades no sistema através do 
   * atributo e termo passados.
   * 
   * @param atributo - atributo/coluna desejado para consulta
   * @param termo - termo de consulta
   * @return - todos os resultados encontrados na busca
   * @throws RemoteException
   * @throws ServerServiceException
   */
  List<Functionality> searchFunctionalities(String atributo, String termo) throws RemoteException, ServerServiceException;

  

}
