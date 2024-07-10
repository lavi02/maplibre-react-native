import NativeMapViewModule from '../specs/NativeMapViewModule';

import React, {Component, ReactElement} from 'react';
import {
  Image,
  findNodeHandle,
  Platform,
  ImageSourcePropType,
} from 'react-native';

function getIosManagerInstance(module: string): any {
  // @ts-expect-error TS says that string cannot be used to index NativeMapViewModule.
  return NativeMapViewModule[getIOSModuleName(module)];
}

export function isAndroid(): boolean {
  return Platform.OS === 'android';
}

export function existenceChange(cur: boolean, next: boolean): boolean {
  if (!cur && !next) {
    return false;
  }
  return (!cur && next) || (cur && !next);
}

export function isFunction(fn: unknown): fn is boolean {
  return typeof fn === 'function';
}

export function isNumber(num: unknown): num is number {
  return typeof num === 'number' && !Number.isNaN(num);
}

export function isUndefined(obj: unknown): obj is undefined {
  return typeof obj === 'undefined';
}

export function isString(str: unknown): str is string {
  return typeof str === 'string';
}

export function isBoolean(bool: unknown): bool is boolean {
  return typeof bool === 'boolean';
}

export function isPrimitive(
  value: unknown,
): value is string | number | boolean {
  return isString(value) || isNumber(value) || isBoolean(value);
}

export type NativeArg =
  | string
  | number
  | boolean
  | null
  | {[k: string]: NativeArg}
  | NativeArg[];

export function runNativeCommand<ReturnType = NativeArg>(
  module: string,
  name: string,
  nativeRef: Component,
  args: NativeArg[] = [],
): ReturnType {
  const handle = findNodeHandle(nativeRef);
  if (!handle) {
    throw new Error(`Could not find handle for native ref ${module}.${name}`);
  }

  try {
    if (isAndroid()) {
      // @ts-expect-error TS says that string cannot be used to index NativeMapViewModule.
      // It can, it's just not pretty.
      return NativeMapViewModule[module]?.(
        handle,
        ...args,
      ) as Promise<ReturnType>;
    }

    return getIosManagerInstance(module)[name](handle, ...args);
  } catch (e) {
    throw new Error(`Error running native command: ${e}`);
  }
}

export function cloneReactChildrenWithProps(
  children: Parameters<typeof React.Children.map>[0],
  propsToAdd: {[key: string]: string} = {},
): ReactElement[] | undefined {
  if (!children) {
    return undefined;
  }

  let foundChildren = null;

  if (!Array.isArray(children)) {
    foundChildren = [children];
  } else {
    foundChildren = children;
  }

  const filteredChildren = foundChildren.filter(child => !!child); // filter out falsy children, since some can be null
  return React.Children.map(filteredChildren, child =>
    React.cloneElement(child, propsToAdd),
  );
}

export function resolveImagePath(imageRef: ImageSourcePropType): string {
  const res = Image.resolveAssetSource(imageRef);
  return res.uri;
}

export function getIOSModuleName(moduleName: string): string {
  if (moduleName.startsWith('RCT')) {
    return moduleName.substring(3);
  }
  return moduleName;
}

export function toJSONString(json: object | string = ''): string {
  return JSON.stringify(json);
}
