package de.awisus.refugeeaidleipzig.views.profile;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Vector;

import de.awisus.refugeeaidleipzig.R;
import de.awisus.refugeeaidleipzig.models.ImageDataObject;
import de.awisus.refugeeaidleipzig.models.Nutzer;
import de.awisus.refugeeaidleipzig.net.WebFlirt;
import de.awisus.refugeeaidleipzig.util.BackgroundTask;
import de.awisus.refugeeaidleipzig.util.Utility;
import de.awisus.refugeeaidleipzig.views.SuperAdapter;

/**
 * Created on 13.03.16.
 *
 * @author Jens Awisus
 */
public class AdapterUserData extends SuperAdapter<ImageDataObject> {

    private Nutzer nutzer;

    public AdapterUserData(Context context, int resource, Vector<ImageDataObject> objects, Nutzer nutzer) {
        super(context, resource, objects, R.layout.entry_userdata);
        this.nutzer = nutzer;
    }

    @Override
    protected void doExtraBits(int position, View view) {
        final ImageDataObject data = liste.get(position);

        ImageView ivBild  = (ImageView) view.findViewById(R.id.ivBild);
        ivBild.setImageBitmap(
                Utility.getInstance().stringToImage(data.getImageData())
        );

        FloatingActionButton fabMinus;
        fabMinus = (FloatingActionButton) view.findViewById(R.id.fab_minus);
        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.fab_minus) {
                    Activity activity = (Activity) getContext();

                    int id = data.getId();
                    new BedarfDelete(activity, R.string.meldung_entfernen).execute("id", "" + id);
                }
            }
        });
    }

    private class BedarfDelete extends BackgroundTask<String, Integer, Integer> {

        public BedarfDelete(Activity context, int textID) {
            super(context, textID);
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String antwort = WebFlirt.getInstance().delete("needs_remote", params);
                return Integer.parseInt(antwort);
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void doPostExecute(Integer result) {
            if(result == null) {
                Toast.makeText(context, R.string.warnung_fehler, Toast.LENGTH_SHORT).show();
            } else {
                nutzer.loescheBedarf(result);
            }
        }
    }
}
