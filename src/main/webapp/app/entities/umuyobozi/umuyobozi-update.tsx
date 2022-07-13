import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUmurimo } from 'app/shared/model/umurimo.model';
import { getEntities as getUmurimos } from 'app/entities/umurimo/umurimo.reducer';
import { IUmuyobozi } from 'app/shared/model/umuyobozi.model';
import { getEntity, updateEntity, createEntity, reset } from './umuyobozi.reducer';

export const UmuyoboziUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const umurimos = useAppSelector(state => state.umurimo.entities);
  const umuyoboziEntity = useAppSelector(state => state.umuyobozi.entity);
  const loading = useAppSelector(state => state.umuyobozi.loading);
  const updating = useAppSelector(state => state.umuyobozi.updating);
  const updateSuccess = useAppSelector(state => state.umuyobozi.updateSuccess);
  const handleClose = () => {
    props.history.push('/umuyobozi' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUmurimos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...umuyoboziEntity,
      ...values,
      umurimo: umurimos.find(it => it.id.toString() === values.umurimo.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...umuyoboziEntity,
          umurimo: umuyoboziEntity?.umurimo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecitizenApp.umuyobozi.home.createOrEditLabel" data-cy="UmuyoboziCreateUpdateHeading">
            Create or edit a Umuyobozi
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="umuyobozi-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="First Name"
                id="umuyobozi-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                }}
              />
              <ValidatedField
                label="Last Name"
                id="umuyobozi-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 3, message: 'This field is required to be at least 3 characters.' },
                  maxLength: { value: 100, message: 'This field cannot be longer than 100 characters.' },
                }}
              />
              <ValidatedField
                label="Phone One"
                id="umuyobozi-phoneOne"
                name="phoneOne"
                data-cy="phoneOne"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 13, message: 'This field cannot be longer than 13 characters.' },
                }}
              />
              <ValidatedField
                label="Phone Two"
                id="umuyobozi-phoneTwo"
                name="phoneTwo"
                data-cy="phoneTwo"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  maxLength: { value: 13, message: 'This field cannot be longer than 13 characters.' },
                }}
              />
              <ValidatedField
                label="Email"
                id="umuyobozi-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  pattern: {
                    value: /^[^@\s]+@[^@\s]+\.[^@\s]+$/,
                    message: "This field should follow pattern for '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+..",
                  },
                }}
              />
              <ValidatedField id="umuyobozi-umurimo" name="umurimo" data-cy="umurimo" label="Umurimo" type="select">
                <option value="" key="0" />
                {umurimos
                  ? umurimos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.umurimo}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/umuyobozi" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UmuyoboziUpdate;
