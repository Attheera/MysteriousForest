package org.andengine.extension.tmx;

import java.util.LinkedList;

import org.andengine.extension.tmx.util.constants.TMXConstants;
import org.andengine.util.SAXUtils;
import org.xml.sax.Attributes;

import android.util.Pair;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 11:21:01 - 29.07.2010
 */
public class TMXObject implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mName;
	private final String mType;
	private final int mX;
	private final int mY;
	private final int mWidth;
	private final int mHeight;
	private final TMXProperties<TMXObjectProperty> mTMXObjectProperties = new TMXProperties<TMXObjectProperty>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public TMXObject(final Attributes pAttributes) {
		this.mName = pAttributes.getValue("", TMXConstants.TAG_OBJECT_ATTRIBUTE_NAME);
		this.mType = pAttributes.getValue("", TMXConstants.TAG_OBJECT_ATTRIBUTE_TYPE);
		this.mX = SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_OBJECT_ATTRIBUTE_X);
		this.mY = SAXUtils.getIntAttributeOrThrow(pAttributes, TMXConstants.TAG_OBJECT_ATTRIBUTE_Y);
		this.mWidth = SAXUtils.getIntAttribute(pAttributes, TMXConstants.TAG_OBJECT_ATTRIBUTE_WIDTH, 0);
		this.mHeight = SAXUtils.getIntAttribute(pAttributes, TMXConstants.TAG_OBJECT_ATTRIBUTE_HEIGHT, 0);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getName() {
		return this.mName;
	}

	public String getType() {
		return this.mType;
	}

	public int getX() {
		return this.mX;
	}

	public int getY() {
		return this.mY;
	}

	public int getWidth() {
		return this.mWidth;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public void addTMXObjectProperty(final TMXObjectProperty pTMXObjectProperty) {
		this.mTMXObjectProperties.add(pTMXObjectProperty);
	}

	public TMXProperties<TMXObjectProperty> getTMXObjectProperties() {
		return this.mTMXObjectProperties;
	}
	
	private final LinkedList<Pair<Integer,Integer>> mTMXObjectPolyline = new LinkedList<Pair<Integer,Integer>>();

	public void setTMXObjectPolygon(final String pPolygon) {
        this.mTMXObjectPolyline.addAll(parsePoints(pPolygon));
        Pair<Integer, Integer> origin = mTMXObjectPolyline.get(0);
        this.mTMXObjectPolyline.add(new Pair<Integer,Integer>(origin.first, origin.second));
}
       
	public void setTMXObjectPolyline(final String pPolyline) {
		this.mTMXObjectPolyline.addAll(parsePoints(pPolyline));
	}

	private LinkedList<Pair<Integer, Integer>> parsePoints(final String pPoints) {
		LinkedList<Pair<Integer, Integer>> list = new LinkedList<Pair<Integer, Integer>>();
		Integer x, y;

		String[] tokens = pPoints.split(" ");
		for (int i = 0; i < tokens.length; i++) {
			x = Integer.parseInt(tokens[i].split(",")[0]);
			y = Integer.parseInt(tokens[i].split(",")[1]);
			list.add(new Pair<Integer, Integer>(x, y));
		}

		return list;
	}

	public LinkedList<Pair<Integer, Integer>> getTMXObjectPolyline() {
		return this.mTMXObjectPolyline;
	}
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
