import React, { useEffect, useState, useRef } from "react";
import { Button, Card, Collapse, Spin } from "antd";
import {Link} from 'react-router-dom';
import styled from "styled-components";
import courierService from "../services/courierService";
import orderService from '../services/orderService';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from "../store";
import { Drawer } from "antd";

const { Panel } = Collapse;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const CardsContainer = styled.div`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
`;

const StyledCard = styled(Card)`
  margin: 16px;
  width: 300px;
  border: 1px solid #d9d9d9;
  padding: 16px;
  transition: box-shadow 0.3s;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

export interface Order {
  id: number;
  courierId: number | null;
  clientName: string | null;
  description: string | null;
  clientPhone: number | null;
  status: string | null;
  orderTime: string;
  address: string | null;
  branchAddress: string | null;
  flat: number | null;
  frontDoor: number | null;
  floor: number | null;
  weight: number | null;
  endCookingTime: string ;
  dishesOrders: any[];
}

const OrderNotificationComponent: React.FC = () => {
  const [notifyOrders, setNotifyOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [expandedPanel, setExpandedPanel] = useState('');

  const [showDrawer, setShowDrawer] = useState(false);
  const dispatch = useDispatch();
  const user = useSelector((store: RootState) => store.auth.user);

  const handleCollapseChange = (key: string | string[]) => {
      if (Array.isArray(key)) {
          setExpandedPanel(key[1]);
      } else {
          setExpandedPanel(key);
      }
  };

  const handleClickAcceptOrder = (idOrder:number) => {
      console.log(user);
      if (user) {

          orderService.assignOrderToCourier({id:idOrder, courierId:user?.id}, dispatch);
      }
  };

  const showNotifyOrders = () => {
    setExpandedPanel('');
    courierService.getNotifyOrders().then((newOrders) => {
      if (newOrders.length > 0) {
        setNotifyOrders(newOrders);
        setLoading(false);
        setShowDrawer(true);
      } else {
        setNotifyOrders([]);
        setLoading(false);
        setShowDrawer(false);
      }
    });
  };

  useEffect(() => {
    showNotifyOrders();
    const intervalId = setInterval(() => {
      showNotifyOrders();
    }, 600000);

    return () => {
      clearInterval(intervalId);
    };
  }, []);
  
  return (
    <Container>
      {loading ? (
        <Spin tip="Loading..." size="large" style={{ margin: "16px auto" }} />
      ) : (
        <Drawer
          title="New Orders"
          placement="right"
          closable={true}
          onClose={() => setShowDrawer(false)}
          visible={showDrawer}
          width={400} 
        >
          <CardsContainer>
          {notifyOrders.map((order) => (
            <StyledCard title={`Order № ${order.id}`} key={order.id}>
            <div>
                <p>
                    <strong>Cooking Time:</strong> {order.orderTime
                    ? new Date(order.endCookingTime).toLocaleString('ru-RU')
                    : 'No data'}
                </p>
                <p>
                    <strong>Delivery Time:</strong>{' '}
                    {new Date(order.orderTime).toLocaleString('ru-RU')}
                </p>
                <p>
                    <strong>Weight:</strong> {order.weight}
                </p>
                <p>
                    <strong>Customer Address:</strong> {order.address}
                </p>
                <p>
                    <strong>Restaurant Address:</strong> {order.branchAddress}
                </p>
                <Collapse activeKey={expandedPanel} onChange={handleCollapseChange}>
                    <Panel header="Show more" key={order.id}>
                        <p>
                            <strong>Customer Name:</strong> {order.clientName}
                        </p>
                        <p>
                            <strong>Customer Number:</strong> {order.clientPhone}
                        </p>
                        <p>
                            <strong>Description:</strong> {order.description}
                        </p>
                        <p>
                            <strong>Status:</strong> {order.status}
                        </p>
                        <p>
                            <strong>Apartment, Entrance, and Floor:</strong> {order.flat},{' '}
                            {order.frontDoor}, {order.floor}
                        </p>
                        <p>
                            <strong>Composition:</strong>{' '}
                            {order.dishesOrders.map((dish) => dish.dishName).join(', ').toLowerCase()}
                        </p>
                    </Panel>
                </Collapse>
            </div>
              <Link to="/active-orders">
                <div
                    style={{ display: 'flex', justifyContent: 'center', marginTop: '16px' }}
                >
                    <Button
                        type="primary"
                        style={{ width: '100%' }}
                        onClick={() => handleClickAcceptOrder(order.id)}
                    >
                        Accept Order
                    </Button>
                </div>
                </Link>
            </StyledCard>
            ))}
          </CardsContainer>
        </Drawer>
      )}
    </Container>
  );
};

export default OrderNotificationComponent;