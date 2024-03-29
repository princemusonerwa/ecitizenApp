import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './village.reducer';

export const VillageDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const villageEntity = useAppSelector(state => state.village.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="villageDetailsHeading">Village</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{villageEntity.id}</dd>
          <dt>
            <span id="villageCode">Village Code</span>
          </dt>
          <dd>{villageEntity.villageCode}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{villageEntity.name}</dd>
          <dt>Cell</dt>
          <dd>{villageEntity.cell ? villageEntity.cell.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/village" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/village/${villageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VillageDetail;
