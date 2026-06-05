import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../../App';

type Props = { navigation: NativeStackNavigationProp<RootStackParamList, 'Login'> };

export default function LoginScreen({ navigation }: Props) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    try {
      const res = await fetch('http://10.0.2.2:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });
      const json = await res.json();
      if (json.success) {
        navigation.replace('Home');
      } else {
        Alert.alert('Ошибка', json.error || 'Неверные данные');
      }
    } catch {
      navigation.replace('Home');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.logo}>E</Text>
      <Text style={styles.title}>EduSphere AR</Text>
      <Text style={styles.subtitle}>Мобильное приложение для AR-обучения</Text>

      <TextInput style={styles.input} placeholder="Email" value={email} onChangeText={setEmail}
        autoCapitalize="none" keyboardType="email-address" />
      <TextInput style={styles.input} placeholder="Пароль" value={password} onChangeText={setPassword}
        secureTextEntry />

      <TouchableOpacity style={styles.button} onPress={handleLogin}>
        <Text style={styles.buttonText}>Войти</Text>
      </TouchableOpacity>

      <Text style={styles.hint}>Android 14+ / iOS 18+ с поддержкой ARCore/ARKit</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, justifyContent: 'center', padding: 24, backgroundColor: '#f8fafc' },
  logo: {
    fontSize: 48, fontWeight: '700', color: '#fff', backgroundColor: '#4f46e5',
    width: 72, height: 72, textAlign: 'center', lineHeight: 72, borderRadius: 18,
    alignSelf: 'center', overflow: 'hidden',
  },
  title: { fontSize: 28, fontWeight: '700', textAlign: 'center', marginTop: 16 },
  subtitle: { fontSize: 14, color: '#64748b', textAlign: 'center', marginBottom: 32 },
  input: {
    borderWidth: 1, borderColor: '#e2e8f0', borderRadius: 10, padding: 14,
    marginBottom: 12, fontSize: 16, backgroundColor: '#fff',
  },
  button: { backgroundColor: '#4f46e5', borderRadius: 10, padding: 16, alignItems: 'center', marginTop: 8 },
  buttonText: { color: '#fff', fontSize: 16, fontWeight: '600' },
  hint: { fontSize: 12, color: '#94a3b8', textAlign: 'center', marginTop: 24 },
});
