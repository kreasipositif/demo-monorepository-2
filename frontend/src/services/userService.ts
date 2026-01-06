// Service A API - User Management
const SERVICE_A_BASE_URL = process.env.REACT_APP_SERVICE_A_URL || 'http://localhost:8081';

export interface User {
  id: string;
  name: string;
  email: string;
  phone: string;
  createdAt: string;
}

export interface CreateUserRequest {
  name: string;
  email: string;
  phone: string;
}

export const userService = {
  getAllUsers: async (): Promise<User[]> => {
    const response = await fetch(`${SERVICE_A_BASE_URL}/api/users`);
    if (!response.ok) {
      throw new Error('Failed to fetch users');
    }
    return response.json();
  },

  getUserById: async (id: string): Promise<User> => {
    const response = await fetch(`${SERVICE_A_BASE_URL}/api/users/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch user');
    }
    return response.json();
  },

  createUser: async (userData: CreateUserRequest): Promise<User> => {
    const response = await fetch(`${SERVICE_A_BASE_URL}/api/users`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    });
    if (!response.ok) {
      throw new Error('Failed to create user');
    }
    return response.json();
  },
};
