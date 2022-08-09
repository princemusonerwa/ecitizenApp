import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './complain.reducer';

export const ComplainDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const complainEntity = useAppSelector(state => state.complain.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="complainDetailsHeading">Complain</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{complainEntity.id}</dd>
          <dt>
            <span id="ikibazo">Ikibazo</span>
          </dt>
          <dd>{complainEntity.ikibazo}</dd>
          <dt>
            <span id="icyakozwe">Icyakozwe</span>
          </dt>
          <dd>{complainEntity.icyakozwe}</dd>
          <dt>
            <span id="icyakorwa">Icyakorwa</span>
          </dt>
          <dd>{complainEntity.icyakorwa}</dd>
          <dt>
            <span id="umwanzuro">Umwanzuro</span>
          </dt>
          <dd>{complainEntity.umwanzuro}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{complainEntity.status}</dd>
          <dt>
            <span id="priority">Priority</span>
          </dt>
          <dd>{complainEntity.priority}</dd>
          <dt>
            <span id="createdAt">Created At</span>
          </dt>
          <dd>{complainEntity.createdAt ? <TextFormat value={complainEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">Updated At</span>
          </dt>
          <dd>{complainEntity.updatedAt ? <TextFormat value={complainEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Category</dt>
          <dd>{complainEntity.category ? complainEntity.category.id : ''}</dd>
          <dt>Umuturage</dt>
          <dd>{complainEntity.umuturage ? complainEntity.umuturage.id : ''}</dd>
          <dt>User</dt>
          <dd>{complainEntity.user ? complainEntity.user.login : ''}</dd>
          <dt>Organization</dt>
          <dd>
            {complainEntity.organizations
              ? complainEntity.organizations.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {complainEntity.organizations && i === complainEntity.organizations.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/complain" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/complain/${complainEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComplainDetail;
