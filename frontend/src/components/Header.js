import React from 'react';
import { Link } from 'react-router-dom';
import '../css/Header.css';
import logo from '../assets/img/Logo1aMarcha.png';

const Header = () => {
  return (
    <header className="main-header">
      <div className="header-content">
        <Link to="/home">
          <img src={logo} alt="Logo 1ª Marcha" className="logo" />
        </Link>
      </div>
    </header>
  );
};

export default Header;
