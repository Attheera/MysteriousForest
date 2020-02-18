package com.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.game.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	private MenuScene menuChildScene;
	
	private final int MENU_PLAY = 1;
	private final int MENU_PLAY2 = 2;
	private final int MENU_PLAY3 = 3;
	private final int MENU_CREDIT = 0;
	
	//---------------------------------------------
	// METHODS FROM SUPERCLASS
	//---------------------------------------------

	@Override
	public void createScene()
	{
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed()
	{
		System.exit(0);
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_MENU;
	}
	

	@Override
	public void disposeScene()
	{
		// TODO Auto-generated method stub
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
			case MENU_PLAY:
				//Load Game Scene!
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case MENU_PLAY2:
				//Load Game Scene!
				SceneManager.getInstance().loadGameScene2(engine);
				return true;
			case MENU_PLAY3:
				//Load Game Scene!
				SceneManager.getInstance().loadGameScene3(engine);
				return true;
			case MENU_CREDIT:
				SceneManager.getInstance().loadCreditScene(engine);
				return true;
			default:
				return false;
		}
	}
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------
	
	private void createBackground()
	{
		attachChild(new Sprite(0, 0, resourcesManager.menu_background_region, vbom)
		{
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
		});
	}
	
	private void createMenuChildScene()
	{
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);
		
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
		final IMenuItem playMenuItem2 = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY2, resourcesManager.play_region, vbom), 1.2f, 1);
		final IMenuItem playMenuItem3 = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY3, resourcesManager.play_region, vbom), 1.2f, 1);
		final IMenuItem creditMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_CREDIT, resourcesManager.credit_region, vbom), 1.2f, 1);
		
		menuChildScene.addMenuItem(playMenuItem);
		//menuChildScene.addMenuItem(playMenuItem2);
		//menuChildScene.addMenuItem(playMenuItem3);
		menuChildScene.addMenuItem(creditMenuItem);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY()-50);
		//playMenuItem2.setPosition(playMenuItem2.getX(), playMenuItem2.getY()-50);
		//playMenuItem3.setPosition(playMenuItem3.getX(), playMenuItem3.getY()+20);
		creditMenuItem.setPosition(creditMenuItem.getX(), creditMenuItem.getY() + 20);
		
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}
}