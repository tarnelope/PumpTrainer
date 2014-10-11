package com.ttarn.pumptrainer;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {
	
	public static Typeface getChunkfive(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/Chunkfive.otf");
	}
	
	public static Typeface getKozopro(Context context) {
		return Typeface.createFromAsset(context.getAssets(), "fonts/KozGoPro.otf");
	}

}
