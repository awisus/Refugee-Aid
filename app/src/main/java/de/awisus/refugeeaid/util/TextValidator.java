package de.awisus.refugeeaid.util;

import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created on 12.03.16.
 *
 * @author jens
 */
public abstract class TextValidator implements TextWatcher {

    private EditText view;

    public TextValidator(EditText view) {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
