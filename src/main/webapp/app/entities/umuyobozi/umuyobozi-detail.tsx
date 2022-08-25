import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './umuyobozi.reducer';

export const UmuyoboziDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const umuyoboziEntity = useAppSelector(state => state.umuyobozi.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="umuyoboziDetailsHeading">Umuyobozi</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{umuyoboziEntity.id}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{umuyoboziEntity.firstName}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{umuyoboziEntity.lastName}</dd>
          <dt>
            <span id="phoneOne">Phone One</span>
          </dt>
          <dd>{umuyoboziEntity.phoneOne}</dd>
          <dt>
            <span id="phoneTwo">Phone Two</span>
          </dt>
          <dd>{umuyoboziEntity.phoneTwo}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{umuyoboziEntity.email}</dd>
          <dt>Umurimo</dt>
          <dd>{umuyoboziEntity.umurimo ? umuyoboziEntity.umurimo.umurimo : ''}</dd>
          <dt>Office</dt>
          <dd>{umuyoboziEntity.office ? umuyoboziEntity.office.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/umuyobozi" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/umuyobozi/${umuyoboziEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UmuyoboziDetail;
