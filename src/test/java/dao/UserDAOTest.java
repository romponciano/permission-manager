package dao;

import org.junit.Before;
import org.junit.Test;

import exception.DBConnectException;
import exception.DBCreateException;
import model.User;

public class UserDAOTest {

	UserDAO userDAO;
	
	@Before
	public void setup() {
		userDAO = new UserDAO();
	}
	
	@Test
	public void createUser() throws DBConnectException, DBCreateException {
		User user = new User();
		user.setLogin("test");
		user.setNome("Usuario teste");
		userDAO.createUser(user);
	}
	
	@Test
	public void getUsers() {
		// criar usuarios;
		// pegar usuários;
		// verificar se qtd foi igual
	}

}
