package com.threet.game.view;

import com.threet.game.App;
import com.threet.game.R;
import com.threet.game.model.Board;
import com.threet.game.model.Owner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The GridAdapter class
 * @author tchin
 *
 */
public class GridAdapter extends BaseAdapter {
	
	// The list of items
    private int[][] items;
    
    // TODO Constructor
    public GridAdapter(int[][] items) {
        this.items = items;
    }
    
    @Override // TODO ArrayAdapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_item, parent, false);
		}
		
		// Calculate the row and column.
		int numberOfColumns = items[0].length;
		int row = position / numberOfColumns;
		int column = position % numberOfColumns;
		
		// Refresh the view.
		Context context = App.getContext();
		TextView text = (TextView) v.findViewById(R.id.slot_title);
		Owner owner = Board.convert(items[row][column]);
		if (owner == Owner.P1) {
			text.setText(context.getString(R.string.p1_text));
			text.setBackgroundColor(Color.GRAY);
		} else if (owner == Owner.P2) {
			text.setText(context.getString(R.string.p2_text));
			text.setBackgroundColor(Color.CYAN);
		} else if (owner == Owner.ROBOT) {
			text.setText(context.getString(R.string.robot_text));
			text.setBackgroundColor(Color.CYAN);
		} else {
			text.setText("");
			text.setBackgroundColor(Color.WHITE);
		}
		
		return v;
	}

	/**
	 * Calculate number of items.
	 */
	@Override
	public int getCount() {
		int count = 0;
		for (int i = 0; i < getRowCount(); i++) {
			count += getColumnCount(i);
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	// TODO Private Methods
	/**
	 * Get the number of rows.
	 * @return the number of rows
	 */
	private int getRowCount() {
        return items.length;
    }

	/**
	 * Get the number of columns in specified row.
	 * @param row
	 * @return the number of columns
	 */
	private int getColumnCount(int row) {
        return items[row].length;
    }
}
