package br.com.gabrielferreira.moreaqui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

public class PermissionsHandler extends AppCompatActivity {
    protected static final String[] PERMISSIONS = {"android.permission.INTERNET",
            "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    protected static final int PERMISSION_REQUEST_CODE = 200;

    private final Context context;
    public Activity activity;

    protected void requestPermission() {
        ActivityCompat.requestPermissions(activity,  PERMISSIONS, PERMISSION_REQUEST_CODE);
        activity.recreate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // verifca se as permissões solicitadas foram concedidas pelo usuário
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0) {

            boolean internetAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean fineLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean coarseLocationAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

            if (internetAccepted && fineLocationAccepted && coarseLocationAccepted) {
                Toast.makeText(context, "Permissões concedidas", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Permissões negadas", Toast.LENGTH_SHORT).show();

                if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel(
                            (dialog, which) -> requestPermissions(PERMISSIONS,
                                    PERMISSION_REQUEST_CODE));
                }
            }
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage("Você precisa autorizar todas as permissões.")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    protected boolean checkPermission() {
        int internetResult = ContextCompat.checkSelfPermission(context, INTERNET);
        int fineLocationResult = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION);
        int coarseLocationResult = ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION);

        // retorna true caso tenha as três permissões ou false caso uma das permissões tenha
        // sido negada
        return internetResult == PackageManager.PERMISSION_GRANTED &&
                fineLocationResult == PackageManager.PERMISSION_GRANTED &&
                coarseLocationResult == PackageManager.PERMISSION_GRANTED;
    }

    public PermissionsHandler(Context context) {
        this.context = context;
        this.activity = getActivity(this.context);
    }

    public Activity getActivity(Context context) //https://stackoverflow.com/questions/9891360/getting-activity-from-context-in-android
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }
        return null;
    }
}
