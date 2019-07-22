package com.android.RemindMePlease.Adapters;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.RemindMePlease.Models.AppData;
import com.android.RemindMePlease.Models.Reminder;


public class ReminderDBAdapter {
    private static ReminderDBAdapter reminderDBAdapter = null;

    private static final String COL_ID = "reminderID";
    private static final String COL_DESCRIPTION = "reminderDescription";
    private static final String COL_IMPORTANT = "isImportant";
    private static final String COL_HOUR = "reminderHour";
    private static final String COL_MINUTE = "reminderMinute";
    private static final String COL_DAY = "reminderDay";
    private static final String COL_MONTH = "reminderMonth";
    private static final String COL_YEAR = "reminderYear";

    private static final int INDEX_ID = 0;
    private static final int INDEX_DESCRIPTION = INDEX_ID + 1;
    private static final int INDEX_IMPORTANT = INDEX_ID + 2;
    private static final int INDEX_HOUR = INDEX_ID + 3;
    private static final int INDEX_MINUTE = INDEX_ID + 4;
    private static final int INDEX_DAY = INDEX_ID + 5;
    private static final int INDEX_MONTH = INDEX_ID + 6;
    private static final int INDEX_YEAR = INDEX_ID + 7;

    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_IMPORTANT + " INTEGER, " +
                    COL_HOUR + " INTEGER, " +
                    COL_MINUTE + " INTEGER, " +
                    COL_DAY + " INTEGER, " +
                    COL_MONTH + " INTEGER, " +
                    COL_YEAR + " INTEGER );";


    private ReminderDBAdapter(Context context) {
        this.context = context;
    }

    public static ReminderDBAdapter getInstance(Context context){
        if(reminderDBAdapter==null)
            reminderDBAdapter = new ReminderDBAdapter(context);
        return reminderDBAdapter;
    }

    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public void insertReminder(Reminder reminder) {
//        int important = reminder.isImportant()? 1:0;
//        String sql =
//                "INSERT or replace INTO " +TABLE_NAME+ " ("
//                        +COL_DESCRIPTION+", "
//                        +COL_IMPORTANT+", "
//                        +COL_HOUR+", "
//                        +COL_MINUTE+", "
//                        +COL_DAY+", "
//                        +COL_MONTH+","
//                        +COL_YEAR
//                        +")" +
//                        " VALUES ("+"'"+reminder.getReminderDescription()+"'"+", "
//                        +important+", "
//                        +"'"+reminder.getHour()+"'"+", "
//                        +"'"+reminder.getMinute()+"'"+", "
//                        +"'"+reminder.getDay()+"'"+", "
//                        +"'"+reminder.getMonth()+"'"+", "
//                        +"'"+reminder.getYear()+"'"+")";
//        mDb.execSQL(sql);

        ContentValues cv = new ContentValues();
        cv.put(COL_DESCRIPTION,reminder.getReminderDescription()); //These Fields should be your String values of actual column names
        cv.put(COL_IMPORTANT,reminder.isImportant()?1:0);
        cv.put(COL_HOUR,reminder.getHour());
        cv.put(COL_MINUTE,reminder.getMinute());
        cv.put(COL_DAY,reminder.getDay());
        cv.put(COL_MONTH,reminder.getMonth());
        cv.put(COL_YEAR,reminder.getYear());
        mDb.insert(TABLE_NAME,null,cv);

        Log.i("Inserted","Successfully " + reminder.getId());
    }

    public void fetchAllReminders() {
        AppData.getAllUserReminders().clear();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = mDbHelper.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int important = cursor.getInt(INDEX_IMPORTANT);
                boolean isImportant = (important ==1);
                Reminder reminder = new Reminder(cursor.getString(INDEX_DESCRIPTION),isImportant);
                Log.i("ID ",""+cursor.getInt(INDEX_ID));
                reminder.setId(cursor.getInt(INDEX_ID));
                reminder.setHour(cursor.getInt(INDEX_HOUR));
                reminder.setMinute(cursor.getInt(INDEX_MINUTE));
                reminder.setDay(cursor.getInt(INDEX_DAY));
                reminder.setMonth(cursor.getInt(INDEX_MONTH));
                reminder.setYear(cursor.getInt(INDEX_YEAR));
                AppData.getAllUserReminders().add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void updateReminder(Reminder reminder) {
        ContentValues cv = new ContentValues();
        cv.put(COL_DESCRIPTION,reminder.getReminderDescription()); //These Fields should be your String values of actual column names
        cv.put(COL_IMPORTANT,reminder.isImportant()?1:0);
        cv.put(COL_HOUR,reminder.getHour());
        cv.put(COL_MINUTE,reminder.getMinute());
        cv.put(COL_DAY,reminder.getDay());
        cv.put(COL_MONTH,reminder.getMonth());
        cv.put(COL_YEAR,reminder.getYear());
        mDb.update(TABLE_NAME,cv,COL_ID + "=" + reminder.getId(),null);
        Log.i("updated","Successfully " + reminder.getId());

    }

    void deleteReminderById(Reminder reminder) {
        mDb.delete(TABLE_NAME,COL_ID+"="+reminder.getId(),null);
        Log.i("deleted","Successfully " + reminder.getId());
    }

    public void deleteAllReminders() {
        String deleteQuery = "delete from ";
        mDb.execSQL(deleteQuery+ TABLE_NAME);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
