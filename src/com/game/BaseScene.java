package com.game;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.game.SceneManager.SceneType;

import android.app.Activity;
import android.content.res.AssetManager;

public abstract class BaseScene extends Scene
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera camera;
	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------
	
	public BaseScene()
	{
		this.resourcesManager = ResourcesManager.getInstance();
		this.engine = resourcesManager.engine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		createScene();
	}
	
	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------
	
	public abstract void createScene();
	
	public abstract void onBackKeyPressed();
	
	public abstract SceneType getSceneType();
	
	public abstract void disposeScene();
}