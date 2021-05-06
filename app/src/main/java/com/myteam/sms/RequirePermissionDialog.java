package com.myteam.sms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RequirePermissionDialog extends AppCompatDialogFragment {

    private TextView dialogTextView;
    private PermissionDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Cấp Quyền Truy Cập Tin Nhắn")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.denyForPermission(true);
                    }
                })
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyForPermission(true);
                    }
                });
        dialogTextView = view.findViewById(R.id.dialog_text);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (PermissionDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement PermissionDialogListener");
        }
    }

    public interface PermissionDialogListener{
        void applyForPermission(boolean isApply);
        void denyForPermission(boolean isDeny);
    }
}
