package com.ateam.mannajob.recycleCalendar;

import android.view.View;

public interface OnCalendarItemClickListener {
    public void onItemClick(CalendarAdapter.ViewHolder viewHolder, View view, int position);
}
