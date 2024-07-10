package com.maplibre.rctmln.components.mapview;

import com.facebook.react.uimanager.ViewManagerDelegate;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.facebook.react.viewmanagers.MBXAndroidTextureMapViewManagerDelegate;
import com.facebook.react.viewmanagers.MBXAndroidTextureMapViewManagerInterface;
import com.facebook.react.viewmanagers.MBXMapViewManagerDelegate;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by hernanmateo on 12/11/18.
 */

public class RCTMLNAndroidTextureMapViewManager extends RCTMLNMapViewManager implements MBXAndroidTextureMapViewManagerInterface<RCTMLNMapView> {
    private final ViewManagerDelegate<RCTMGLMapView> mDelegate;
    public static final String LOG_TAG = "RCTMLNAndroidTextureMapViewManager";
    public static final String REACT_CLASS = "RCTMLNAndroidTextureMapView";

    public RCTMLNAndroidTextureMapViewManager(ReactApplicationContext context) {
        super(context);
        mDelegate = new MBXAndroidTextureMapViewManagerDelegate<>(this);
    }

    @Override
    public ViewManagerDelegate<RCTMGLMapView> getDelegate() {
        return mDelegate;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected RCTMLNAndroidTextureMapView createViewInstance(ThemedReactContext themedReactContext) {
        MapboxMapOptions options = new MapboxMapOptions();
        options.textureMode(true);
        return new RCTMLNAndroidTextureMapView(themedReactContext, this, options);
    }
}
