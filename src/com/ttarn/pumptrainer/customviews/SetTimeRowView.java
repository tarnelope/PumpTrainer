package com.ttarn.pumptrainer.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ttarn.pumptrainer.Fonts;
import com.ttarn.pumptrainer.R;

public class SetTimeRowView extends RelativeLayout {
	
	private Typeface mFont;
	private Typeface mEditTextFont;
	
	private boolean isTimer;
	private String labelText;
	
	private TextView labelTextView;
	private EditText minText;
	private EditText secText;
	
	private int minutes;
	private int seconds;

	public SetTimeRowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//get the attributes specified in attrs.xml using the name we included
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
		    R.styleable.SetTimeRowView, 0, 0);
		
		try {
			isTimer = a.getBoolean(R.styleable.SetTimeRowView_timer, true);
			labelText = a.getString(R.styleable.SetTimeRowView_label_text);
		} finally {
			a.recycle();
		}
		
		mFont = Fonts.getChunkfive(context);
		mEditTextFont = Fonts.getKozopro(context);
		
		View.inflate(context, R.layout.set_time_row_view, this);
		
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		labelTextView = (TextView) findViewById(R.id.set_time_row_label);
		labelTextView.setTypeface(mFont);
		labelTextView.setText(labelText);
		
		secText = (EditText) findViewById(R.id.second_text);
		secText.setTypeface(mEditTextFont);
		secText.setHintTextColor(getResources().getColor(R.color.light_gray));
		minText = (EditText) findViewById(R.id.minute_text);
		minText.setTypeface(mEditTextFont);
		minText.setHintTextColor(getResources().getColor(R.color.light_gray));
		
		if (!isTimer) {
			minText.setVisibility(GONE);
			findViewById(R.id.time_colon).setVisibility(GONE);
			secText.setHint("#");
		}
	};
	
	
	public int getMinutes() {
		String minEntry = minText.getText().toString();
		if (minEntry.isEmpty() || minEntry.matches("")) {
			minutes = 0;
		} else {
			minutes = Integer.parseInt(minEntry);
		}
		return minutes * 60;
	}
	
	public int getSeconds() {
		String secEntry = secText.getText().toString();
		if (secEntry.isEmpty() || secEntry.matches("")) {
			seconds = 0;
		} else {
			seconds = Integer.parseInt(secEntry);
		}
		return seconds;
	}

}
