package com.game;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.Body;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.util.Log;

public class ResourcesManager
{
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------
	
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public MainActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	
	public Font font;
	
	//---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	//---------------------------------------------
	public ITextureRegion splash_region;
	public ITextureRegion menu_background_region;
	public ITextureRegion credit_background_region;
	public ITextureRegion play_region;
	public ITextureRegion credit_region;
	
	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	
	// Game Texture Regions
	public ITextureRegion platform1_region;
	public ITextureRegion platform2_region;
	public ITextureRegion platform3_region;
	public ITextureRegion coin_region;
	public ITiledTextureRegion player_region;
	
	public BitmapTextureAtlas splashTextureAtlas;
	public BuildableBitmapTextureAtlas menuTextureAtlas,creditTextureAtlas;
	
	
	public BitmapTextureAtlas mBitmapTextureAtlas;
	public TiledTextureRegion mPlayerTextureRegion;
	
	public BitmapTextureAtlas dBitmapTextureAtlas;
	public TiledTextureRegion dDevilTextureRegion;
	
	public BitmapTextureAtlas seedBitmapTextureAtlas;
	public TiledTextureRegion SeedTextureRegion;
	
	public BitmapTextureAtlas soulBitmapTextureAtlas;
	public TiledTextureRegion SoulTextureRegion;
	
	public BitmapTextureAtlas stumpBitmapTextureAtlas;
	public TiledTextureRegion StumpTextureRegion;
	
	public BitmapTextureAtlas cTreeBitmapTextureAtlas;
	public ITextureRegion cTreeTextureRegion;
	
	public BitmapTextureAtlas nBitmapTextureAtlas;
	public TiledTextureRegion nTextureRegion;
	
	public TMXTiledMap mTMXTiledMap;
	public int mCactusCount;
	
	public BitmapTextureAtlas PinkmagicBitmapTextureAtlas;
	public TiledTextureRegion PinkTextureRegion;
	public BitmapTextureAtlas GreenmagicBitmapTextureAtlas;
	public TiledTextureRegion GreenTextureRegion;
	public BitmapTextureAtlas KnifeBitmapTextureAtlas;
	public TiledTextureRegion KnifeTextureRegion;
	public BitmapTextureAtlas InvBitmapTextureAtlas;
	public ITextureRegion InvTextureRegion;
	public ITextureRegion iSeedTextureRegion;
	
	public BitmapTextureAtlas PinkTabBitmapTextureAtlas;
	public TiledTextureRegion PinkTabTextureRegion;
	public BitmapTextureAtlas GreenTabBitmapTextureAtlas;
	public TiledTextureRegion GreenTabTextureRegion;
	
	public BitmapTextureAtlas VineBitmapTextureAtlas;
	public TiledTextureRegion VineTextureRegion;
	public BitmapTextureAtlas SpikeVineBitmapTextureAtlas;
	public TiledTextureRegion SpikeVineTextureRegion;
	
	public BitmapTextureAtlas fBitmapTextureAtlas;
	public ITextureRegion fTextureRegion;
	
	public BitmapTextureAtlas SaplingBitmapTextureAtlas;
	public ITextureRegion SaplingTextureRegion;
	
	public BitmapTextureAtlas mOnScreenControlTexture;
	public ITextureRegion mOnScreenControlBaseTextureRegion;
	public ITextureRegion mOnScreenControlKnobTextureRegion;
		
	public BitmapTextureAtlas WarpBitmapTextureAtlas;
	public TiledTextureRegion WarpTextureRegion;
    
    public BitmapTextureAtlas backBitmapTextureAtlas;
    public ITextureRegion backTextureRegion;
    
    public BitmapTextureAtlas dGrassRepeatingAtlas1;
    public ITextureRegion dGrassTextureRegion;
    public ITextureRegion dDirtTextureRegion;
	
