const SERVICE_B_BASE_URL = process.env.REACT_APP_SERVICE_B_URL || 'http://localhost:8082';

export interface Order {
  id: string;
  orderNumber: string;
  customerId: string;
  productName: string;
  quantity: string;
  unitPrice: string;
  totalAmount: string;
  status: string;
  createdAt: string;
}

export interface CreateOrderRequest {
  customerId: string;
  productName: string;
  quantity: number;
  unitPrice: number;
}

export const orderService = {
  getAllOrders: async (): Promise<Order[]> => {
    const response = await fetch(`${SERVICE_B_BASE_URL}/api/orders`);
    if (!response.ok) {
      throw new Error('Failed to fetch orders');
    }
    return response.json();
  },

  getOrderById: async (id: string): Promise<Order> => {
    const response = await fetch(`${SERVICE_B_BASE_URL}/api/orders/${id}`);
    if (!response.ok) {
      throw new Error('Failed to fetch order');
    }
    return response.json();
  },

  createOrder: async (orderData: CreateOrderRequest): Promise<Order> => {
    const response = await fetch(`${SERVICE_B_BASE_URL}/api/orders`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(orderData),
    });
    if (!response.ok) {
      throw new Error('Failed to create order');
    }
    return response.json();
  },
};
