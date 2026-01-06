import React from 'react';
import UserList from './components/UserList';
import OrderList from './components/OrderList';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>🚀 Spring Boot Monorepo Dashboard</h1>
        <p>React Frontend consuming Service A (Users) and Service B (Orders)</p>
      </header>

      <div className="dashboard">
        <div className="section">
          <UserList />
        </div>
        <div className="section">
          <OrderList />
        </div>
      </div>

      <footer className="App-footer">
        <p>
          <strong>Service A:</strong> http://localhost:8081 | 
          <strong> Service B:</strong> http://localhost:8082 | 
          <strong> Frontend:</strong> http://localhost:3000
        </p>
      </footer>
    </div>
  );
}

export default App;
