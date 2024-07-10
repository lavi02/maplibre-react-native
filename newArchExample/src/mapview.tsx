import React, {ReactElement} from 'react';
import {Alert} from 'react-native';
import MapLibreGL from '@maplibre/maplibre-react-native';

const ShowMap = (): ReactElement => {

  const onUserMarkerPress = (): void => {
    Alert.alert('You pressed on the user location annotation');
  };

  return (
      <MapLibreGL.MapView
        style={{ flex: 1 }}>
        <MapLibreGL.UserLocation onPress={onUserMarkerPress} />
      </MapLibreGL.MapView>
  );
};

export default ShowMap;
