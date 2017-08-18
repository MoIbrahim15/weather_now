package com.mohamedibrahim.weathernow.widget;

import android.content.Context;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mohamedibrahim.weathernow.R;
import com.mohamedibrahim.weathernow.data.CityContract;

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor mCursor;
    private Context mContext;

    private static final int POSITION_CITY = 1;
    private static final int POSITION_DEGREE = 2;

    public WidgetFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        String title = mCursor.getString(POSITION_CITY).concat(" ").concat(mCursor.getString(POSITION_DEGREE));
        views.setTextViewText(R.id.tv_widget, title);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(CityContract.CityEntry.CONTENT_URI, null, null, null, null);
    }

}