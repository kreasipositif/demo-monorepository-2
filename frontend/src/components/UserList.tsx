import React, { useEffect, useState } from 'react';
import { userService, User } from '../services/userService';
import './UserList.css';

const UserList: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [retryCount, setRetryCount] = useState(0);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  useEffect(() => {
    if (error && retryCount < 10) {
      const timer = setTimeout(() => {
        setRetryCount(retryCount + 1);
        fetchUsers();
      }, 3000);
      return () => clearTimeout(timer);
    }
  }, [error, retryCount]);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const data = await userService.getAllUsers();
      setUsers(data);
      setError(null);
      setRetryCount(0); // Reset retry count on success
    } catch (err) {
      setError('Service A is starting up... Retrying automatically.');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await userService.createUser(formData);
      setFormData({ name: '', email: '', phone: '' });
      setShowForm(false);
      fetchUsers();
    } catch (err) {
      setError('Failed to create user');
    }
  };

  if (loading) return <div className="loading">Loading users...</div>;

  return (
    <div className="user-list">
      <div className="header">
        <h2>👥 User Management (Service A)</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Cancel' : 'Add User'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="user-form">
          <input
            type="text"
            placeholder="Name"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            required
          />
          <input
            type="tel"
            placeholder="Phone"
            value={formData.phone}
            onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
            required
          />
          <button type="submit" className="btn-primary">Create User</button>
        </form>
      )}

      <div className="list">
        {users.length === 0 ? (
          <p className="empty">No users found. Create your first user!</p>
        ) : (
          users.map((user) => (
            <div key={user.id} className="card">
              <h3>{user.name}</h3>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Phone:</strong> {user.phone}</p>
              <p><strong>ID:</strong> {user.id}</p>
              <p className="date"><strong>Created:</strong> {user.createdAt}</p>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default UserList;
