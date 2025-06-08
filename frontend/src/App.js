import React, { useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import Home from './components/Home';
import TestCoche from './components/TestCoche';
import AdminVerExamen from './components/AdminVerExamen';
import AdminEspecial from './components/AdminEspecial';

import './css/Global.css';

function App() {
  const [usuario, setUsuario] = useState(null);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login onLogin={setUsuario} />} />
        <Route path="/register" element={<Register />} />
        <Route path="/home" element={<Home />} />
        <Route path="/examen/:idExamen" element={<TestCoche />} />
        <Route path="/admin-especial" element={<AdminEspecial />} />
        <Route path="/admin-examen/:idExamen" element={<AdminVerExamen />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
