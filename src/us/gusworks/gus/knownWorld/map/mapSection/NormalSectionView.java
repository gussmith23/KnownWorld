package us.gusworks.gus.knownWorld.map.mapSection;

import us.gusworks.gus.knownWorld.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NormalSectionView extends ImageView implements MapSection {

	public NormalSectionView(Context context) {
		super(context);

		super.setImageResource(R.drawable.ic_launcher);
		
	}

}
