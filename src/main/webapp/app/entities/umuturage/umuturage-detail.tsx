import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './umuturage.reducer';

export const UmuturageDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const umuturageEntity = useAppSelector(state => state.umuturage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="umuturageDetailsHeading">Umuturage</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{umuturageEntity.id}</dd>
          <dt>
            <span id="indangamuntu">Indangamuntu</span>
          </dt>
          <dd>{umuturageEntity.indangamuntu}</dd>
          <dt>
            <span id="amazina">Amazina</span>
          </dt>
          <dd>{umuturageEntity.amazina}</dd>
          <dt>
            <span id="dob">Dob</span>
          </dt>
          <dd>{umuturageEntity.dob ? <TextFormat value={umuturageEntity.dob} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="gender">Gender</span>
          </dt>
          <dd>{umuturageEntity.gender}</dd>
          <dt>
            <span id="ubudeheCategory">Ubudehe Category</span>
          </dt>
          <dd>{umuturageEntity.ubudeheCategory}</dd>
          <dt>
            <span id="phone">Phone</span>
          </dt>
          <dd>{umuturageEntity.phone}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{umuturageEntity.email}</dd>
          <dt>User</dt>
          <dd>{umuturageEntity.user ? umuturageEntity.user.login : ''}</dd>
          <dt>Village</dt>
          <dd>{umuturageEntity.village ? umuturageEntity.village.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/umuturage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/umuturage/${umuturageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UmuturageDetail;
