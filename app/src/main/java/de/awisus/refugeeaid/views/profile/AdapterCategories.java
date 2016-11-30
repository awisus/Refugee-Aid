package de.awisus.refugeeaid.views.profile;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.Vector;

import de.awisus.refugeeaid.R;
import de.awisus.refugeeaid.models.Kategorie;
import de.awisus.refugeeaid.util.ImageUtility;
import de.awisus.refugeeaid.views.SuperAdapter;

/**
 * Created on 04.08.16.
 *
 * @author jens
 */
public class AdapterCategories extends SuperAdapter<Kategorie> {

    public AdapterCategories(Context context, int resource, Vector<Kategorie> objects, int layoutID) {
        super(context, resource, objects, layoutID);
    }

    protected void doExtraBits(int position, View view) {
        ImageView ivBild;
        ivBild = (ImageView) view.findViewById(R.id.ivBild);
        ivBild.setImageBitmap(
                ImageUtility.stringToImage(liste.get(position).getImageData())
        );
    }
}
