package com.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import com.game.SceneManager.SceneType;
public class CreditScene extends BaseScene implements IOnSceneTouchListener
{
	@Override
	public void createScene()
	{
		createBackground();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (pSceneTouchEvent.isActionDown()) {
			SceneManager.getInstance().loadMenuScene(engine);
			return true;
		}
		return false; 
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_CREDIT;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	private void createBackground()
	{
		attachChild(new Sprite(0, 0, resourcesManager.credit_background_region, vbom)
		{
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
		});
	}
}