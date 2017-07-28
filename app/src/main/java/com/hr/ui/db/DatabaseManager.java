package com.hr.ui.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;

/**
 * 编写一个负责管理单个 SQLiteOpenHelper 对象的单例
 * 
 * @author 800hr：yelong
 * 
 *         2015-5-5
 */
public class DatabaseManager {
	private static DatabaseManager instance;
	private static DAO_DBOpenHelper mHelper;
	private SQLiteDatabase mDatabase;
	// 打开数据库操作计数器
	private AtomicInteger mOpenCounter = new AtomicInteger();

	public static synchronized void initialize(DAO_DBOpenHelper helper) {
		if (instance == null) {
			instance = new DatabaseManager();
			mHelper = helper;
		}
	}

	public static synchronized DatabaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					DatabaseManager.class.getSimpleName()
							+ "is not initialized, call initialize(..) method first");
		}

		return instance;
	}

	/**
	 * 当你需要使用数据库连接，你可以通过调用类
	 * DatabaseManager的方法openDatabase()。在方法里面，内置一个标志数据库被打开多少次的计数器
	 * 。如果计数为1，代表我们需要打开一个新的数据库连接，否则，数据库连接已经存在。
	 * 
	 * @return
	 */
	public synchronized SQLiteDatabase openDatabase() {
		if (mOpenCounter.incrementAndGet() == 1) {
			// opening new database
			mDatabase = mHelper.getWritableDatabase();
		}
		return mDatabase;
	}

	/**
	 * 每次我们调用 closeDatabase() 方法，计数器都会递减，直到计数为0，我们就需要关闭数据库连接了。
	 */
	public synchronized void closeDatabase() {
		if (mOpenCounter.decrementAndGet() == 0) {
			// closing database
			mDatabase.close();
		}
	}

}
