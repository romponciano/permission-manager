package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import common.ServerInterface;
import common.User;

public class Server implements ServerInterface {

  public static void main(String args[]) {
    try {
      ServerInterface server = new Server();
      UnicastRemoteObject.exportObject(server, ServerInterface.RMI_PORT);

      Registry registry = LocateRegistry.createRegistry(ServerInterface.RMI_PORT);
      registry.rebind(ServerInterface.REFERENCE_NAME, server);

      System.out.println("Server ready");
    }
    catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }
  }

  @Override
  public List<User> getUsers() throws RemoteException {
    List<User> users = new ArrayList<>();
    return users;
  }
}
