import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { PayPalScriptProvider, PayPalButtons } from '@paypal/react-paypal-js';

const Register = () => {
  const [formData, setFormData] = useState({
    nombreUsuario: '',
    contrasenaUsuario: '',
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
    try {
      const response = await axios.post('http://localhost:8080/api/usuarios/crear-con-pago', {
        ...formData,
        orderId: orderId,
      });

      const nombreUsuario = response.data.nombreUsuario;

      const loginResponse = await axios.post('http://localhost:8080/api/auth/login', {
        nombreUsuario: nombreUsuario,
        contrasenaUsuario: formData.contrasenaUsuario
      });

      localStorage.setItem('token', loginResponse.data.token);
      localStorage.setItem('nombreUsuario', nombreUsuario);

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
    <div>
      <h2>Registro de Usuario</h2>

      <input
        type="text"
        name="nombreUsuario"
        placeholder="Nombre de usuario"
        value={formData.nombreUsuario}
        onChange={handleChange}
        required
      />
      <br />
      <input
        type="password"
        name="contrasenaUsuario"
        placeholder="Contraseña"
        value={formData.contrasenaUsuario}
        onChange={handleChange}
        required
      />
      <br />
      <input
        type="text"
        name="fotoPerfil"
        placeholder="URL de foto de perfil"
        value={formData.fotoPerfil}
        onChange={handleChange}
      />
      <br />
      <select
        name="tipoSuscripcion"
        value={formData.tipoSuscripcion}
        onChange={handleChange}
      >
        <option value={0}>Superusuario</option>
        <option value={1}>Usuario Coche</option>
        <option value={2}>Usuario Moto</option>
      </select>
      <br />

      <PayPalScriptProvider options={{ 'client-id': 'AWA7f_1U9IV0dYshg_utJOLvm7w1oA0BVRvWVxwsTlq4DMHARBIDhkFHp9V4YoIR9PTNnfQy3zgt2BXP' }}>
        <PayPalButtons
          createOrder={() =>
            fetch('http://localhost:8080/api/paypal/create-order', {
              method: 'POST'
            })
              .then(res => {
                if (!res.ok) throw new Error("Error al crear orden");
                return res.text();
              })
              .then(orderId => {
                console.log("✅ orderId recibido:", orderId);
                if (!orderId || orderId.includes("Error")) {
                  throw new Error("❌ orderId inválido");
                }
                return orderId;
              })
              .catch(err => {
                console.error("🛑 Fallo en createOrder:", err);
                throw err;
              })
          }

          onApprove={async (data, actions) => {
            await actions.order.capture();
            await crearCuentaTrasPago(data.orderID);
          }}
        />
      </PayPalScriptProvider>

      {mensaje && <p style={{ color: 'red', marginTop: '10px' }}>{mensaje}</p>}

      <button onClick={() => navigate('/')}>Volver a Iniciar Sesión</button>
    </div>
  );
};

export default Register;
