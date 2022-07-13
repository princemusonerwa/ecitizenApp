import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './office.reducer';

export const OfficeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const officeEntity = useAppSelector(state => state.office.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="officeDetailsHeading">Office</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{officeEntity.id}</dd>
          <dt>
            <span id="parentId">Parent Id</span>
          </dt>
          <dd>{officeEntity.parentId}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{officeEntity.name}</dd>
          <dt>
            <span id="officeType">Office Type</span>
          </dt>
          <dd>{officeEntity.officeType}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>
            {officeEntity.createdAt ? <TextFormat value={officeEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>Office</dt>
          <dd>{officeEntity.office ? officeEntity.office.id : ''}</dd>
          <dt>Children</dt>
          <dd>{officeEntity.children ? officeEntity.children.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/office" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/office/${officeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OfficeDetail;
