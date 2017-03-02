package com.application.ar.renderer;

import android.content.Context;
import android.util.Log;

import com.application.ar.loadObj.Instances;
import com.application.ar.loadObj.OBJParser;
import com.application.ar.loadObj.RGBColor;
import com.application.ar.loadObj.TDModel;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;
import org.artoolkit.ar.base.rendering.Cube;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by ludovic on 11/01/17.
 */

public class SceneRenderer extends ARRenderer
{
    private static final float PI = 3.1415f;
    private static final float MARKER_HEIGHT = 0f, MARKER_ANGLE = 0f, MARKER_DIST = 0f;
    private static float GRID_SCALE_X = 20.0f, GRID_SCALE_Y = 20.0f, GRID_SCALE_Z = 0.0f;
    private static float GRID_X = -1000.0f, GRID_Y = 200.0f, GRID_Z = 10.0f;

    private Cube cube = new Cube(40.0f,0,0,0);

    private int markerID = -1;
    private OBJParser parser;
    private List<Scene> scenes;
    private int currentScene;

    private Context context;

    private boolean ONLINE;

    public SceneRenderer(Context context, boolean online)
    {
        super();
        this.context = context;
        scenes = new ArrayList<>();
        currentScene = 0;
        ONLINE = online;


    }


    @Override
    public boolean configureARScene()
    {
        Log.v("Step","SceneRenderer()");

        //Instances.context = context;
        //Instances.loadModels();

        Log.v("Step","SceneRenderer() /");

        if(!ONLINE) {
            //loadDebugScene();
            //loadDebugScene2();
            loadDebugScene3();
            loadDebugScene4();
        }

        markerID = ARToolKit.getInstance().addMarker("single;Data/patt.hiro;80");
        if (markerID < 0) return false;

        return true;
    }

    public void click()
    {
        if(scenes.size() != 0)
            currentScene = (currentScene+1)%scenes.size();
    }

    public void draw(GL10 gl)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(ARToolKit.getInstance().getProjectionMatrix(), 0);


        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glFrontFace(GL10.GL_CW);

        gl.glMatrixMode(GL10.GL_MODELVIEW);

        if (ARToolKit.getInstance().queryMarkerVisible(markerID)) {

            gl.glLoadMatrixf(ARToolKit.getInstance().queryMarkerTransformation(markerID), 0);

            if(scenes.size() != 0)
                drawScene(gl,scenes.get(currentScene));
            else
                drawCube(gl);
        }
    }


    private void drawScene(GL10 gl, Scene scene)
    {
        scene.draw(gl);
    }

    private void drawCube(GL10 gl)
    {
        gl.glPushMatrix();

        cube.draw(gl);

        gl.glPopMatrix();
    }

    private void loadDebugScene3() {
        float X = 4, Y = 4;
        GRID_X = -((float) X / 2) * GRID_SCALE_X;

        Scene scene = new Scene();

        addObject2(scene, Instances.NAMES.TREE1, 0, 0);
        addObject2(scene, Instances.NAMES.TREE2, 0, 1);
        addObject2(scene, Instances.NAMES.TREE1, 0, 2);
        addObject2(scene, Instances.NAMES.TREE2, 0, 3);

        addObject2(scene, Instances.NAMES.TREE2, 1, 0);
        addObject2(scene, Instances.NAMES.TREE1, 1, 1);
        addObject2(scene, Instances.NAMES.TREE2, 1, 2);
        addObject2(scene, Instances.NAMES.TREE1, 1, 3);

        addObject2(scene, Instances.NAMES.TREE1, 2, 0);
        addObject2(scene, Instances.NAMES.TREE2, 2, 1);
        addObject2(scene, Instances.NAMES.TREE1, 2, 2);
        addObject2(scene, Instances.NAMES.TREE2, 2, 3);

        addObject2(scene, Instances.NAMES.TREE2, 3, 0);
        addObject2(scene, Instances.NAMES.TREE1, 3, 1);
        addObject2(scene, Instances.NAMES.TREE2, 3, 2);
        addObject2(scene, Instances.NAMES.TREE1, 3, 3);

        Log.v("scene", "Scene added : " + scene.models.size() + " element(s)");
        scenes.add(scene);
    }

    private void loadDebugScene4() {
        float X = 4, Y = 4;
        GRID_X = -((float) X / 2) * GRID_SCALE_X;

        Scene scene = new Scene();

        addObject2(scene, Instances.NAMES.TREE1, 0, 2);

        addObject2(scene, Instances.NAMES.TREE2, 1, 0);
        addObject2(scene, Instances.NAMES.TREE2, 1, 2);

        addObject2(scene, Instances.NAMES.TREE1, 2, 2);

        addObject2(scene, Instances.NAMES.TREE2, 3, 0);
        addObject2(scene, Instances.NAMES.TREE2, 3, 2);

        Log.v("scene", "Scene added : " + scene.models.size() + " element(s)");
        scenes.add(scene);
    }

    private void addObject(Scene scene, String filename, int i, int j)
    {
        TDModel model;

        parser=new OBJParser(context);
        model = parser.parseOBJ(filename);

        //model.setRotation(-MARKER_ANGLE,1,0,0);
        model.setSize(20);

        model.setPosition(new RGBColor(GRID_X + GRID_SCALE_X *i, GRID_SCALE_Y*j, GRID_SCALE_Z * j));

        scene.addObject(model);
    }

    private void addObject2(Scene scene, Instances.NAMES name, int i, int j)
    {
        TDModel model;

        parser=new OBJParser(context);
        model = new TDModel(Instances.models.get(name.ordinal()));

        //model.setRotation(-MARKER_ANGLE,1,0,0);
        model.setSize(10);

        model.setPosition(new RGBColor(GRID_X + GRID_SCALE_X *i, GRID_SCALE_Y*j, GRID_SCALE_Z * j));

        scene.addObject(model);
    }

    public int getCurrentScene(){return currentScene;}
}