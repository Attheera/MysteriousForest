package com.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.TexturedPolygon;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.extension.physics.box2d.util.triangulation.EarClippingTriangulator;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.game.SceneManager.SceneType;
public class GameScene3 extends BaseScene implements IOnSceneTouchListener
{
	protected static final int CAMERA_WIDTH = 1280;
	protected static final int CAMERA_HEIGHT = 752;  
	
	private HUD gameHUD;
	private PhysicsWorld physicsWorld;
		
	//***************************************************************************************
	private AnimatedSprite player;
	private Body pBody;
    private Vector2 velocity;
    private int v=0, maxv=10;
    private boolean isAnimate = false;
    private int direction = 0;
    
    private Rectangle gRec;
	private AnimatedSprite[] devil;
	private Body[] dBody;
	private Rectangle[] devilRec;
	private Vector2[] devilPos;
	
    private AnimatedSprite[] stump;
    private Body[] stumpBody;
    private Rectangle[] stumpRec;
    private Vector2[] stumpPos;
    private Sprite[] Sapling;
    
    private AnimatedSprite[] seed;
    private Body[] seedBody;    
    private Rectangle[] seedRec;
    private Vector2[] seedPos;
    
    private TMXTiledMap mTMXTiledMap;
    
    private int state = 0;
    private ArrayList<Vector2> myCoordinates = new ArrayList<Vector2>();
	
    private boolean isCreate = false, isCut = false, isRope = false;
	private TMXLayer tmxLayer;
	
	private boolean isDebug = true;
		
	private boolean isSeed = false;
	private Sprite iseed, Vine;
	private AnimatedSprite pink,green,knife,number,pinktab,greentab;
	private int sumgreen = 0;
	private int maxTree = 4, countTree = 0;
	private boolean ismoving;
	private Body PolygonBody;
	private int countPink = 0;

	private boolean isDrawFlower = false;
	private boolean[] isDrawFinish;
	private int maxFlower;
	private Vector2[] FlowerPos;
	private Sprite[] FlowerSprite;
	private Body[] FlowerBody;
	private Rectangle[] FlowerRec;
	private int curFlower = 1;
	private int[] VineLong;
	private boolean onVine = false;
	private Body[][] VineBody;
	private Rectangle[][] VineRec;
	private AnimatedSprite[][] VineSprite;
	private Joint[][] VineJoint;
		
	private TexturedPolygon myRepeatingSpriteShape;
	
	private Vector2 start = new Vector2(0,0);
	private Vector2 end = new Vector2(0,0);
	
	private AnimatedSprite warp;
	private Rectangle warpRec;
	@Override  
	public void createScene()
	{
		//TODO
		declare();
		createBackground();
		
		createPhysics();
		initMap();
		
		initDevil();
		initStump();
		initSeed();
		initPlayer(52,200);
		CreatFlower();
		initOnScreenControls();
		
		DrawBox();
		createInterface();
		
		setOnSceneTouchListener(this);
		//attachChild(new DebugRenderer(physicsWorld, vbom));
	}

