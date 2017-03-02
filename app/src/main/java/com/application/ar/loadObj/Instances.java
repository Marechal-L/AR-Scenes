package com.application.ar.loadObj;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludovic on 15/02/17.
 */

public final class Instances
{
    public enum NAMES{TREE1, TREE2, SWING, FOUNTAIN, BENCH};

    private static String[] files = new String[]{"Data/tree1.obj","Data/tree2.obj","Data/swing.obj","Data/fountain.obj","Data/bench.obj"};//new String[]{"Data/tree1.obj","Data/tree2.obj"};
    private static boolean loaded = false;
    private static int currentID = 0;

    public static Context context;
    public static List<TDModel> models = new ArrayList<>();

    public static void loadModels()
    {
        if(loaded) return;

        Log.v("Step","Load Models");
        OBJParser parser;

        for(String file : files)
        {
            parser=new OBJParser(context);
            models.add(parser.parseOBJ(file));
            Log.v("Step",file+" loaded");
        }

        loaded = true;
    }

    public static int loadNext()
    {
        if(currentID == files.length){
            loaded = true;
            return -1;
        }

        Log.v("Step",files[currentID]+" loaded");
        OBJParser parser=new OBJParser(context);
        models.add(parser.parseOBJ(files[currentID]));

        currentID++;

        return (int) ((currentID / (float) files.length) * 100);
    }
}
