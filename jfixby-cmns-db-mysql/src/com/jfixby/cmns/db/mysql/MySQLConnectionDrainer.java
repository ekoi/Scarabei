
package com.jfixby.cmns.db.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.Sys;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySQLConnectionDrainer extends Thread {

	private static final long MINUTE = 60_000;
	private final long delta;
	long closeConnectionAt = 0;
	private boolean isRetired = true;
	private final long drainerID;
	static long drainerIDs = 0;
	private Connection mysql_connection;
	private final Object lock;
	private final MySQLConnectionDrained mySQLConnection;

	private synchronized long drainerIDs () {
		drainerIDs++;
		return drainerIDs;
	}

	public MySQLConnectionDrainer (final MySQLConnectionDrained mySQLConnection) {
		this.lock = mySQLConnection.lock;
		this.mySQLConnection = mySQLConnection;
		{
			/** Ugly hack. Should be replaced with readers-writer lock. */
			this.delta = Math.max(mySQLConnection.connectionDrainTime * 1000L, 0);
		}

		this.drainerID = this.drainerIDs();

	}

	public void connect () throws SQLException {
		L.d("connecting", this.mySQLConnection.serverName);
		final MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser(this.mySQLConnection.login);
		dataSource.setPassword(this.mySQLConnection.password);
		dataSource.setServerName(this.mySQLConnection.serverName);
		dataSource.setUseSSL(this.mySQLConnection.useSSL);
		dataSource.setDatabaseName(this.mySQLConnection.dbName);
		this.mysql_connection = dataSource.getConnection();
		L.d("connecting", "OK");
		this.isRetired = false;
		this.updateCloseTime();
	}

	public void retire () {
		this.isRetired = true;
		this.dispose();
	}

	public void updateCloseTime () {
		this.closeConnectionAt = System.currentTimeMillis() + this.delta;
	}

	@Override
	public void run () {
		this.updateCloseTime();
		while (true) {
			synchronized (this.lock) {
				if (this.isRetired) {
					L.d("connection drainer retired", this);
					return;
				}
				final long currentTime = System.currentTimeMillis();
				if (currentTime >= this.closeConnectionAt) {
					L.d("connection drainer done", this);
					this.dispose();
					this.mySQLConnection.state.expectState(CONNECTON_STATE.LIVE);
					this.mySQLConnection.state.switchState(CONNECTON_STATE.OPEN);
					return;
				}
			}
			Sys.sleep(5000);
		}
	}

	@Override
	public void start () {
		L.d("connection drainer start", this);
		super.start();
	}

	@Override
	public String toString () {
		return "MySQLConnectionDrainer[" + this.drainerID + "] delta=" + this.delta;
	}

	public Connection getSQLConnection () {
		return this.mysql_connection;
	}

	public void dispose () {
		if (this.mysql_connection != null) {
			try {
				if (!this.mysql_connection.isClosed()) {
					this.mysql_connection.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
		this.mysql_connection = null;

	}

}