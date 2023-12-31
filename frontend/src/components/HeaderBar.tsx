import { Layout, Button } from 'antd';
import React, { useState } from 'react';
import { MenuOutlined, UserOutlined  } from '@ant-design/icons';
import Sidebar from './SideBar';
import { Link } from 'react-router-dom';

const { Header } = Layout;

const HeaderBar: React.FC = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div>
      <Header style={{ backgroundColor: 'white', textAlign: 'center', position: 'fixed', width: '100%', zIndex: 100 }}>
        <Button
          icon={<MenuOutlined />}
          onClick={toggleSidebar}
          style={{ float: 'left', marginTop: '15px', marginLeft: '-30px' }}
        />
        <Link to="/info">
          <div
            style={{
              width: '40px',
              height: '40px',
              float: 'right',
              backgroundColor: '#EFEFEF',
              display: 'flex',
              justifyContent: 'center',
              borderRadius: '50%',
              marginTop: '10px',
              marginRight: '-30px'
            }}
          >
            <UserOutlined style={{ float: 'right', fontSize: '32px' }} />
          </div>
        </Link>
        <h1 style={{ color: 'black', marginTop: '0px' }}>Мои карты</h1>
      </Header>
      <div style={{ paddingTop: '64px' }}>
      </div>
      <Sidebar visible={isSidebarOpen} onClose={toggleSidebar} />
    </div>
  );
};

export default HeaderBar;