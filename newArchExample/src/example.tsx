import React from 'react';
import {View, StyleSheet} from 'react-native';
import ShowMap from './mapview';

const styles = StyleSheet.create({
  exampleList: {
    flex: 1,
  },
  exampleListItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 32,
  },
  exampleListItemBorder: {
    borderBottomColor: '#ccc',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  exampleListLabel: {
    fontSize: 18,
  },
});

function Home() {
  return (
    <View style={styles as any}>
      <ShowMap />
    </View>
  );
}

export default Home;
