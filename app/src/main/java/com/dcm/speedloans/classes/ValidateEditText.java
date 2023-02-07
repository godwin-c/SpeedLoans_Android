package com.dcm.speedloans.classes;

import android.text.TextUtils;
import android.widget.EditText;

public class ValidateEditText {
        private EditText editText;

    public ValidateEditText(EditText editText) {
        this.editText = editText;
    }

    public boolean isValid(){
        if (editText.getText().toString().trim().isEmpty()) {

            return false;

        }
        return true;
    }

    public boolean isValidEmail() {
        return !TextUtils.isEmpty(editText.getText().toString().trim()) && android.util.Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString().trim()).matches();
    }

}
