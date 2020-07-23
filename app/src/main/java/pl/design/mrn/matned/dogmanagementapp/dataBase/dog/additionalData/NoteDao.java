package pl.design.mrn.matned.dogmanagementapp.dataBase.dog.additionalData;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static pl.design.mrn.matned.dogmanagementapp.Statics.DATE_FORMAT;

public class NoteDao extends SQLiteOpenHelper implements DaoFragmentInterface<Note> {

    private static final String NOTES_TABLE = "NOTES_TABLE" ;
    private static final String NOTE_ID = "NOTE_ID" ;
    private static final String NOTE = "NOTE" ;
    private static final String NOTE_DATE = "NOTE_DATE";
    private static final String DOG_ID = "DOG_ID" ;

    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public NoteDao(@Nullable Context context) {
        super(context, "dogs_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableWhenNotExist = "CREATE TABLE " + NOTES_TABLE + "(" +
                NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTE + " TEXT, " +
                NOTE_DATE + " TEXT, " +
                DOG_ID + " INTEGER )";
        db.execSQL(createTableWhenNotExist);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public boolean add(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE, note.getNote());
        cv.put(NOTE_DATE, dateFormat.format(note.getDate()));
        cv.put(DOG_ID, note.getDogId());
        long insert = db.insert(NOTES_TABLE, null, cv);
        return insert != -1;
    }

    @Override
    public List<Note> findAll() {
        String query = "SELECT * FROM " + NOTES_TABLE;
        return getNotesListByQuery(query);
    }

    @Override
    public boolean remove(Note note) {
        String query = "DELETE FROM " + NOTES_TABLE + " WHERE " + NOTE_ID + " = " + note.getNoteId();
        return getCursorByQuery(query, this.getWritableDatabase());
    }

    @Override
    public boolean removeAll() {
        String query = "DELETE FROM " + NOTES_TABLE;
        return getCursorByQuery(query, this.getWritableDatabase());
    }

    @Override
    public Note findById(int id) {
        String query = "SELECT * FROM " + NOTES_TABLE + " WHERE " + NOTE_ID + " = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = getNote(cursor);
        }
        cursor.close();
        db.close();
        return note;
    }

    @Override
    public boolean update(int id_toUpdate, Note updated_T_Data) {
        String query = "" +
                "UPDATE " + NOTES_TABLE + " SET " +
                NOTE + " = " + updated_T_Data.getNote() + ", " +
                NOTE_DATE + " = " + dateFormat.format(updated_T_Data.getDate()) + ", " +
                DOG_ID + " = " + updated_T_Data.getDogId() + " " +
                "WHERE " +
                NOTE_ID + " = " + updated_T_Data.getNoteId();
        return getCursorByQuery(query, this.getWritableDatabase());
    }

    @Override
    public List<Note> getListByMasterId(int id) {
        String query = "SELECT * FROM " + NOTES_TABLE + " WHERE " + DOG_ID + " = " + id;
        return getNotesListByQuery(query);    }

    private boolean getCursorByQuery(String query , SQLiteDatabase db) {
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }

    private Note getNote(Cursor cursor) {
        Note note = new Note(cursor.getInt(0));
        note.setNote(cursor.getString(1));
        try {
            note.setDate(dateFormat.parse(cursor.getString(2)));
        } catch (ParseException e) {
            note.setDate(new Date());
        }
        note.setDogId(cursor.getInt(3));
        return note;
    }

    private List<Note> getNotesListByQuery(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> list = new LinkedList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do{
                Note note = getNote(cursor);
                list.add(note);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