	@Override
	public void onBackKeyPressed()
	{
		SceneManager.getInstance().loadMenuScene(engine);
	}
	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_GAME3;
	}
	@Override
	public void disposeScene()
	{
		camera.setHUD(null);
		camera.setChaseEntity(null); 
		camera.setCenter(640, 400);
	}
	
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
	{
		if (pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove() || pSceneTouchEvent.isActionUp()) {
		if(physicsWorld != null) {
        	float oldX=0,oldY=0;
        	if (isCreate == true && countPink == 0) {
				if (pSceneTouchEvent.isActionDown()) {
					if (state == 0) {
						state = 1;
						myCoordinates.add(new Vector2(pSceneTouchEvent.getX(),
								pSceneTouchEvent.getY()));
						return true;
					}
				}
				else if (pSceneTouchEvent.isActionMove() && 
						((pSceneTouchEvent.getX() > oldX+3 || pSceneTouchEvent.getX() < oldX-3) && 
						(pSceneTouchEvent.getY() > oldY+3 || pSceneTouchEvent.getY() < oldY-3))) {
					if (state == 1) {						
						myCoordinates.add(new Vector2(pSceneTouchEvent.getX(),pSceneTouchEvent.getY()));
						calculateRange();
						oldX = pSceneTouchEvent.getX();
			        	oldY = pSceneTouchEvent.getY();
						return true;
					}
				}
				else if (pSceneTouchEvent.isActionUp()) {
					if (state == 1 && myCoordinates.size() > 2) {
						state = 0;
						CalculatePoint(myCoordinates);
						myCoordinates.clear();
						return true;
					} else {
						myCoordinates.clear();
					}
				}
			} 
        	if (isRope == true && isDrawFlower == true) {
	        	if (pSceneTouchEvent.isActionMove()) {
	        		//start = new Vector2(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
	        		
	        		for(int i=0; i < maxFlower;i++){
	        			if (   pSceneTouchEvent.getX() > FlowerRec[i].getX()
							&& pSceneTouchEvent.getX() < FlowerRec[i].getX()+ FlowerRec[i].getWidth()
							&& pSceneTouchEvent.getY() > FlowerRec[i].getY()
							&& pSceneTouchEvent.getY() < FlowerRec[i].getY()+ FlowerRec[i].getHeight()) {
	        				curFlower = i;
	        			}
	        		}
	        		calculateRangeG(FlowerRec[curFlower].getY(),pSceneTouchEvent);
					return true;
				} 
	        	if (pSceneTouchEvent.isActionUp()  && isDrawFinish[curFlower] == false) {
	        		end = new Vector2(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
	        		sumgreen += (int)Math.abs(start.y-end.y);
	        		
	        		CreateVine(start,end);		
	        		
					return true;
				}            
			}
		}
		}
		return false;
	}
	private void calculateRangeG(float y1, TouchEvent pSceneTouchEvent){
		int sum = 0;
		sum = (int)Math.abs(y1-pSceneTouchEvent.getY());
		if((sum)/1000 > ResourcesManager.getInstance().powergreen){
			isDrawFinish[curFlower]=true;
			
			end = new Vector2(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
			
			sumgreen += (int)Math.abs(start.y-end.y);
			
			CreateVine(start,end);
			ResourcesManager.getInstance().powergreen = 0;
			greentab.stopAnimation(ResourcesManager.getInstance().powergreen);
		}else if((sum+sumgreen)%1000 >= 0 && (sum+sumgreen)%1000 <= 40 && ResourcesManager.getInstance().powergreen > 0)
		{
	    	ResourcesManager.getInstance().powergreen--;
	    	greentab.stopAnimation(ResourcesManager.getInstance().powergreen);
		}
	}
	private void calculateRange(){
		double sum = 0,s = 0;
		for(int i=1; i<myCoordinates.size();i++){
			sum += Math.sqrt(
					Math.pow((myCoordinates.get(i-1).x- myCoordinates.get(i).x), 2) +
					Math.pow((myCoordinates.get(i-1).y- myCoordinates.get(i).y), 2));
		}
		s = sum;
		if((int)(sum/100) > ResourcesManager.getInstance().powerpink){
			ResourcesManager.getInstance().powerpink = 0;
			pinktab.stopAnimation(ResourcesManager.getInstance().powerpink);
			state = 0;
			CalculatePoint(myCoordinates);
			myCoordinates.clear();
		}else if((int)(s)%100 >= 0 && (int)(s)%100 <= 5)
		{
			ResourcesManager.getInstance().powerpink--;
			pinktab.stopAnimation(ResourcesManager.getInstance().powerpink);
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void declare(){
		devil = new AnimatedSprite[4];
		dBody = new Body[4];
		devilRec = new Rectangle[4];
		devilPos = new Vector2[]{ new Vector2(680,950), new Vector2(880,950), new Vector2(1080,950), new Vector2(1200,1200) };
		
	    stump = new AnimatedSprite[4];
	    stumpBody = new Body[4];
	    stumpRec = new Rectangle[4];
	    stumpPos = new Vector2[]{ new Vector2(700,1150), new Vector2(900,1150), new Vector2(1100,1150), new Vector2(1100,1400) };
	    Sapling = new Sprite[4];
	    
	    seed = new AnimatedSprite[4];
	    seedBody = new Body[4];
	    seedRec = new Rectangle[4];
	    seedPos = new Vector2[]{ new Vector2(405,925), new Vector2(430,1315), new Vector2(755,800), new Vector2(1075,870) };
	   
	    
		
		maxFlower = 20;
		isDrawFinish = new boolean[maxFlower];
		for(int i = 0 ; i < maxFlower;i++){
			isDrawFinish[i] = false;
		}
		FlowerPos = new Vector2[]{ new Vector2(183,653), new Vector2(245,527), new Vector2(305,662), new Vector2(380,770), new Vector2(455,542), 
				new Vector2(305,384), new Vector2(410,314), new Vector2(474,395), new Vector2(557,494), new Vector2(616,704),
				new Vector2(685,371), new Vector2(746,290), new Vector2(713,533), new Vector2(820,338), new Vector2(881,647),
				new Vector2(983,437), new Vector2(1052,539), new Vector2(941,758), new Vector2(1073,683), new Vector2(1148,752)};
	    FlowerSprite = new Sprite[maxFlower];
		FlowerBody = new Body[maxFlower];
		FlowerRec = new Rectangle[maxFlower];
		
		VineLong = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0};
		
		VineSprite = new AnimatedSprite[maxFlower][40];
		VineBody = new Body[maxFlower][40];
		VineRec = new Rectangle[maxFlower][40];
		VineJoint = new Joint[maxFlower][40];
	}
	private void initMap(){
		try {			
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), engine.getTextureManager(), TextureOptions.NEAREST, vbom);
			mTMXTiledMap = tmxLoader.loadFromAsset("tmx/map33.tmx"); 
			Log.i("maploading","map loaded");
		} catch (final TMXLoadException e) {
			Debug.e(e); 
			Log.i("maploading","map failed");
		}
		
		for (int i = 0; i < this.mTMXTiledMap.getTMXLayers().size(); i++) {
			TMXLayer layer = this.mTMXTiledMap.getTMXLayers().get(i);
			if (!layer.getTMXLayerProperties().containsTMXProperty("ground","true"))
				attachChild(layer); 
		}
		
		createUnwalkableObjects(mTMXTiledMap);
		
		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
		
		camera.setBounds(0, 0, tmxLayer.getWidth(), tmxLayer.getHeight());
		camera.setBoundsEnabled(true);

	}
	private void createUnwalkableObjects(TMXTiledMap map) {
		for (final TMXObjectGroup group : this.mTMXTiledMap
				.getTMXObjectGroups()) {
			for (final TMXObject object : group.getTMXObjects()) {
				final Rectangle rect = new Rectangle(object.getX(),
						object.getY(), object.getWidth(), object.getHeight(),
						vbom);
				final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
				PhysicsFactory.createBoxBody(physicsWorld, rect,BodyType.StaticBody, boxFixtureDef);
				rect.setVisible(false);
				attachChild(rect);
			}
		}
	}
	private void initPlayer(final float pX, final float pY) {
		player = new AnimatedSprite(pX, pY, ResourcesManager.getInstance().mPlayerTextureRegion, vbom);
		
		player.stopAnimation(5);
		camera.setChaseEntity(player); // Char camera
		FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 1f);
		pBody = PhysicsFactory.createBoxBody(physicsWorld, player.getX(), player.getY(), 28, 86, BodyType.DynamicBody, boxFixtureDef);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, pBody, true, true));
		
		attachChild(this.player);
	} 
	private void initDevil() {
		for(int i = 0 ; i < devil.length; i++){
			devil[i] = new AnimatedSprite(devilPos[i].x, devilPos[i].y, ResourcesManager.getInstance().dDevilTextureRegion, vbom);
		
			devil[i].animate(50);
		
			devilRec[i] = new Rectangle(devil[i].getX() + 20, devil[i].getY(), 40, 140, vbom);
		
			//devilRec[i].setVisible(true);
			//attachChild(devilRec[i]);  
			attachChild(devil[i]);
		}
	} 
	private void initStump() {
		for(int i = 0 ; i < stump.length; i++){
			stump[i] = new AnimatedSprite(stumpPos[i].x, stumpPos[i].y, ResourcesManager.getInstance().StumpTextureRegion, vbom);
			Sapling[i] = new Sprite(stumpPos[i].x-20, stumpPos[i].y-47, ResourcesManager.getInstance().SaplingTextureRegion, vbom);
			
			stumpBody[i] = PhysicsFactory.createBoxBody(physicsWorld, this.stump[i].getX(), this.stump[i].getY(), 80, 15, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0, 0, 1f));
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(this.stump[i], this.stumpBody[i], true, true));
			
			stumpRec[i] = new Rectangle(stump[i].getX() -45, stump[i].getY()-25, 90, 40, vbom);
		
			//stumpRec[i].setVisible(true);
			//attachChild(stumpRec[i]); 
			
			attachChild(stump[i]);
			//attachChild(Sapling[i]);
		}
	} 
	private void initSeed() {
		for(int i = 0 ; i < seed.length; i++){
			seed[i] = new AnimatedSprite(seedPos[i].x, seedPos[i].y, ResourcesManager.getInstance().SeedTextureRegion, vbom);
		
			seed[i].animate(50);
		
			seedRec[i] = new Rectangle(seed[i].getX() + 5, seed[i].getY(), 20, 30, vbom);
		
			//seedRec[i].setVisible(true);
			//attachChild(seedRec[i]);  
			attachChild(seed[i]);
		}
	} 
	 
	private void initOnScreenControls() {
		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(0, 752 - ResourcesManager.getInstance().mOnScreenControlBaseTextureRegion.getHeight(), camera, ResourcesManager.getInstance().mOnScreenControlBaseTextureRegion, ResourcesManager.getInstance().mOnScreenControlKnobTextureRegion, 0.1f, vbom, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange
			(final BaseOnScreenControl pBaseOnScreenControl, 
					float pValueX, float pValueY) {
				
				float oldVx = 0, oldVy = 0;
				if(oldVx == pValueY && oldVx == pValueX){
					pValueX = 0.0f;
					pValueY = 0.0f;
				}
				oldVx = pValueX;	
				oldVy = pValueY;
				
				if (pValueX < 0) {
					direction = -1;
				} else if (pValueX > 0) {
					direction = 1;
				}
				
				if(onVine == false){
					if (pValueY < 0 && pValueX > -0.33 && pValueX < 0.33) {
						velocity = Vector2Pool.obtain(pValueX * 5, pValueY * 5);
						if (direction == -1) {
							player.setCurrentTileIndex(2);
						} else if (direction == 1) {
							player.setCurrentTileIndex(7);
						}
					}
					else if (pValueY < 0.33 && pValueY > -0.33 && pValueX < 0) {
						velocity = Vector2Pool.obtain(-5, 0);direction = -1;
						if(!isAnimate){
							
							player.animate(new long[] { 200, 200, 200, 200 }, 1, 4, true);
							isAnimate = true;
						}
					}
					else if (pValueY < 0.33 && pValueY > -0.33 && pValueX > 0) {
						velocity = Vector2Pool.obtain(5, 0);direction = 1;
						if(!isAnimate){
							
							player.animate(new long[] { 200, 200, 200, 200 }, 6, 9, true);
							isAnimate = true;
						}
					}
					else if (pValueY < -0.5 && pValueX > 0.5) {
						velocity = Vector2Pool.obtain(5, -5);
						direction = 1;
						player.setCurrentTileIndex(7);
					}
					else if (pValueY < -0.5 && pValueX < -0.5) {
						velocity = Vector2Pool.obtain(-5, -5);
						direction = -1;
						player.setCurrentTileIndex(2);
					}
					else{
						
						velocity = Vector2Pool.obtain(0, 5);
						
						isAnimate = false;
						player.stopAnimation();
						
						if (direction == -1) {
							player.setCurrentTileIndex(0);
						} else if (direction == 1) {
							player.setCurrentTileIndex(5);
						}
					}	
				}
				else if(onVine == true){
					
					if (pValueY > 0.5) {
						velocity = Vector2Pool.obtain(0, 2);
						if (pValueX > 0.5) {
							velocity = Vector2Pool.obtain(2, 2);
							}
						if (pValueX < -0.5) {
							velocity = Vector2Pool.obtain(-2, 2);
						}
						if(!isAnimate){
						player.animate(new long[] { 200, 200 }, 10, 11, true);
						isAnimate = true;
						}
					}
					else if (pValueY < -0.5) {
						velocity = Vector2Pool.obtain(0, -2);
						if (pValueX > 0.5) {
							velocity = Vector2Pool.obtain(2, -2);
							}
						if (pValueX < -0.5) {
							velocity = Vector2Pool.obtain(-2, -2);
						}
						if(!isAnimate){
						player.animate(new long[] { 200, 200 }, 10, 11, true);
						isAnimate = true;
						}
					}
					else if (pValueX > 0.5) {
						velocity = Vector2Pool.obtain(2, 0);direction = 1;
						if(!isAnimate){
							
						player.animate(new long[] { 200, 200 }, 10, 11, true);
						isAnimate = true;
						}
					}
					else if (pValueX < -0.5) {
						velocity = Vector2Pool.obtain(-2, 0);direction = -1;
						if(!isAnimate){
							
						player.animate(new long[] { 200, 200 }, 10, 11, true);
						isAnimate = true;
						}
					}
					/*else {
						velocity = Vector2Pool.obtain(0.0f, 0.0f);
						isAnimate = false;
						player.stopAnimation();
						player.setCurrentTileIndex(10);
					}*/
				}
					pBody.setLinearVelocity(velocity);
					Vector2Pool.recycle(velocity);
					
			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
			}
		});
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.refreshControlKnobPosition();

		setChildScene(analogOnScreenControl);

	}
	private void DrawBox(){		 
        final Rectangle ground = new Rectangle(0, tmxLayer.getHeight() - 2, tmxLayer.getWidth(), 2, vbom);
        gRec = ground;
        final Rectangle roof = new Rectangle(0, -30, tmxLayer.getWidth(), -28, vbom);
        final Rectangle left = new Rectangle(0, 0, 2, tmxLayer.getHeight(), vbom);
        final Rectangle right = new Rectangle(tmxLayer.getWidth() - 2, 0, 2, tmxLayer.getHeight(), vbom);

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsWorld, left, BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsWorld, right, BodyType.StaticBody, wallFixtureDef);
	}
	
	private void createInterface()
	{
		//TODO
		gameHUD = new HUD();
		
		
		pink = new AnimatedSprite(300, 600, ResourcesManager.getInstance().PinkTextureRegion, vbom);
	    final Rectangle pinkRec = new Rectangle(300, 600, 90, 90, vbom)
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
				if (touchEvent.isActionDown()) {
					//ResourcesManager.getInstance().effect.play();
					isCreate = !isCreate;
					if (isCreate == true) {
						pink.setCurrentTileIndex(1);
												
						isRope = false;
						green.setCurrentTileIndex(0);
					} else if (isCreate == false){
						pink.setCurrentTileIndex(0);
						if(PolygonBody != null) { 
							if(PolygonBody.isActive()){
								physicsWorld.destroyBody(PolygonBody);
								GameScene3.this.detachChild(myRepeatingSpriteShape);
							}
						}
						ResourcesManager.getInstance().powerpink = ResourcesManager.getInstance().maxpink;
						pinktab.stopAnimation(ResourcesManager.getInstance().powerpink);
						countPink = 0;
					}
				}
	                                          
	            return true;
	        };
	    };
	    pinktab = new AnimatedSprite(360, 625, ResourcesManager.getInstance().PinkTabTextureRegion, vbom);
	    gameHUD.attachChild(pinktab);
		pinktab.stopAnimation(ResourcesManager.getInstance().powerpink);
	    
	    pinkRec.setVisible(false);	
	    gameHUD.registerTouchArea(pinkRec);
	    gameHUD.attachChild(pinkRec);
	    gameHUD.attachChild(pink);
	    
	    green = new AnimatedSprite(600, 600, ResourcesManager.getInstance().GreenTextureRegion, vbom);
	    final Rectangle greenRec = new Rectangle(600, 600, 90, 90, vbom)
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
				if (touchEvent.isActionDown()) {
					//ResourcesManager.getInstance().effect.play();
					isRope = !isRope;
					if (isRope == true) {
						green.setCurrentTileIndex(1);
						
						isCreate = false;
						
						pink.setCurrentTileIndex(0);
					} else {
						green.setCurrentTileIndex(0);
						/*if (isDrawFinish == true) {
							for (int i = 0; i < VineBody.length; i++) {
								if (VineBody[i].isActive()) {
									physicsWorld.destroyBody(VineBody[i]);
								}
								detachChild(VineSprite[i]);
								isDrawFinish = false;
							}
						}*/
					}
				}
	                                          
	            return true;
	        };
	    };
	    greentab = new AnimatedSprite(660, 625, ResourcesManager.getInstance().GreenTabTextureRegion, vbom);
	    gameHUD.attachChild(greentab);
	    greentab.stopAnimation(ResourcesManager.getInstance().powergreen);
	    
	    greenRec.setVisible(false);	
	    gameHUD.registerTouchArea(greenRec);
	    gameHUD.attachChild(greenRec);
	    gameHUD.attachChild(green);
			    
	    
	    final Sprite inv = new Sprite(1100, 600, ResourcesManager.getInstance().InvTextureRegion, vbom);
	    final Sprite cTree = new Sprite(1100, 30, ResourcesManager.getInstance().cTreeTextureRegion, vbom);
	    
	    number = new AnimatedSprite(1160, 50, ResourcesManager.getInstance().nTextureRegion, vbom);
		
	    this.iseed = new Sprite(1100, 600, ResourcesManager.getInstance().iSeedTextureRegion, vbom);

	    gameHUD.attachChild(inv);
		gameHUD.attachChild(cTree);
		gameHUD.attachChild(number);
		
		
	    
		camera.setHUD(gameHUD);
	}
	
	private void CalculatePoint(ArrayList<Vector2> myCoordinates) {
		ArrayList<Vector2> point = new ArrayList<Vector2>();
		ArrayList<Vector2> point2 = new ArrayList<Vector2>();
		//first element
		point.add(myCoordinates.get(0));
		
		for (int i=0; i<myCoordinates.size()-1; i++) {
			Vector2 p1 = myCoordinates.get(i);
			Vector2 p2 = myCoordinates.get(i+1);

			Vector2 Q = dis(p1, p2, "left");
			Vector2 R = dis(p1, p2, "right");
			point.add(Q);
			point2.add(R);
		}
		//last element
		point.add(myCoordinates.get(myCoordinates.size()-1));
		
		Collections.reverse(point2);
		point.addAll(point2);
		CreatePolygon(point);
	}
	private void CreatePolygon(ArrayList<Vector2> myCoordinates) {			
		Vector2[] myV2 = new Vector2[myCoordinates.size()];
		for (int i = 0; i < myCoordinates.size(); i++) {
			myV2[i] = new Vector2(myCoordinates.get(i).x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					myCoordinates.get(i).y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		}
		float[] vertexX1 = new float[myV2.length];
		float[] vertexY1 = new float[myV2.length];
		for (int i = 0; i < myV2.length; i++) {
			vertexX1[i] = myV2[i].x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			vertexY1[i] = myV2[i].y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		}
		myRepeatingSpriteShape = new TexturedPolygon(0, 0,vertexX1, vertexY1, ResourcesManager.getInstance().dGrassTextureRegion, vbom);
		attachChild(myRepeatingSpriteShape);

		final float myConversion = PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		Vector2[] myVector2 = new Vector2[myCoordinates.size()];
		for (int i = 0; i < myCoordinates.size(); ++i) {
			myVector2[i] = new Vector2(myCoordinates.get(i).x / myConversion,
					myCoordinates.get(i).y / myConversion);
		}
		EarClippingTriangulator myEar = new EarClippingTriangulator();
		List<Vector2> myList = myEar.computeTriangles(Arrays.asList(myVector2));
		final FixtureDef myFixtureDef = PhysicsFactory.createFixtureDef(1f, 1f,1f);
		PolygonBody = PhysicsFactory.createTrianglulatedBody(physicsWorld,myRepeatingSpriteShape, myList, BodyType.DynamicBody,myFixtureDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(myRepeatingSpriteShape, PolygonBody));
		countPink++;
	}
	private Vector2 dis(Vector2 p1, Vector2 p2, String s){
		float distanceX, distanceY;
		float x1,x2,y1,y2,b;
		x1 = p1.x;
		x2 = p2.x;
		y1 = p1.y;
		y2 = p2.y;
		
		Vector2 pl = new Vector2();
		Vector2 pr = new Vector2();
		
		distanceX=Math.abs(x1+x2)/2;
		distanceY=Math.abs(y1+y2)/2;
		
		if(x1>x2){
			if(y1>y2){
				pl = new Vector2(distanceX+5,distanceY-5);
				pr = new Vector2(distanceX-5,distanceY+5);
			}
			else if(y1<y2){
				pl = new Vector2(distanceX-5,distanceY-5);
				pr = new Vector2(distanceX+5,distanceY+5);
			}
		}
		else if(x1<x2){
			if(y1>y2){
				pl = new Vector2(distanceX+5,distanceY+5);
				pr = new Vector2(distanceX-5,distanceY-5);
			}
			else if(y1<y2){
				pl = new Vector2(distanceX-5,distanceY+5);
				pr = new Vector2(distanceX+5,distanceY-5);
			}
		}
		if(s=="left")
		{
			return pl;
		}
		else if(s=="right")
		{
			return pr;
		}
		return null;
	}
	
	private void createBackground()
	{
		setBackground(new Background(Color.BLUE));
	}
	private void createPhysics()
	{
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, SensorManager.GRAVITY_EARTH), false); 
		registerUpdateHandler(physicsWorld);
		updateGame();
	}
	private void updateGame(){
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				/*if(sound.isPlaying() == false) {
					sound.play();
				}*/
				Rectangle pRec = new Rectangle(player.getX() + 16, GameScene3.this.player.getY(), 30, 95, vbom);
				if(isAnimate==false){
					if (direction == -1) {
						player.setCurrentTileIndex(0);
					} else if (direction == 1) {
						player.setCurrentTileIndex(5);
					}
				}
				for (int i = 0; i < 4; i++) {
					if (pRec.collidesWith(seedRec[i])
							&& GameScene3.this.isSeed == false) {
						seed[i].setVisible(false);
						isSeed = true;
						gameHUD.attachChild(iseed);
						seedRec[i] = new Rectangle(0, 0, 0, 0, vbom);
						// GameScene3.this.seedRec.dispose();
					} else if (pRec.collidesWith(devilRec[i])) {
						detachChild(player);
						physicsWorld.destroyBody(GameScene3.this.pBody);
						initPlayer(100, 1000);
					} else if (pRec.collidesWith(stumpRec[i])
							&& GameScene3.this.isSeed == true) {
						gameHUD.detachChild(GameScene3.this.iseed);
						if (stumpBody[i].isActive()) {
							physicsWorld.destroyBody(stumpBody[i]);
						}
						devil[i].setVisible(false);
						stump[i].setVisible(false);
						isSeed = false;
						devilRec[i] = new Rectangle(0, 0, 0, 0,vbom);
						stumpRec[i] = new Rectangle(0, 0, 0, 0,vbom);
						countTree++;
						if(countTree == maxTree){
							creatWarp();
						}
						number.setCurrentTileIndex(countTree);
						GameScene3.this.attachChild(Sapling[i]);
						GameScene3.this.detachChild(player);
						GameScene3.this.attachChild(player);
						// GameScene3.this.stumpRec.dispose();

					}
				}
				if (pRec.collidesWith(gRec)) {
					detachChild(player);
					physicsWorld.destroyBody(GameScene3.this.pBody);
					initPlayer(100, 1000);
				}
				if (pRec.collidesWith(warpRec)) {
					SceneManager.getInstance().loadMenuScene(engine);
				}
				if(onVine == true && isAnimate == false){
					//player.setCurrentTileIndex(10);
				}
				for( int i = 0 ; i < maxFlower;i++){
					for( int j = 0 ; j < VineLong[i];j++){
						
							VineRec[i][j] = new Rectangle(VineSprite[i][j].getX(), VineSprite[i][j].getY(), 20, 20, vbom);
							//attachChild(VineRec[i][j]);
							if (pRec.collidesWith(VineRec[i][j])) {
								if(VineBody[i][j].isActive()){
									for( int k = 0 ; k < VineLong[i];k++){
										VineBody[i][k].setActive(false);
									}
								}
								velocity = Vector2Pool.obtain(0, 0);
								onVine = true;
								//physicsWorld.destroyBody(VineBody[i][j]);
								//detachChild(VineSprite[i][j]);
								break;
							}
							else{
								onVine = false;
							}
						 /*else if(VineBody[i][j].isActive() == false && pRec.collidesWith(VineRec[i][j]) == false){
							for( int k = 0 ; k < VineLong[i];k++){
								VineBody[i][k].setActive(true);
							}
						}*/
					}
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	//TODO
	private void CreatFlower() {
		for(int i=0; i < maxFlower ; i++){
			FlowerSprite[i] = new Sprite(FlowerPos[i].x, FlowerPos[i].y,ResourcesManager.getInstance().fTextureRegion, vbom);
			FlowerBody[i] = PhysicsFactory.createBoxBody(physicsWorld, FlowerSprite[i].getX(), FlowerSprite[i].getY(), 30, 30, BodyType.StaticBody, PhysicsFactory.createFixtureDef(0.5f, 0.2f, 0.5f));
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(FlowerSprite[i], FlowerBody[i], true, true));
			attachChild(FlowerSprite[i]);
			
			FlowerRec[i] = new Rectangle(FlowerSprite[i].getX()-15, FlowerSprite[i].getY()-15, 30, 30, vbom) {
				public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y) {
					if (isRope == true) {
						if (touchEvent.isActionDown()) {
							start = new Vector2(touchEvent.getX(),touchEvent.getY());
							
							for(int i=0; i < maxFlower;i++){
			        			if (   touchEvent.getX() > FlowerRec[i].getX()
									&& touchEvent.getX() < FlowerRec[i].getX()+ FlowerRec[i].getWidth()
									&& touchEvent.getY() > FlowerRec[i].getY()
									&& touchEvent.getY() < FlowerRec[i].getY()+ FlowerRec[i].getHeight()) {
			        				curFlower = i;
			        			}
			        		}
							
							isDrawFlower = true;
							return true;
						}
						if (touchEvent.isActionUp()) {
							isDrawFlower = false;
							return true;
						}
					}
					return false;
				};
			};
		
			FlowerRec[i].setVisible(false);
			registerTouchArea(FlowerRec[i]);
			attachChild(FlowerRec[i]);
		}
	}

	private void CreateVine(Vector2 start, Vector2 end) {
		double distance = end.y - start.y;// Math.sqrt(Math.pow(start.x-end.x,
											// 2)+Math.pow(start.y-end.y, 2));
		double mod = (distance % 18);
		int sum = (int) (distance / 18);
		/*if (mod > 9) {
			sum++;
		}*/
		if(sum>39){
			sum = 39 ;
		}
		VineLong[curFlower] = sum;
		for(int i=0;i<sum;i++){
			VineSprite[curFlower][i] = new AnimatedSprite(FlowerRec[curFlower].getX(), FlowerRec[curFlower].getY() + i*20, ResourcesManager.getInstance().VineTextureRegion, vbom);
			VineBody[curFlower][i] = PhysicsFactory.createBoxBody(physicsWorld, FlowerRec[curFlower].getX(), FlowerRec[curFlower].getY() + i*20, 20, 20,BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0.5f, 0.2f, 0.5f));
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(VineSprite[curFlower][i], VineBody[curFlower][i], true, true));
			attachChild(VineSprite[curFlower][i]);
			if (i%2 == 0) {
				VineSprite[curFlower][i].setCurrentTileIndex(1);
			}
			if (i == sum -1 ) {
				VineSprite[curFlower][i].setCurrentTileIndex(2);
			}
		}
		detachChild(FlowerSprite[curFlower]);
		attachChild(FlowerSprite[curFlower]);
		detachChild(player);
		attachChild(player);
		for(int i=0;i<sum;i++){
			if (i == 0) {
				ConnectVineJoint(FlowerBody[curFlower], VineBody[curFlower][i], 9, curFlower, i);
			} else {
				ConnectVineJoint(VineBody[curFlower][i-1], VineBody[curFlower][i], 9, curFlower, i);
			}
		}
		isDrawFlower = false;
		isDrawFinish[curFlower] = true;
	}

	private void ConnectVineJoint(Body b1, Body b2, float distance, int index, int i) {
		RevoluteJointDef chainLinkDef = new RevoluteJointDef();
		chainLinkDef.collideConnected = false;
		chainLinkDef.initialize(b1, b2, b1.getWorldCenter());
		chainLinkDef.localAnchorA.set(0.0f, distance
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		chainLinkDef.localAnchorB.set(0.0f, -1 * distance
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		chainLinkDef.enableMotor = false;
		chainLinkDef.motorSpeed = 0;
		chainLinkDef.enableLimit = false;
		chainLinkDef.lowerAngle = MathUtils.degToRad(3);
		chainLinkDef.upperAngle = MathUtils.degToRad(180);
		 
		VineJoint[index][i] = physicsWorld.createJoint(chainLinkDef);
	}
	private void creatWarp(){
		warp = new AnimatedSprite(610, 100, ResourcesManager.getInstance().WarpTextureRegion, vbom);
		warp.animate(50);
	    warpRec = new Rectangle(610, 100, 128, 128, vbom);
	    attachChild(warp);
	    
	}
}