package io.kaeawc.flow.app.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.BaseDatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;

import io.kaeawc.flow.app.R;
import io.kaeawc.flow.app.data.DatabaseModule;
import io.kaeawc.flow.app.data.Widget;
import timber.log.Timber;

public class MainActivityFragment extends Fragment {

    private Button mAddWidget;
    private Button mDestroyDatabase;
    private Button mBuildDatabase;
    private Button mRebootApp;

    private TextView mWidgetCount;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mAddWidget = (Button) view.findViewById(R.id.add_widget_button);
        mAddWidget.setOnClickListener(onAddClicked);

        mDestroyDatabase = (Button) view.findViewById(R.id.destroy_database_button);
        mDestroyDatabase.setOnClickListener(onDestroyClicked);

        mBuildDatabase = (Button) view.findViewById(R.id.build_database_button);
        mBuildDatabase.setOnClickListener(onBuildClicked);

        mRebootApp = (Button) view.findViewById(R.id.reboot_app_button);
        mRebootApp.setOnClickListener(onRebootClicked);

        mWidgetCount = (TextView) view.findViewById(R.id.widget_count);
        long count = Widget.getCount();
        mWidgetCount.setText(String.valueOf(count));

        return view;
    }

    private View.OnClickListener onAddClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!isAdded()) {
                return;
            }

            Timber.d("Creating new Widget");
            Widget widget = new Widget();
            widget.name = "asdf";
            widget.save();

            Timber.d("Updating Widget count");
            long count = Widget.getCount();
            mWidgetCount.setText(String.valueOf(count));
        }
    };

    private View.OnClickListener onDestroyClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!isAdded()) {
                return;
            }

            Timber.d("FlowManager.destroy");
            FlowManager.destroy();
            Context context = getActivity().getApplicationContext();
            String databaseName = getDatabaseName();
            boolean deleted = context.deleteDatabase(databaseName);

            if (deleted) {
                Timber.v("Database deleted");
            } else {
                Timber.v("Database not deleted");
            }

        }
    };

    private View.OnClickListener onBuildClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!isAdded()) {
                return;
            }

            Timber.d("FlowManager.init");
            Context context = getActivity().getApplicationContext();
            String databaseName = getDatabaseName();
            FlowManager.init(context);

            if (exists(context)) {
                Timber.v("Database exists");
            } else {
                Timber.v("Database does not exist");
            }

            try {
                BaseDatabaseDefinition definition = FlowManager.getDatabase(databaseName);
                Timber.v("Database does exist?");
            } catch (Exception exception) {
                Timber.v(exception, "Database does not exist");
            }

        }
    };

    private View.OnClickListener onRebootClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!isAdded()) {
                return;
            }

            Timber.d("Exiting app");
            System.exit(0);

        }
    };

    @NonNull
    public static String getDatabaseName() {
        // TODO: write a test that ensures this resource exists.
        return String.format("%s.db", DatabaseModule.NAME);
    }

    public static boolean exists(@NonNull Context context) {

        String databaseName = getDatabaseName();

        try {
            File dbFile = context.getDatabasePath(databaseName);
            return dbFile.exists();
        } catch (Exception exception) {
            Timber.v(exception, "Database %s doesn't exist.", databaseName);
            return false;
        }
    }
}
