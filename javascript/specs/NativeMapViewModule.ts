import type {TurboModule} from 'react-native/Libraries/TurboModule/RCTExport';
import {Int32} from 'react-native/Libraries/Types/CodegenTypes';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  takeSnap: (viewRef: Int32 | null, writeToDisk: boolean) => Promise<object>;
  queryTerrainElevation: (
    viewRef: Int32 | null,
    coordinates: ReadonlyArray<number>,
  ) => Promise<object>;
  setSourceVisibility: (
    viewRef: Int32 | null,
    visible: boolean,
    sourceId: string,
    sourceLayerId: string,
  ) => Promise<object>;
  getCenter: (viewRef: Int32 | null) => Promise<object>;
  getCoordinateFromView: (
    viewRef: Int32 | null,
    atPoint: ReadonlyArray<number>,
  ) => Promise<object>;
  getPointInView: (
    viewRef: Int32 | null,
    atCoordinate: ReadonlyArray<number>,
  ) => Promise<object>;
  getZoom: (viewRef: Int32 | null) => Promise<object>;
  getVisibleBounds: (viewRef: Int32 | null) => Promise<object>;
  queryRenderedFeaturesAtPoint: (
    viewRef: Int32 | null,
    atPoint: ReadonlyArray<number>,
    withFilter: ReadonlyArray<Record<string, any>>,
    withLayerIDs: ReadonlyArray<string>,
  ) => Promise<object>;
  queryRenderedFeaturesInRect: (
    viewRef: Int32 | null,
    withBBox: ReadonlyArray<number>,
    withFilter: ReadonlyArray<Record<string, any>>,
    withLayerIDs: ReadonlyArray<string>,
  ) => Promise<object>;
  setHandledMapChangedEvents: (
    viewRef: Int32 | null,
    events: ReadonlyArray<string>,
  ) => Promise<object>;
  clearData: (viewRef: Int32 | null) => Promise<object>;
  querySourceFeatures: (
    viewRef: Int32 | null,
    sourceId: string,
    withFilter: ReadonlyArray<Record<string, any>>,
    withSourceLayerIDs: ReadonlyArray<string>,
  ) => Promise<object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RCTMLNMapViewModule');
