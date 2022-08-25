import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>&copy; {new Date().getFullYear()} Government of the Republic of Rwanda</p>
      </Col>
    </Row>
  </div>
);

export default Footer;
