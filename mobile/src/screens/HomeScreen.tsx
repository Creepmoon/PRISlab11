import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { NativeStackNavigationProp } from '@react-navigation/native-stack';
import { RootStackParamList } from '../../App';
import { arApi, ArSession } from '../api/client';

type Props = { navigation: NativeStackNavigationProp<RootStackParamList, 'Home'> };

export default function HomeScreen({ navigation }: Props) {
  const [sessions, setSessions] = useState<ArSession[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    arApi.getActive()
      .then(setSessions)
      .catch(() => setSessions([
        { id: 'demo', title: 'Демо: Молекула H₂O', subject: 'Химия', arModelUrl: 'https://example.com/models/h2o.glb', status: 'ACTIVE' },
      ]))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <ActivityIndicator style={{ flex: 1 }} size="large" color="#4f46e5" />;

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Активные AR-классы</Text>
      <FlatList
        data={sessions}
        keyExtractor={item => item.id}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.card}
            onPress={() => navigation.navigate('ArClass', {
              sessionId: item.id,
              modelUrl: item.arModelUrl,
              title: item.title,
            })}
          >
            <Text style={styles.cardTitle}>{item.title}</Text>
            <Text style={styles.cardSubject}>{item.subject}</Text>
            <View style={styles.badge}>
              <Text style={styles.badgeText}>{item.status}</Text>
            </View>
          </TouchableOpacity>
        )}
        ListEmptyComponent={
          <Text style={styles.empty}>Нет активных сессий</Text>
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16, backgroundColor: '#f8fafc' },
  header: { fontSize: 20, fontWeight: '700', marginBottom: 16 },
  card: {
    backgroundColor: '#fff', borderRadius: 12, padding: 16, marginBottom: 12,
    borderWidth: 1, borderColor: '#e2e8f0',
  },
  cardTitle: { fontSize: 16, fontWeight: '600' },
  cardSubject: { fontSize: 13, color: '#64748b', marginTop: 4 },
  badge: {
    alignSelf: 'flex-start', backgroundColor: '#d1fae5', borderRadius: 12,
    paddingHorizontal: 10, paddingVertical: 4, marginTop: 8,
  },
  badgeText: { fontSize: 12, color: '#065f46', fontWeight: '500' },
  empty: { textAlign: 'center', color: '#94a3b8', marginTop: 40 },
});
