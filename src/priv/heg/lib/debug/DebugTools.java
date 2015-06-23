package priv.heg.lib.debug;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DebugTools {
	public static final boolean debug = true;
	private static int level = Log.DEBUG;
	private static String TAG = "hegang-lib";

	public static void log(String msg) {
		if (debug) {
			if (level == Log.ERROR) {
				Log.e(TAG, msg);
			} else if (level == Log.WARN) {
				Log.w(TAG, msg);
			} else if (level == Log.INFO) {
				Log.i(TAG, msg);
			} else {
				Log.d(TAG, msg);
			}
		}
	}
	
	public static void shortToast(Context context,String msg){
		if(debug){
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
