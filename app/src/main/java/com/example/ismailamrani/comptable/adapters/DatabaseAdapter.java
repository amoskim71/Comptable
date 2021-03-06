package com.example.ismailamrani.comptable.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.example.ismailamrani.comptable.models.Activation;
import com.example.ismailamrani.comptable.models.Local;
import com.example.ismailamrani.comptable.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 29/04/2016.
 */
public class DatabaseAdapter {

    /**
     * The singleton instance.
     */
    private static DatabaseAdapter instance;

    public static final String KEY_ID = "id";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CREATION_DATE = "creationDate";
    public static final String KEY_EXPIRATION_DATE = "expirationDate";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_COMPANY_ID = "companyID";
    public static final String KEY_LOCALE_ID = "localeID";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_FIX = "fix";
    public static final String KEY_FAX = "fax";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ACTIVITY = "activity";

    public static final String KEY_CODE = "code";
    public static final String KEY_IS_ACTIVATED = "isActivated";

    private static final String TAG = DatabaseAdapter.class.getSimpleName();
    private static final String DATABASE_NAME = "comptableDatabase";
    private static final String USER_TABLE = "User";
    private static final String ACTIVATION_TABLE = "Activation";
    private static final String LOCAL_TABLE = "Local";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_FIRSTNAME + " TEXT NOT NULL, " +
                    KEY_LASTNAME + " TEXT NOT NULL, " +
                    KEY_TYPE + " TEXT NOT NULL, " +
                    KEY_CREATION_DATE + " INTEGER, " +
                    KEY_EXPIRATION_DATE + " INTEGER, " +
                    KEY_USERNAME + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL, " +
                    KEY_COMPANY_ID + " INTEGER, " +
                    KEY_LOCALE_ID + " INTEGER, " +
                    KEY_ADDRESS + " TEXT NOT NULL, " +
                    KEY_CITY + " TEXT NOT NULL, " +
                    KEY_COUNTRY + " TEXT NOT NULL, " +
                    KEY_TELEPHONE + " TEXT NOT NULL)";

    private static final String CREATE_ACTIVATION_TABLE =
            "CREATE TABLE " + ACTIVATION_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_CODE + " TEXT NOT NULL, " +
                    KEY_COMPANY_ID + " INTEGER, " +
                    KEY_IS_ACTIVATED + " INTEGER)";

    private static final String CREATE_LOCAL_TABLE =
            "CREATE TABLE " + LOCAL_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_ADDRESS + " TEXT NOT NULL, " +
                    KEY_CITY + " TEXT NOT NULL, " +
                    KEY_COUNTRY + " TEXT NOT NULL, " +
                    KEY_TELEPHONE + " TEXT NOT NULL, " +
                    KEY_FIX + " TEXT, " +
                    KEY_FAX + " TEXT, " +
                    KEY_EMAIL + " TEXT, " +
                    KEY_ACTIVITY + " TEXT)";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context mContext;

