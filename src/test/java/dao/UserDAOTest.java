package dao;

import org.junit.Before;
import org.junit.Test;

import common.Const;
import exception.DBConnectException;

public class UserDAOTest {

	UserDAO userDAO;
	
	@Before
	public void setup() throws DBConnectException {
		userDAO = new UserDAO(Const.DB_TEST_URL, Const.USER_TEST, Const.PASS_TEST);
	}
	
	@Test
	public void getUsers() {
		// criar usuarios;
		// pegar usuários;
		// verificar se qtd foi igual
	}

}