    public int powerpink = 5, maxpink = 5, powergreen = 0, maxgreen = 0;
    
   
	//---------------------------------------------
	// CLASS LOGIC
	//---------------------------------------------

	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}
	public void loadCreditResources()
	{
		loadCreditGraphics();
	}
	public void loadGameResources()
	{
		ResourcesManager.getInstance().powerpink=5;
		ResourcesManager.getInstance().powergreen=0;
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}
	  
	private void loadMenuGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1792, 1024, TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
        play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        credit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "credit.png");
       
    	try 
    	{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
	}
	private void loadCreditGraphics()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/credit/");
        creditTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1280, 752, TextureOptions.BILINEAR);
        credit_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditTextureAtlas, activity, "credit_background.png");
       
    	try 
    	{
			creditTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			creditTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
	}
	private void loadMenuAudio()
	{
		
	}
	
	private void loadMenuFonts()
	{
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "fff.ttf", 80, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	private void loadGameGraphics() 
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
       
		mBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 320, 288, TextureOptions.DEFAULT);
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, activity, "sprite_animate_kid.png", 0, 0, 5, 3);
		mBitmapTextureAtlas.load();  
		
		dBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1840, 160, TextureOptions.DEFAULT);
		dDevilTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(dBitmapTextureAtlas, activity, "sprite_devil.png", 0, 0, 23, 1);
		dBitmapTextureAtlas.load();
		
		stumpBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 49, TextureOptions.DEFAULT);
		StumpTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(stumpBitmapTextureAtlas, activity, "stump.png", 0, 0, 2, 1);
		stumpBitmapTextureAtlas.load();
		
		seedBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 64, 32, TextureOptions.DEFAULT);
		SeedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(seedBitmapTextureAtlas, activity, "seed.png", 0, 0, 2, 1);
		seedBitmapTextureAtlas.load();
		
		soulBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 64, 32, TextureOptions.DEFAULT);
		SoulTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(soulBitmapTextureAtlas, activity, "soul.png", 0, 0, 2, 1);
		soulBitmapTextureAtlas.load();
		
		PinkmagicBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 180, 90, TextureOptions.DEFAULT);
		PinkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(PinkmagicBitmapTextureAtlas, activity, "sprite_magic_icon.png", 0, 0, 2, 1);
		PinkmagicBitmapTextureAtlas.load();
		
		GreenmagicBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 180, 90, TextureOptions.DEFAULT);
		GreenTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GreenmagicBitmapTextureAtlas, activity, "sprite_green_icon.png", 0, 0, 2, 1);
		GreenmagicBitmapTextureAtlas.load();
		
		KnifeBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 270, 90, TextureOptions.DEFAULT);
		KnifeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(KnifeBitmapTextureAtlas, activity, "sprite_knife_icon.png", 0, 0, 3, 1);
		KnifeBitmapTextureAtlas.load();
		
		InvBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 100, 200, TextureOptions.BILINEAR);
		InvTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(InvBitmapTextureAtlas, activity, "inv.png", 0, 0);
		iSeedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(InvBitmapTextureAtlas, activity, "iseed.png", 0, 100);
		InvBitmapTextureAtlas.load();
		
		PinkTabBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 400, TextureOptions.DEFAULT);
		PinkTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(PinkTabBitmapTextureAtlas, activity, "sprite_pink.png", 0, 0, 1, 11);
		PinkTabBitmapTextureAtlas.load();
		
		GreenTabBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),  200, 400, TextureOptions.DEFAULT);
		GreenTabTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(GreenTabBitmapTextureAtlas, activity, "sprite_green.png", 0, 0, 1, 11);
		GreenTabBitmapTextureAtlas.load();
		
		VineBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 60, 20, TextureOptions.DEFAULT);
		VineTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(VineBitmapTextureAtlas, activity, "sprite_vine.png", 0, 0, 3, 1);
		VineBitmapTextureAtlas.load();
		
		SpikeVineBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 60, 20, TextureOptions.DEFAULT);
		SpikeVineTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(SpikeVineBitmapTextureAtlas, activity, "sprite_spike_vine.png", 0, 0, 3, 1);
		SpikeVineBitmapTextureAtlas.load();
				
		fBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 30, 28, TextureOptions.BILINEAR);
		fTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fBitmapTextureAtlas, activity, "flower.png", 0, 0);
		fBitmapTextureAtlas.load();
		
		SaplingBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 50, 66, TextureOptions.BILINEAR);
		SaplingTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(SaplingBitmapTextureAtlas, activity, "sapling.png", 0, 0);
		SaplingBitmapTextureAtlas.load();
		
		cTreeBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 172, 80, TextureOptions.BILINEAR);
		cTreeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(cTreeBitmapTextureAtlas, activity, "04.png", 0, 0);
		cTreeBitmapTextureAtlas.load();
		
		nBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 50, TextureOptions.BILINEAR);
		nTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(nBitmapTextureAtlas, activity, "sprite_number.png", 0, 0, 5, 1);
		nBitmapTextureAtlas.load();
		
		nBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 50, TextureOptions.BILINEAR);
		nTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(nBitmapTextureAtlas, activity, "sprite_number.png", 0, 0, 5, 1);
		nBitmapTextureAtlas.load();
		
		mOnScreenControlTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "onscreen_control_base.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "onscreen_control_knob.png", 256, 0);
		mOnScreenControlTexture.load();
		         			
        dGrassRepeatingAtlas1 = new BitmapTextureAtlas(activity.getTextureManager(), 32, 32, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        dGrassTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(dGrassRepeatingAtlas1, activity, "grass.png", 0, 0);
        dGrassRepeatingAtlas1.load();
        
        backBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 128, 128, TextureOptions.BILINEAR);
        backTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backBitmapTextureAtlas, activity, "back.png", 0, 0);
		backBitmapTextureAtlas.load();
		  
		WarpBitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 128, TextureOptions.DEFAULT);
		WarpTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(WarpBitmapTextureAtlas, activity, "warp.png", 0, 0, 2, 1);
		WarpBitmapTextureAtlas.load();
	}
	
	private void loadGameFonts()
	{
		
	}
	
	private void loadGameAudio()
	{
		
	}
	
	public void unloadGameTextures()
	{
		mBitmapTextureAtlas.unload();  
		
		dBitmapTextureAtlas.unload();
		
		stumpBitmapTextureAtlas.unload();
		
		seedBitmapTextureAtlas.unload();
		
		soulBitmapTextureAtlas.unload();
		
		PinkmagicBitmapTextureAtlas.unload();
		
		GreenmagicBitmapTextureAtlas.unload();
		
		KnifeBitmapTextureAtlas.unload();
		
		InvBitmapTextureAtlas.unload();
		
		PinkTabBitmapTextureAtlas.unload();
		
		GreenTabBitmapTextureAtlas.unload();
		
		VineBitmapTextureAtlas.unload();
		
		SpikeVineBitmapTextureAtlas.unload();
				
		fBitmapTextureAtlas.unload();
		
		SaplingBitmapTextureAtlas.unload();
		
		cTreeBitmapTextureAtlas.unload();
		
		nBitmapTextureAtlas.unload();
		
		nBitmapTextureAtlas.unload();
		
		mOnScreenControlTexture.unload();
		dGrassRepeatingAtlas1.unload();
        backBitmapTextureAtlas.unload();
        WarpBitmapTextureAtlas.unload();
	}
	
	public void loadSplashScreen()
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
        splashTextureAtlas.load();	
	}
	
	public void unloadSplashScreen()
	{
		splashTextureAtlas.unload();
		splash_region = null;
	}
	
	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}
	
	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
	}
	public void loadCreditTextures()
	{
		creditTextureAtlas.load();
	}
	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br><br>
	 * We use this method at beginning of game loading, to prepare Resources Manager properly,
	 * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, MainActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	//---------------------------------------------
	// GETTERS AND SETTERS
	//---------------------------------------------
	
	public static ResourcesManager getInstance()
	{
		return INSTANCE;
	}
}