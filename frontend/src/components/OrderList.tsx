import React, { useEffect, useState } from 'react';
import { orderService, Order } from '../services/orderService';
import './OrderList.css';

const OrderList: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [retryCount, setRetryCount] = useState(0);
  const [formData, setFormData] = useState({
    customerId: '',
    productName: '',
    quantity: 1,
    unitPrice: 0,
  });

  useEffect(() => {
    fetchOrders();
  }, []);

  useEffect(() => {
    if (error && retryCount < 10) {
      const timer = setTimeout(() => {
        setRetryCount(retryCount + 1);
        fetchOrders();
      }, 3000); 
      return () => clearTimeout(timer);
    }
  }, [error, retryCount]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const data = await orderService.getAllOrders();
      setOrders(data);
      setError(null);
      setRetryCount(0);
    } catch (err) {
      setError('Service B is starting up... Retrying automatically.');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await orderService.createOrder(formData);
      setFormData({ customerId: '', productName: '', quantity: 1, unitPrice: 0 });
      setShowForm(false);
      fetchOrders();
    } catch (err) {
      setError('Failed to create order');
    }
  };

  if (loading) return <div className="loading">Loading orders...</div>;

  return (
    <div className="order-list">
      <div className="header">
        <h2>📦 Order Management (Service B)</h2>
        <button onClick={() => setShowForm(!showForm)} className="btn-primary">
          {showForm ? 'Cancel' : 'Create Order'}
        </button>
      </div>

      {error && <div className="error">{error}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="order-form">
          <input
            type="text"
            placeholder="Customer ID"
            value={formData.customerId}
            onChange={(e) => setFormData({ ...formData, customerId: e.target.value })}
            required
          />
          <input
            type="text"
            placeholder="Product Name"
            value={formData.productName}
            onChange={(e) => setFormData({ ...formData, productName: e.target.value })}
            required
          />
          <input
            type="number"
            placeholder="Quantity"
            min="1"
            value={formData.quantity}
            onChange={(e) => setFormData({ ...formData, quantity: parseInt(e.target.value) })}
            required
          />
          <input
            type="number"
            placeholder="Unit Price"
            step="0.01"
            min="0"
            value={formData.unitPrice}
            onChange={(e) => setFormData({ ...formData, unitPrice: parseFloat(e.target.value) })}
            required
          />
          <button type="submit" className="btn-primary">Create Order</button>
        </form>
      )}

      <div className="list">
        {orders.length === 0 ? (
          <p className="empty">No orders found. Create your first order!</p>
        ) : (
          orders.map((order) => (
            <div key={order.id} className="card">
              <div className="order-header">
                <h3>{order.productName}</h3>
                <span className={`status ${order.status.toLowerCase()}`}>{order.status}</span>
              </div>
              <p><strong>Order #:</strong> {order.orderNumber}</p>
              <p><strong>Customer ID:</strong> {order.customerId}</p>
              <p><strong>Quantity:</strong> {order.quantity}</p>
              <p><strong>Unit Price:</strong> {order.unitPrice}</p>
              <p><strong>Total:</strong> {order.totalAmount}</p>
              <p className="date"><strong>Created:</strong> {order.createdAt}</p>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default OrderList;
