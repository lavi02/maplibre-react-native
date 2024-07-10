package com.maplibre.rctmln.components.mapview;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.MBXMapViewManagerDelegate;
import com.facebook.react.viewmanagers.MBXMapViewManagerInterface;
import com.mapbox.maps.MapInitOptions;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.maplibre.rctmln.components.AbstractEventEmitter;
import com.maplibre.rctmln.events.constants.EventKeys;
import com.maplibre.rctmln.utils.ConvertUtils;
import com.maplibre.rctmln.utils.ExpressionParser;
import com.maplibre.rctmln.utils.GeoJSONUtils;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import javax.annotation.Nullable;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * Created by nickitaliano on 8/18/17.
 */

public class RCTMLNMapViewManager extends AbstractEventEmitter<RCTMLNMapView> implements MBXMapViewManagerInterface<RCTMLNMapView> {
    public static final String LOG_TAG = "RCTMLNMapViewManager";
    public static final String REACT_CLASS = "RCTMLNMapView";

    private Map<Integer, RCTMLNMapView> mViews;
    private final ViewManagerDelegate<RCTMLNMapView> mDelegate;

    public RCTMLNMapViewManager(ReactApplicationContext context) {
        super(context);
        mViews = new HashMap<>();
        mDelegate = new MBXMapViewManagerDelegate<RCTMLNMapView, RCTMLNMapViewManager>(this);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        return new MapShadowNode(this);
    }

    @Override
    public Class<? extends LayoutShadowNode> getShadowNodeClass() {
        return MapShadowNode.class;
    }

    @Override
    protected void onAfterUpdateTransaction(RCTMLNMapView mapView) {
        super.onAfterUpdateTransaction(mapView);

        if (mapView.getMapboxMap() == null) {
            mViews.put(mapView.getId(), mapView);
            mapView.init();
        }
    }

    @Override
    public void addView(RCTMLNMapView mapView, View childView, int childPosition) {
        mapView.addFeature(childView, childPosition);
    }

    @Override
    public int getChildCount(RCTMLNMapView mapView) {
        return mapView.getFeatureCount();
    }

    @Override
    public View getChildAt(RCTMLNMapView mapView, int index) {
        return mapView.getFeatureAt(index);
    }

    @Override
    public void removeViewAt(RCTMLNMapView mapView, int index) {
        mapView.removeFeature(index);
    }

    public Context getMapViewContext(ThemedReactContext themedReactContext) {
        Context activity = getCurrentActivity();
        return activity != null ? activity : themedReactContext;
    }

    @Override
    protected RCTMLNMapView createViewInstance(ThemedReactContext themedReactContext) {
        Context context = getMapViewContext(themedReactContext);
        return new RCTMLNMapView(context, this, null);
    }

    @Override
    public void onDropViewInstance(RCTMLNMapView mapView) {
        int reactTag = mapView.getId();

        if (mViews.containsKey(reactTag)) {
            mViews.remove(reactTag);
        }

        mapView.onDropViewInstance();
        super.onDropViewInstance(mapView);
    }

    public RCTMLNMapView getByReactTag(int reactTag) {
        return mViews.get(reactTag);
    }

    //region React Props

    @ReactProp(name="styleURL")
    public void setStyleURL(RCTMLNMapView mapView, String styleURL) {
        mapView.setReactStyleURL(styleURL);
    }

    @ReactProp(name="preferredFramesPerSecond")
    public void setPreferredFramesPerSecond(RCTMLNMapView mapView, int preferredFramesPerSecond) {
        mapView.setReactPreferredFramesPerSecond(preferredFramesPerSecond);
    }

    @ReactProp(name="localizeLabels")
    public void setLocalizeLabels(RCTMLNMapView mapView, boolean localizeLabels) {
        mapView.setLocalizeLabels(localizeLabels);
    }

    @ReactProp(name="zoomEnabled")
    public void setZoomEnabled(RCTMLNMapView mapView, boolean zoomEnabled) {
        mapView.setReactZoomEnabled(zoomEnabled);
    }

    @ReactProp(name="scrollEnabled")
    public void setScrollEnabled(RCTMLNMapView mapView, boolean scrollEnabled) {
        mapView.setReactScrollEnabled(scrollEnabled);
    }

    @ReactProp(name="pitchEnabled")
    public void setPitchEnabled(RCTMLNMapView mapView, boolean pitchEnabled) {
        mapView.setReactPitchEnabled(pitchEnabled);
    }

    @ReactProp(name="rotateEnabled")
    public void setRotateEnabled(RCTMLNMapView mapView, boolean rotateEnabled) {
        mapView.setReactRotateEnabled(rotateEnabled);
    }

    @ReactProp(name="attributionEnabled")
    public void setAttributionEnabled(RCTMLNMapView mapView, boolean attributionEnabled) {
        mapView.setReactAttributionEnabled(attributionEnabled);
    }

