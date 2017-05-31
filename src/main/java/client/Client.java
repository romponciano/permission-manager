package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import common.ServerInterface;
import common.User;

public class Client {

  private static ServerInterface server;

  public static ServerInterface getServer() throws RemoteException, NotBoundException {
    if (Client.server == null) {
      Registry registry = LocateRegistry.getRegistry("localhost", ServerInterface.RMI_PORT);
      Client.server = (ServerInterface) registry.lookup(ServerInterface.REFERENCE_NAME);
    }
    return Client.server;
  }

  public static void main(String[] args) {
    try {
      List<User> users = Client.getServer().getUsers();
      System.out.println(users.size());
    }
    catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }
}
