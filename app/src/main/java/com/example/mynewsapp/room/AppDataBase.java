package com.example.mynewsapp.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mynewsapp.models.NewsModel;


@Database(entities = {NewsModel.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract NewsDao newsDao();

}
