package com.game;

import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

public class SceneManager
{
	//---------------------------------------------
	// SCENES
	//---------------------------------------------
	
	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene creditScene;
	public BaseScene gameScene;
	public BaseScene gameScene2;
	public BaseScene gameScene3;
	private BaseScene loadingScene;
	
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	private static final SceneManager INSTANCE = new SceneManager();
	
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	
	private BaseScene currentScene;
	public BaseScene oldScene;
	
	private Engine engine = ResourcesManager.getInstance().engine;
	
	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_GAME2,
		SCENE_GAME3,
		SCENE_CREDIT,
		SCENE_LOADING,
	}
	
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------
	
	public void setScene(BaseScene scene)
	{
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
			case SCENE_MENU:
				setScene(menuScene);
				break;
			case SCENE_GAME:
				setScene(gameScene);
				break;
			case SCENE_GAME2:
				setScene(gameScene2);
				break;
			case SCENE_GAME3:
				setScene(gameScene3);
				break;
			case SCENE_SPLASH:
				setScene(splashScene);
				break;
			case SCENE_LOADING:
				setScene(loadingScene);
				break;
			case SCENE_CREDIT:
				setScene(creditScene);
				break;
			default:
				break;
		}
	}
	
	public void createMenuScene()
	{
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		oldScene = menuScene;
		loadingScene = new LoadingScene();
        SceneManager.getInstance().setScene(menuScene);
        disposeSplashScene();
	}
	
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	private void disposeSplashScene()
	{
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}
	
	public void loadGameScene(final Engine mEngine)
	{
		setScene(loadingScene);
		if(oldScene != menuScene){
			oldScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadMenuTextures();
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadGameResources();
        		gameScene = new GameScene();
        		oldScene = gameScene;
        		setScene(gameScene);
            }
		}));
	}
	
	public void loadGameScene2(final Engine mEngine)
	{
		setScene(loadingScene);
		if(oldScene != menuScene){
			oldScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadMenuTextures();
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadGameResources();
        		gameScene2 = new GameScene2();
        		oldScene = gameScene2;
        		setScene(gameScene2);
            }
		}));
	}
	
	public void loadGameScene3(final Engine mEngine)
	{
		setScene(loadingScene);
		if(oldScene != menuScene){
			oldScene.disposeScene();
		}
		ResourcesManager.getInstance().unloadMenuTextures();
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadGameResources();
        		gameScene3 = new GameScene3();
        		oldScene = gameScene3;
        		setScene(gameScene3);        		
            }
		}));
	}
	
	public void loadMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);		
		oldScene.disposeScene();
		ResourcesManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadMenuTextures();
            	oldScene = menuScene;
        		setScene(menuScene);
            }
		}));
	}
	public void loadCreditScene(final Engine mEngine)
	{
		setScene(loadingScene);		
		//oldScene.disposeScene();
		ResourcesManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
            	mEngine.unregisterUpdateHandler(pTimerHandler);
            	ResourcesManager.getInstance().loadCreditTextures();
            	oldScene = creditScene;
        		setScene(creditScene);
            }
		}));
	}
	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------
	
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
}