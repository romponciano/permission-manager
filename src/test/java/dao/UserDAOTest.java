package dao;

import org.junit.Before;
import org.junit.Test;

import exception.DBConnectException;
import exception.DBCreateException;

public class UserDAOTest {

	UserDAO userDAO;
	
	@Before
	public void setup() {
		userDAO = new UserDAO();
	}
	
	@Test
	public void createUser() throws DBConnectException, DBCreateException {
		
	}
	
	@Test
	public void getUsers() {
		// criar usuarios;
		// pegar usuários;
		// verificar se qtd foi igual
	}

}
