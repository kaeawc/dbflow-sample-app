package io.kaeawc.flow.app.data;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = DatabaseModule.NAME, version = DatabaseModule.VERSION)
public class DatabaseModule {

    public static final String NAME = "SampleApp";
    public static final int VERSION = 1;
}