    @ReactProp(name="attributionPosition")
    public void setAttributionPosition(RCTMLNMapView mapView, @Nullable ReadableMap attributionPosition) {
        mapView.setReactAttributionPosition(attributionPosition);
    }

    @ReactProp(name="logoEnabled")
    public void setLogoEnabled(RCTMLNMapView mapView, boolean logoEnabled) {
        mapView.setReactLogoEnabled(logoEnabled);
    }

    @ReactProp(name="logoPosition")
    public void setLogoPosition(RCTMLNMapView mapView, ReadableMap logoPosition) {
        mapView.setReactLogoPosition(logoPosition);
    }

    @ReactProp(name="compassEnabled")
    public void setCompassEnabled(RCTMLNMapView mapView, boolean compassEnabled) {
        mapView.setReactCompassEnabled(compassEnabled);
    }

    @ReactProp(name="compassViewMargins")
    public void setCompassViewMargins(RCTMLNMapView mapView, ReadableMap compassViewMargins){
        mapView.setReactCompassViewMargins(compassViewMargins);
    }

    @ReactProp(name="compassViewPosition")
    public void setCompassViewPosition(RCTMLNMapView mapView, int compassViewPosition) {
        mapView.setReactCompassViewPosition(compassViewPosition);
    }

    @ReactProp(name="contentInset")
    public void setContentInset(RCTMLNMapView mapView, ReadableArray array) {
        mapView.setReactContentInset(array);
    }

    @ReactProp(name = "tintColor", customType = "Color")
    public void setTintColor(RCTMLNMapView mapView, @Nullable Integer tintColor) {
        mapView.setTintColor(tintColor);
    }

    //endregion

    //region Custom Events

    @Override
    public Map<String, String> customEvents() {
        return MapBuilder.<String, String>builder()
                .put(EventKeys.MAP_CLICK, "onPress")
                .put(EventKeys.MAP_LONG_CLICK,"onLongPress")
                .put(EventKeys.MAP_ONCHANGE, "onMapChange")
                .put(EventKeys.MAP_ON_LOCATION_CHANGE, "onLocationChange")
                .put(EventKeys.MAP_USER_TRACKING_MODE_CHANGE, "onUserTrackingModeChange")
                .put(EventKeys.MAP_ANDROID_CALLBACK, "onAndroidCallback")
                .build();
    }

    //endregion

    //region React Methods
    public static final int METHOD_QUERY_FEATURES_POINT = 2;
    public static final int METHOD_QUERY_FEATURES_RECT = 3;
    public static final int METHOD_VISIBLE_BOUNDS = 4;
    public static final int METHOD_GET_POINT_IN_VIEW = 5;
    public static final int METHOD_GET_COORDINATE_FROM_VIEW = 6;
    public static final int METHOD_TAKE_SNAP = 7;
    public static final int METHOD_GET_ZOOM = 8;
    public static final int METHOD_GET_CENTER = 9;
    public static final int METHOD_SET_HANDLED_MAP_EVENTS = 10;
    public static final int METHOD_SHOW_ATTRIBUTION = 11;
    public static final int METHOD_SET_SOURCE_VISIBILITY = 12;

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.<String, Integer>builder()
                .put("queryRenderedFeaturesAtPoint", METHOD_QUERY_FEATURES_POINT)
                .put("queryRenderedFeaturesInRect", METHOD_QUERY_FEATURES_RECT)
                .put("getVisibleBounds", METHOD_VISIBLE_BOUNDS)
                .put("getPointInView", METHOD_GET_POINT_IN_VIEW)
                .put("getCoordinateFromView", METHOD_GET_COORDINATE_FROM_VIEW)
                .put("takeSnap", METHOD_TAKE_SNAP)
                .put("getZoom", METHOD_GET_ZOOM)
                .put("getCenter", METHOD_GET_CENTER)
                .put( "setHandledMapChangedEvents", METHOD_SET_HANDLED_MAP_EVENTS)
                .put("showAttribution", METHOD_SHOW_ATTRIBUTION)
                .put("setSourceVisibility", METHOD_SET_SOURCE_VISIBILITY)
                .build();
    }

    //endregion

    private static final class MapShadowNode extends LayoutShadowNode {
        private RCTMLNMapViewManager mViewManager;

        public MapShadowNode(RCTMLNMapViewManager viewManager) {
            mViewManager = viewManager;
        }

        @Override
        public void dispose() {
            super.dispose();
            diposeNativeMapView();
        }

        /**
         * We need this mapview to dispose (calls into nativeMap.destroy) before ReactNative starts tearing down the views in
         * onDropViewInstance.
         */
        private void diposeNativeMapView() {
            final RCTMLNMapView mapView = mViewManager.getByReactTag(getReactTag());

            if (mapView != null)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        try
                        {
                            mapView.dispose();
                        }
                        catch (Exception ex)
                        {
                            Log.e(LOG_TAG , " disposeNativeMapView() exception destroying map view", ex);
                        }
                    }
                });
            }
        }
    }
}

