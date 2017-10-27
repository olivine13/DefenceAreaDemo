package com.cvte.defencearea;

import android.content.Context;

/**
 * TODO Description
 *
 * @author laizhenqi
 * @since 2017/10/23
 */
public class DFMUtils {

    private static final String SERVICE_NAME = "defence_area_manager";

    public static DefenceAreaManager getSystemService(Context context) throws RuntimeException {
        DefenceAreaManager manager = (DefenceAreaManager) context.getSystemService(SERVICE_NAME);
        if (manager == null) throw new RuntimeException("not support DefenceArea control");
        return manager;
    }

    public static int readStatusBinary(int status, int num) {
        return status >>> (num*2) & 3;
    }
}
