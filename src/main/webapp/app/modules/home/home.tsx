import './home.scss';

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import Chart from 'chart.js/auto';

import axios from 'axios';
import { Pie, Bar } from 'react-chartjs-2';

import { CategoryScale } from 'chart.js';
Chart.register(CategoryScale);

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [chartPriorityData, setChartPriorityData] = useState([]);
  const [chartStatusData, setChartStatusData] = useState([]);

  const url = 'http://localhost:8080/api/reports/complains';

  const reportComplainByPriority = async () => {
    const { data } = await axios.get(`${url}/priority`);
    setChartPriorityData(data);
  };

  const reportComplainByStatus = async () => {
    const { data } = await axios.get(`${url}/status`);
    setChartStatusData(data);
  };

  const priorityData = {
    labels: chartPriorityData.map(d => d.priority),
    datasets: [
      {
        label: 'Priority',
        fill: false,
        lineTension: 0.0,
        pointHitRadius: 20,
        data: chartPriorityData.map(d => d.count),
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)',
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  const statusData = {
    labels: chartStatusData.map(d => d.status),
    datasets: [
      {
        label: 'Status',
        fill: false,
        lineTension: 0.0,
        pointHitRadius: 20,
        data: chartStatusData.map(d => d.count),
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)',
          'rgba(54, 162, 235, 0.2)',
          'rgba(255, 206, 86, 0.2)',
          'rgba(75, 192, 192, 0.2)',
          'rgba(153, 102, 255, 0.2)',
          'rgba(255, 159, 64, 0.2)',
        ],
        borderColor: [
          'rgba(255, 99, 132, 1)',
          'rgba(54, 162, 235, 1)',
          'rgba(255, 206, 86, 1)',
          'rgba(75, 192, 192, 1)',
          'rgba(153, 102, 255, 1)',
          'rgba(255, 159, 64, 1)',
        ],
        borderWidth: 1,
      },
    ],
  };

  useEffect(() => {
    reportComplainByPriority();
    reportComplainByStatus();
  }, []);

  return (
    <Row>
      {!account?.login && (
        <Col md="3" className="pad">
          <span className="hipster rounded" />
        </Col>
      )}

      <Col md="12">
        <h2>Welcome to Minaloc E-Citizen Application!</h2>
        {account?.login ? (
          <div>
            <Alert color="success">You are logged in as user {account.login}.</Alert>
            <div className="row">
              <div className="col-4">
                <div style={{ width: 290 }}>
                  <h5>Complain priority report</h5>
                  <Pie data={priorityData} />
                </div>
              </div>
              <div className="col-4">
                <div style={{ width: 290 }}>
                  <h5>Complain status report</h5>
                  <Pie data={statusData} />
                </div>
              </div>
              <div className="col-4"></div>
            </div>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                {' '}
                sign in
              </Link>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
            </Alert>

            {/* <Alert color="warning">
              You do not have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert> */}
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
