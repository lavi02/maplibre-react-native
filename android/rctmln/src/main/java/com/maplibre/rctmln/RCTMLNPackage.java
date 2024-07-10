package com.maplibre.rctmln;

import androidx.annotation.Nullable;

import com.facebook.react.ReactPackage;
import com.facebook.react.TurboReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maplibre.rctmln.components.annotation.RCTMLNCalloutManager;
import com.maplibre.rctmln.components.annotation.RCTMLNPointAnnotationManager;
import com.maplibre.rctmln.components.annotation.RCTMLNMarkerViewManager;
import com.maplibre.rctmln.components.camera.RCTMLNCameraManager;
import com.maplibre.rctmln.components.images.RCTMLNImagesManager;
import com.maplibre.rctmln.components.location.RCTMLNNativeUserLocationManager;
import com.maplibre.rctmln.components.mapview.NativeMapViewModule;
import com.maplibre.rctmln.components.mapview.RCTMLNMapViewManager;
import com.maplibre.rctmln.components.mapview.RCTMLNAndroidTextureMapViewManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNBackgroundLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNCircleLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNFillExtrusionLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNFillLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNHeatmapLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNLineLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNRasterLayerManager;
import com.maplibre.rctmln.components.styles.layers.RCTMLNSymbolLayerManager;
import com.maplibre.rctmln.components.styles.light.RCTMLNLightManager;
import com.maplibre.rctmln.components.styles.sources.RCTMLNImageSourceManager;
import com.maplibre.rctmln.components.styles.sources.RCTMLNRasterSourceManager;
import com.maplibre.rctmln.components.styles.sources.RCTMLNShapeSourceManager;
import com.maplibre.rctmln.components.styles.sources.RCTMLNVectorSourceManager;
import com.maplibre.rctmln.modules.RCTMLNLocationModule;
import com.maplibre.rctmln.modules.RCTMLNLogging;
import com.maplibre.rctmln.modules.RCTMLNModule;
import com.maplibre.rctmln.modules.RCTMLNOfflineModule;
import com.maplibre.rctmln.modules.RCTMLNSnapshotModule;

/**
 * Created by nickitaliano on 8/18/17.
 */

public class RCTMLNPackage implements TurboReactPackage {

    @Nullable
    @Override
    public NativeModule getModule(String s, ReactApplicationContext reactApplicationContext) {
        switch (s) {
            case RCTMLNModule.REACT_CLASS:
                return new RCTMLNModule(reactApplicationContext);
            case RCTMLNLocationModule.REACT_CLASS:
                return new RCTMLNLocationModule(reactApplicationContext);
            case RCTMLNOfflineModule.REACT_CLASS:
                return new RCTMLNOfflineModule(reactApplicationContext);
            case RCTMLNSnapshotModule.REACT_CLASS:
                return new RCTMLNSnapshotModule(reactApplicationContext);
            case RCTMLNLogging.REACT_CLASS:
                return new RCTMLNLogging(reactApplicationContext);
            case NativeMapViewModule.NAME:
                return new NativeMapViewModule(reactApplicationContext);
        }

        return null;
    }

    @Deprecated
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        List<ViewManager> managers = new ArrayList<>();

        // components
        managers.add(new RCTMLNCameraManager(reactApplicationContext));
        managers.add(new RCTMLNMapViewManager(reactApplicationContext));
        managers.add(new RCTMLNMarkerViewManager(reactApplicationContext));
        managers.add(new RCTMLNAndroidTextureMapViewManager(reactApplicationContext));
        managers.add(new RCTMLNLightManager());
        managers.add(new RCTMLNPointAnnotationManager(reactApplicationContext));
        managers.add(new RCTMLNCalloutManager());
        managers.add(new RCTMLNNativeUserLocationManager());

        // sources
        managers.add(new RCTMLNVectorSourceManager(reactApplicationContext));
        managers.add(new RCTMLNShapeSourceManager(reactApplicationContext));
        managers.add(new RCTMLNRasterSourceManager(reactApplicationContext));
        managers.add(new RCTMLNImageSourceManager());

        // images
        managers.add(new RCTMLNImagesManager(reactApplicationContext));

        // layers
        managers.add(new RCTMLNFillLayerManager());
        managers.add(new RCTMLNFillExtrusionLayerManager());
        managers.add(new RCTMLNHeatmapLayerManager());
        managers.add(new RCTMLNLineLayerManager());
        managers.add(new RCTMLNCircleLayerManager());
        managers.add(new RCTMLNSymbolLayerManager());
        managers.add(new RCTMLNRasterLayerManager());
        managers.add(new RCTMLNBackgroundLayerManager());

        return managers;
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return () -> {
            final Map<String, ReactModuleInfo> moduleInfos = new HashMap<>();
            boolean isTurboModule = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED;

            moduleInfos.put(
                    RCTMLNModule.REACT_CLASS,
                    new ReactModuleInfo(
                            RCTMLNModule.REACT_CLASS,
                            RCTMLNModule.REACT_CLASS,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            false // isTurboModule
                    ));

            moduleInfos.put(
                    RCTMLNLocationModule.REACT_CLASS,
                    new ReactModuleInfo(
                            RCTMLNLocationModule.REACT_CLASS,
                            RCTMLNLocationModule.REACT_CLASS,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            false // isTurboModule
                    ));

            moduleInfos.put(
                    RCTMLNOfflineModule.REACT_CLASS,
                    new ReactModuleInfo(
                            RCTMLNOfflineModule.REACT_CLASS,
                            RCTMLNOfflineModule.REACT_CLASS,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            false // isTurboModule
                    ));

            moduleInfos.put(
                    RCTMLNSnapshotModule.REACT_CLASS,
                    new ReactModuleInfo(
                            RCTMLNSnapshotModule.REACT_CLASS,
                            RCTMLNSnapshotModule.REACT_CLASS,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            false // isTurboModule
                    ));

            moduleInfos.put(
                    RCTMLNLogging.REACT_CLASS,
                    new ReactModuleInfo(
                            RCTMLNLogging.REACT_CLASS,
                            RCTMLNLogging.REACT_CLASS,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            true, // hasConstants
                            false, // isCxxModule
                            false // isTurboModule
                    ));

            moduleInfos.put(
                    NativeMapViewModule.NAME,
                    new ReactModuleInfo(
                            NativeMapViewModule.NAME,
                            NativeMapViewModule.NAME,
                            false, // canOverrideExistingModule
                            false, // needsEagerInit
                            false, // hasConstants
                            false, // isCxxModule
                            isTurboModule // isTurboModule
                    ));

            return moduleInfos;
        };
    }
}
