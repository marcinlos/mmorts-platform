package com.example;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Module;

import pl.edu.agh.ki.mmorts.server.core.ModuleTable;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManagerImpl;
import pl.edu.agh.ki.mmorts.server.data.ConnectionCreator;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.DerbyConnectionCreator;
import pl.edu.agh.ki.mmorts.server.data.DerbyDatabase;
import pl.edu.agh.ki.mmorts.server.data.NoConnectionException;
import pl.edu.agh.ki.mmorts.server.data.PlayerData;
import pl.edu.agh.ki.mmorts.server.data.PlayerDataImpl;
import pl.edu.agh.ki.mmorts.server.data.SimpleConnectionPool;
import pl.edu.agh.ki.mmorts.server.data.utils.QueriesCreator;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;
import pl.edu.agh.ki.mmorts.server.modules.builtin.LoginModule;
import pl.edu.agh.ki.mmorts.server.util.DI;

public class DerbyDatabaseTest {

	private static TransactionManager tm = new TransactionManagerImpl();
	private static SimpleConnectionPool connPool;
	// private static QueriesCreator qc = new QueriesCreator();
	private static ModuleDescriptor md;
	private static Database database;

	private PlayerData johnData = new PlayerDataImpl("John", "JohnPass",
			"Master");

	private PlayerData olderJohnData = new PlayerDataImpl(johnData.getName(),
			"OlderJohnPass", "GrandMaster");
	
	private ExData exampleData = new ExData(12345, false);
	private ExData exampleSecData = new ExData(54321, true);

