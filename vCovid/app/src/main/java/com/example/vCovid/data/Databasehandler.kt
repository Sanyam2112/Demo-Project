package com.example.vCovid.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.vCovid.models.FavouriteCountryModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Databasehandler @Inject constructor(@ApplicationContext context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CountryDatabase"
        private val TABLE_FAVOURITES = "FavoriteCountry"

        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_DETAILS = "details"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery = ("CREATE TABLE " + TABLE_FAVOURITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DETAILS + " TEXT" + ")")
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES)
        onCreate(db)
    }

    fun addFavouriteCountry(favouriteCountry: FavouriteCountryModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, favouriteCountry.name)
        contentValues.put(KEY_DETAILS, favouriteCountry.details)

        val success = db.insert(TABLE_FAVOURITES, null, contentValues)

//        db.close() // Closing database connection
        return success
    }



    fun fetchFavCountries(): ArrayList<FavouriteCountryModel> {
        val favouriteCountryList: ArrayList<FavouriteCountryModel> = ArrayList<FavouriteCountryModel>()
        val selectQuery = "SELECT  * FROM $TABLE_FAVOURITES"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var details: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                details = cursor.getString(cursor.getColumnIndex(KEY_DETAILS))

                val emp = FavouriteCountryModel(id = id, name = name, details = details)
                favouriteCountryList.add(emp)

            } while (cursor.moveToNext())
        }
        return favouriteCountryList
    }

    /**
     * Function to update record
     */

//    fun updateEmployee(emp: EmpModelClass): Int {
//        val db = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(KEY_NAME, emp.name) // EmpModelClass Name
//        contentValues.put(KEY_EMAIL, emp.email) // EmpModelClass Email
//
//        // Updating Row
//        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + emp.id, null)
//        //2nd argument is String containing nullColumnHack
//
//        db.close() // Closing database connection
//        return success
//    }

    /**
     * Function to delete record
     */
    fun deleteFavouriteCountry(favouriteCountryId: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, favouriteCountryId) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_FAVOURITES, "$KEY_ID=$favouriteCountryId", null)
        //2nd argument is String containing nullColumnHack
//        db.close() // Closing database connection
        return success
    }




}
