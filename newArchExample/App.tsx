import React from 'react';
import MapLibreGL from '@maplibre/maplibre-react-native';
import { StyleSheet, Text, View, LogBox, SafeAreaView, Platform } from 'react-native';

import sheet from './styles/sheet';
import colors from './styles/colors';
import Home from './src/example';

LogBox.ignoreLogs([
  'Warning: isMounted(...) is deprecated',
  'Module RCTImageLoader',
]);

const styles = StyleSheet.create({
  noPermissionsText: {
    fontSize: 18,
    fontWeight: 'bold',
  },
});

MapLibreGL.setAccessToken(null);

interface AppState {
  isFetchingAndroidPermission: boolean;
  isAndroidPermissionGranted: boolean;
  activeExample: number;
}

class App extends React.Component<{}, AppState> {
  constructor(props: {}) {
    super(props);

    this.state = {
      isFetchingAndroidPermission: Platform.OS === 'android',
      isAndroidPermissionGranted: false,
      activeExample: -1,
    };
  }

  async componentDidMount() {
    if (Platform.OS === 'android') {
      const isGranted = await MapLibreGL.requestAndroidLocationPermissions();
      this.setState({
        isAndroidPermissionGranted: isGranted,
        isFetchingAndroidPermission: false,
      });
    }
  }

  render() {
    if (Platform.OS === 'android' && !this.state.isAndroidPermissionGranted) {
      if (this.state.isFetchingAndroidPermission) {
        return null;
      }
      return (
        <SafeAreaView
          style={[sheet, { backgroundColor: colors.primary.blue }]}
        >
          <View style={styles as any}>
            <Text style={styles.noPermissionsText}>
              You need to accept location permissions in order to use this
              example application
            </Text>
          </View>
        </SafeAreaView>
      );
    }

    return <Home />;
  }
}

export default App;
