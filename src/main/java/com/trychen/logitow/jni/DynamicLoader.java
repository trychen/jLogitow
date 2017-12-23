package com.trychen.logitow.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public interface DynamicLoader {
    static boolean loadLibrary(){
        try {
            InputStream in = DynamicLoader.class.getClass().getResource("/logitow" + SystemType.getCurrentSystem().getJniLibSuffix()).openStream();
            File jnilib = File.createTempFile("logitow", SystemType.getCurrentSystem().getJniLibSuffix());
            FileOutputStream out = new FileOutputStream(jnilib);
            int i;
            byte [] buf = new byte[1024];
            while((i=in.read(buf))!=-1) {
                out.write(buf,0,i);
            }

            in.close();
            out.close();
            jnilib.deleteOnExit();
            System.load(jnilib.toString());
            return true;
        }catch (Exception e) {
            System.err.println("Unable to load jni lib!");
            e.printStackTrace();
            return false;
        }
    }

    static boolean isCurrentSystemSupport() {
        if (SystemType.getCurrentSystem() == SystemType.MACOSX) {
            return SystemType.getCurrentSystemVersion().startsWith("10.1");
        } else if (SystemType.getCurrentSystem() == SystemType.WINDOWS) {
            return SystemType.getCurrentSystemVersion().startsWith("10.");
        }
        return false;
    }
}