	@BeforeClass
	public static void init() {
		try {

			QueriesCreator qc = new QueriesCreator();
			ModuleDescriptor.Builder mdCreat = ModuleDescriptor.create("Module 1 T_est", LoginModule.class);
			mdCreat.addProperty("datatype","com.example.ExData");
			md = mdCreat.build();
			
			
			
			
			
			DerbyConnectionCreator connCreator = new DerbyConnectionCreator();
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			for (Field f : connCreator.getClass().getDeclaredFields()) {
				if (f.getName() == "connectionURL") {
					f.setAccessible(true);
					f.set(connCreator, "jdbc:derby:test;create=true");
				}
			}

			connPool = new SimpleConnectionPool();
			for (Field f : connPool.getClass().getDeclaredFields()) {
				if (f.getName() == "creator") {
					f.setAccessible(true);
					f.set(connPool, connCreator);
				}
				if (f.getName() == "maxConnections") {
					f.setAccessible(true);
					f.set(connPool, 10);
				}
			}

			ModuleTable mt = new ModuleTable() {
				List<ModuleDescriptor> mdLIst;

				@Override
				public Collection<ModuleDescriptor> getModuleDescriptors() {
					if (mdLIst == null) {
						mdLIst = new ArrayList<ModuleDescriptor>();
						mdLIst.add(md);
					}
					return mdLIst;
				}
			};

			Module mtModule = DI.objectModule(mt, ModuleTable.class);
			Module connPoolModule = DI.objectModule(connPool,
					SimpleConnectionPool.class);
			Module qcModule = DI.objectModule(qc, QueriesCreator.class);
			Module tmModule = DI.objectModule(tm, TransactionManager.class);

			database = DI.createWith(DerbyDatabase.class, mtModule,
					connPoolModule, qcModule, tmModule);

			database.init();
			clean();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void clean() {
		Connection c;
		try {
			c = connPool.getConnection();
			c.createStatement().execute(
					"DELETE FROM " + QueriesCreator.PLAYER_MAIN_TAB);
			c.createStatement().execute(
					"DELETE FROM " + prepareModuleName(md.name));
			if(!c.getAutoCommit()){
			c.commit();
			}
			connPool.returnConnection(c);
		} catch (NoConnectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@AfterClass
	public static void shutdown() {
		database.shutdown();
		connPool.shutdown();
	}

	@Test
	public void addingPlayerSucc() throws Exception {
		tm.begin();
		try {
			database.createPlayer(johnData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();

		tm.begin();
		PlayerData rec = null;
		try {
			rec = database.receivePlayer(johnData.getName());
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}

		tm.commit();
		String[] got = { rec.getName(), rec.getLogin(), rec.getPasswordHash() };
		String[] org = { johnData.getName(), johnData.getLogin(),
				johnData.getPasswordHash() };
		Assert.assertArrayEquals(org, got);
	}

	/* Player john is added above */

	@Test(expected = IllegalArgumentException.class)
	public void addingTheSamePlayer() throws Exception {
		tm.begin();
		try {
			database.createPlayer(johnData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
	}

	@Test(expected = IllegalArgumentException.class)
	public void deletingNonExistingPlayer() throws Exception {
		tm.begin();
		try {
			database.deletePlayer("NonExisting");
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		Assert.assertTrue(false);
	}

	// expect to just invoke without exceptions
	@Test
	public void deletingExistingPlayer() throws Exception {
		tm.begin();
		try {
			database.deletePlayer(johnData.getName());
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			throw e;
		}
		tm.commit();
		Assert.assertTrue(true);
	}


	@Test
	public void updateCorrect() throws Exception {
		clean();
		tm.begin();
		try {
			database.createPlayer(johnData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		tm.begin();
		try {
			database.updatePlayer(johnData.getName(), olderJohnData);

		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		
		tm.begin();
		PlayerData rec = null;
		try {
			rec = database.receivePlayer(olderJohnData.getName());
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		String[] got = { rec.getName(), rec.getLogin(), rec.getPasswordHash() };
		String[] org = { olderJohnData.getName(), olderJohnData.getLogin(),
				olderJohnData.getPasswordHash() };
		Assert.assertArrayEquals(org, got);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateNonExisting() throws Exception {
		clean();
		tm.begin();
		try {
			database.updatePlayer("NonExisting", olderJohnData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
	}
	
	@Test
	public void receiveNonExistingPlayer() throws Exception {
		clean();
		tm.begin();
		PlayerData data = johnData;
		try {
			data = database.receivePlayer("NonExisting");
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			throw e;
		}
		tm.commit();
		Assert.assertNull(data);
	}
	
	@Test
	public void createBindingSucc() throws Exception {
		clean();
		tm.begin();
		try {
			database.createBinding(md.name, johnData.getName(), exampleData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		
		tm.begin();
		ExData rec = null;
		try {
			rec = (ExData)database.receiveBinding(md.name, johnData.getName());
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}

		tm.commit();
		String[] got = { String.valueOf(rec.getBoolObject()), String.valueOf(rec.getIntField())};
		String[] org = { String.valueOf(exampleData.getBoolObject()), String.valueOf(exampleData.getIntField()) };
		Assert.assertArrayEquals(org, got);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createTheSameBinding() throws Exception {
		tm.begin();
		try {
			database.createBinding(md.name, johnData.getName(), exampleData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
	}
	
	
	@Test
	public void updateBindingSucc() throws Exception {
		tm.begin();
		try {
			database.updateBinding(md.name, johnData.getName(), exampleSecData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		
		tm.begin();
		ExData rec = null;
		try {
			rec = (ExData)database.receiveBinding(md.name, johnData.getName());
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}

		tm.commit();
		String[] got = { String.valueOf(rec.getBoolObject()), String.valueOf(rec.getIntField())};
		String[] org = { String.valueOf(exampleSecData.getBoolObject()), String.valueOf(exampleSecData.getIntField()) };
		Assert.assertArrayEquals(org, got);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateBindingNonExistingSucc() throws Exception {
		tm.begin();
		try {
			database.updateBinding(md.name, "NonExisting", exampleSecData);
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		}
	
	
	/*Just execute without exception*/
	@Test
	public void deleteExistingBinding() throws Exception {
		tm.begin();
		try {
			database.deleteBinding(md.name, johnData.getName());
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		Assert.assertTrue(true);
		}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void deleteNonExistingBinding() throws Exception {
		tm.begin();
		try {
			database.deleteBinding(md.name, "NonExisting");
		} catch (Exception e) {
			tm.rollback();
			throw e;
		}
		tm.commit();
		}
	
	@Test
	public void receiveNonExistingBinding() throws Exception {
		clean();
		tm.begin();
		Object o = new Object();
		try {
			o = database.receiveBinding(md.name, "NonExisting");
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			throw e;
		}
		tm.commit();
		Assert.assertNull(o);
		}
	
	
	private static String prepareModuleName(String moduleName) {
		moduleName = moduleName.replace(" ", "__");
		return moduleName;
	}
	
	
}
