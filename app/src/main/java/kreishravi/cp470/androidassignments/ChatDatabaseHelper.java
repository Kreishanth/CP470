package kreishravi.cp470.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper  extends SQLiteOpenHelper {
    protected static final String ACTIVITY_NAME = "Chat Database Helper";

    public static final String DATABASE_NAME = "assignment3.db";
    public static final int VERSION_NUM = 4;

    public static final String TABLE_NAME = "table1";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "MESSAGE";

    public static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_ID
            + " integer primary key autoincrement, " + KEY_MESSAGE
            + " text not null);";

    public static final String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        db.execSQL(DATABASE_DROP);
        onCreate(db);
    }
}
