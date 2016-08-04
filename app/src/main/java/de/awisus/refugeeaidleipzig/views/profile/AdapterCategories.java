package de.awisus.refugeeaidleipzig.views.profile;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.Vector;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.Kategorie;
import de.awisus.refugeeaidleipzig.util.Utility;
import de.awisus.refugeeaidleipzig.views.SuperAdapter;

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
                Utility.getInstance().stringToImage(liste.get(position).getImageData())
        );
    }
}
