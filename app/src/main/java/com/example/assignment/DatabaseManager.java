package com.example.assignment;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* This is made by GPT
ask i have a database, the main page create it , one page insert record to it , one page select record out of it . how to pass the database between page
To pass the database between pages, you can follow these steps:

1. Create a class to manage the database instance as a singleton. This ensures that there is only one instance of the database throughout your application. For example, you can create a class called DatabaseManager:

```java
public class DatabaseManager {
    private static DatabaseManager instance;
    private SQLiteDatabase database;

    private DatabaseManager(Context context) {
        // Initialize the database instance
        database = openOrCreateDatabase("YourDatabaseName", Context.MODE_PRIVATE, null);
        // Create your database tables and perform any necessary setup
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
```

2. In the main page where you create the database, initialize the DatabaseManager instance:

```java
DatabaseManager databaseManager = DatabaseManager.getInstance(MainActivity.this);
```

3. In the page where you insert records to the database, get the database instance from the DatabaseManager and insert the records:

```java
DatabaseManager databaseManager = DatabaseManager.getInstance(InsertActivity.this);
SQLiteDatabase database = databaseManager.getDatabase();

// Perform insert operations on the database
```

4. In the page where you select records from the database, get the database instance from the DatabaseManager and retrieve the records:

```java
DatabaseManager databaseManager = DatabaseManager.getInstance(SelectActivity.this);
SQLiteDatabase database = databaseManager.getDatabase();

// Perform select operations on the database
```

By using the singleton pattern and the DatabaseManager class, you can share the same database instance across different pages in your application.
*/
public class DatabaseManager {
    private static DatabaseManager instance;
    private SQLiteDatabase db;

    private DatabaseManager(Context context) {
        // Initialize the database instance
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/GameDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
           /* sql = "DROP TABLE IF EXISTS GamesLog;";
            db.execSQL(sql);*/
            String sql = "CREATE TABLE GamesLog (gameID INTEGER PRIMARY KEY AUTOINCREMENT, playDate DATE, playTime TIME, moves INTEGER, actions STRING, drawable STRING, grid STRING)";
            db.execSQL(sql);
        } catch (SQLException e) {
            // Handle any error that may occur during database initialization
            Log.d("hi", e.getMessage());
        }
    }

    // Get the instance of the DatabaseManager
    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    // Get the SQLiteDatabase object
    public SQLiteDatabase getDatabase() {
        return db;
    }
}