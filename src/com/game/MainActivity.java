package com.game;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class MainActivity extends BaseGameActivity {

	Scene scene;
	protected static final int CAMERA_WIDTH = 1280;
	protected static final int CAMERA_HEIGHT = 752;  
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTexureRegion;
	PhysicsWorld physicsWorld;

	SceneManager sceneManager;
	BoundCamera mCamera;

	public Sound effect;
	public Music sound;
	public boolean isPlay = false;
	@Override
	public EngineOptions onCreateEngineOptions() {

		mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		ResourcesManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager());
		
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		/*try {
			sound = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this, "test.ogg");
			sound.setLooping(true);

			effect = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "choosemenu.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}*/
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene,  
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {

		mEngine.registerUpdateHandler(new TimerHandler(1f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						SceneManager.getInstance().createMenuScene();
					}
				}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
}