package br.com.vandre.KeepCalmAndKaraokeVoto.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import br.com.vandre.KeepCalmAndKaraokeVoto.R;

public class Funcoes {
    public static boolean isLong(final String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static AlertDialog.Builder mensagem(Context context,
                                               String mensagem, boolean show) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setCancelable(false);
        builder.setMessage(mensagem);
        if (show) {
            builder.setNeutralButton(android.R.string.ok, null);
            builder.create().show();
        }
        return builder;
    }

    public static Intent openWhatsApp(String menssagem) {
        String smsNumber = "5519982682030"; //sem '+'

        Intent sendIntent = new Intent("android.intent.action.MAIN");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, menssagem);
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");
        return sendIntent;
    }

}
