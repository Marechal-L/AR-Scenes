package com.application.ar.loadObj;

import android.opengl.GLES10;

import org.artoolkit.ar.base.rendering.RenderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class TDModel {
	Vector<Float> v;
	Vector<Float> vn;
	Vector<Float> vt;
	Vector<TDModelPart> parts;
	FloatBuffer vertexBuffer;
	FloatBuffer	mColorBuffer;
	float size, rotation, rotationX, rotationY, rotationZ;
	RGBColor position;

	public TDModel(Vector<Float> v, Vector<Float> vn, Vector<Float> vt,
			Vector<TDModelPart> parts) {
		super();
		this.v = v;
		this.vn = vn;
		this.vt = vt;
		this.parts = parts;
		position = new RGBColor(0,0,0);

		RGBColor color = RGBColor.getRandomColor();
		List<Float> colors = new ArrayList<>();
		for(int i=0;i<v.size();i++) {
			if(i%3==0)
			{
				colors.add(color.getR());
				colors.add(color.getG());
				colors.add(color.getB());
				colors.add(1.0f);
			}
		}

		float[] colors2 = new float[colors.size()];
		for(int i=0;i<colors.size();i++)
			colors2[i] = colors.get(i);

		buildVertexBuffer();
		mColorBuffer = RenderUtils.buildFloatBuffer(colors2);
	}

	public TDModel(TDModel o)
	{
		mColorBuffer = o.mColorBuffer.duplicate();//FloatBuffer.wrap(o.mColorBuffer.array());
		vertexBuffer = o.vertexBuffer.duplicate();//FloatBuffer.wrap(o.vertexBuffer.array());
		position = o.position;
		size = o.size;
		rotation = o.rotation;
		rotationX = o.rotationX;
		rotationY = o.rotationY;
		rotationZ = o.rotationZ;

		parts = new Vector<>();
		for(TDModelPart part : o.parts)
		{
			parts.add(new TDModelPart(part));
		}

	}


	public void setSize(float size) {this.size = size;}
	public float getSize(){return size;}
	public void setRotation(float rot, float x, float y , float z)
	{
		this.rotation = rot;
		rotationX=x;
		rotationY=y;
		rotationZ=z;
	}

	public float getRotation(){return rotation;}
	public float getRotationX(){return rotationX;}
	public float getRotationY(){return rotationY;}
	public float getRotationZ(){return rotationZ;}
	public void setPosition(RGBColor pos){position = pos;}
	public RGBColor getPosition(){return position;}

	public String toString(){
		/*String str=new String();
		str+="Number of parts: "+parts.size();
		str+="\nNumber of vertexes: "+v.size();
		str+="\nNumber of vns: "+vn.size();
		str+="\nNumber of vts: "+vt.size();
		str+="\n/////////////////////////\n";
		for(int i=0; i<parts.size(); i++){
			str+="Part "+i+'\n';
			str+=parts.get(i).toString();
			str+="\n/////////////////////////";
		}*/
		return "";
	}
	public void draw(GL10 unused) {

		GLES10.glColorPointer(4, GLES10.GL_FLOAT, 0, mColorBuffer);
		GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertexBuffer);

		GLES10.glEnableClientState(GLES10.GL_COLOR_ARRAY);
		GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
		GLES10.glEnableClientState(GLES10.GL_NORMAL_ARRAY);

		for(int i=0; i<parts.size(); i++){
			TDModelPart t=parts.get(i);
			//Material m=t.getMaterial();
			/*if(m!=null){
				FloatBuffer a=m.getAmbientColorBuffer();
				FloatBuffer d=m.getDiffuseColorBuffer();
				FloatBuffer s=m.getSpecularColorBuffer();
				GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK,GL10.GL_AMBIENT,a);
				GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK,GL10.GL_SPECULAR,s);
				GLES10.glMaterialfv(GLES10.GL_FRONT_AND_BACK,GL10.GL_DIFFUSE,d);
			}*/
			//scaleM(scaleMatrix, 0, x, y, z);
			//GLES10.
			GLES10.glNormalPointer(GLES10.GL_FLOAT, 0, t.getNormalBuffer());
			GLES10.glDrawElements(GLES10.GL_TRIANGLES,t.getFacesCount(),GLES10.GL_UNSIGNED_SHORT,t.getFaceBuffer());

			GLES10.glDisableClientState(GLES10.GL_COLOR_ARRAY);
			GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
			GLES10.glDisableClientState(GLES10.GL_NORMAL_ARRAY);

		}
	}
	public void buildVertexBuffer(){
		ByteBuffer vBuf = ByteBuffer.allocateDirect(v.size() * 4);
		vBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = vBuf.asFloatBuffer();
		vertexBuffer.put(toPrimitiveArrayF(v));
		vertexBuffer.position(0);
	}
	private static float[] toPrimitiveArrayF(Vector<Float> vector){
		float[] f;
		f=new float[vector.size()];
		for (int i=0; i<vector.size(); i++){
			f[i]=vector.get(i);
		}
		return f;
	}
}


