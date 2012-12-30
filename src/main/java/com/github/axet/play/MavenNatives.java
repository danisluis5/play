package com.github.axet.play;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.sun.jna.NativeLibrary;

public class MavenNatives {

    /**
     * enumerate native names, keep librarys dependency order.
     * 
     * to make this function works, your project should use
     * maven-nativedependencies-plugin. when active, it will unpack all native
     * librarys to the class path folder. this function will check all
     * ./natives/ folders and look for specified librarys to load. remeber to
     * enumerate all librarys, including dependecies, otherwise load library
     * will fail.
     * 
     * @param natives
     *            ex: ["vlccore", "vlc"]
     */
    static public void mavenNatives(String[] nativeLibrarys) {
        // use eclipse + maven-nativedependencies-plugin

        // VLC.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        // 1) under debugger, /Users/axet/source/mircle/play/target/classes/
        //
        // 2) mac osx wihtout debugger path -
        // /Users/axet/source/mircle/mircle/macosx/Mircle.app/Contents/Resources/Java/mircle.jar
        // case above 1) works prefectly
        //
        // 3) pack with maven under debugger
        // /Users/axet/.m2/repository/com/github/axet/play/0.0.3/play-0.0.3.jar

        for (String nativeLibrary : nativeLibrarys) {
            mavenNatives(nativeLibrary);
        }
    }

    /**
     * find and load one library
     * 
     * @param nativeLibrary
     *            ex: "vlc"
     * @return path to the library
     */
    static public File mavenNatives(String nativeLibrary) {
        File lib = null;

        {
            Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
            for (Thread thread : map.keySet()) {
                StackTraceElement[] stack = thread.getStackTrace();
                if (stack.length == 0)
                    continue;

                for (StackTraceElement main : stack) {
                    String mainClass = main.getClassName();

                    String path;
                    try {
                        Class<?> cls = Class.forName(mainClass);
                        CodeSource src = cls.getProtectionDomain().getCodeSource();
                        if (src == null)
                            continue;
                        path = src.getLocation().getPath();
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    // windows return path with %20
                    try {
                        path = URLDecoder.decode(path, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    File natives = new File(path);
                    natives = new File(natives.getParent());
                    natives = FileUtils.getFile(natives, "natives");
                    if (MavenNatives.checkPath(nativeLibrary, natives)) {
                        lib = natives;
                        break;
                    }
                }
            }
        }

        if (lib == null)
            throw new RuntimeException(nativeLibrary + " natives not found" + "\n"
                    + "you have to place natives next to the /classes folder or /main.jar file in the /natives folder");

        MavenNatives.setPath(nativeLibrary, lib);

        return lib;
    }

    public static boolean checkPath(String natives, File path) {
        String[] any = new String[] { "lib" + natives + ".so", "lib" + natives + ".dylib", "lib" + natives + ".jnilib",
                "lib" + natives + ".dll", natives + ".dll" };

        for (String l : any) {
            File vlc = FileUtils.getFile(path, l);
            if (vlc.exists())
                return true;
        }

        return false;
    }

    public static void setPath(String natives, File path) {
        NativeLibrary.addSearchPath(natives, path.toString());

        NativeLibrary.getInstance(natives);
    }

}
