package com.maplibre.rctmln.components.mapview;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.common.UIManagerType;
import com.maplibre.rctmln.BuildConfig;
import com.maplibre.rctmln.NativeMapViewModuleSpec;
import com.maplibre.rctmln.utils.ConvertUtils;
import com.maplibre.rctmln.utils.ExpressionParser;
import com.maplibre.rctmln.utils.extensions.ToCoordinate;
import com.maplibre.rctmln.utils.extensions.ToScreenCoordinate;

public class NativeMapViewModule extends NativeMapViewModuleSpec {
    public NativeMapViewModule(ReactApplicationContext context) {
        super(context);
    }

    private void withMapViewOnUIThread(Double viewRef, Promise promise, MapViewCallback callback) {
        if (viewRef == null) {
            return;
        }

        reactApplicationContext.runOnUiQueueThread(() -> {
            UIManagerType managerType = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED ? UIManagerType.FABRIC : UIManagerType.DEFAULT;
            Object manager = UIManagerHelper.getUIManager(reactApplicationContext, managerType);

            RCTMGLMapView view = (RCTMGLMapView) ((manager != null) ? manager.resolveView(viewRef.intValue()) : null);

            if (view != null) {
                callback.apply(view);
            } else {
                promise.reject(new Exception("cannot find map view for tag " + viewRef.intValue()));
            }
        });
    }

    private CommandResponse createCommandResponse(Promise promise) {
        return new CommandResponse() {
            @Override
            public void success(CommandResponseCallback builder) {
                WritableMap payload = new WritableNativeMap();
                builder.apply(payload);

                promise.resolve(payload);
            }

            @Override
            public void error(String message) {
                promise.reject(new Exception(message));
            }
        };
    }

    @Override
    public void takeSnap(Double viewRef, boolean writeToDisk, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> view.takeSnap(writeToDisk, createCommandResponse(promise)));
    }

    @Override
    public void queryTerrainElevation(Double viewRef, ReadableArray coordinates, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> 
            view.queryTerrainElevation(coordinates.getDouble(0), coordinates.getDouble(1), createCommandResponse(promise))
        );
    }

    @Override
    public void setSourceVisibility(Double viewRef, boolean visible, String sourceId, String sourceLayerId, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> {
            view.setSourceVisibility(visible, sourceId, sourceLayerId);
            promise.resolve(null);
        });
    }

    @Override
    public void getCenter(Double viewRef, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> view.getCenter(createCommandResponse(promise)));
    }

    @Override
    public void getCoordinateFromView(Double viewRef, ReadableArray atPoint, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> 
            view.getCoordinateFromView(ToScreenCoordinate.apply(atPoint), createCommandResponse(promise))
        );
    }

    @Override
    public void getPointInView(Double viewRef, ReadableArray atCoordinate, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> 
            view.getPointInView(ToCoordinate.apply(atCoordinate), createCommandResponse(promise))
        );
    }

    @Override
    public void getZoom(Double viewRef, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> view.getZoom(createCommandResponse(promise)));
    }

    @Override
    public void getVisibleBounds(Double viewRef, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> view.getVisibleBounds(createCommandResponse(promise)));
    }

    @Override
    public void queryRenderedFeaturesAtPoint(
        Double viewRef, ReadableArray atPoint, ReadableArray withFilter, ReadableArray withLayerIDs, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> {
            java.util.List<String> layerIds = ConvertUtils.toStringList(withLayerIDs);
            view.queryRenderedFeaturesAtPoint(
                ConvertUtils.toPointF(atPoint),
                ExpressionParser.from(withFilter),
                layerIds.isEmpty() ? null : layerIds,
                createCommandResponse(promise)
            );
        });
    }

    @Override
    public void queryRenderedFeaturesInRect(
        Double viewRef, ReadableArray withBBox, ReadableArray withFilter, ReadableArray withLayerIDs, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> {
            java.util.List<String> layerIds = ConvertUtils.toStringList(withLayerIDs);
            view.queryRenderedFeaturesInRect(
                ConvertUtils.toRectF(withBBox),
                ExpressionParser.from(withFilter),
                layerIds.isEmpty() ? null : layerIds,
                createCommandResponse(promise)
            );
        });
    }

    @Override
    public void setHandledMapChangedEvents(Double viewRef, ReadableArray events, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> {
            view.setHandledMapChangedEvents(events.asArrayString());
            promise.resolve(null);
        });
    }

    @Override
    public void clearData(Double viewRef, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> view.clearData(createCommandResponse(promise)));
    }

    @Override
    public void querySourceFeatures(
        Double viewRef, String sourceId, ReadableArray withFilter, ReadableArray withSourceLayerIDs, Promise promise) {
        withMapViewOnUIThread(viewRef, promise, view -> {
            java.util.List<String> sourceLayerIds = ConvertUtils.toStringList(withSourceLayerIDs);
            view.querySourceFeatures(
                sourceId,
                ExpressionParser.from(withFilter),
                sourceLayerIds.isEmpty() ? null : sourceLayerIds,
                createCommandResponse(promise)
            );
        });
    }

    public static final String NAME = "RCTMLNMapViewModule";
}

@FunctionalInterface
interface MapViewCallback {
    void apply(RCTMGLMapView view);
}

@FunctionalInterface
interface CommandResponse {
    void success(CommandResponseCallback builder);
    void error(String message);
}

@FunctionalInterface
interface CommandResponseCallback {
    void apply(WritableMap payload);
}
