import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders dashboard header', () => {
  render(<App />);
  const headerElement = screen.getByText(/Spring Boot Monorepo Dashboard/i);
  expect(headerElement).toBeInTheDocument();
});

test('renders user management section', () => {
  render(<App />);
  const loadingElement = screen.getByText(/Loading users.../i);
  expect(loadingElement).toBeInTheDocument();
});

test('renders order management section', () => {
  render(<App />);
  const loadingElement = screen.getByText(/Loading orders.../i);
  expect(loadingElement).toBeInTheDocument();
});

test('renders service information in footer', () => {
  render(<App />);
  const serviceAInfo = screen.getByText(/Service A:/i);
  const serviceBInfo = screen.getByText(/Service B:/i);
  expect(serviceAInfo).toBeInTheDocument();
  expect(serviceBInfo).toBeInTheDocument();
});
