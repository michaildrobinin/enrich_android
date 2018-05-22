package enrich.and.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static DatabaseHelper mInstance;
    private static final String DATABASE_NAME = "mycontactdb";
    private static final String DATABASE_TABLE = "contacts_table";
    private static final int DATABASE_VERSION = 1;

    public static final String[] DATABASE_TABLE_COLS = new String[]{
            "id",
            "name",
            "email",
            "publickey"
    };


    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableScript = "CREATE TABLE '"+DATABASE_TABLE+"' "+
                "("+
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "'name' VARCHAR,"+
                "'email' VARCHAR,"+
                "'publickey' VARCHAR"+
                ");";
        db.execSQL(createTableScript);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '"+DATABASE_TABLE+"'");
        onCreate(db);
    }

    /*public synchronized List<LoginResponse> getAllUsers()
    {
        List<LoginResponse> userArray = new ArrayList<LoginResponse>();

        final SQLiteDatabase readableDatabse = getReadableDatabase();

        final Cursor cursor = readableDatabse.query(DATABASE_TABLE,
                DATABASE_TABLE_COLS,
                "",
                new String[]{},
                null,
                null,
                null,
                null);
        try{
            while(cursor.moveToNext()) {
                final LoginResponse userInfo = getUserInfoFromCurosr(cursor);
                if(userInfo != null)
                    userArray.add(userInfo);
            }
        }finally {
            cursor.close();
        }
        return userArray;
    }

    public synchronized LoginResponse getUserInfo(Integer id)
    {
        LoginResponse userInfo = null;
        final SQLiteDatabase readableDatabse = getReadableDatabase();

        final Cursor cursor = readableDatabse.query(DATABASE_TABLE,
                DATABASE_TABLE_COLS,
                "id = ?",
                new String[]{id.toString()},
                null,
                null,
                null,
                null);
        try{
            cursor.moveToNext();

            userInfo = getUserInfoFromCurosr(cursor);
        }finally {
            cursor.close();
        }


        return userInfo;
    }

    public synchronized int insertUserInfo(LoginResponse userInfo)
    {
        final SQLiteDatabase writableDatabase = getWritableDatabase();
        final ContentValues contentValue = new ContentValues();

        contentValue.put("name" , userInfo.getName());
        contentValue.put("email" , userInfo.getEmail());
        contentValue.put("publickey" , userInfo.getPublicKey());

        int id = (int) writableDatabase.insertOrThrow(DATABASE_TABLE , null , contentValue);
        return id;
    }

    public synchronized  void updateUserInfo(LoginResponse userInfo)
    {
        final SQLiteDatabase writableDatabse = getWritableDatabase();
        try
        {
            final ContentValues contentValue = new ContentValues();
            contentValue.put("id" , userInfo.getId());
            contentValue.put("name" , userInfo.getName());
            contentValue.put("email" , userInfo.getEmail());
            contentValue.put("publickey" , userInfo.getPublicKey());

            writableDatabse.update(DATABASE_TABLE , contentValue , "id = ?", new String[]{String.valueOf(userInfo.getId())});
        }finally {

        }
    }

    public synchronized void deleteUserInfo(LoginResponse userInfo)
    {
        if(userInfo.getId()>0)
        {
            getWritableDatabase().delete(DATABASE_TABLE , "id = ?" , new String[]{String.valueOf(userInfo.getId())});

        }
    }


    public synchronized int getUserCount()
    {
        try {
            Cursor cursor = getReadableDatabase().rawQuery("select count(*) from " + DATABASE_TABLE, null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    public LoginResponse getUserInfoFromCurosr(Cursor cursor)
    {
        LoginResponse userInfo = new LoginResponse();
        if(cursor != null)
        {
            userInfo.setId(cursor.getInt(0));
            userInfo.setName(cursor.getString(1));
            userInfo.setEmail(cursor.getString(2));
            userInfo.setPublicKey(cursor.getString(3));
        }
        return  userInfo;
    }*/

}
