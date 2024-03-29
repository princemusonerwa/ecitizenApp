import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cell.reducer';

export const CellDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cellEntity = useAppSelector(state => state.cell.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cellDetailsHeading">Cell</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cellEntity.id}</dd>
          <dt>
            <span id="sectorCode">Sector Code</span>
          </dt>
          <dd>{cellEntity.sectorCode}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{cellEntity.name}</dd>
          <dt>Sector</dt>
          <dd>{cellEntity.sector ? cellEntity.sector.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/cell" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cell/${cellEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CellDetail;