    /**
     * A static factory method.
     *
     * @param context
     * @return the singleton instance of the DatabaseAdapter.
     */
    public static DatabaseAdapter getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseAdapter.class) {
                if (instance == null)
                    instance = new DatabaseAdapter(context).open();
            }
        }

        return instance;
    }

    private DatabaseAdapter(Context context) {
        mContext = context;
        dbHelper = new DatabaseHelper(context);
    }

    private DatabaseAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    /**
     * @return a User object representing the currently logged-in user,
     * if there was any.
     */
    public User getLoggedUser() {
        Cursor cursor = db.query(
                true,
                USER_TABLE,
                new String[]{KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME,
                        KEY_TYPE, KEY_USERNAME, KEY_PASSWORD, KEY_COMPANY_ID,
                        KEY_LOCALE_ID, KEY_ADDRESS, KEY_CITY, KEY_COUNTRY, KEY_TELEPHONE},
                null, null, null, null, null, null);

        User user = extractUserFromCursor(cursor);
        cursor.close();
        return user;
    }

    public void logout() {
        removeCurrentUser();
        removeCurrentLocales();
    }

    public void removeCurrentUser() {
        db.delete(USER_TABLE, null, null) ;
    }

    public void removeCurrentLocales() {
        db.delete(LOCAL_TABLE, null, null) ;
    }

    public void removeCurrentActivation() {
        db.delete(ACTIVATION_TABLE, null, null) ;
    }

    private User extractUserFromCursor(Cursor cursor) {
        User user = cursor.moveToFirst() ?
                new User.Builder()
                        .id(cursor.getInt(0))
                        .firstname(cursor.getString(1))
                        .lastname(cursor.getString(2))
                        .type(cursor.getString(3))
                        .username(cursor.getString(4))
                        .password(cursor.getString(5))
                        .companyID(cursor.getInt(6))
                        .localeID(cursor.getInt(7))
                        .address(cursor.getString(8))
                        .city(cursor.getString(9))
                        .country(cursor.getString(10))
                        .telephone(cursor.getString(11))
                        .createUser()
                : null;

        cursor.close();
        return user;
    }

    /**
     * Saves the passed-in user into the local database.
     *
     * @param user to be saved.
     */
    public void saveLoggedInUser(User user) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_FIRSTNAME, user.getFirstname());
        values.put(KEY_LASTNAME, user.getLastname());
        values.put(KEY_TYPE, user.getType());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_COMPANY_ID, user.getCompanyID());
        values.put(KEY_LOCALE_ID, user.getLocaleID());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_CITY, user.getCity());
        values.put(KEY_COUNTRY, user.getCountry());
        values.put(KEY_TELEPHONE, user.getTelephone());

        insertUser(values);
    }

    private long insertUser(ContentValues values) {
        return db.insert(USER_TABLE, null, values);
    }

    public boolean updateUser(User newUser) {
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, newUser.getFirstname());
        values.put(KEY_LASTNAME, newUser.getLastname());
        values.put(KEY_TYPE, newUser.getType());
        values.put(KEY_USERNAME, newUser.getUsername());
        values.put(KEY_PASSWORD, newUser.getPassword());
        values.put(KEY_COMPANY_ID, newUser.getCompanyID());
        values.put(KEY_LOCALE_ID, newUser.getLocaleID());
        values.put(KEY_ADDRESS, newUser.getAddress());
        values.put(KEY_CITY, newUser.getCity());
        values.put(KEY_COUNTRY, newUser.getCountry());
        values.put(KEY_TELEPHONE, newUser.getTelephone());

        return db.update(USER_TABLE, values, null, null) > 0;
    }

    public void activateApplication(Activation activation) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, activation.getId());
        values.put(KEY_CODE, activation.getCode());
        values.put(KEY_COMPANY_ID, activation.getCompanyID());
        values.put(KEY_IS_ACTIVATED, activation.isActivated() ? 1 : 0);

        db.insert(ACTIVATION_TABLE, null, values);
    }

    public void saveLocales(List<Local> locals) {
        Log.i("Locales", "Saving locales: " + locals.toString());
        Stream.of(locals)
                .forEach(new Consumer<Local>() {
                    @Override
                    public void accept(Local value) {
                        insertLocal(value);
                    }
                });
    }

    public void insertLocal(Local local) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, local.getId());
        values.put(KEY_ADDRESS, local.getAddress());
        values.put(KEY_CITY, local.getCity());
        values.put(KEY_COUNTRY, local.getCountry());
        values.put(KEY_TELEPHONE, local.getTelephone());

        db.insert(LOCAL_TABLE, null, values);
    }

    public List<Local> retrieveCurrentLocales() {
        Cursor cursor = db.query(
                true,
                LOCAL_TABLE,
                new String[]{KEY_ID, KEY_ADDRESS, KEY_CITY, KEY_COUNTRY, KEY_TELEPHONE},
                null, null, null, null, null, null);

        List<Local> locals = extractLocalesFromCursor(cursor);
        Log.i("Locales", "Retrieved locales: " + locals.toString());
        cursor.close();

        return locals;
    }

    private List<Local> extractLocalesFromCursor(Cursor cursor) {
        List<Local> localList = new ArrayList<>();

        while (cursor.moveToNext()) {
            localList.add(new Local(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    "", "", "", "", 0
            ));
        }

        return localList;
    }

    /**
     * @return the locale address of the current user.
     */
    public String getUserAddress() {
        String address = "Locale address not found.";
        Cursor cursor = db.query(
                true,
                USER_TABLE,
                new String[]{KEY_ADDRESS, KEY_CITY},
                null, null, null, null, null, null);

        if (cursor.moveToFirst())
            address = cursor.getString(0) + ", " + cursor.getString(1);

        cursor.close();
        return address;
    }

    public String getUserType() {
        String type = "";
        Cursor cursor = db.query(
                true,
                USER_TABLE,
                new String[]{KEY_TYPE},
                null, null, null, null, null, null);

        if (cursor.moveToFirst())
            type = cursor.getString(0);

        type = type.toLowerCase();
        Log.i("DATABASE", "Current user type: " + type);

        cursor.close();
        return type;
    }

    public int getUserCompanyID() {
        int id = -1;
        Cursor cursor = db.query(true, ACTIVATION_TABLE, new String[]{KEY_COMPANY_ID},
                null, null, null, null, null, null);

        if (cursor.moveToFirst())
            id = cursor.getInt(0);

        cursor.close();
        return id;
    }

    /**
     * @return the locale ID of the current user.
     */
    public int getCurrentLocaleID() {
        int id = -1;
        Cursor cursor = db.query(true, USER_TABLE, new String[]{KEY_LOCALE_ID},
                null, null, null, null, null, null);

        if (cursor.moveToFirst())
            id = cursor.getInt(0);

        cursor.close();
        return id;
    }

    public Activation getCurrentActivation() {
        Cursor cursor = db.query(
                true,
                ACTIVATION_TABLE,
                new String[]{KEY_ID, KEY_CODE, KEY_IS_ACTIVATED, KEY_COMPANY_ID},
                null, null, null, null, null, null);
        Activation activation = extractActivationFromCursor(cursor);

        cursor.close();
        return activation;
    }

    private Activation extractActivationFromCursor(Cursor cursor) {
        return cursor == null ? null :
                cursor.moveToFirst() ? new Activation(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2) == 1,
                        cursor.getInt(3)) : null;
    }

    public void close() {
        dbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_ACTIVATION_TABLE);
            db.execSQL(CREATE_LOCAL_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion +
                    " to " + newVersion + ", which will destroy all old data.");
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ACTIVATION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_TABLE);
            onCreate(db);
        }
    }
}