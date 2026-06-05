import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from './src/screens/HomeScreen';
import ArClassScreen from './src/screens/ArClassScreen';
import LoginScreen from './src/screens/LoginScreen';

export type RootStackParamList = {
  Login: undefined;
  Home: undefined;
  ArClass: { sessionId: string; modelUrl: string; title: string };
};

const Stack = createNativeStackNavigator<RootStackParamList>();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Login" screenOptions={{ headerShown: true }}>
        <Stack.Screen name="Login" component={LoginScreen} options={{ title: 'EduSphere' }} />
        <Stack.Screen name="Home" component={HomeScreen} options={{ title: 'AR-классы' }} />
        <Stack.Screen name="ArClass" component={ArClassScreen} options={{ title: 'AR-сессия' }} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
