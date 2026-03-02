import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { PayPalScriptProvider, PayPalButtons } from '@paypal/react-paypal-js';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../css/Register.css';
import '../css/Global.css';

const Register = () => {
  const API_URL = process.env.REACT_APP_API_URL;

  const [formData, setFormData] = useState({
    nombreUsuario: '',
    contrasenaUsuario: '',
    correoElectronico: '',
    fotoPerfil: '',
    tipoSuscripcion: 1,
  });

  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const crearCuentaTrasPago = async (orderId) => {
    const { nombreUsuario, contrasenaUsuario, correoElectronico } = formData;

    if (!nombreUsuario || !contrasenaUsuario || !correoElectronico) {
      setMensaje('Todos los campos son obligatorios.');
      return;
    }

    try {
      // Crear usuario tras pago
      const response = await axios.post(`${API_URL}/api/usuarios/crear-con-pago`, {
        ...formData,
        orderId: orderId,
      });

      const nombreUsuarioCreado = response.data.nombreUsuario;

      // Login automático
      const loginResponse = await axios.post(`${API_URL}/api/auth/login`, {
        nombreUsuario: nombreUsuarioCreado,
        contrasenaUsuario: formData.contrasenaUsuario
      });

      localStorage.setItem('token', loginResponse.data.token);
      localStorage.setItem('nombreUsuario', nombreUsuarioCreado);

      navigate('/home');
    } catch (error) {
      console.error(error);
      if (error.response && error.response.data) {
        setMensaje(error.response.data);
      } else {
        setMensaje('Error al registrar o iniciar sesión.');
      }
    }
  };

  return (
    <>
      <Header />
      <div className="register-container">
        <div className="register-box">
          <h2>Registro de Usuario</h2>

          <input
            type="text"
            name="nombreUsuario"
            placeholder="Nombre de usuario"
            value={formData.nombreUsuario}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="contrasenaUsuario"
            placeholder="Contraseña"
            value={formData.contrasenaUsuario}
            onChange={handleChange}
            required
          />
          <input
            type="email"
            name="correoElectronico"
            placeholder="Correo electrónico"
            value={formData.correoElectronico}
            onChange={handleChange}
            required
          />

          <select
            name="tipoSuscripcion"
            value={formData.tipoSuscripcion}
            onChange={handleChange}
          >
            <option value={0}>Usuario Coche y Moto</option>
            <option value={1}>Usuario Coche</option>
            <option value={2}>Usuario Moto</option>
          </select>

          <PayPalScriptProvider
            options={{
              'client-id': 'TU_CLIENT_ID_SANDBOX_AQUI'
            }}
          >
            <PayPalButtons
              createOrder={() =>
                fetch(`${API_URL}/api/paypal/create-order`, {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/json'
                  },
                  body: JSON.stringify({
                    tipoSuscripcion: formData.tipoSuscripcion
                  })
                })
                  .then(res => {
                    if (!res.ok) throw new Error("Error al crear orden");
                    return res.text();
                  })
                  .then(orderId => {
                    if (!orderId) {
                      throw new Error("orderId inválido");
                    }
                    return orderId;
                  })
              }
              onApprove={async (data, actions) => {
                await actions.order.capture();
                await crearCuentaTrasPago(data.orderID);
              }}
            />
          </PayPalScriptProvider>

          {mensaje && (
            <p style={{ color: 'red', marginTop: '10px' }}>
              {mensaje}
            </p>
          )}

          <button onClick={() => navigate('/')}>
            Volver a Iniciar Sesión
          </button>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Register;