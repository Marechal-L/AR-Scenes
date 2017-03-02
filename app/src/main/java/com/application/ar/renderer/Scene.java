package com.application.ar.renderer;

import android.util.Log;

import com.application.ar.loadObj.TDModel;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by ludovic on 11/01/17.
 */

public class Scene
{
    public List<TDModel> models;

    public Scene()
    {
        models = new ArrayList<>();
    }

    public void addObject(TDModel o){models.add(o);
        Log.v("obj",""+o);}

    public void draw(GL10 gl)
    {
        Log.v("Draw","Draw Scene : "+models.size()+" elements");

        for(TDModel model : models) {
            gl.glPushMatrix();

            gl.glRotatef(model.getRotation(), model.getRotationX(), model.getRotationY(), model.getRotationZ());
            gl.glTranslatef(model.getPosition().getR(), model.getPosition().getG(), model.getPosition().getB());
            gl.glScalef(model.getSize(), model.getSize(), model.getSize());

            model.draw(gl);

            gl.glPopMatrix();
        }
    }
}
