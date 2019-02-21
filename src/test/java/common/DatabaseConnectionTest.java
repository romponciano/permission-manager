package common;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

import server.exceptions.DBConnectException;

public class DatabaseConnectionTest {

	DatabaseConnection db;
	Connection conn;
	
	@Test
	public void return_connection_if_connection_success() throws DBConnectException {
		conn = null;
		db = new DatabaseConnection(Const.DB_TEST_URL, Const.USER_TEST, Const.PASS_TEST);
		conn = db.getConnection();
		assertNotNull(conn);
	}
	
	@Test(expected = DBConnectException.class)
	public void throw_dbconnectexception_if_wrong_url() throws DBConnectException {
		db = new DatabaseConnection("url errada", Const.USER_TEST, Const.PASS_TEST);
		db.getConnection();
	}
	
	@Test(expected = DBConnectException.class)
	public void throw_dbconnectexception_if_wrong_user() throws DBConnectException {
		db = new DatabaseConnection(Const.DB_TEST_URL, "usuario errado", Const.PASS_TEST);
		db.getConnection();
	}
	
	@Test(expected = DBConnectException.class)
	public void throw_dbconnectexception_if_wrong_pass() throws DBConnectException {
		db = new DatabaseConnection(Const.DB_TEST_URL, Const.USER_TEST, "pass errada");
		db.getConnection();
	}
}
