package common;

import java.io.Serializable;
import java.rmi.RemoteException;

import model.Functionality;
import model.Plugin;

public interface UserInterface extends Serializable {

	/**
	   * Cadastra novo plugin no sistema.
	   * 
	   * @return plugin cadastrado
	   * @throws RemoteException .
	   */
	  Plugin createPlugin(Plugin plugin) throws RemoteException;
	  
	  /**
	   * Cadastra nova funcionalidade no sistema.
	   * 
	   * @return funcionalidade cadastrada
	   * @throws RemoteException .
	   */
	  Functionality createFunctionality(Functionality funct) throws RemoteException;
	  
	  /**
	   * Busca a string digitada (searchString) no tipo de data (type)
	   * 
	   * @return funcionalidade cadastrada
	   * @throws RemoteException .
	   */
	  Object searchData(Integer type, String searchString) throws RemoteException;
}
